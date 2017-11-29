HTMLTextAreaElement.prototype.autoresize = function() {
    this.style.height = "auto"
    if (this.scrollHeight > this.clientHeight) {
        this.style.height = this.scrollHeight + "px"
    }
}

document.addEventListener("DOMContentLoaded", function() {
    const autoresizedElems = document.getElementsByClassName("autoresize")
    Array.prototype.forEach.call(autoresizedElems, function(autoresizedElem) {
        switch (autoresizedElem.tagName) {
            case "TEXTAREA":
                autoresizedElem.addEventListener("input", function () {
                    this.autoresize()
                }, false)
                break
            default:
                break
        }
    })
});