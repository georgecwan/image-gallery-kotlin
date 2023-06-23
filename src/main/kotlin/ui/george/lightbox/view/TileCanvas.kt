package ui.george.lightbox.view

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.scene.layout.FlowPane
import ui.george.lightbox.model.Model

class TileCanvas(private val model: Model) : FlowPane(), InvalidationListener {
    init {
        model.addListener(this)
        invalidated(null)
    }

    override fun invalidated(observable: Observable?) {

    }
}