function deleteVideo(event) {
    let target = event.target;
    if(target.tagName === "I") {
        target = target.parentElement;
    }

    if(!target.classList.contains("remove-btn")) {
        return;
    }

    const id = target.dataset.videoid;

    fetch('/api/videos/' + id, new deleteHeaderData())
        .then(response => {
            if(response.status !== 204) {
                throw response.text();
            }
            target.closest(".video").remove()
        })
        .catch(error => {
                error.then((message) => alert(message))
            }
        );
}

function deleteHeaderData() {
    this.method = "DELETE",
    this.headers = {
        "Content-Type": "application/json;charset=UTF-8"
    }
}