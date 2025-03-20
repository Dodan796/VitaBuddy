/**
 * 폰트 사이즈 js => body로 감쌀시, header까지 폰트가 변화. JSP마다 wrap의 유무가 다름. 도입시, main-content div 태그를 이용해 새롭게 감싸야 함. 
 */
function adjustFontSize(size) {
    document.getElementById('main-content').style.fontSize = size + 'em';
}

