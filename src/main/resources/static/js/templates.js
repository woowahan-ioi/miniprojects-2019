const Templates = {
    commentTemplate : (comment, writtenTime) => {return `<li class="comment mrg-btm-30" data-commentid="${comment.id}">
                <img class="img-circle width-50 comment-writer-img" src="/images/default/eastjun_big.jpg" alt="">
                <div class="comment-block">
                    <div class="font-size-13">
                        <span class="user-name">${comment.writerName}</span>
                        <span class="update-date">${writtenTime}</span>
                    </div>
                    <div class="comment-more-box dropdown">
                        <button class="comment-more-button dropdown-toggle" data-toggle="dropdown">
                            <i class="ti-more-alt"></i>
                        </button>
                    
                        <div class="dropdown-menu comment-dropdown-menu" role="menu">
                            <button class="list-group-item list-group-item-action comment-edit-button">
                                <i class="fa fa-pencil"></i>
                                <span>수정</span>
                            </button>
                            <button class="list-group-item list-group-item-action comment-delete-button">
                                <i class="fa fa-trash"></i>
                                <span>삭제</span>
                            </button>
                        </div>
                    </div>
                    <span class="comment-contents font-size-15">${comment.contents}</span>
                    <div>
                        <button class="like-btn">
                            <i class="ti-thumb-up"></i>
                        </button>
                        <span>3.5천</span>
                        <button class="reply-toggle-btn">답글</button>
                    </div>
                </div>
                <div class="comment-update-area display-none mrg-btm-50">
                    <div>
                        <img class="img-circle width-50 comment-writer-img" src="/images/default/eastjun_big.jpg"
                             alt="">
                        <input class="comment-input" type="text" value="${comment.contents}">
                    </div>
                    <button class="btn comment-btn comment-update-cancel-btn">취소</button>
                    <button class="btn comment-btn edit comment-update-btn">수정</button>
                </div>
                <div class="mrg-top-5 reply-area">
                    <div class="reply-edit display-none">
                        <div class="mrg-btm-10">
                            <img class="img-circle width-50 comment-writer-img" src="/images/default/eastjun_big.jpg"
                                 alt="">
                            <input class="comment-input" type="text" placeholder="공개 답글 추가...">
                        </div>
                        <button class="btn comment-btn edit reply-save-btn disabled">답글</button>
                        <button class="btn comment-btn reply-cancel-btn">취소</button>
                    </div>
                    <div id="reply-list-more-area" class="mrg-btm-10">
                        <button class="reply-list-open-button">
                            <span>답글 더보기</span>
                            <i class="fa fa-angle-down"></i>
                        </button>
                        <button class="reply-list-close-button display-none">
                            <span>답글 숨기기</span>
                            <i class="fa fa-angle-up"></i>
                        </button>
                    </div>
                    <ul class="reply-list">

                    </ul>
                </div>
            </li>`
    },
    replyTemplate : (reply, writtenTime) => { return `<li class="reply mrg-btm-30" data-replyid="${reply.id}">
                            <img class="img-circle width-50 comment-writer-img" src="/images/default/eastjun_big.jpg" alt="">
                            <div class="comment-block">
                                <div class="font-size-13">
                                    <span class="user-name">${reply.writerName}</span>
                                    <span class="update-date">${writtenTime}</span>
                                </div>
                                <div class="reply-more-box dropdown">
                                    <button class="reply-more-button dropdown-toggle" data-toggle="dropdown">
                                        <i class="ti-more-alt"></i>
                                    </button>
                                
                                    <div class="dropdown-menu" role="menu">
                                        <button class="list-group-item list-group-item-action reply-edit-button">
                                            <i class="fa fa-pencil"></i>
                                            <span>수정</span>
                                        </button>
                                        <button class="list-group-item list-group-item-action reply-delete-button">
                                            <i class="fa fa-trash"></i>
                                            <span>삭제</span>
                                        </button>
                                    </div>
                                </div>
                                <span class="reply-contents font-size-15">${reply.contents}</span>
                                <div>
                                    <button class="like-btn">
                                        <i class="ti-thumb-up"></i>
                                    </button>
                                    <span>3.5천</span>
                                </div>
                            </div>
                            <div class="reply-update-area display-none mrg-btm-50">
                                <div>
                                    <img class="img-circle width-50 comment-writer-img" src="/images/default/eastjun_big.jpg"
                                         alt="">
                                    <input class="comment-input" type="text" value="${reply.contents}">
                                </div>
                                <button class="btn comment-btn reply-update-cancel-btn">취소</button>
                                <button class="btn comment-btn edit reply-update-btn">수정</button>
                            </div>
                        </li>`
    },
    videoTemplate : (video) => {
        video.createTime = calculateWrittenTime(video.createTime);
        return `<div class="col-lg-2 padding-2">
                        <a href="/videos/${video.id}"
                           style="text-decoration: none; color: #000;">
                            <div class="card bg-transparent no-border">
                                <div class="card-media">
                                    <img alt="" class="img-responsive"
                                         src="${video.thumbnailPath}">
                                </div>
                                <div class="card-block padding-10">
                                    <h5 class="mrg-btm-10 no-mrg-top text-bold font-size-14 ls-0">${video.title}</h5>
                                    <span class="font-size-13">${video.writerName}</span>
                                    <div class="font-size-11">
                                        <span>조회수 <span>${video.views}</span>회</span>
                                        <span> · </span>
                                        <span class="createTimeSpan">${video.createTime}</span>
                                    </div>
                                </div>
                            </div>
                        </a>
                    </div>`
    },
    videoArea : (areaName) => {
        return `<div>
                    <div>
                        <h3 class="text-bold height-40px">${areaName}</h3>
                    </div>
                    <div class="row">
                        
                    </div>
                </div>`;
    }
}