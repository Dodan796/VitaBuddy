var pwd = 0;
var email = 0;

function pwCheck() {
    var pw3 = document.getElementById('userPwd').value;
    if (pw3.length < 8) {
        document.getElementById('pwLeng').innerHTML = "비밀번호는 8자리 이상이어야 합니다.";
        document.getElementById('pwLeng').style.color = 'red';
    } else {
        document.getElementById('pwLeng').innerHTML = "";
    }
    if (document.getElementById('userPwd').value !== '' && document.getElementById('confirmPwd').value !== '') {
        if (document.getElementById('userPwd').value === document.getElementById('confirmPwd').value) {
            document.getElementById('pwOk').innerHTML = "비밀번호가 일치합니다.";
            document.getElementById('pwOk').style.color = 'green';
            pwd = 1;
        } else {
            document.getElementById('pwOk').innerHTML = "비밀번호가 일치하지 않습니다.";
            document.getElementById('pwOk').style.color = 'red';
            pwd = 0;
        }
    }
}

/*// 25.01.04수정
//emailCheck -> emailCheckfromDB()로 수정.
function emailCheckfromDB() {
    if (document.getElementById('userEmail').value.indexOf("@") > 0) {
        document.getElementById('emOk').innerHTML = "";
        email = 1;
    } else {
        document.getElementById('emOk').innerHTML = "올바른 이메일 형식이 아닙니다.";
        document.getElementById('emOk').style.color = 'red';
        email = 0;
    }
    console.log("email 상태: ", email); // 디버깅 로그
}*/

// 아이디 중복 체크
function fnDbCheckId() {
    var userId = document.getElementById("userId").value;
    
    fetch(`/member/checkId?userId=${userId}`)
        .then(response => response.json())
        .then(data => {
            if (data.available) {
                alert("사용 가능한 아이디입니다.");
            } else {
                alert("이미 사용 중인 아이디입니다.");
            }
        })
        .catch(error => console.error("Error:", error));
}

// 우편번호 찾기 API 호출 함수
function openZipSearch() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 우편번호와 주소 정보를 각각의 input 필드에 입력
            document.getElementById('userZipcode').value = data.zonecode;
            document.getElementById('userAddress1').value = data.roadAddress || data.jibunAddress;

            // 상세 주소 입력란으로 포커스 이동
            document.getElementById('userAddress2').focus();
        }
    }).open();
}

function registerMember() {
    if (email === 0 || pwd === 0) {
        alert("올바른 정보를 입력해 주세요.");
        return;
    }

    var memberData = {
        userName: document.getElementById("userName").value,
        userId: document.getElementById("userId").value,
        userPwd: document.getElementById("userPwd").value,
        userEmail: document.getElementById("userEmail").value,
        userPh: document.getElementById("userPh1").value + "-" +
                document.getElementById("userPh2").value + "-" +
                document.getElementById("userPh3").value,
        userZipcode: document.getElementById("userZipcode").value,
        userAddress1: document.getElementById("userAddress1").value,
        userAddress2: document.getElementById("userAddress2").value
    };

    fetch("/member/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(memberData)
    })
    .then(response => response.json()) // JSON 형태로 응답 받음
    .then(data => {
        if (data.success) {
            window.location.href = data.redirect; // 리다이렉트 URL로 이동
        } else {
            alert(data.message || "회원가입에 실패했습니다."); // 실패 시 메시지 출력
        }
    })
    .catch(error => console.error("Error:", error));
}
