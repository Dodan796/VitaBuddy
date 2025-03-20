$(document).ready(function() {
    $('#searchForm').on('submit', function(event) {
        event.preventDefault();
        var keyword = $('#keyword').val().trim();
        var page = 1;
        var searchUrl = `/supplement/supplementList?keyword=${encodeURIComponent(keyword)}&page=${page}`;
        history.replaceState({ keyword: keyword, page: page }, '', searchUrl);
        performSearch(keyword, page, false);
    });

    function setupPagination(currentPage, totalPages) {
    $('.pagination').empty();

    if (totalPages > 0) {
        // 시작 페이지/끝 페이지 설정 (5개씩 표시)
        let startPage = Math.max(1, currentPage - 2);
        let endPage = Math.min(totalPages, currentPage + 2);

        // 첫 페이지 설정
        if (endPage - startPage < 4) {
            if (currentPage < 3) {
                endPage = Math.min(totalPages, 5);
            } else {
                startPage = Math.max(1, totalPages - 4);
            }
        }

        // 이전 버튼
        $('.pagination').append(`
            <button class="prev ${currentPage === 1 ? 'disabled' : ''}" data-page="${currentPage - 1}" ${currentPage === 1 ? 'disabled' : ''}>
                <i class="fa-solid fa-caret-left"></i>
            </button>
        `);

        // 페이지 번호 버튼
        for (let i = startPage; i <= endPage; i++) {
            const activeClass = (i == currentPage) ? 'active' : '';
            $('.pagination').append(`<button class="page-btn ${activeClass}" data-page="${i}">${i}</button>`);
        }

        // 다음 버튼
        $('.pagination').append(`
            <button class="next ${currentPage === totalPages ? 'disabled' : ''}" data-page="${currentPage + 1}" ${currentPage === totalPages ? 'disabled' : ''}>
                <i class="fa-solid fa-caret-right"></i>
            </button>
        `);
    }

    // 페이지 버튼 클릭
    $('.page-btn').off('click').on('click', function(event) {
        event.preventDefault();
        const page = $(this).data('page');
        const keywordFromUrl = new URLSearchParams(window.location.search).get('keyword');
        performSearch(keywordFromUrl, page);
    });

    // 이전 버튼 클릭
    $('.prev').off('click').on('click', function(event) {
        event.preventDefault();
        if (currentPage > 1) {
            const keywordFromUrl = new URLSearchParams(window.location.search).get('keyword');
            performSearch(keywordFromUrl, currentPage - 1);
        }
    });

    // 다음 버튼 클릭
    $('.next').off('click').on('click', function(event) {
        event.preventDefault();
        if (currentPage < totalPages) {
            const keywordFromUrl = new URLSearchParams(window.location.search).get('keyword');
            performSearch(keywordFromUrl, currentPage + 1);
        }
    });
}


    const urlParams = new URLSearchParams(window.location.search);
    const keyword = urlParams.get('keyword');
    const page = urlParams.get('page') || 1;

    if (keyword) {
        performSearch(keyword, page, false);
    }

    function performSearch(keyword, page, isHistoryUpdate = true) {
        var searchUrl = `/supplement/supplementList?keyword=${encodeURIComponent(keyword)}&page=${page}`;
        
        if (isHistoryUpdate) {
            history.pushState({ keyword: keyword, page: page }, '', searchUrl);
        } else {
            history.replaceState({ keyword: keyword, page: page }, '', searchUrl);
        }

        $.ajax({
            url: '/search',
            type: 'GET',
            data: { keyword: keyword, page: page },
            success: function(response) {
                $('.products').empty();
                if (response.pagingkeywordsupList && response.pagingkeywordsupList.length <= 0) {
                    $('.products').append('<h4>검색 결과가 없습니다</h4>');
                } else if (response.pagingkeywordsupList) {
                    response.pagingkeywordsupList.forEach(function(keywordsup) {
                        $('.products').append(`
                            <div class="productItem">
                                <a href="/api/supplement/supplementDetail/${keywordsup.supId}">
                                    <img class="prdImg" src="data:image/png;base64,${keywordsup.base64SupImg}" width="300" height="300">
                                </a>
                                <p>${keywordsup.supName}</p>
                                <p>${new Intl.NumberFormat('ko-KR', { style: 'currency', currency: 'KRW' }).format(keywordsup.supPrice)}</p>
                                <p>${keywordsup.supBrand}</p>
                            </div>
                        `);
                    });
                    setupPagination(response.currentPage, response.totalPages);
                    window.scrollTo(0, 0);
                }
            },
            error: function() {
                alert('오류가 발생했습니다.');
            }
        });
    }

    window.addEventListener('popstate', function(event) {
        if (event.state) {
            performSearch(event.state.keyword, event.state.page, false);
        }
    });
});