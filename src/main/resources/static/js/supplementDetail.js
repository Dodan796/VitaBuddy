document.addEventListener("DOMContentLoaded", function () {
    const toggleText = document.getElementById("toggleNutrition");
    const nutritionInfo = document.getElementById("nutritionInfo");

    toggleText.addEventListener("click", function () {
        nutritionInfo.classList.toggle("hidden");
        
        // 10/28 아이콘 변경
        const arrowIcon = toggleText.querySelector(".arrow i");
        if (arrowIcon.classList.contains("fa-caret-down")) {
            arrowIcon.classList.remove("fa-caret-down");
            arrowIcon.classList.add("fa-caret-up");
        } else {
            arrowIcon.classList.remove("fa-caret-up");
            arrowIcon.classList.add("fa-caret-down");
        }
    });
});
