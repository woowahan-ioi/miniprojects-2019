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

            fetch('/api/videos/' + videoId + '/comments', {
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
                appendComment(comment);
                let currentCommentCount = parseInt(commentCount.innerText)
                commentCount.innerText = String(currentCommentCount + 1);
                inputComment.value = "";
            }).catch(error => {
                error.text().then(json => alert(json))
            });
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

            fetch('/api/videos/' + videoId + '/comments/' + commentId, {
                headers: {
                    'Content-type': 'application/json; charset=UTF-8'
                },
                method: 'PUT',
                body: JSON.stringify({
                    contents: contents
                })
            }).then(response => {
                if (response.status === 204) {
                    toggleCommentMoreButton(event);
                    target.parentElement.previousElementSibling.querySelector(".comment-contents").innerText = contents;

                    const commentButtonDiv = event.target.parentElement;
                    commentButtonDiv.classList.toggle("display-none");
                    commentButtonDiv.previousElementSibling.classList.toggle("display-none");
                    commentButtonDiv.previousElementSibling.previousElementSibling.classList.toggle("display-none");
                } else {
                    throw response;
                }
            }).catch(error => {
                error.text().then(json => alert(json))
            });
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

            fetch('/api/videos/' + videoId + '/comments/' + commentId, {
                method: 'DELETE'
            }).then(response => {
                if (response.status === 204) {
                    toggleCommentMoreButton(event);
                    target.closest("li").remove();
                    let currentCommentCount = parseInt(commentCount.innerText)
                    commentCount.innerText = String(currentCommentCount - 1);
                } else {
                    throw response;
                }
            }).catch(error => {
                error.text().then(json => alert(json))
            });
        }

        const appendComment = (comment) => {
            const writtenTime = calculateWrittenTime(comment.updateTime);
            const commentList = document.querySelector("#comment-area");
            commentList.insertAdjacentHTML("beforeend", commentTemplate);
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