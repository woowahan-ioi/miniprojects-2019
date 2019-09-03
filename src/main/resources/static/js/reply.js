const replyButton = (function () {
    const ReplyController = function () {
        const replyService = new ReplyService();
        const commentArea = document.querySelector("#comment-area");

        const saveReply = function () {
            commentArea.addEventListener("click", replyService.save);
        }

        const updateReply = function () {
            commentArea.addEventListener("click", replyService.update);
        }

        const deleteReply = function () {
            commentArea.addEventListener("click", replyService.delete);
        }

        const replyToggle = function () {
            commentArea.addEventListener("click", replyService.toggleReplyCancel);
            commentArea.addEventListener("click", replyService.toggleReplyWrite);
            commentArea.addEventListener("keyup", replyService.toggleReplySaveButton);
            commentArea.addEventListener("click", replyService.toggleReplyEditButton);
            commentArea.addEventListener("click", replyService.toggleReplyListButton);
        }

        const sortReplyByUpdateTime = function () {
            commentArea.addEventListener("click", replyService.sortReplyByUpdateTime)
        }

        const increaseLike = function () {
            commentArea.addEventListener('click', replyService.increaseLike);
        }

        const decreaseLike = function () {
            commentArea.addEventListener('click', replyService.decreaseLike);
        }

        const init = function () {
            replyToggle();
            saveReply();
            updateReply();
            deleteReply();
            sortReplyByUpdateTime();
            increaseLike();
            decreaseLike();
        };

        return {
            init: init
        }
    };

    const ReplyService = function () {
        const videoId = document.querySelector("#video-contents").dataset.videoid;

        const markReplytLike = (replyListDiv, reply) => {
            const replyId = reply.id;
            const replyList = replyListDiv.querySelector("li");

            if (!replyList.dataset.commentid === replyId) {
                return;
            }

            if (reply.likedUser) {
                replyList.querySelector(".reply-like-btn").classList.add("display-none");
                replyList.querySelector(".reply-dislike-btn").classList.remove("display-none");
            }

            if (!reply.likedUser) {
                replyList.querySelector(".reply-like-btn").classList.remove("display-none");
                replyList.querySelector(".reply-dislike-btn").classList.add("display-none");
            }
        }


        const sortReplyByUpdateTime = (event) => {
            let target = event.target;

            if (target.tagName === "I" || target.tagName === "SPAN") {
                target = target.parentElement;
            }

            if (!target.classList.contains("reply-list-open-button")) {
                return;
            }
            const commentList = target.closest("li");
            const commentId = commentList.dataset.commentid;
            const requestUri = `/api/videos/${videoId}/comments/${commentId}/replies/sort/updatetime`;

            const callback = (response) => {
                if (response.status === 200) {
                    response.json().then(data => {
                        const replyListDiv = commentList.querySelector(".reply-list");
                        replyListDiv.innerHTML = "";

                        for (const reply of data) {
                            appendReply(reply, target);
                            markReplytLike(replyListDiv, reply);
                        }

                        commentList.querySelector(".reply-list-open-button").classList.toggle("display-none");
                        commentList.querySelector(".reply-list-close-button").classList.toggle("display-none");
                        replyListDiv.classList.remove("display-none");
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
            if (!event.target.classList.contains("reply-save-btn")) {
                return;
            }

            if (event.target.classList.contains("disabled")) {
                return;
            }

            const commentList = event.target.closest("li");
            const commentid = commentList.dataset.commentid;
            const inputComment = event.target.closest(".reply-edit").querySelector(".comment-input");
            const requestUri = `/api/videos/${videoId}/comments/${commentid}/replies`;

            const requestBody = {
                contents: inputComment.value
            };
            const callback = (response) => {
                if (response.status === 201) {
                    response.json().then(comment => {
                        appendReply(comment, event.target);
                        inputComment.value = "";
                        event.target.closest(".reply-edit").classList.add("display-none")
                        commentList.querySelector("#reply-list-more-area").classList.toggle("display-none");
                    });

                    return;
                }
                throw response;
            }
            const handleError = (error) => {
                const errorJson = JSON.parse(error);
                alert(errorJson.message);
            }
            AjaxRequest.POST(requestUri, requestBody, callback, handleError)
        }

        function updateReply(event) {
            const target = event.target;

            if (!target.classList.contains("reply-update-btn")) {
                return;
            }
            const replyListDiv = target.closest("li");
            const replyId = replyListDiv.dataset.replyid;
            const commentId = target.closest("ul").closest("li").dataset.commentid;
            const inputEditReply = target.closest("div").querySelector("input");
            const requestUri = `/api/videos/${videoId}/comments/${commentId}/replies/${replyId}`;

            const requestBody = {
                contents: inputEditReply.value
            }

            const callback = (response) => {
                if (response.status === 204) {
                    replyListDiv.querySelector(".reply-contents").innerText = inputEditReply.value;
                    replyListDiv.querySelector(".reply-update-area").classList.toggle("display-none");
                    replyListDiv.querySelector(".comment-block").classList.toggle("display-none");
                    replyListDiv.querySelector(".reply-writer-img").classList.toggle("display-none");
                    return;
                }
                throw response;
            };

            const handleError = (error) => {
                const errorJson = JSON.parse(error);
                alert(errorJson.message);
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
            const requestUri = `/api/videos/${videoId}/comments/${commentId}/replies/${replyId}`;

            const callback = (response) => {
                if (response.status === 204) {
                    target.closest("li").remove();
                    return;
                }
                throw response;
            };

            const handleError = (error) => {
                const errorJson = JSON.parse(error);
                alert(errorJson.message);
            };

            AjaxRequest.DELETE(requestUri, callback, handleError);
        }

        function toggleReplyCancel(event) {
            if (event.target.classList.contains("reply-cancel-btn")) {
                event.target.closest("li").querySelector(".reply-edit").classList.toggle("display-none");
                event.target.closest("li").querySelector("#reply-list-more-area").classList.toggle("display-none");
            }
        }

        function toggleReplyWrite(event) {
            if (event.target.classList.contains("reply-toggle-btn")) {
                event.target.closest("li").querySelector(".reply-edit").classList.toggle("display-none");
                event.target.closest("li").querySelector("#reply-list-more-area").classList.toggle("display-none");
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
            replyListButton.parentElement.parentElement.querySelector(".reply-list").classList.add("display-none");
            replyListButton.parentElement.querySelector(".reply-list-open-button").classList.toggle("display-none");
        }

        function toggleReplyEditButton(event) {
            let target = event.target;
            if (target.tagName === "I" || target.tagName === "SPAN") {
                target = target.parentElement;
            }
            if (target.classList.contains("reply-update-cancel-btn")) {
                const replyButtonDiv = target.closest(".reply-update-area");
                replyButtonDiv.classList.toggle("display-none");
                replyButtonDiv.parentElement.querySelector(".comment-block").classList.toggle("display-none")
                replyButtonDiv.parentElement.querySelector(".reply-writer-img").classList.toggle("display-none");
            }
            if (target.classList.contains("reply-edit-button")) {
                const replyButtonDiv = target.closest(".reply-more-box");
                replyButtonDiv.closest(".comment-block").classList.toggle("display-none");
                replyButtonDiv.parentElement.parentElement.querySelector(".reply-writer-img").classList.toggle("display-none");
                replyButtonDiv.parentElement.parentElement.querySelector(".reply-update-area").classList.toggle("display-none");
            }
        }

        function appendReply(reply, target) {
            const writtenTime = calculateWrittenTime(reply.updateTime);
            const replyList = target.closest(".reply-area").querySelector(".reply-list");
            replyList.insertAdjacentHTML("afterbegin", Templates.replyTemplate(reply, writtenTime));
        }

        const increaseLike = (event) => {
            let target = event.target;

            if (target.tagName === "I") {
                target = target.parentElement;
            }

            if (!target.classList.contains("reply-like-btn")) {
                return;
            }

            const replyId = target.closest("li").dataset.replyid;
            const commentId = target.closest("li").parentElement.closest("li").dataset.commentid;
            const requestUri = `/api/videos/${videoId}/comments/${commentId}/replies/${replyId}/likes`;

            const requestBody = {};

            const callback = (response) => {
                if (response.status === 201) {
                    response.json().then(data => {
                        const replyLikeCountDiv = target.parentElement.querySelector(".reply-like-count");
                        replyLikeCountDiv.innerText = data.count;
                        target.parentElement.querySelector(".reply-like-btn").classList.add("display-none");
                        target.parentElement.querySelector(".reply-dislike-btn").classList.remove("display-none");
                    })
                    return;
                }
                throw response;
            };

            const handleError = (error) => {
                const errorJson = JSON.parse(error);
                alert(errorJson.message);
            };

            AjaxRequest.POST(requestUri, requestBody, callback, handleError);
        }

        const decreaseLike = (event) => {
            let target = event.target;

            if (target.tagName === "I") {
                target = target.parentElement;
            }

            if (!target.classList.contains("reply-dislike-btn")) {
                return;
            }

            const replyId = target.closest("li").dataset.replyid;
            const commentId = target.closest("li").parentElement.closest("li").dataset.commentid;
            const requestUri = `/api/videos/${videoId}/comments/${commentId}/replies/${replyId}/likes`;

            const callback = (response) => {
                if (response.status === 201) {
                    response.json().then(data => {
                        const replyLikeCountDiv = target.parentElement.querySelector(".reply-like-count");
                        replyLikeCountDiv.innerText = data.count;
                        target.parentElement.querySelector(".reply-dislike-btn").classList.add("display-none");
                        target.parentElement.querySelector(".reply-like-btn").classList.remove("display-none");
                    })
                    return;
                }
                throw response;
            };

            const handleError = (error) => {
                const errorJson = JSON.parse(error);
                alert(errorJson.message);
            };

            AjaxRequest.DELETE(requestUri, callback, handleError);
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
            sortReplyByUpdateTime: sortReplyByUpdateTime,
            increaseLike: increaseLike,
            decreaseLike: decreaseLike
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