/**
 * Scrolls below the current screen.
 * @param screen : HTMLElement */
function scrollScreenDown(screen) {
    const screenFrame = screen.getBoundingClientRect()
    scrollTo(screenFrame.left, screenFrame.bottom)
}

document.addEventListener("DOMContentLoaded", function() {
    const arrows = document.getElementsByClassName("screen-down-arrow")
    Array.prototype.forEach.call(arrows, function(arrow) {
        arrow.addEventListener("click", function() {
            scrollScreenDown(arrow.findOwner('screen'))
        });
    })
});