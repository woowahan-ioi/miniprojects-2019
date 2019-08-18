function hover(element) {
    element.setAttribute('src', '/images/logo/youtube-upload-logo-hover.png');
}

function unHover(element) {
    element.setAttribute('src', '/images/logo/youtube-upload-logo.png');
}

function move() {
    var elem = document.getElementById("progressBar");
    var width = 0;
    var id = setInterval(frame, 0);

    function frame() {
        if (width >= 100) {
            clearInterval(id);
        } else {
            width++;
            elem.style.width = width + '%';
            elem.innerHTML = width + '%';
        }
    }
}

$(".preview-file-input").on("change", function() {
    var previewImage = $(this).val().split("\\").pop();
    $(this).siblings(".custom-file-label").addClass("selected").html(previewImage);
});