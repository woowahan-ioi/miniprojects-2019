function readMoreTag() {
    const desc = document.getElementById("description");
    const descStorage = document.querySelector("#description-storage");

    if(descStorage.innerText.length > 30) {
        desc.innerText = descStorage.innerText.substr(0, 50) + " ...";
        document.querySelector("#readMoreSpan").addEventListener("click", readMoreTagEvent);
        return;
    }

    desc.innerText = descStorage.innerText;
}

function readMoreTagEvent() {
    const desc = document.getElementById("description");
    const readMoreSpan = document.querySelector("#readMoreSpan");

    if(readMoreSpan.classList.contains("clicked")) {
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

window.onload = function() {
    videoUpdateTime();
    getLikeCount();
}

function videoUpdateTime() {
    const date = (new Date(/*[[${video.createTime}]]*/).toLocaleDateString());
    document.getElementById("videoCreateTime").innerText = date;
}

function getLikeCount() {
    const videoId = document.querySelector("#video-contents").dataset.videoid;
    const requestUri = '/api/videos/' + videoId + '/likes/counts';

    const callback = (response) => {
        if(response.status === 200) {
            response.json()
                .then(data => {
                    const count = data.count;
                    document.querySelector("#likeCount").innerText = count;
                });
        }
    }

    const requestBody = {
    };

    const handleError = (error) => {
        alert(error);
    }

    AjaxRequest.POST(requestUri, requestBody, callback, handleError)
}

const videoButton = (function() {
    const VideoController = function () {
        const videoService = new VideoService();

        const increaseLike = function () {
            const videoLikeButton = document.querySelector('#title-like-btn');
            videoLikeButton.addEventListener('click', videoService.increase);
        }

        const videoToggle = function () {
            document.querySelector('#title-like-btn').addEventListener('click', videoService.toggleVideoLike);
        }

        const init = function () {
            increaseLike();
            videoToggle();
        }

        return {
            init: init,
        }
    };

    const VideoService = function () {
        function toggleVideoLike(count) {
            document.querySelector("#likeCount").innerText = count;
        };

        const increaseLike = () => {
            const videoId = document.querySelector("#video-contents").dataset.videoid;
            const requestUri = '/api/videos/' + videoId + '/likes';

            const callback = (response) => {
                if(response.status === 200) {
                    response.json().then(data => { const count = data.count;
                        document.querySelector("#likeCount").innerText = count;});
                }
            }

            const requestBody = {
            };

            const handleError = (error) => {
                alert(error);
            }

            AjaxRequest.POST(requestUri, requestBody, callback, handleError)
        }

        return {
            increase: increaseLike,
            toggleVideoLike: toggleVideoLike
        }
    }

    const init = function() {
        const videoButtonController = new VideoController();
        videoButtonController.init();
    };

    return {
        init: init
    }
})();
videoButton.init();