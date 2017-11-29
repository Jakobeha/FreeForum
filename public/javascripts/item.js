/**
 * Sets the source of an image to an raised version of its original.
 */
HTMLImageElement.prototype.raiseImage = function() {
    if (this.imageName.endsWith("-lowered")) {
        this.imageName = this.imageName.substr(0, this.imageName.length - "-lowered".length) + "-raised"
    } else if (!this.imageName.endsWith("-raised")) {
        this.imageName += "-raised"
    }
}

/**
 * Sets the source of an image to a lowered version of its original.
 */
HTMLImageElement.prototype.lowerImage = function() {
    if (this.imageName.endsWith("-raised")) {
        this.imageName = this.imageName.substr(0, this.imageName.length - "-raised".length) + "-lowered"
    } else if (!this.imageName.endsWith("-lowered")) {
        this.imageName += "-lowered"
    }
}

/**
 * Sets the source of an image to an unraised if it's raised,
 * and unlowered if it's lowered.
 */
HTMLImageElement.prototype.resetImage = function() {
    if (this.imageName.endsWith("-lowered")) {
        this.imageName = this.imageName.substr(0, this.imageName.length - "-lowered".length)
    } else if (this.imageName.endsWith("-raised")) {
        this.imageName = this.imageName.substr(0, this.imageName.length - "-raised".length)
    }
}

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
                itemButton.addEventListener("mousedown", function() {
                    this.lowerImage()
                })
                itemButton.addEventListener("mouseup", function() {
                    this.resetImage()
                })
                itemButton.addEventListener("mouseover", function() {
                    this.raiseImage()
                })
                itemButton.addEventListener("mouseout", function() {
                    this.resetImage()
                })
                break
            default:
                break
        }
    })
})