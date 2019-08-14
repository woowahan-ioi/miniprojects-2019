function readMoreTag() {
    var dots = document.getElementById("dots");
    var moreText = document.getElementById("more");
    var btnText = document.getElementById("readMoreSpan");

    if (dots.style.display === "none") {
        dots.style.display = "inline";
        btnText.innerHTML = "더보기";
        moreText.style.display = "none";
    } else {
        dots.style.display = "none";
        btnText.innerHTML = "간략히";
        moreText.style.display = "inline";
    }
}