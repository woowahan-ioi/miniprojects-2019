function hover(element) {
    element.setAttribute('src', '/images/logo/youtube-upload-logo-hover.png');
}

function unHover(element) {
    element.setAttribute('src', '/images/logo/youtube-upload-logo.png');
}

function submitForm(){
    const title = document.forms["video-form"]["title"].value;
    const file = document.forms["video-form"]["file-input"].value;
    const description = document.forms["video-form"]["description"].value;

    if(checkValidation(title) && checkValidation(file) && checkValidation(description)) {
        document.querySelector("#video-submit-button").disabled = true;
        return true;
    }

    alert("빈 칸이 존재합니다. 다시 입력해주세요.");
    return false;
}

function checkValidation(data) {
    return !(data === '' || data === null);
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