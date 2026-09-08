// draw_stock_chart.js 최상단 영역

let stockChartInstance = null; 
let currentChartData = [];     // 실시간 1분봉 데이터를 누적 보관할 배열
let currentEventSource = null;  // 현재 가동 중인 SSE 연결 객체
let lastZoomRange = null;       // 사용자가 현재 확대해 놓은 범위를 기억할 저장소

// 💡 [교정] 데이터 유실 방지용 실시간 대기 큐(Queue) 배열 체계 도입
let isUserHovering = false;
let pendingTicksQueue = []; 



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

    currentEventSource.addEventListener("chart-tick", function (event) {
        try {
            const newTick = JSON.parse(event.data); // [밀리초, [시,고,저,종,량]]
            console.log("실시간 데이터 도착:", newTick);
            
            // 💡 [정정] 상단 현재가 UI는 차트 락과 무관하므로 언제나 즉시 실시간 갱신합니다!
            updateTopRealTimeUI(newTick);

            // 차트 영역은 툴팁 파괴 방지를 위해 마우스 오버 시 대기 큐에만 적재
            if (isUserHovering) {
                pendingTicksQueue.push(newTick);
                return; 
            }

            // 마우스가 없을 때만 차트 데이터 반영 및 리렌더링
            applyTickData(newTick);

        } catch (e) {
            console.error("실시간 데이터 파싱 실패:", e);
        }
    });

    currentEventSource.onerror = function () {
        console.error("실시간 스트리밍 통로 일시 정지 또는 해제");
    };
}



// 2. 실제 데이터 반영 및 1분 봉 분기 렌더링 함수 교정
function applyTickData(newTick) {
	// 1. 안전하게 0번 인덱스의 밀리초 타임스탬프 추출
	const newTickTime = newTick[0]; 

	if (currentChartData.length > 0) {
	    const lastDataIndex = currentChartData.length - 1;
	    const lastTickTime = currentChartData[lastDataIndex][0];
	    
	    // 초/밀리초 단위를 완전히 배제하고 '연-월-일 시:분'으로 변환
	    const newDate = new Date(newTickTime);
	    const newMinutesKey = `${newDate.getFullYear()}-${newDate.getMonth()}-${newDate.getDate()} ${newDate.getHours()}:${newDate.getMinutes()}`;
	    
	    const lastDate = new Date(lastTickTime);
	    const lastMinutesKey = `${lastDate.getFullYear()}-${lastDate.getMonth()}-${lastDate.getDate()} ${lastDate.getHours()}:${lastDate.getMinutes()}`;

	    // 💡 2. [핵심 교정] 같은 분 단위 안의 데이터라면 중복 배열을 생성하지 않고, 
	    // 기존 마지막 칸을 제거(pop)한 뒤 완전히 새로운 데이터로 교체(push)하여 누적 잔상을 지웁니다.
	    if (newMinutesKey === lastMinutesKey) {
	        currentChartData.pop(); // 잔상이 남은 마지막 봉 제거
	        currentChartData.push(newTick); // 완전히 새로운 단일 패킷 주입
	        console.log("-> [무결성 완료] 동일 분 영역: 완벽하게 제자리 교체되었습니다.");
	    } else {
	        // 새로운 분이 시작되면 그냥 추가
	        currentChartData.push(newTick);
	    }
	} else {
	    currentChartData.push(newTick);
	}

	if (currentChartData.length > 500) {
	    currentChartData.shift(); 
	}

	// 💡 3. 혹시 모를 중복 타임스탬프 겹침을 최종 방어하기 위해 Map 객체로 한 번 더 고정 정제
	const uniqueMap = new Map();
	currentChartData.forEach(item => {
	    const d = new Date(item[0]);
	    const key = `${d.getFullYear()}-${d.getMonth()}-${d.getDate()} ${d.getHours()}:${d.getMinutes()}`;
	    uniqueMap.set(key, item); // 같은 '분' 키값이 오면 마지막 데이터가 앞의 데이터를 완전히 덮어씀
	});

	// 최종 깨끗하게 정제된 1분 단위 단일 배열 완성
	const purifiedChartData = Array.from(uniqueMap.values());

	// 덮어쓰기 흔적이 완전히 소멸된 데이터로만 차트 드로잉
	renderStockChart(purifiedChartData);
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
		// 💡 3. 줌을 땡긴 적이 있다면 축 눈금 범위를 강제로 기존 줌 상태에 고정시킵니다.
		if (lastZoomRange !== null) {
		    stockChartInstance.updateOptions({
		        series: newSeries,
		        xaxis: {
		            min: lastZoomRange.min,
		            max: lastZoomRange.max
		        }
		    }, false, false); // 애니메이션을 false로 제어하여 튕김 현상 방지
		} else {
		    // 줌이 없는 상태면 그냥 평소대로 데이터만 갱신
		    stockChartInstance.updateSeries(newSeries, false);
		}
    }
}

