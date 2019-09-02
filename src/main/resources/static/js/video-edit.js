function hover(element) {
    element.setAttribute('src', '/images/logo/youtube-upload-logo-hover.png');
}

function unHover(element) {
    element.setAttribute('src', '/images/logo/youtube-upload-logo.png');
}

function videoSubmit(event) {
    const submitButton = event.target;
    if(checkTextLength() === false) {
        return;
    }
    document.querySelector("#video-form").submit();
    submitButton.disabled = true;
}

function checkTextLength() {
    const textArea = document.querySelector("#description");
    if(textArea.value.length > 1000) {
        alert("1000자 이내의 내용을 입력해주세요.");
        textArea.value = textArea.value.substr(0, 1000);
        return false;
    }
    return true;
}

document.querySelector("#description").addEventListener("keyup", checkTextLength);
document.querySelector("#video-submit-button").addEventListener("click", videoSubmit);