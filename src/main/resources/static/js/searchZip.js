/* 회원정보 수정 폼 - 우편번호입력 (daumAPI)활용*/

function searchZip() {

    new daum.Postcode({
 			oncomplete:function(data){
 			//팝업창에서 검색결과 클릭했을 때
 			var address1="";
 			
 			//도로명 주소인 경우
 			if(data.userSelectedType=='R'){
 				address1 = data.roadAddress +"("+data.bname +data.buildingName+")";
 			}else{//지번 주소인 경우
 				address1 = data.jibunAddress;
 			}
 			//입력란에 우편번호, 주소1 출력
 			document.getElementById('userZipcode').value=data.zonecode;
 			document.getElementById('userAddress1').value=address1;
 			
 			//상세 주소 입력하도록 이미 입력되어져 있는 값 삭제하고 상세주소에 포커스
 			var address2 = document.getElementById('userAddress2');
 			address2.value="";
 			address2.focus(); 			
 		
 			}//function 끝
}).open();

}