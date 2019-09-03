const navButton = (() => {
    const NavController = function() {
        const navService = new NavService();
        const pathName = location.pathname;

        const getPopularVideos = function() {
            const popularsButton = document.querySelector("#nav-item-populars");
            popularsButton.addEventListener("click", navService.resetVideoArea);
            popularsButton.addEventListener("click", navService.getPopularVideos);
            appendVideosInMainPage("/", navService.getPopularVideos);
        }

        const getSubscribeVideos = function() {
            const subscriptionsButton = document.querySelector("#nav-item-subscriptions");
            if(subscriptionsButton === null) {
                return;
            }

            subscriptionsButton.addEventListener("click", navService.resetVideoArea);
            subscriptionsButton.addEventListener("click", navService.getSubscribeVideos);
            appendVideosInMainPage("/", navService.getSubscribeVideos);
        }

        const getLatestVideos = function () {
            const latestsButton = document.querySelector("#nav-item-latests");
            latestsButton.addEventListener("click", navService.resetVideoArea);
            latestsButton.addEventListener("click", navService.getLatestVideos);
            appendVideosInMainPage("/", navService.getLatestVideos);
        }

        const getRecommendedVideos = function () {
            const recommendedButton = document.querySelector("#nav-item-recommends");
            recommendedButton.addEventListener("click", navService.resetVideoArea);
            recommendedButton.addEventListener("click", navService.getRecommendedVideos);
            appendVideosInMainPage("/", navService.getRecommendedVideos);
        }

        const appendVideosInMainPage = function (pageName, appendMethod) {
            if(pathName === pageName) {
                appendMethod();
            }
        }

        const init = function() {
            getPopularVideos();
            getSubscribeVideos();
            getLatestVideos();
            getRecommendedVideos()
        }

        return {
            init: init
        }
    }

    const NavService = function() {
        const getPopularVideos = () => {
            const uri = "/api/videos/populars";
            const popularVideos = Templates.videoArea("popularsArea", "인기 동영상");
            const callback = (response) => {
                response.json().then((videos) => {
                    const videoArea = document.querySelector("#popularsArea");
                    for(video of videos) {
                        videoArea.innerHTML += Templates.videoTemplate(video);
                    }
                }).catch(error => {});
            };
            document.querySelector("#video-area").innerHTML += popularVideos;
            AjaxRequest.GET(uri, callback, alertError);

            document.querySelector("#nav-bar").classList.remove("collapsed");
        }

        const getSubscribeVideos = () => {
            const uri = "/api/videos/subscriptions";
            const subscribeVideos = Templates.videoArea("subscribesArea", "구독한 동영상");
            const callback = (response) => {
                response.json().then((videos) => {
                    const videoArea = document.querySelector("#subscribesArea");
                    for(video of videos) {
                        videoArea.innerHTML += Templates.videoTemplate(video);
                    }
                }).catch(error => {});
            };
            document.querySelector("#video-area").innerHTML += subscribeVideos;
            AjaxRequest.GET(uri, callback, alertError);

            document.querySelector("#nav-bar").classList.remove("collapsed");
        }

        const getLatestVideos = () => {
            const uri = "/api/videos/latests";
            const latestVideos = Templates.videoArea("latestsArea", "최근 동영상");
            const callback = (response) => {
                response.json().then((videos) => {
                    const videoArea = document.querySelector("#latestsArea");
                    for(video of videos) {
                        videoArea.innerHTML += Templates.videoTemplate(video);
                    }
                }).catch(error => {});
            };
            document.querySelector("#video-area").innerHTML += latestVideos;
            AjaxRequest.GET(uri, callback, alertError);

            document.querySelector("#nav-bar").classList.remove("collapsed");
        }

        const getRecommendedVideos = () => {
            const uri = "/api/videos/recommends";
            const recommendedVideos = Templates.videoArea("recommendsArea", "추천 동영상");
            const callback = (response) => {
                response.json().then((videos) => {
                    const videoArea = document.querySelector("#recommendsArea");
                    for(video of videos) {
                        videoArea.innerHTML += Templates.videoTemplate(video);
                    }
                }).catch(error => {});
            };
            document.querySelector("#video-area").innerHTML += recommendedVideos;
            AjaxRequest.GET(uri, callback, alertError);

            document.querySelector("#nav-bar").classList.remove("collapsed");
        }

        const alertError = (error) => {
            alert(error);
        }

        const resetVideoArea = () => {
            const videoArea = document.querySelector("#video-area");
            videoArea.innerHTML = "";
        }

        return {
            getPopularVideos,
            getSubscribeVideos,
            getLatestVideos,
            getRecommendedVideos,
            resetVideoArea
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
