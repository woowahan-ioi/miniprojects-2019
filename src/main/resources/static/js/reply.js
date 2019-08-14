// const replyTemplate = `<li class="reply mrg-btm-30">
//                         <i class="user-logo"></i>
//                         <div class="font-size-15">
//                             <span class="user-name">${authorName}</span>
//                             <span class="update-date">${updateDate}</span>
//                         </div>
//                         <span class="reply-contents font-size-20">${contents}</span>
//                         <div class="mrg-top-20">
//                             <button class="like-btn">
//                                 <i class="ti-thumb-up"></i>
//                             </button>
//                             <span>3.5천</span>
//                             <button class="reply-toggle-btn">답글</button>
//                             <div class="reply-area display-none">
//                                 <div class="mrg-btm-10">
//                                     <i class="ti-truck"></i>
//                                     <input class="reply-input" type="text" placeholder="공개 답글 추가...">
//                                 </div>
//                                 <button class="btn reply-btn reply-save-btn disabled" value="${id}">답글</button>
//                                 <button class="btn reply-btn reply-cancel-btn">취소</button>
//                             </div>
//                         </div>
//                     </li>`;

const replyButton = (function () {
    const ReplyController = function () {
        const replyService = new ReplyService();

        // const readReplys = function () {
        //     onload = () => {
        //         replyService.read();
        //     };
        // };

        // const saveReply = function () {
        //     const replyAddButton = document.querySelector('#save-reply-button');
        //     replyAddButton.addEventListener('click', replyService.save);
        // };

        // const updateReply = function() {
        //     const replyList = document.getElementById('reply-list');
        //     replyList.addEventListener('click', replyService.modifyToggle);
        //     replyList.addEventListener('click', replyService.modify);
        // };
        //
        // const removeReply = function() {
        //     const replyList = document.getElementById('reply-list');
        //     replyList.addEventListener('click', replyService.remove);
        // };

        const replyToggle = function () {
            document.querySelector("#comment-area").addEventListener("click", replyService.toggleReplyCancel);
            document.querySelector("#comment-area").addEventListener("click", replyService.toggleReplyWrite);
            document.querySelector("#comment-area").addEventListener("keyup", replyService.toggleReplySaveButton);
        }

        const init = function () {
            // readReplys();
            //  saveReply();
            // updateReply();
            // removeReply();
            replyToggle();
        };

        return {
            init: init
        }
    };

    const ReplyService = function () {
        // const read = () => {
        //     fetch('/articles/' + articleId + '/reply')
        //         .then(response => {
        //             if(response.status === HttpStatus.OK){
        //                 return response.json();
        //             }
        //             throw response;
        //         }).then(replys => {
        //         const replySize = replys.length;
        //         for(let i = 0; i < replySize; i++){
        //             appendReply(replys[i]);
        //         }
        //         replyCount.innerText = String(replySize);
        //     }).catch(response => {response.json().then(json => alert(json.message))});
        // };

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
                event.target.closest("div").nextElementSibling.classList.remove("disabled")
                return;
            }
            event.target.closest("div").nextElementSibling.classList.add("disabled")
        }

        // const save = (event) => {
        //     fetch('/articles/' + articleId + '/reply', {
        //         headers: {
        //             'Content-type': 'application/json; charset=UTF-8'
        //         },
        //         method: 'POST',
        //         body: JSON.stringify({
        //             contents: editor.getMarkdown()
        //         })
        //     }).then(response => {
        //         if(response.status === HttpStatus.CREATED){
        //             return response.json();
        //         }
        //         throw response;
        //     }).then(reply => {
        //         appendReply(reply);
        //         let currentReplyCount = parseInt(replyCount.innerText)
        //         replyCount.innerText = String(currentReplyCount + 1);
        //     }).catch(error => {error.json().then(json => alert(json.message))});
        // };

        // const modify = (event) => {
        //     if (!event.target.classList.contains('reply-modify-button')) {
        //         return;
        //     }
        //     const replyId = event.target.value;
        //     const inputReply = document.querySelector('#reply-' + replyId);
        //     fetch('/articles/' + articleId + '/reply/' + replyId, {
        //         headers: {
        //             'Content-type': 'application/json; charset=UTF-8'
        //         },
        //         method: 'PUT',
        //         body: JSON.stringify({
        //             contents: inputReply.value
        //         })
        //     }).then(response => {
        //         if (response.status === HttpStatus.OK) {
        //             return response.json();
        //         }
        //         throw response;
        //     }).then(json => {
        //         const replyContents = document.querySelector('#reply-'+replyId+'-contents');
        //         replyContents.innerText = json.contents;
        //     }).catch(error => {
        //         error.json().then(json => alert(json.message));
        //     });
        // };
        //
        // const remove = (event) => {
        //     if (!event.target.classList.contains('reply-remove-button')) {
        //         return;
        //     }
        //     const replyId = event.target.value;
        //     fetch('/articles/' + articleId + '/reply/' + replyId, {
        //         method: 'DELETE'
        //     }).then(response => {
        //         if (response.status === HttpStatus.OK) {
        //             return response.text();
        //         }
        //         throw response;
        //     }).then(text => {
        //         event.target.closest("LI").remove();
        //         let currentReplyCount = parseInt(replyCount.innerText);
        //         replyCount.innerText = String(currentReplyCount - 1);
        //         alert(text);
        //     }).catch(error => {
        //         error.json().then(json => alert(json.message));
        //     });
        // };

        // const appendReply = (reply) => {
        //     const replyList = document.querySelector('#reply-list');
        //     const replyTemplate = document.querySelector('#reply-template').innerText;
        //     const buttonsTemplate = document.querySelector('#reply-buttons-template').innerText;
        //     var compiledReplyTemplate = compile(reply, replyTemplate);
        //     if (reply.writer.id === sessionUser.id) {
        //         compiledReplyTemplate = compiledReplyTemplate.split('{{buttons}}').join(compile(reply, buttonsTemplate));
        //     } else {
        //         compiledReplyTemplate = compiledReplyTemplate.split('{{buttons}}').join('');
        //     }
        //     replyList.insertAdjacentHTML("beforeend", compiledReplyTemplate);
        // };

        // function compile(object, template) {
        //     const matchesFromTemplate = template.match(/{{\s*[\w\.]+\s*}}/g);
        //     if (matchesFromTemplate !== null) {
        //         let matchResults = matchesFromTemplate.map((matches) => matches.match(/[\w\.]+/)[0]);
        //         for (let matches = matchResults.values(), matchIterator = matches.next(); !matchIterator.done; matchIterator = matches.next()) {
        //             let resolvedValue = resolve(matchIterator.value, object);
        //             if (resolvedValue) {
        //                 template = template.replace('{{' + matchIterator.value + '}}',resolvedValue);
        //             }
        //         }
        //     }
        //     return template;
        // };
        //
        // function resolve (path, obj) {
        //     return path.split('.').reduce(function(prev, curr) {
        //         return prev ? prev[curr] : null
        //     }, obj || self)
        // };
        //
        // const modifyToggle = (event) => {
        //     if (event.target.classList.contains('reply-modify')) {
        //         const replyList = event.target.closest("li");
        //         replyList.classList.toggle('editing');
        //     }
        // };

        return {
            // read: read,
            //  save: save,
            // modifyToggle: modifyToggle,
            // modify: modify,
            // remove: remove,
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