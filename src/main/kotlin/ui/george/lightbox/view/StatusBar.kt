package ui.george.lightbox.view

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import ui.george.lightbox.model.Model

class StatusBar(private val model: Model) : AnchorPane(), InvalidationListener {
    private val imageCounter = Label()
    private val currentImage = Label()

    init {
        children.add(imageCounter.apply {
            setLeftAnchor(this, 5.0)
        })
        children.add(currentImage.apply {
            setRightAnchor(this, 5.0)
        })
        model.addListener(this)
        invalidated(null)
    }

    // Updates the two labels
    override fun invalidated(observable: Observable?) {
        imageCounter.text = "${model.getImageCount()} images loaded"
        currentImage.text = if (model.getSelectedImage() == null) {
            "No image selected"
        }
        else {
            "Current image: ${model.getSelectedImage()!!.url}"
        }
    }
}