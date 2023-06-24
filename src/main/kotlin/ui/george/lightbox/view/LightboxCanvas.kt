package ui.george.lightbox.view

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.scene.control.ScrollPane
import ui.george.lightbox.model.Model

class LightboxCanvas(private val model: Model) : ScrollPane(), InvalidationListener {
    val cascade = CascadeCanvas(model)
    val tile = TileCanvas(model)

    init {
        hbarPolicy = ScrollBarPolicy.AS_NEEDED
        model.addListener(this)
        invalidated(null)
    }

    override fun invalidated(observable: Observable?) {
        if (model.getViewMode()) {
            content = cascade
            maxWidth = Double.MAX_VALUE
        }
        else {
            content = tile
            maxWidth = model.getStageSize().first
        }
    }
}