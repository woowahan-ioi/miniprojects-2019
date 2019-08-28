const replyButton = (function () {
    const ReplyController = function () {
        const replyService = new ReplyService();

        const saveReply = function () {
            document.querySelector("#comment-area").addEventListener("click", replyService.save);
        }

        const updateReply = function () {
            document.querySelector("#comment-area").addEventListener("click", replyService.update);
        }

        const deleteReply = function () {
            document.querySelector("#comment-area").addEventListener("click", replyService.delete);
        }

        const replyToggle = function () {
            document.querySelector("#comment-area").addEventListener("click", replyService.toggleReplyCancel);
            document.querySelector("#comment-area").addEventListener("click", replyService.toggleReplyWrite);
            document.querySelector("#comment-area").addEventListener("keyup", replyService.toggleReplySaveButton);
            document.querySelector("#comment-area").addEventListener("click", replyService.toggleReplyEditButton);
            document.querySelector("#comment-area").addEventListener("click", replyService.toggleReplyListButton);
        }

        const sortReplyByUpdateTime = function () {
            document.querySelector("#comment-area").addEventListener("click", replyService.sortReplyByUpdateTime)
        }

        const init = function () {
            replyToggle();
            saveReply();
            updateReply();
            deleteReply();
            sortReplyByUpdateTime();
        };

        return {
            init: init
        }
    };

    const ReplyService = function () {
        const videoId = document.querySelector("#video-contents").dataset.videoid;

        const sortReplyByUpdateTime = (event) => {
            let target = event.target;

            if (target.tagName === "I" || target.tagName === "SPAN") {
                target = target.parentElement;
            }

            if(!target.classList.contains("reply-list-open-button")) {
                return;
            }
            const commentId = target.parentElement.closest("li").dataset.commentid;
            const requestUri = '/api/videos/' + videoId + '/comments/' + commentId +'/replies/sort/updatetime';

            const callback = (response) => {
                if (response.status === 200) {
                    response.json().then(data => {
                        const replyListDiv = target.parentElement.nextElementSibling;
                        $(replyListDiv).empty();

                        console.log(data);
                        for (const reply of data) {
                            appendReply(reply, target);
                        }

                        target.classList.toggle("display-none");
                        target.nextElementSibling.classList.toggle("display-none");
                        target.parentElement.nextElementSibling.classList.remove("display-none");
                    });
                    return;
                }
                throw response;
            };

            const handleError = (error) => {
                alert(error);
            };

            AjaxRequest.GET(requestUri, callback, handleError);
        }

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

        function updateReply(event) {
            let target = event.target;

            if(!target.classList.contains("reply-update-btn")) {
                return;
            }
            const replyId = target.closest("li").dataset.replyid;
            const commentId = target.closest("ul").parentElement.parentElement.dataset.commentid;
            const inputEditReply = target.closest("div").querySelector("input");
            const requestUri = '/api/videos/' + videoId + '/comments/' + commentId + '/replies/' + replyId;

            const requestBody = {
                contents : inputEditReply.value
            }

            const callback = (response) => {
                if (response.status === 204) {

                    target.parentElement.previousElementSibling.querySelector(".reply-contents").innerText = inputEditReply.value;

                    const replyButtonDiv = target.parentElement;
                    replyButtonDiv.classList.toggle("display-none");
                    replyButtonDiv.previousElementSibling.classList.toggle("display-none");
                    replyButtonDiv.previousElementSibling.previousElementSibling.classList.toggle("display-none");

                    return;
                }
                throw response;
            };

            const handleError = error => {
                alert(error)
            };

            AjaxRequest.PUT(requestUri, requestBody, callback, handleError);
        }

        function deleteReply(event) {
            let target = event.target;

            if (target.tagName === "I" || target.tagName === "SPAN") {
                target = target.parentElement;
            }

            if (!target.classList.contains("reply-delete-button")) {
                return;
            }

            const replyId = target.closest("li").dataset.replyid;
            const commentId = target.closest("ul").closest("li").dataset.commentid;
            const requestUri = '/api/videos/' + videoId + '/comments/' + commentId + '/replies/' + replyId;

            const callback = (response) => {
                if (response.status === 204) {
                    target.closest("li").remove();
                    return;
                }
                throw response;
            };

            const handleError = (error) => {
                alert(error);
            };

            AjaxRequest.DELETE(requestUri, callback, handleError);
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

        function toggleReplyListButton(event) {
            let replyListButton = event.target;

            if (replyListButton.tagName === "I" || replyListButton.tagName === "SPAN") {
                replyListButton = replyListButton.parentElement;
            }

            if (!replyListButton.classList.contains("reply-list-close-button")) {
                return;
            }

            replyListButton.classList.toggle("display-none");
            replyListButton.parentElement.nextElementSibling.classList.add("display-none");
            replyListButton.previousElementSibling.classList.toggle("display-none");
        }

        function toggleReplyEditButton(event) {
            let target = event.target;
            if(target.tagName === "I" || target.tagName === "SPAN") {
                target = target.parentElement;
            }
            if (target.classList.contains("reply-update-cancel-btn")) {
                const replyButtonDiv = target.parentElement;
                replyButtonDiv.classList.toggle("display-none");
                replyButtonDiv.previousElementSibling.classList.toggle("display-none");
                replyButtonDiv.previousElementSibling.previousElementSibling.classList.toggle("display-none");
            }
            if (target.classList.contains("reply-edit-button")) {
                const replyButtonDiv = target.parentElement.parentElement;
                replyButtonDiv.parentElement.classList.toggle("display-none");
                replyButtonDiv.parentElement.previousElementSibling.classList.toggle("display-none");
                replyButtonDiv.parentElement.nextElementSibling.classList.toggle("display-none");
            }
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
            toggleReplyEditButton: toggleReplyEditButton,
            toggleReplyListButton: toggleReplyListButton,
            save: saveReply,
            update: updateReply,
            delete: deleteReply,
            sortReplyByUpdateTime: sortReplyByUpdateTime
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