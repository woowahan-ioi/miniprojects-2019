const commentButton = (function () {
    const CommentController = function () {
        const commentService = new CommentService();

        const saveComment = function () {
            const commentAddButton = document.querySelector('#comment-save-button');
            commentAddButton.addEventListener('click', commentService.save);
        };

        const updateComment = function () {
            const commentArea = document.querySelector('#comment-area');
            commentArea.addEventListener('click', commentService.update);
        };

        const deleteComment = function () {
            const commentArea = document.querySelector('#comment-area');
            commentArea.addEventListener('click', commentService.delete);
        }

        const commentToggle = function () {
            document.querySelector("#comment-cancel-button").addEventListener("click", commentService.toggleCommentCancel);
            document.querySelector("#comment-input-text").addEventListener("click", commentService.toggleCommentWrite);
            document.querySelector("#comment-input-text").addEventListener("keyup", commentService.toggleCommentSaveButton);
            document.querySelector("#comment-area").addEventListener("mouseover", commentService.toggleCommentMoreButton);

            document.querySelector("#comment-area").addEventListener("click", commentService.toggleCommentEditButton);
        }

        const init = function () {
            saveComment();
            updateComment();
            commentToggle();
            deleteComment();
        };

        return {
            init: init
        }
    };

    const CommentService = function () {
        const videoId = document.querySelector("#video-contents").dataset.videoid;
        const commentCount = document.querySelector("#comment-count");

        function toggleCommentCancel(event) {
            if (event.target.tagName === "BUTTON") {
                document.querySelector("#comment-button-area").classList.add("display-none");
            }
        }

        function toggleCommentWrite(event) {
            if (event.target.tagName === "INPUT") {
                document.querySelector("#comment-button-area").classList.remove("display-none");
            }
        }

        function toggleCommentSaveButton(event) {
            if (event.target.className === "comment-input" && event.target.value !== "") {
                document.querySelector("#comment-save-button").classList.remove("disabled")
                return;
            }
            document.querySelector("#comment-save-button").classList.add("disabled")
        }

        function toggleCommentMoreButton(event) {
            if (event.target.className === "comment") {
                event.target.querySelector(".more-button").classList.remove("display-none");
            }
        }

        const toggleCommentEditButton = (event) => {
            let target = event.target;
            if (target.tagName === "I") {
                target = target.parentElement;
            }
            if (target.classList.contains("comment-update-cancel-btn")) {
                const commentButtonDiv = target.parentElement;
                commentButtonDiv.classList.toggle("display-none");
                commentButtonDiv.previousElementSibling.classList.toggle("display-none");
                commentButtonDiv.previousElementSibling.previousElementSibling.classList.toggle("display-none");
            }
            if (target.classList.contains("comment-edit-button")) {
                const commentButtonDiv = target.parentElement;
                commentButtonDiv.parentElement.classList.toggle("display-none");
                commentButtonDiv.parentElement.previousElementSibling.classList.toggle("display-none");
                commentButtonDiv.parentElement.nextElementSibling.classList.toggle("display-none");
            }
        }

        const saveComment = (event) => {
            const inputComment = event.target.parentElement.parentElement.querySelector("INPUT");
            const requestUri = '/api/videos/' + videoId + '/comments';
            const requestBody = {
                contents: inputComment.value
            };
            const callback = (response) => {
                if (response.status === 201) {
                    response.json().then(comment => {
                        appendComment(comment);
                        let currentCommentCount = parseInt(commentCount.innerText)
                        commentCount.innerText = String(currentCommentCount + 1);
                        inputComment.value = "";
                    })
                    return;
                }
                throw response;
            };
            const handleError = (error) => {
                alert(error);
            }

            AjaxRequest.POST(requestUri, requestBody, callback, handleError);
        };

        const updateComment = (event) => {
            let target = event.target;

            if (target.tagName === "I") {
                target = target.parentElement;
            }

            if (!target.classList.contains("comment-update-btn")) {
                return;
            }

            const commentId = target.closest("li").dataset.commentid;
            const contents = target.parentElement.querySelector("INPUT").value;

            const requestUri = '/api/videos/' + videoId + '/comments/' + commentId;
            const requestBody = {
                contents: contents
            };
            const callback = (response) => {
                if (response.status === 204) {

                    toggleCommentMoreButton(event);
                    target.parentElement.previousElementSibling.querySelector(".comment-contents").innerText = contents;

                    const commentButtonDiv = event.target.parentElement;
                    commentButtonDiv.classList.toggle("display-none");
                    commentButtonDiv.previousElementSibling.classList.toggle("display-none");
                    commentButtonDiv.previousElementSibling.previousElementSibling.classList.toggle("display-none");

                    return;
                }
                throw response;
            };
            const handleError = (error) => {
                alert(error);
            };

            AjaxRequest.PUT(requestUri, requestBody, callback, handleError);
        }

        const deleteComment = (event) => {
            let target = event.target;

            if (target.tagName === "I") {
                target = target.parentElement;
            }

            if (!target.classList.contains("comment-delete-button")) {
                return;
            }

            const commentId = target.closest("li").dataset.commentid;

            const requestUri = '/api/videos/' + videoId + '/comments/' + commentId;
            const callback = (response) => {
                if (response.status === 204) {
                    toggleCommentMoreButton(event);
                    target.closest("li").remove();
                    let currentCommentCount = parseInt(commentCount.innerText)
                    commentCount.innerText = String(currentCommentCount - 1);

                    return;
                }
                throw response;
            };
            const handleError = (error) => {
                alert(error);
            };

            AjaxRequest.DELETE(requestUri, callback, handleError);
        }

        const appendComment = (comment) => {
            const writtenTime = calculateWrittenTime(comment.updateTime);
            const commentList = document.querySelector("#comment-area");
            commentList.insertAdjacentHTML("beforeend", Templates.commentTemplate(comment, writtenTime));
        };

        return {
            save: saveComment,
            update: updateComment,
            delete: deleteComment,
            toggleCommentCancel: toggleCommentCancel,
            toggleCommentWrite: toggleCommentWrite,
            toggleCommentSaveButton: toggleCommentSaveButton,
            toggleCommentMoreButton: toggleCommentMoreButton,
            toggleCommentEditButton: toggleCommentEditButton
        }
    };

    const init = function () {
        const buttonController = new CommentController();
        buttonController.init();
    };

    return {
        init: init
    }
})();
commentButton.init();