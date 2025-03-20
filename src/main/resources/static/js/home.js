let currentPage = 1;
let itemsPerPage = 6;

//카테고리 선택
function selectCategory(category) {
    document.querySelectorAll('.category-section').forEach(section => {
        section.style.display = 'none';
    });
    const selectedSection = document.getElementById(category);
    selectedSection.style.display = 'grid';

    currentPage = 1;
    updatePagination();

    document.querySelectorAll('.category-buttons button').forEach(btn => btn.classList.remove('active'));
    document.querySelector(`button[data-category="${category}"]`).classList.add('active');
}


function goToPage(direction) {
    const visibleSection = document.querySelector('.category-section[style*="display: grid;"]');
    const totalItems = visibleSection.querySelectorAll('.bestItem').length;
    const totalPages = Math.ceil(totalItems / itemsPerPage);

    if (direction === 'next' && currentPage < totalPages) {
        currentPage++;
    } else if (direction === 'prev' && currentPage > 1) {
        currentPage--;
    }
    updatePagination();
}

function goToSpecificPage(pageNumber) {
    currentPage = pageNumber;
    updatePagination();
}

function updatePagination() {
    const visibleSection = document.querySelector('.category-section[style*="display: grid;"]');
    const items = visibleSection.querySelectorAll('.bestItem');
    const totalItems = items.length;
    const totalPages = Math.ceil(totalItems / itemsPerPage);

    items.forEach((item, index) => {
        item.style.display = (index >= (currentPage - 1) * itemsPerPage && index < currentPage * itemsPerPage) ? 'grid' : 'none';
    });

    document.getElementById('pagination').style.display = totalItems > itemsPerPage ? 'flex' : 'none';

    // 페이지 인디케이터에 페이지네이션 업데이트
    const pageIndicator = document.getElementById('page-indicator');
    pageIndicator.innerHTML = '';

    for (let i = 1; i <= totalPages; i++) {
        const pageIcon = document.createElement('i');
        pageIcon.className = 'fa-solid fa-circle fa-2xs';
        pageIcon.style.cursor = 'pointer';
        pageIcon.style.margin = '0 5px';
        pageIcon.onclick = () => goToSpecificPage(i);

        // 현재 페이지 표시
        if (i === currentPage) {
            pageIcon.style.color = '#583b1f'; // 활성화된 페이지 색상
        }

        pageIndicator.appendChild(pageIcon);
    }
}

// 페이지 로드시 기본 카테고리 선택
document.addEventListener("DOMContentLoaded", () => {
    selectCategory('brand');
});