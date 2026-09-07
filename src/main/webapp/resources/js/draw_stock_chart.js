// draw_stock_chart.js

let stockChartInstance = null; 
let currentChartData = [];     // 실시간 1분봉 데이터를 누적 보관할 배열
let currentEventSource = null;  // 현재 가동 중인 SSE 연결 객체

document.addEventListener("DOMContentLoaded", function () {
    const initialChartData = window.SERVER_CHART_DATA;

    // 💡 [자체 파싱] 주소창(URL) 경로에서 종목코드를 직접 추출합니다. (/stock/005930 ➔ 005930)
    const pathname = window.location.pathname; 
    const pathParts = pathname.split('/');
    const currentStockCode = pathParts[pathParts.length - 1].trim();

    if (initialChartData && initialChartData.length > 0) {
        console.log("자동 인식된 종목코드:", currentStockCode);
        currentChartData = initialChartData; 
        
        // 1. 초기 과거 차트 렌더링
        renderStockChart(currentChartData);
        
        // 2. 렌더링이 완료되면 즉시 실시간 SSE 스트리밍 연결
        if (currentStockCode) {
            startRealTimeStream(currentStockCode);
        }
    }
});

/**
 * 💡 실시간 SSE 파이프라인 개방 및 차트 덧붙이기(Push) 함수
 */
function startRealTimeStream(stockCode) {
    currentEventSource = new EventSource(`/api/stock/stream/connect?stockCode=${stockCode}`);

    // 💡 명세 반영: 백엔드 name("chart-tick") 이벤트 전용 리스너 매핑
	currentEventSource.addEventListener("chart-tick", function (event) {
	    try {
	        const newTick = JSON.parse(event.data); // [밀리초, [시,고,저,종,량]]
	        console.log("실시간 데이터 도착:", newTick);

	        const newTickTime = newTick[0]; // 새로 들어온 데이터의 타임스탬프(시간값)

	        if (currentChartData.length > 0) {
	            // 💡 1. 현재 차트에 그려진 데이터 중 맨 마지막 봉의 시간값을 가져옵니다.
	            const lastDataIndex = currentChartData.length - 1;
	            const lastTickTime = currentChartData[lastDataIndex][0];

	            // 💡 2. 시간값이 똑같다면? ➔ 같은 1분 안에서 변하는 데이터이므로 덮어씁니다.
	            if (newTickTime === lastTickTime) {
	                currentChartData[lastDataIndex] = newTick; 
	                console.log("-> 동일 시간대 데이터: 마지막 봉을 갱신(덮어쓰기)했습니다.");
	            } 
	            // 💡 3. 시간값이 더 크다면? ➔ 새로운 1분이 시작된 것이므로 꼬리에 새로 붙입니다.
	            else if (newTickTime > lastTickTime) {
	                currentChartData.push(newTick);
	                console.log("-> 새로운 시간대 데이터: 새 1분 봉을 생성했습니다.");
	            }
	        } else {
	            // 차트에 데이터가 아예 비어있었다면 최초로 추가
	            currentChartData.push(newTick);
	        }

	        // 최근 500개 분량 캔들만 유지 (메모리 방어)
	        if (currentChartData.length > 500) {
	            currentChartData.shift(); 
	        }

	        // 덮어쓰기 혹은 추가가 완료된 전체 배열로 차트 리렌더링
	        renderStockChart(currentChartData);

	    } catch (e) {
	        console.error("실시간 데이터 파싱 및 차트 갱신 실패:", e);
	    }
	});

    currentEventSource.onerror = function () {
        console.error("실시간 스트리밍 통로 일시 정지 또는 해제");
    };
}

/**
 * 💡 캔들 및 거래량 데이터를 매핑하여 실시간 드로잉하는 공통 함수
 */
