const navButton = (() => {
    const NavController = function() {
        const navService = new NavService();

        const getPopularVideos = function() {
            document.querySelector("#nav-item-populars").addEventListener("click", navService.getPopularVideos);
        }

        const getSubscribeVideos = function() {
            document.querySelector("#nav-item-subscriptions").addEventListener("click", navService.getSubscribeVideos);
        }

        const init = function() {
            getPopularVideos();
            getSubscribeVideos();
        }
        return {
            init: init
        }
    }
    const NavService = function() {

        const getPopularVideos = (event) => {
            const uri = "/api/videos/populars";
            const popularVideos = Templates.videoArea("인기 동영상");
            resetVideoArea(popularVideos);
            AjaxRequest.GET(uri, appendVideos, alertError);

            event.target.closest(".side-nav").classList.toggle("collapsed");
        }

        const getSubscribeVideos = (event) => {
            const uri = "/api/videos/subscriptions";
            const subscribeVideos = Templates.videoArea("구독한 동영상");
            resetVideoArea(subscribeVideos);
            AjaxRequest.GET(uri, appendVideos, alertError);

            event.target.closest(".side-nav").classList.toggle("collapsed");
        }

        const appendVideos = (response) => {
            response.json().then((videos) => {
                const videoArea = document.querySelector("#video-area").querySelector(".row");
                for(video of videos) {
                    videoArea.innerHTML += Templates.videoTemplate(video);
                }
            }).catch(error => {alert("영상이 없습니다.")});
        }

        const alertError = (error) => {
            alert(error);
        }

        const resetVideoArea = (videoTemplate) => {
            const videoArea = document.querySelector("#video-area");
            videoArea.innerHTML = "";
            videoArea.innerHTML += videoTemplate;
        }

        return {
            getPopularVideos,
            getSubscribeVideos
        }
    }

    const init = () => {
        const navController = new NavController();
        navController.init();
    }

    return {
        init:init
    }
})();

function sideNavToggle(event) {
    event.preventDefault();
    document.querySelector(".side-nav").classList.toggle("collapsed");
}

window.onload = function () {
    document.querySelector("#side-nav-toggle-list").classList.remove("display-none");
    document.querySelector(".side-nav-toggle").addEventListener("click", sideNavToggle);

    const createTimes = document.querySelectorAll(".createTimeSpan");

    for (let index = 0, length = createTimes.length; index < length; index++) {
        createTimes[index].innerText = calculateWrittenTime(createTimes[index].innerText)
    }

    navButton.init();
};