// 일반 주식 환경 최적화 고정 옵션셋
const baseChartOptions = {
    chart: {
        type: 'candlestick', height: 480, background: '#161a1e', foreColor: '#90a4ae',
        toolbar: { show: true, autoSelected: 'pan', tools: { download: false, selection: false, zoom: false, zoomin: false, zoomout: false, pan: true, reset: true } },
		// 💡 [추가 포인트 1] 데이터가 변경되어 리렌더링될 때 마우스 포인터 위치의 툴팁 유지를 도와줍니다.
		redrawOnParentResize: false,
		redrawOnWindowResize: false,
		// ⭐ [핵심 추가] 사용자의 마우스 무빙(줌/패닝)을 실시간으로 감시하는 리스너
		events: {
			beforeResetZoom: function() { lastZoomRange = null; },
			zoomed: function(chartContext, { xaxis }) { lastZoomRange = { min: xaxis.min, max: xaxis.max }; },
			scrolled: function(chartContext, { xaxis }) { lastZoomRange = { min: xaxis.min, max: xaxis.max }; },

			// 💡 마우스가 차트 위에 올라가는 순간 갱신 정지 플래그 온
			mouseMove: function() {
			    isUserHovering = true;
			},

			// baseChartOptions.chart.events 객체 내부의 mouseLeave 메서드 부분

			// baseChartOptions.chart.events 객체 내부의 mouseLeave 메서드 부분

			mouseLeave: function() {
			    isUserHovering = false;
			    
			    // 마우스가 나가는 순간 큐에 밀려있던 차트 데이터만 순서대로 방출 병합합니다.
			    if (pendingTicksQueue.length > 0) {
			        console.log(`[큐 방출] 밀린 차트 데이터 ${pendingTicksQueue.length}개 일괄 반영`);
			        
			        pendingTicksQueue.forEach(tick => {
			            applyTickData(tick); // 차트 1분 봉 정제 및 리렌더링 함수만 가동
			        });
			        
			        pendingTicksQueue = []; // 대기 큐 초기화
			    }
			}


		}
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
		intersect: false,
		followCursor: false,
		custom: function({ series, seriesIndex, dataPointIndex, w }) {
		    try {
		        // 💡 [핵심 변경] 차트 내장 캐시 대신 w.config.series에 보관된 우리 원본 가공 데이터를 직접 뜯어옵니다.
		        const candleSeries = w.config.series[0].data[dataPointIndex];
		        const volumeSeries = w.config.series[1].data[dataPointIndex];
		        
		        // 데이터가 순간적으로 누락되었을 때 튕기는 현상 방지 방어 코드
		        if (!candleSeries || !volumeSeries) return '';

		        const candleO = candleSeries.y[0]; // 시가
		        const candleH = candleSeries.y[1]; // 고가
		        const candleL = candleSeries.y[2]; // 저가
		        const candleC = candleSeries.y[3]; // 종가
		        const volume  = volumeSeries.y;    // 거래량

		        const timestamp = candleSeries.x;
		        const date = new Date(timestamp);
		        const timeStr = String(date.getHours()).padStart(2, '0') + ':' + String(date.getMinutes()).padStart(2, '0');

		        return `
		            <div class="apexcharts-custom-tooltip" style="padding: 10px; background: #22262a; border: 1px solid #444; color: #fff; font-size: 12px; border-radius: 4px; pointer-events: none;">
		                <div style="font-weight: bold; margin-bottom: 5px; color: #90a4ae;">시간: ${timeStr}</div>
		                <div style="margin-bottom: 3px;">시가(O): <span style="color:#fff; float:right; margin-left:10px;">${candleO ? candleO.toLocaleString() + '원' : ''}</span></div>
		                <div style="margin-bottom: 3px;">고가(H): <span style="color:#f12d2d; float:right; margin-left:10px;">${candleH ? candleH.toLocaleString() + '원' : ''}</span></div>
		                <div style="margin-bottom: 3px;">저가(L): <span style="color:#2357ef; float:right; margin-left:10px;">${candleL ? candleL.toLocaleString() + '원' : ''}</span></div>
		                <div style="margin-bottom: 5px;">종가(C): <span style="color:#fff; float:right; margin-left:10px;">${candleC ? candleC.toLocaleString() + '원' : ''}</span></div>
		                <div style="border-top: 1px solid #444; padding-top: 5px;">거래량: <span style="color:#00b4d8; float:right; margin-left:10px;">${volume ? volume.toLocaleString() + '주' : '0주'}</span></div>
		            </div>
		        `;
		    } catch (e) {
		        console.error("툴팁 렌더링 에러:", e);
		        return '';
		    }
		}
    }
};

