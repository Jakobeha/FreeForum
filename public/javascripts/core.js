/**
 * Finds the owner of this node with the class:
 * this node itself or the nearest ancestor.
 */
Node.prototype.findOwner = function(cls) {
    if (this.classList.contains(cls)) {
        return this
    } else {
        if (this.parentNode === null) {
            throw "Node has no owner"
        }

        return this.parentNode.findOwner(cls)
    }
};