// stock-chart.js

// 💡 전역 변수로 차트 객체를 선언하여 페이지 어디서든 접근 가능하게 합니다.
let stockChartInstance = null; 

// 현재 active-stock으로 설정된 요소의 코드를 찾아 차트를 그리는 함수
function initChartFromDOM() {
    const activeCodeElement = document.querySelector(".active-stock .stock-code");
    
    if (activeCodeElement) {
        const stockCode = activeCodeElement.textContent.trim();
        console.log("DOM에서 찾은 첫 종목코드:", stockCode);
        
        // 이전에 만들어둔 차트 데이터 요청 함수 호출
        loadChartData(stockCode); 
    } else {
        console.error("화면에서 active-stock을 찾을 수 없어 차트 초기화에 실패했습니다.");
    }
}

/**
 * ⭐ 핵심 기능: Rest API로 데이터를 받아와 차트를 갱신하는 함수
 */
function loadChartData(stockCode) {
    // 서버의 RestController 주소로 비동기 요청
    fetch(`/api/stock/timeline?stockCode=${stockCode}`)
        .then(response => {
            if (!response.ok) throw new Error("네트워크 응답에 문제가 있습니다.");
            return response.json(); // 서버에서 String으로 보낸 ObjectMapper 결과물을 JSON 객체로 파싱
        })
        .then(body => {
            const rawChartData = body.data;
            if (!rawChartData || rawChartData.length === 0) {
                console.warn("해당 종목의 데이터가 비어있습니다.");
                return;
            }

            // 캔들스틱 데이터 가공
            const candleSeriesData = rawChartData.map(item => ({
                x: item[0],
                y: [item[1][0], item[1][1], item[1][2], item[1][3]]
            }));

            // 거래량 데이터 가공
            const volumeSeriesData = rawChartData.map((item, index) => {
                let isUp = true;
                if (index > 0 && item[1][3] < rawChartData[index - 1][1][3]) isUp = false;
                else if (index === 0 && item[1][3] < item[1][0]) isUp = false;

                return {
                    x: item[0],
                    y: item[1][4],
                    fillColor: isUp ? '#f12d2d' : '#2357ef'
                };
            });

            // 새롭게 매핑된 데이터셋 규격 생성
            const newSeries = [
                { name: '시세', type: 'candlestick', data: candleSeriesData },
                { name: '거래량', type: 'bar', data: volumeSeriesData }
            ];

            // 💡 차트가 존재하지 않으면 새로 만들고, 이미 존재하면 데이터만 주입
            if (stockChartInstance === null) {
                const initialOptions = {
                    ...baseChartOptions, 
                    series: newSeries
                };
                stockChartInstance = new ApexCharts(document.querySelector("#stock-chart"), initialOptions);
                stockChartInstance.render();
            } else {
                // 🔄 이미 켜진 차트의 데이터셋만 부드럽게 스왑
                stockChartInstance.updateSeries(newSeries);
            }
        })
        .catch(error => console.error("차트 데이터를 가져오는 중 오류 발생:", error));
}

// 💡 일반 주식 규격에 맞춰 Y축 및 툴팁 가공 로직 수정
const baseChartOptions = {
    chart: {
        type: 'candlestick',
        height: 480,
        background: '#161a1e',
        foreColor: '#90a4ae',
        toolbar: {
            show: true,
            autoSelected: 'pan',
            tools: { download: false, selection: false, zoom: false, zoomin: false, zoomout: false, pan: true, reset: true }
        }
    },
    theme: { mode: 'dark' },
    plotOptions: {
        candlestick: { colors: { upward: '#f12d2d', downward: '#2357ef' }, wick: { useFillColor: true } },
        bar: { columnWidth: '80%' }
    },
    xaxis: {
        type: 'datetime',
        labels: { datetimeUTC: false, datetimeFormatter: { hour: 'HH:mm', minute: 'HH:mm', second: 'HH:mm' } }
    },
    yaxis: [
        { 
            // 💡 가격 Y축: 100 나누기 제거, 원화 단위 세 세자리 콤마 처리
            labels: { formatter: val => val ? val.toLocaleString() + '원' : '' }, 
            title: { text: '가격' } 
        },
        { 
            opposite: true, 
            min: 0, 
            max: max => max * 1.8, 
            // 💡 거래량 Y축: '천 주' 대신 원래 숫자에 '주' 단위 표기
            labels: { formatter: val => val ? val.toLocaleString() + '주' : '0주' }, 
            title: { text: '거래량' } 
        }
    ],
    tooltip: {
        shared: true, 
        custom: function({ series, seriesIndex, dataPointIndex, w }) {
            const candleO = w.globals.seriesCandleO[0][dataPointIndex];
            const candleH = w.globals.seriesCandleH[0][dataPointIndex];
            const candleL = w.globals.seriesCandleL[0][dataPointIndex];
            const candleC = w.globals.seriesCandleC[0][dataPointIndex];
            
            const volume = w.globals.series[1][dataPointIndex];
            
            const timestamp = w.globals.seriesX[0][dataPointIndex];
            const date = new Date(timestamp);
            const timeStr = String(date.getHours()).padStart(2, '0') + ':' + String(date.getMinutes()).padStart(2, '0');

            // 💡 말풍선 툴팁: 연산 걷어내고 원래 숫자 그대로 자릿수 콤마(,)와 '원', '주' 매핑
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
