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
    if(data === '' || data === null) {
        return false;
    }
    return true;
}