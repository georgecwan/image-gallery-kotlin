package ui.george.lightbox.view

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.geometry.Orientation
import javafx.scene.image.ImageView
import javafx.scene.layout.FlowPane
import ui.george.lightbox.model.Model

class TileCanvas(private val model: Model) : FlowPane(Orientation.HORIZONTAL, 5.0, 5.0), InvalidationListener {
    init {
        model.addListener(this)
        invalidated(null)
    }

    override fun invalidated(observable: Observable?) {
        prefHeight = model.getStageSize().second - 86.0
        children.clear()
        model.getImages().forEach {
            children.add(ImageView(it.first).apply {
                fitWidth = model.imageWidth
                isPreserveRatio = true
            })
        }
    }
}