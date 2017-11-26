/**
 * Sets the source of an image to an inverted version of its original.
 */
HTMLImageElement.prototype.invertImage = function() {
    if (!this.imageName.endsWith("-invert")) {
        this.imageName += "-invert"
    }
};

/**
 * Sets the source of an image to an original if it's inverted.
 */
HTMLImageElement.prototype.uninvertImage = function() {
    if (this.imageName.endsWith("-invert")) {
        this.imageName = this.imageName.substr(0, this.imageName.length - "-invert".length)
    }
};

Object.defineProperty(HTMLImageElement.prototype, "imageName", {
    get: function() {
        const pathComponents = this.src.split("/")
        const nameAndExt = pathComponents[pathComponents.length - 1].split(".")
        return nameAndExt[0]
    },
    set: function (newImageName) {
        let initPathComponents = this.src.split("/")
        const oldNameAndExt = initPathComponents.pop().split(".")
        let newNameAndExt = oldNameAndExt.slice(1) // currently only extension
        newNameAndExt.unshift(newImageName)
        let newPathComponents = initPathComponents
        newPathComponents.push(newNameAndExt.join("."))
        this.src = newPathComponents.join("/")
    }
})

document.addEventListener("DOMContentLoaded", function() {
    const itemButtons = document.getElementsByClassName("item-button")
    Array.prototype.forEach.call(itemButtons, function(itemButton) {
        switch (itemButton.tagName) {
            case "IMG":
                itemButton.addEventListener("mouseover", function () {
                    itemButton.invertImage()
                })
                itemButton.addEventListener("mouseout", function () {
                    itemButton.uninvertImage()
                })
                break
            default:
                break
        }
    })
});