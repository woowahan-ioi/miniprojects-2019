document.querySelector(".side-nav-toggle").addEventListener("click", sideNavToggle);

function sideNavToggle(event) {
    event.preventDefault();
    document.querySelector(".app").classList.toggle("is-collapsed");
}