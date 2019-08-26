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

            const requestUri = '/api/videos/' + videoId + '/comments/' + id + '/replies';
            const requestBody = {
                contents: inputComment.value
            };
            const callback = response => {
                if (response.status === 201) {
                    response.json().then(comment => {
                        appendReply(comment, event.target);
                        inputComment.value = "";
                        event.target.closest(".reply-edit").classList.add("display-none")
                    });

                    return;
                }
                throw response;
            }
            const handleError = error => {
                alert(error)
            }
            AjaxRequest.POST(requestUri, requestBody, callback, handleError)
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

            replyList.insertAdjacentHTML("beforeend", Templates.replyTemplate(reply, writtenTime));
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