document.addEventListener("DOMContentLoaded", function() {
    const errorNodeList = document.getElementById("errors").children
    const errorNodes = Array.prototype.slice.call(errorNodeList)
    const errors = errorNodes.filter(function(child) {
        return child.classList.contains("error")
    }).map(function(child) {
        return child.innerText
    })
    errors.forEach(function(error) {
        console.warn("Server error: " + error)
    })
})