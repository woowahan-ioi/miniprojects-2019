const replyButton = (function () {
    const ReplyController = function () {
        const replyService = new ReplyService();

        const saveReply = function () {
            document.querySelector("#comment-area").addEventListener("click", replyService.save);
        }

        const replyToggle = function () {
            document.querySelector("#comment-area").addEventListener("click", replyService.toggleReplyCancel);
            document.querySelector("#comment-area").addEventListener("click", replyService.toggleReplyWrite);
            document.querySelector("#comment-area").addEventListener("keyup", replyService.toggleReplySaveButton);
        }

        const init = function () {
            replyToggle();
            saveReply();
        };

        return {
            init: init
        }
    };

    const ReplyService = function () {
        const videoId = document.querySelector("#video-contents").dataset.videoid;

        function saveReply(event) {
            if(!event.target.classList.contains("reply-save-btn")) {
                return;
            }

            const id = event.target.closest("li").dataset.commentid;
            const inputComment = event.target.closest("div").querySelector("input");

            fetch('/api/videos/' + videoId + '/comments/' + id + '/replies', {
                headers: {
                    'Content-type': 'application/json; charset=UTF-8'
                },
                method: 'POST',
                body: JSON.stringify({
                    contents: inputComment.value
                })
            }).then(response => {
                if (response.status === 201) {
                    return response.json();
                }
                throw response;
            }).then(comment => {
                appendReply(comment, event.target);
                inputComment.value = "";
                event.target.closest(".reply-edit").classList.add("display-none")
            }).catch(error => {
                error.text().then(json => alert(json))
            });
        }

        function toggleReplyCancel(event) {
            if (event.target.classList.contains("reply-cancel-btn")) {
                event.target.closest("li").querySelector(".reply-edit").classList.add("display-none");
            }
        }

        function toggleReplyWrite(event) {
            if (event.target.classList.contains("reply-toggle-btn")) {
                event.target.closest("li").querySelector(".reply-edit").classList.remove("display-none");
            }
        }

        function toggleReplySaveButton(event) {
            if (event.target.classList.contains("comment-input") && event.target.value !== "") {
                event.target.parentElement.parentElement.querySelector(".edit").classList.remove("disabled")
                return;
            }
            event.target.parentElement.parentElement.querySelector(".edit").classList.add("disabled")
        }

        function appendReply(reply, target) {
            const writtenTime = calculateWrittenTime(reply.updateTime);

            const replyList = target.closest(".reply-area").querySelector(".reply-list");

            replyList.insertAdjacentHTML("beforeend", replyTemplate);
        }

        return {
            toggleReplyCancel: toggleReplyCancel,
            toggleReplyWrite: toggleReplyWrite,
            toggleReplySaveButton: toggleReplySaveButton,
            save: saveReply
        }
    };

    const init = function () {
        const buttonController = new ReplyController();
        buttonController.init();
    };

    return {
        init: init
    }
})();
replyButton.init();