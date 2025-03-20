/**
 * 회원정보 수정 폼 : 정합성 검사 
 */
 
		var pwd = 0;
		function pwCheck(){
			var pw3 = document.getElementById('userPwd').value ;
			if(pw3.length<8){
				document.getElementById('pwLeng').innerHTML="비밀번호는 8자리 이상이어야 합니다.";
		    	document.getElementById('pwLeng').style.color='red';
			}else{
				document.getElementById('pwLeng').innerHTML="";
			}
			if(document.getElementById('userPwd').value != '' && document.getElementById('confirmPwd').value != ''){
				if(document.getElementById('userPwd').value == document.getElementById('confirmPwd').value){
				document.getElementById('pwOk').innerHTML="비밀번호가 일치합니다.";
				document.getElementById('pwOk').style.color='green';
				pwd = 1;
				 }else{
		    	document.getElementById('pwOk').innerHTML="비밀번호가 일치하지 않습니다.";
		    	document.getElementById('pwOk').style.color='red';  
		    	pwd = 0;
		    	}
		    }
		}

		var email = 0;
		
		function emailCheck(){
			if(document.getElementById('userEmail').value.indexOf("@")>0){
				document.getElementById('emOk').innerHTML="";
				email = 1;
			}else{
				document.getElementById('emOk').innerHTML="올바른 이메일 형식이 아닙니다.";
		    	document.getElementById('emOk').style.color='red'; 
				email = 0;
			}
		}