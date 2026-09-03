// 일단 띄우기용 더미 데이터
// 2. 가상의 주식/코인 데이터 생성 (타임스탬프, [시가, 고가, 저가, 종가])
// 데이터 포맷: [Timestamp, [Open, High, Low, Close]]
const chartData = [
	[1791244800000, [42000, 42300, 41800, 42100]], // 1일차
	[1791331200000, [42100, 42600, 42000, 42450]], // 2일차
	[1791417600000, [42450, 42500, 41500, 41700]], // 3일차
	[1791504000000, [41700, 42200, 41600, 42000]], // 4일차
	[1791590400000, [42000, 43100, 41900, 42900]], // 5일차
	[1791676800000, [42900, 43500, 42800, 43200]], // 6일차
	[1791763200000, [43200, 43800, 43000, 43600]], // 7일차
	[1791849600000, [43600, 44200, 43500, 43900]], // 8일차
	[1791936000000, [43900, 44000, 42100, 42500]], // 9일차
	[1792022400000, [42500, 43000, 42300, 42800]]  // 10일차
];

// 3. 차트 설정 옵션 정의
const options = {
	series: [{
		name: 'candle',
		data: chartData.map(item => ({
			x: new Date(item[0]),
			y: item[1]
		}))
	}],
	chart: {
		type: 'candlestick',
		height: 450,
		background: '#161a1e',
		foreColor: '#90a4ae', // 글자 색상
		toolbar: {
			show: true, // 우측 상단 드로잉/다운로드 툴바 활성화
			tools: {
				download: true,
				selection: true,
				zoom: true,
				zoomin: true,
				zoomout: true,
				pan: true,
				reset: true
			}
		}
	},
	theme: {
		mode: 'dark' // 💡 다크모드 적용
	},
	title: {
		text: '실시간 시세 현황',
		align: 'left'
	},
	xaxis: {
		type: 'datetime',
		labels: {
			style: {colors: '#90a4ae'}
		}
	},
	yaxis: {
		tooltip: {
			enabled: true // 마우스 올렸을 때 가격 표시
		},
		labels: {
			formatter: function (val) {
				return '$' + val.toLocaleString(); // 달러 표기 포맷팅
			}
		}
	},
	plotOptions: {
		candlestick: {
			colors: {
				upward: '#00b4d8',  // 상승 봉 색상 (초록/파랑 계열)
				downward: '#ff4d4d' // 하락 봉 색상 (빨강 계열)
			}
		}
	}
};

// 4. 차트 생성 및 렌더링
const chart = new ApexCharts(document.querySelector("#stock-chart"), options);
chart.render();