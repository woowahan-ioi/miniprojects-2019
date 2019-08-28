document.querySelector(".side-nav-toggle").addEventListener("click", sideNavToggle);

function sideNavToggle(event) {
    event.preventDefault();
    document.querySelector(".app").classList.toggle("is-collapsed");
}

window.onload = function () {
    const createTimes = document.querySelectorAll(".createTimeSpan");

    for (let index = 0, length = createTimes.length; index < length; index++) {
        createTimes[index].innerText = calculateWrittenTime(createTimes[index].innerText)
    }
};
