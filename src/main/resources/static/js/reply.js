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

            console.log(inputComment)
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
                appendReply(comment, event);
                inputComment.value = "";
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

        function appendReply(reply, event) {
            const writtenTime = calculateWrittenTime(reply.updateTime);

            const replyTemplate = `<li class="reply mrg-btm-30" data-commentid="${reply.id}">
                            <img class="img-circle width-50 comment-writer-img" src="/images/default/eastjun_big.jpg" alt="">
                            <div class="comment-block">
                                <div class="font-size-13">
                                    <span class="user-name">${reply.authorName}</span>
                                    <span class="update-date">${writtenTime}</span>
                                </div>
                                <div class="comment-more-box">
                                    <button class="comment-more-buttons reply-edit-button">
                                        <i class="ti-pencil"> 수정</i>
                                    </button>
                                    <button class="comment-more-buttons reply-delete-button">
                                        <i class="ti-trash"> 삭제</i>
                                    </button>
                                </div>
                                <span class="reply-contents font-size-15">${reply.contents}</span>
                                <div>
                                    <button class="like-btn">
                                        <i class="ti-thumb-up"></i>
                                    </button>
                                    <span>3.5천</span>
                                </div>
                            </div>
                            <div class="comment-update-area display-none mrg-btm-50">
                                <div>
                                    <img class="img-circle width-50 comment-writer-img" src="/images/default/eastjun_big.jpg"
                                         alt="">
                                    <input class="comment-input" type="text" value="${reply.contents}">
                                </div>
                                <button class="btn comment-btn reply-update-cancel-btn">취소</button>
                                <button class="btn comment-btn edit reply-update-btn">수정</button>
                            </div>
                        </li>`;

            const replyList = event.target.closest("reply-area").querySelector(".reply-list");

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