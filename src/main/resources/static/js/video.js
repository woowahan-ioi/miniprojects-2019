function readMoreTag() {
    const desc = document.getElementById("description");
    const descStorage = document.querySelector("#description-storage");

    if (descStorage.innerText.length > 30) {
        desc.innerText = descStorage.innerText.substr(0, 50) + " ...";
        document.querySelector("#readMoreSpan").addEventListener("click", readMoreTagEvent);
        return;
    }

    desc.innerText = descStorage.innerText;
}

function readMoreTagEvent() {
    const desc = document.getElementById("description");
    const readMoreSpan = document.querySelector("#readMoreSpan");

    if (readMoreSpan.classList.contains("clicked")) {
        readMoreSpan.innerText = "간략히";
        readMoreSpan.classList.remove("clicked");

        desc.innerText = document.querySelector("#description-storage").innerText;
        return;
    }

    readMoreSpan.innerText = "더보기";
    readMoreSpan.classList.add("clicked");
    desc.innerText = document.querySelector("#description-storage").innerText.substr(0, 50) + " ...";
}

readMoreTag();