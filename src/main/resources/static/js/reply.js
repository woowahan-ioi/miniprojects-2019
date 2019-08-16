const replyButton = (function () {
    const ReplyController = function () {
        const replyService = new ReplyService();

        const replyToggle = function () {
            document.querySelector("#comment-area").addEventListener("click", replyService.toggleReplyCancel);
            document.querySelector("#comment-area").addEventListener("click", replyService.toggleReplyWrite);
            document.querySelector("#comment-area").addEventListener("keyup", replyService.toggleReplySaveButton);
        }

        const init = function () {
            replyToggle();
        };

        return {
            init: init
        }
    };

    const ReplyService = function () {

        function toggleReplyCancel(event) {
            if (event.target.classList.contains("comment-cancel-btn")) {
                event.target.parentElement.classList.add("display-none");
            }
        }

        function toggleReplyWrite(event) {
            if (event.target.className === "reply-toggle-btn") {
                event.target.nextElementSibling.classList.remove("display-none");
            }
        }

        function toggleReplySaveButton(event) {
            if (event.target.className === "comment-input" && event.target.value !== "") {
                event.target.parentElement.parentElement.querySelector(".edit").classList.remove("disabled")
                return;
            }
            event.target.parentElement.parentElement.querySelector(".edit").classList.add("disabled")
        }

        return {
            toggleReplyCancel: toggleReplyCancel,
            toggleReplyWrite: toggleReplyWrite,
            toggleReplySaveButton: toggleReplySaveButton
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