// stock-chart.js

document.addEventListener("DOMContentLoaded", function () {
    // 1. JSP에서 전역 변수로 심어둔 실데이터 가져오기
    // 데이터 구조: [밀리초, [시가, 고가, 저가, 종가, 거래량]]
    const rawChartData = window.SERVER_CHART_DATA;

    if (!rawChartData || rawChartData.length === 0) return;

    // 2. 캔들스틱 데이터 매핑
    const candleSeriesData = rawChartData.map(item => ({
        x: item[0], // 밀리초 타임스탬프 바로 주입
        y: [item[1][0], item[1][1], item[1][2], item[1][3]] // [시, 고, 저, 종]
    }));

    // 3. 거래량 데이터 매핑 (양봉/음봉 색상 조건 포함)
    const volumeSeriesData = rawChartData.map((item, index) => {
        let isUp = true;
        if (index > 0) {
            const prevClose = rawChartData[index - 1][1][3];
            const currClose = item[1][3];
            if (currClose < prevClose) isUp = false;
        } else {
            if (item[1][3] < item[1][0]) isUp = false;
        }

        return {
            x: item[0],
            y: item[1][4], // 거래량
            fillColor: isUp ? '#f12d2d' : '#2357ef' // 국내 기준: 양봉 빨강, 음봉 파랑
        };
    });

    // 4. 단일 차트(이중 Y축) 옵션 설정
	const options = {
	    series: [
	        {
	            name: '시세',
	            type: 'candlestick',
	            data: candleSeriesData
	        },
	        {
	            name: '거래량',
	            type: 'bar',
	            data: volumeSeriesData
	        }
	    ],
	    chart: {
	        type: 'candlestick',
	        height: 480,
	        background: '#161a1e',
	        foreColor: '#90a4ae',
	        toolbar: {
	            show: true,
	            autoSelected: 'pan',
				tools: {
				    // 💡 불필요한 버튼 전부 제거 (false 처리)
				    download: false,   // 메뉴 삼선 바 숨김
				    selection: false,  // 드래그 선택 숨김
				    zoom: false,       // 돋보기 줌 숨김
				    zoomin: false,     // + 버튼 숨김
				    zoomout: false,    // - 버튼 숨김
				    
				    // ⭐ 필요한 기능만 켜기 (true 처리)
				    pan: true,         // 손바닥 모양(좌우 이동) 활성화
				    reset: true        // 🔄 홈 버튼(기본 축으로 리셋) 활성화
				}
	        }
	    },
	    theme: {
	        mode: 'dark'
	    },
	    title: {
	        text: '실시간 시세 및 거래량 현황',
	        align: 'left'
	    },
	    plotOptions: {
	        candlestick: {
	            colors: {
	                upward: '#f12d2d',  // 📈 양봉 빨간색
	                downward: '#2357ef' // 📉 음봉 파란색
	            },
	            wick: { useFillColor: true }
	        },
	        bar: {
	            columnWidth: '80%'
	        }
	    },
	    xaxis: {
	        type: 'datetime',
	        labels: {
	            datetimeUTC: false,
	            datetimeFormatter: {
	                year: 'yyyy',
	                month: 'yyyy-MM',
	                day: 'MM-dd',
	                hour: 'HH:mm',
	                minute: 'HH:mm', // 💡 분 단위 고정
	                second: 'HH:mm'  // 💡 초 단위 노출 방지
	            }
	        }
	    },
	    yaxis: [
	        {
	            seriesName: '시세',
	            labels: {
	                // 💡 왼쪽 Y축 지수 가공 (100 분할 + 소수점 2자리)
	                formatter: val => val ? (val / 100).toFixed(2) : ''
	            },
	            title: { text: '코스피 지수' }
	        },
	        {
	            seriesName: '거래량',
	            opposite: true,
	            min: 0, 
	            max: function(max) { return max * 1.8; }, // 💡 거래량 스케일 확대 및 하단 배치
	            labels: {
	                formatter: val => {
	                    if (val < 0) return '0주';
	                    return val ? (val / 10000).toFixed(0) + '만 주' : '0주';
	                }
	            },
	            title: { text: '거래량' }
	        }
	    ],
	    // ⭐ [여기 추가] 말풍선(Tooltip) 내부 데이터 가공 옵션
	    tooltip: {
	        shared: true, // 캔들과 거래량 정보 한 말풍선에 같이 보기
	        custom: function({ series, seriesIndex, dataPointIndex, w }) {
	            // 캔들스틱(시세) 데이터 가져오기
	            const candleO = w.globals.seriesCandleO[0][dataPointIndex];
	            const candleH = w.globals.seriesCandleH[0][dataPointIndex];
	            const candleL = w.globals.seriesCandleL[0][dataPointIndex];
	            const candleC = w.globals.seriesCandleC[0][dataPointIndex];
	            
	            // 거래량 데이터 가져오기 (두 번째 시리즈)
	            const volume = w.globals.series[1][dataPointIndex];
	            
	            // X축 날짜 가공
	            const timestamp = w.globals.seriesX[0][dataPointIndex];
	            const date = new Date(timestamp);
	            const timeStr = String(date.getHours()).padStart(2, '0') + ':' + String(date.getMinutes()).padStart(2, '0');

	            // HTML 형태로 말풍선 내부를 직접 리디자인 (100으로 나눈 소수점 반영)
	            return `
	                <div class="apexcharts-custom-tooltip" style="padding: 10px; background: #22262a; border: 1px solid #444; color: #fff; font-size: 12px; border-radius: 4px;">
	                    <div style="font-weight: bold; margin-bottom: 5px; color: #90a4ae;">시간: ${timeStr}</div>
	                    <div style="margin-bottom: 3px;">시가(O): <span style="color:#fff; float:right; margin-left:10px;">${(candleO / 100).toFixed(2)}</span></div>
	                    <div style="margin-bottom: 3px;">고가(H): <span style="color:#f12d2d; float:right; margin-left:10px;">${(candleH / 100).toFixed(2)}</span></div>
	                    <div style="margin-bottom: 3px;">저가(L): <span style="color:#2357ef; float:right; margin-left:10px;">${(candleL / 100).toFixed(2)}</span></div>
	                    <div style="margin-bottom: 5px;">종가(C): <span style="color:#fff; float:right; margin-left:10px;">${(candleC / 100).toFixed(2)}</span></div>
	                    <div style="border-top: 1px solid #444; padding-top: 5px;">거래량: <span style="color:#00b4d8; float:right; margin-left:10px;">${volume ? volume.toLocaleString() + '주' : '0주'}</span></div>
	                </div>
	            `;
	        }
	    }
	};


    // 5. 차트 생성 및 렌더링 (단일 div 구조)
    const chart = new ApexCharts(document.querySelector("#stock-chart"), options);
    chart.render();
});