/**
 * 💡 실시간 체결 데이터를 받아 상단 현재가 및 등락률 UI를 동적으로 갱신하는 함수
 */
function updateTopRealTimeUI(newTick) {
    // 1. HTML 엘리먼트 낚아채기
    const priceEl = document.querySelector("#realtime-price");
    const rateEl = document.querySelector("#daily-change-rate");
    const prevCloseEl = document.querySelector("#prev-day-close-price");

    // 방어 코드: 엘리먼트가 화면에 없으면 중단
    if (!priceEl || !rateEl || !prevCloseEl) return;

    // 2. 데이터 추출: 실시간 데이터에서 '현재 종가(C)'를 추출하여 현재가로 취급합니다.
    const currentPrice = newTick[1][3]; // [밀리초, [시, 고, 저, 종, 량]] 에서 3번 인덱스가 종가
    
    // 3. 전일 종가 가져오기 (화면에 박혀있는 텍스트를 숫자로 파싱)
    const prevClosePrice = parseInt(prevCloseEl.textContent.replace(/[^0-9]/g, ""), 10);

    if (isNaN(prevClosePrice) || prevClosePrice === 0) return;

    // 4. ⭐ 실시간 등락률 계산 연산 공식
    // (현재가 - 전일종가) / 전일종가 * 100
    const changePercent = ((currentPrice - prevClosePrice) / prevClosePrice) * 100;
    // 소수점 둘째 자리까지 반올림 가공
    const roundedPercent = changePercent.toFixed(2); 
	
	priceEl.textContent = `${currentPrice.toLocaleString('ko-KR')}원`;
	
    // 5. 🎨 가격 조건별 텍스트 및 UI 색상 스왑 분기 처리 (JSP c:choose 로직을 JS로 이식)
    if (currentPrice > prevClosePrice) {        
        rateEl.style.color = "#f12d2d";
        rateEl.textContent = `▲${roundedPercent}%`;
    } 
    else if (currentPrice < prevClosePrice) {
        rateEl.style.color = "#2357ef";
        // 마이너스 기호(-)가 중복으로 붙는 걸 방지하기 위해 Math.abs(절대값) 처리
        rateEl.textContent = `▼${Math.abs(roundedPercent)}%`;
    } 
    else {
        rateEl.style.color = "#90a4ae";
        rateEl.textContent = `${roundedPercent}%`;
    }
}
