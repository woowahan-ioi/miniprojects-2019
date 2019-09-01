function hover(element) {
    element.setAttribute('src', '/images/logo/youtube-upload-logo-hover.png');
}

function unHover(element) {
    element.setAttribute('src', '/images/logo/youtube-upload-logo.png');
}

function videoSubmit(event) {
    const submitButton = event.target;
    document.querySelector("#video-form").submit();
    submitButton.disabled = true;
}

document.querySelector("#video-submit-button").addEventListener("click", videoSubmit);