function renderStockChart(dataList) {
    const candleSeriesData = dataList.map(item => ({
        x: item[0],
        y: [item[1][0], item[1][1], item[1][2], item[1][3]]
    }));

    const volumeSeriesData = dataList.map((item, index) => {
        let isUp = true;
        if (index > 0 && item[1][3] < dataList[index - 1][1][3]) isUp = false;
        else if (index === 0 && item[1][3] < item[1][0]) isUp = false;

        return {
            x: item[0],
            y: item[1][4],
            fillColor: isUp ? '#f12d2d' : '#2357ef'
        };
    });

    const newSeries = [
        { name: '시세', type: 'candlestick', data: candleSeriesData },
        { name: '거래량', type: 'bar', data: volumeSeriesData }
    ];

    if (stockChartInstance === null) {
        const initialOptions = { ...baseChartOptions, series: newSeries };
        stockChartInstance = new ApexCharts(document.querySelector("#stock-chart"), initialOptions);
        stockChartInstance.render();
    } else {
        stockChartInstance.updateSeries(newSeries);
    }
}

// 일반 주식 환경 최적화 고정 옵션셋
const baseChartOptions = {
    chart: {
        type: 'candlestick', height: 480, background: '#161a1e', foreColor: '#90a4ae',
        toolbar: { show: true, autoSelected: 'pan', tools: { download: false, selection: false, zoom: false, zoomin: false, zoomout: false, pan: true, reset: true } }
    },
    theme: { mode: 'dark' },
    title: { text: '실시간 시세 및 거래량 현황', align: 'left' },
    plotOptions: {
        candlestick: { colors: { upward: '#f12d2d', downward: '#2357ef' }, wick: { useFillColor: true } },
        bar: { columnWidth: '80%' }
    },
    xaxis: { type: 'datetime', labels: { datetimeUTC: false, datetimeFormatter: { hour: 'HH:mm', minute: 'HH:mm', second: 'HH:mm' } } },
    yaxis: [
        { labels: { formatter: val => val ? val.toLocaleString() + '원' : '' }, title: { text: '가격' } },
        { opposite: true, min: 0, max: max => max * 1.8, labels: { formatter: val => val ? val.toLocaleString() + '주' : '0주' }, title: { text: '거래량' } }
    ],
    tooltip: {
        shared: true,
        custom: function({ dataPointIndex, w }) {
            const candleO = w.globals.seriesCandleO[0][dataPointIndex];
            const candleH = w.globals.seriesCandleH[0][dataPointIndex];
            const candleL = w.globals.seriesCandleL[0][dataPointIndex];
            const candleC = w.globals.seriesCandleC[0][dataPointIndex];
            const volume = w.globals.series[1][dataPointIndex];
            const date = new Date(w.globals.seriesX[0][dataPointIndex]);
            const timeStr = String(date.getHours()).padStart(2, '0') + ':' + String(date.getMinutes()).padStart(2, '0');

            return `
                <div class="apexcharts-custom-tooltip" style="padding: 10px; background: #22262a; border: 1px solid #444; color: #fff; font-size: 12px; border-radius: 4px;">
                    <div style="font-weight: bold; margin-bottom: 5px; color: #90a4ae;">시간: ${timeStr}</div>
                    <div style="margin-bottom: 3px;">시가(O): <span style="color:#fff; float:right; margin-left:10px;">${candleO ? candleO.toLocaleString() + '원' : ''}</span></div>
                    <div style="margin-bottom: 3px;">고가(H): <span style="color:#f12d2d; float:right; margin-left:10px;">${candleH ? candleH.toLocaleString() + '원' : ''}</span></div>
                    <div style="margin-bottom: 3px;">저가(L): <span style="color:#2357ef; float:right; margin-left:10px;">${candleL ? candleL.toLocaleString() + '원' : ''}</span></div>
                    <div style="margin-bottom: 5px;">종가(C): <span style="color:#fff; float:right; margin-left:10px;">${candleC ? candleC.toLocaleString() + '원' : ''}</span></div>
                    <div style="border-top: 1px solid #444; padding-top: 5px;">거래량: <span style="color:#00b4d8; float:right; margin-left:10px;">${volume ? volume.toLocaleString() + '주' : '0주'}</span></div>
                </div>
            `;
        }
    }
};
