function readMoreTag() {
    const dots = document.getElementById("dots");
    const moreText = document.getElementById("more");
    const btnText = document.getElementById("readMoreSpan");

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