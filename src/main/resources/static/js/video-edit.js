function hover(element) {
    element.setAttribute('src', '/images/logo/youtube-upload-logo-hover.png');
}

function unHover(element) {
    element.setAttribute('src', '/images/logo/youtube-upload-logo.png');
}

function move() {
    const elem = document.getElementById("progressBar");
    const width = 0;
    const id = setInterval(frame, 0);

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

function videoSubmit(event) {
    const submitButton = event.target;
    document.querySelector("#video-form").submit();
    submitButton.disabled = true;
}

document.querySelector("#video-submit-button").addEventListener("click", videoSubmit);