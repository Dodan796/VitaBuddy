
function goToPage(pageNumber) {
    // 현재 URL에서 카테고리, 태그, 검색어를 받음
    const urlParams = new URLSearchParams(window.location.search);
    const category = urlParams.get('category');
    const tag = urlParams.get('tag');
    const keyword = urlParams.get('keyword'); // 검색어 추가

    // 페이지를 이동할 URL을 초기화
    let newUrl = '';

    // 카테고리와 태그가 null이 아닌 경우에 URL 생성
    if (category && tag) {
        newUrl = `/supplements/tagsearch?category=${category}&tag=${encodeURIComponent(tag)}&page=${pageNumber}`;
    } else {
        // 검색어가 있을 경우
        if (keyword) {
            newUrl = `/supplement/supplement?keyword=${encodeURIComponent(keyword)}&page=${pageNumber}`;
        } else {
            // 전체 상품 페이지로 가는 경우
            newUrl = `/supplement/supplementList?page=${pageNumber}`;
        }
    }

    // 페이지를 새 URL로 이동합니다.
    window.location.href = newUrl;
}