const commentButton = (function () {
    const CommentController = function () {
        const commentService = new CommentService();
        const commentArea = document.querySelector('#comment-area');

        const getComment = function () {
            document.addEventListener("DOMContentLoaded", commentService.sortCommentByUpdateTime);
        };

        const saveComment = function () {
            const commentAddButton = document.querySelector('#comment-save-button');
            commentAddButton.addEventListener('click', commentService.save);
        };

        const updateComment = function () {
            commentArea.addEventListener('click', commentService.update);
        };

        const deleteComment = function () {
            commentArea.addEventListener('click', commentService.delete);
        };

        const commentToggle = function () {
            document.querySelector("#comment-cancel-button").addEventListener("click", commentService.toggleCommentCancel);
            document.querySelector("#comment-input-text").addEventListener("click", commentService.toggleCommentWrite);
            document.querySelector("#comment-input-text").addEventListener("keyup", commentService.toggleCommentSaveButton);
            commentArea.addEventListener("click", commentService.toggleCommentEditButton);
        };

        const sortCommentByUpdateTime = function () {
            const commentAddButton = document.querySelector('#sort-comment-update-time');
            commentAddButton.addEventListener('click', commentService.sortCommentByUpdateTime);
        };

        const sortCommentByLikeCount = function () {
            const commentAddButton = document.querySelector('#sort-comment-like');
            commentAddButton.addEventListener('click', commentService.sortCommentByLikeCount);
        };

        const increaseLike = function () {
            commentArea.addEventListener('click', commentService.increaseLike);
        }

        const decreaseLike = function () {
            commentArea.addEventListener('click', commentService.decreaseLike);
        }

        const init = function () {
            saveComment();
            updateComment();
            deleteComment();
            commentToggle();
            sortCommentByUpdateTime();
            sortCommentByLikeCount();
            getComment();
            increaseLike();
            decreaseLike();
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
            if (event.target.classList.contains("comment-input") && event.target.value !== "") {
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
            if (target.tagName === "I" || target.tagName === "SPAN") {
                target = target.parentElement;
            }
            if (target.classList.contains("comment-update-cancel-btn") || target.classList.contains("comment-edit-button")) {
                const commentList = target.closest("li");
                commentList.querySelector(".reply-area").classList.toggle("display-none");
                commentList.querySelector(".comment-update-area").classList.toggle("display-none");
                commentList.querySelector(".comment-block").classList.toggle("display-none");
                commentList.querySelector(".comment-writer-img").classList.toggle("display-none");
            }
        }

        const sortCommentByUpdateTime = () => {
            const requestUri = `/api/videos/${videoId}/comments/sort/updatetime`;

            const callback = (response) => {
                const commentListDiv = document.querySelector("#comment-area");
                commentListDiv.innerHTML = "";

                if (response.status === 200) {
                    response.json().then(data => {
                        let count = 0;
                        for (const comment of data) {
                            appendComment(comment);
                            markCommentLike(commentListDiv, comment);
                            count++;
                        }
                        commentCount.innerText = count;
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

        const sortCommentByLikeCount = () => {
            const requestUri = `/api/videos/${videoId}/comments/sort/likecount`;
            const callback = (response) => {
                const commentListDiv = document.querySelector("#comment-area");
                commentListDiv.innerHTML = "";
                if (response.status === 200) {
                    response.json().then(data => {
                        let count = 0;
                        for (const comment of data) {
                            appendComment(comment);
                            markCommentLike(commentListDiv, comment);
                            count++;
                        }
                        commentCount.innerText = count;
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

        const saveComment = (event) => {
            if (event.target.classList.contains("disabled")) {
                return;
            }

            const inputComment = event.target.parentElement.parentElement.querySelector(".comment-input");
            const requestUri = `/api/videos/${videoId}/comments`;
            const requestBody = {
                contents: inputComment.value
            };
            const callback = (response) => {
                if (response.status === 201) {
                    response.json().then(comment => {
                        appendComment(comment);
                        const currentCommentCount = parseInt(commentCount.innerText)
                        commentCount.innerText = String(currentCommentCount + 1);
                        inputComment.value = "";
                    });
                    return;
                }
                throw response;
            };
            const handleError = (error) => {
                const errorJson = JSON.parse(error);
                alert(errorJson.message);
            };

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

            const commentList = target.closest("li");
            const commentId = commentList.dataset.commentid;
            const contents = commentList.querySelector(".comment-input").value;

            const requestUri = `/api/videos/${videoId}/comments/${commentId}`;
            const requestBody = {
                contents: contents
            };
            const callback = (response) => {
                if (response.status === 204) {
                    toggleCommentMoreButton(event);
                    commentList.querySelector(".comment-contents").innerText = contents;
                    commentList.querySelector(".comment-update-area").classList.toggle("display-none");
                    commentList.querySelector(".reply-area").classList.toggle("display-none");
                    commentList.querySelector(".comment-block").classList.toggle("display-none");
                    commentList.querySelector(".comment-writer-img").classList.toggle("display-none");
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

        const deleteComment = (event) => {
            let target = event.target;

            if (target.tagName === "I" || target.tagName === "SPAN") {
                target = target.parentElement;
            }

            if (!target.classList.contains("comment-delete-button")) {
                return;
            }

            const commentList = target.closest("li");
            const commentId = commentList.dataset.commentid;

            const requestUri = `/api/videos/${videoId}/comments/${commentId}`;
            const callback = (response) => {
                if (response.status === 204) {
                    toggleCommentMoreButton(event);
                    commentList.remove();
                    let currentCommentCount = parseInt(commentCount.innerText)
                    commentCount.innerText = String(currentCommentCount - 1);

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

        const appendComment = (comment) => {
            const writtenTime = calculateWrittenTime(comment.updateTime);
            const commentList = document.querySelector("#comment-area");
            commentList.insertAdjacentHTML("afterbegin", Templates.commentTemplate(comment, writtenTime));
        };

        const markCommentLike = (commentListDiv, comment) => {
            const commentId = comment.id;
            const commentList = commentListDiv.querySelector("li");

            if (!commentList.dataset.commentid === commentId) {
                return;
            }

            if (comment.likedUser) {
                commentList.querySelector(".comment-like-btn").classList.add("display-none");
                commentList.querySelector(".comment-dislike-btn").classList.remove("display-none");
            }

            if (!comment.likedUser) {
                commentList.querySelector(".comment-like-btn").classList.remove("display-none");
                commentList.querySelector(".comment-dislike-btn").classList.add("display-none");
            }

        }

        const increaseLike = (event) => {
            let target = event.target;

            if (target.tagName === "I") {
                target = target.parentElement;
            }

            if (!target.classList.contains("comment-like-btn")) {
                return;
            }
            const commentList = target.closest("li");
            const commentId = commentList.dataset.commentid;
            const requestUri = `/api/videos/${videoId}/comments/${commentId}/likes`;

            const requestBody = {};

            const callback = (response) => {
                if (response.status === 201) {
                    response.json().then(data => {
                        const commentLikeCountDiv = commentList.querySelector(".comment-like-count");
                        commentLikeCountDiv.innerText = data.count;
                        commentList.querySelector(".comment-like-btn").classList.add("display-none");
                        commentList.querySelector(".comment-dislike-btn").classList.remove("display-none");
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

            if (!target.classList.contains("comment-dislike-btn")) {
                return;
            }

            const commentList = target.closest("li");
            const commentId = commentList.dataset.commentid;
            const requestUri = `/api/videos/${videoId}/comments/${commentId}/likes`;

            const callback = (response) => {
                if (response.status === 201) {
                    response.json().then(data => {
                        const commentLikeCountDiv = commentList.querySelector(".comment-like-count");
                        commentLikeCountDiv.innerText = data.count;
                        commentList.querySelector(".comment-dislike-btn").classList.add("display-none");
                        commentList.querySelector(".comment-like-btn").classList.remove("display-none");
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
            save: saveComment,
            update: updateComment,
            delete: deleteComment,
            toggleCommentCancel: toggleCommentCancel,
            toggleCommentWrite: toggleCommentWrite,
            toggleCommentSaveButton: toggleCommentSaveButton,
            toggleCommentEditButton: toggleCommentEditButton,
            sortCommentByUpdateTime: sortCommentByUpdateTime,
            sortCommentByLikeCount: sortCommentByLikeCount,
            increaseLike: increaseLike,
            decreaseLike: decreaseLike
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