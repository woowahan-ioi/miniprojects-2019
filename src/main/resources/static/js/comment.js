const commentButton = (function () {
    const CommentController = function () {
        const commentService = new CommentService();

        const saveComment = function () {
            const commentAddButton = document.querySelector('#comment-save-button');
            commentAddButton.addEventListener('click', commentService.save);
        };

        const commentToggle = function () {
            document.querySelector("#comment-cancel-button").addEventListener("click", commentService.toggleCommentCancel);
            document.querySelector("#comment-input-text").addEventListener("click", commentService.toggleCommentWrite);
            document.querySelector("#comment-input-text").addEventListener("keyup", commentService.toggleCommentSaveButton);
            document.querySelector("#comment-area").addEventListener("mouseover", commentService.toggleCommentMoreButton);
        }

        const init = function () {
            saveComment();
            commentToggle();
        };

        return {
            init: init
        }
    };

    const CommentService = function () {
        const articleId = 1;
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

        function toggleCommentMoreButton(event){
            if(event.target.className === "comment") {
                event.target.querySelector(".more-button").classList.remove("display-none");
            }
        }

        const save = (event) => {
            fetch('/watch/' + articleId + '/comments', {
                headers: {
                    'Content-type': 'application/json; charset=UTF-8'
                },
                method: 'POST',
                body: JSON.stringify({
                    contents: event.target.parentElement.parentElement.querySelector("INPUT").value
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
            }).catch(error => {
                error.text().then(json => alert(json))
            });
        };

        const appendComment = (comment) => {
            const commentTemplate = `<li class="comment mrg-btm-30">
                            <i class="user-logo"></i>
                            <div class="font-size-13">
                                <span class="user-name">${comment.authorName}</span>
                                <span class="update-date">${comment.updateDate}</span>
                            </div>
                            <span class="comment-contents font-size-15">${comment.contents}</span>
                            <div class="mrg-top-5">
                                <button class="like-btn">
                                    <i class="ti-thumb-up"></i>
                                </button>
                                <span>3.5천</span>
                                <button class="reply-toggle-btn">답글</button>
                                <div class="reply-area display-none">
                                    <div class="mrg-btm-10">
                                        <i class="ti-truck"></i>
                                        <input class="comment-input" type="text" placeholder="공개 답글 추가...">
                                    </div>
                                    <button class="btn comment-btn comment-save-btn disabled" value="${comment.id}">답글</button>
                                    <button class="btn comment-btn comment-cancel-btn">취소</button>
                                </div>
                            </div>
                        </li>`;
            const commentList = document.querySelector("#comment-area");
            commentList.insertAdjacentHTML("beforeend", commentTemplate);
        };

        return {
            save: save,
            toggleCommentCancel: toggleCommentCancel,
            toggleCommentWrite: toggleCommentWrite,
            toggleCommentSaveButton: toggleCommentSaveButton,
            toggleCommentMoreButton: toggleCommentMoreButton
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