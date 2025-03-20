$(function () {
    var timerInterval; // 전역 변수로 타이머 관리

    // 이메일 인증 버튼 클릭 시 동작
    $("#verificationEmail").on("click", function (event) {
        event.preventDefault();

        var userEmail = $('#userEmail').val();

        if (validateEmail(userEmail)) {
            $.ajax({
                type: "POST",
                url: "/member/emailverify",
                data: { "userEmail": userEmail },
                success: function (data) {
                    console.log("서버에서 받은 인증코드입니다: " + data);
                    alert("해당 이메일 주소로 인증코드가 전송되었습니다.");

                    $('#verificationEmailCode').html(`
                        <div style="display: flex; align-items: center; justify-content: flex-start; gap: 10px; max-width: 400px; width: 100%; box-sizing: border-box;">
                            <label for="verificationEmailCodeInput" style="flex-shrink: 0; white-space: nowrap; margin-right: -20px;">인증코드 입력</label>
                            <input type="text" name="verificationEmailCode" id="verificationEmailCodeInput" required
                                   style="flex: none; width: 200px; height: 35px; box-sizing: border-box; margin-left: 0;" />
                            <div id="timer"
                                 style="flex-shrink: 0; width: 250px; height: 35px; line-height: 35px; text-align: left; white-space: nowrap; overflow: hidden; color: green;">
                            </div>
                            <button type="button" id="verificationEmailCodeBtn" style="margin-top: 15px;">인증하기</button>
                        </div>
                    `);

                    var timerDuration = 120; // 2분 (120초)

                    function startTimer() {
                        if (timerInterval) {
                            clearInterval(timerInterval); // 기존 타이머 제거
                        }

                        timerInterval = setInterval(function () {
                            var minutes = Math.floor(timerDuration / 60);
                            var seconds = timerDuration % 60;

                            $('#timer').text(`남은 시간: ${minutes}:${seconds.toString().padStart(2, '0')}`);

                            if (timerDuration <= 0) {
                                clearInterval(timerInterval);
                                $('#timer').text("인증코드가 만료되었습니다.").css({ "color": "red" });
                            }

                            timerDuration--;
                        }, 1000);
                    }

                    startTimer(); // 타이머 시작
                },
                error: function () {
                    alert("인증코드 요청 중 오류가 발생했습니다. 다시 시도해주세요.");
                }
            });
        } else {
            alert("올바른 이메일 형식이 아닙니다. 다시 입력해주세요.");
        }
    });

    // 인증번호 확인 버튼 클릭 시 동작
    $(document).on("click", "#verificationEmailCodeBtn", function () {
        var userEmail = $('#userEmail').val();
        var userInputCode = $('#verificationEmailCodeInput').val();

        if (!userInputCode) {
            alert("인증코드를 입력해주세요.");
            return;
        }

        $.ajax({
            type: "POST",
            url: "/member/verifyCode",
            data: { userEmail: userEmail, code: userInputCode },
            success: function (isVerified) {
                console.log("isVerified 값:", isVerified);
                if (isVerified) {
                    alert("이메일 인증이 완료되었습니다!");
                    $("#verificationEmailCodeBtn").prop("disabled", true);
                    $("#verificationEmailCodeInput").prop("disabled", true);

                    // 타이머 중지
                    clearInterval(timerInterval);

                    // 타이머 텍스트 및 스타일 업데이트
                    $("#timer").text("인증이 완료되었습니다.").css({ "color": "green" });
                } else {
                    alert("인증번호가 일치하지 않습니다. 다시 입력해주세요.");
                }
            },
            error: function () {
                alert("인증번호 확인 중 오류가 발생했습니다. 다시 시도해주세요.");
            }
        });
    });
});


// 이메일 형식 체크
function validateEmail(email){
    var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}


/*25/01/05 이메일 중복체크 구현*/
//이메일 중복체크
var email = 0;
function emailCheckfromDB() {
    var userEmail = document.getElementById("userEmail").value;

    //ajax 코드
    $.ajax({
        type: "GET",
        url: "/member/checkEmail",
        data : {"userEmail" : userEmail},
        dataType: "text",
        success: function(data){
            console.log(data);

            var verificationBtn = document.getElementById('verificationEmail');
            var emOK = document.getElementById('emOk');

            if(data=="0"){
                emOK.innerHTML = "중복된 이메일입니다. 다른 이메일을 입력해주세요.";
                emOK.style.color = 'red';
                email = 0;

                //버튼 비활성화
                verificationBtn.disabled=true;
                verificationButton.style.backgroundColor = '#cccccc';
                verificationButton.style.cursor = 'not-allowed';

            }else{
                document.getElementById('emOk').innerHTML = "사용 가능한 이메일입니다.";
                document.getElementById('emOk').style.color = 'green';
                email = 1;

                //버튼 활성화
                verificationBtn.disabled=false;
                verificationButton.style.backgroundColor = '';
                verificationButton.style.cursor = '';
            }


        },
        error: function(error){
            alert("오류가 발생했습니다. 다시 시도해주세요");
        }


    });  //ajax 종료


}
