document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("loginForm");

    loginForm.addEventListener("submit", function (event) {
        event.preventDefault(); // 폼의 기본 제출 동작 방지

        // (1) 폼에서 입력값 추출
        const username = document.getElementById("id").value;
        const password = document.getElementById("pwd").value;

        // (2) fetch로 로그인 요청
        fetch('/login', {
            method: 'POST',
            credentials: 'include', // 쿠키 자동 전송
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        })
        .then(response => {
            if (response.ok) {
                // 로그인 성공 시 alert을 띄움
                alert("로그인 성공!");

                // 헤더에서 userRole 값 읽기
                const userRole = response.headers.get("userRole");

                // userRole에 맞게 리다이렉트 처리
                if (userRole === "ROLE_USER") {
                    window.location.href = "/"; // 일반 사용자는 메인 페이지로
                } else if (userRole === "ROLE_ADMIN") {
                    window.location.href = "/supplementRegister/admin"; // 관리자는 admin 페이지로
                } else {
                    console.error("알 수 없는 역할입니다.");
                    alert("알 수 없는 역할입니다.");
                }
            } else {
                throw new Error("로그인 실패: 아이디 또는 비밀번호를 확인하세요.");
            }
        })
        .catch(error => {
            console.error(error);
            alert(error.message);
        });
    });
});


// 로그아웃 기능
document.querySelector('#logoutButton').addEventListener('click', function (event) {
event.preventDefault(); // 기본 동작 방지

    fetch('/logout', {
        method: 'POST',
        credentials: 'include', // 쿠키 포함
        headers: {
            'Content-Type': 'application/json',
        },
    })
    .then((response) => {
        if (response.ok) {
            window.location.href = '/'; // 메인 페이지로 리다이렉트
        } else {
            response.text().then((message) => alert('로그아웃 실패: ' + message));
        }
    })
    .catch((error) => {
        console.error('Error:', error);
        alert('로그아웃 요청 중 문제가 발생했습니다.');
    });

});
