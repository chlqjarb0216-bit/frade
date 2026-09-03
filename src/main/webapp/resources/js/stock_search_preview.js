const searchInput = document.getElementById('searchKeyword');
const previewList = document.getElementById('previewList');
let debounceTimer;

// 사용자가 키를 입력할 때마다 실행
searchInput.addEventListener('input', (e) => {
	const keyword = e.target.value.trim();

	// 1. 기존 타이머가 작동 중이면 취소 (폭풍 타이핑 중일 때 서버 요청을 막음)
	clearTimeout(debounceTimer);

	// 2. 검색어가 짧으면 미리보기 창 닫기
	if (keyword.length < 1) {
		previewList.style.display = 'none';
		return;
	}

	// 3. 300ms(0.3초) 동안 입력이 없으면 최종적으로 서버에 딱 한 번 요청
	debounceTimer = setTimeout(() => {
		fetch(`/stock/api/search-preview?keyword=${encodeURIComponent(keyword)}`)
			.then(response => {
				// 404, 500 에러 등이 나면 response.ok가 false가 됩니다.
				if (!response.ok) {
					throw new Error(`서버 에러 발생! 상태코드: ${response.status}`);
					// 강제로 에러를 던져서 맨 아래 .catch로 즉시 순간이동시킵니다.
				}
				return response.json();
			})
			.then(body => {
				if (body.code == "suc_002") {
					drawPreview([]);
					return;
				} else if (body.code == "suc_001") {
					drawPreview(body.data);
					return;
				}
				throw new Error(`알 수 없는 에러: ${body.code}`);
			})
			.catch(err => console.error("미리보기 에러:", err));
	}, 300);
});

// 화면에 미리보기 목록을 그리는 함수
function drawPreview(items) {
	if (items.length === 0) {
		previewList.style.display = 'none';
		return;
	}

	previewList.addEventListener('click', (event) => {
		const targetChild = event.target.closest('.preview');
		if (!targetChild) return;
		const stockCode = targetChild.querySelector('.stock-code');
		if (stockCode) {
			const value = stockCode.textContent;
			location.href = "/stock/" + value;
		} else {
			alert("이동 오류")
		}
	});

	previewList.innerHTML = items
		.map(stock => `<li>
	<div class="preview" style="display:flex; justify-content:space-between">
			<p>
				<strong>${ stock.stockName }</strong>
			</p>
			<p class="stock-code">${ stock.stockCode }</p>
	</div>
</li>`)
		.join('');

	previewList.style.display = 'block'; // 목록 표시
}