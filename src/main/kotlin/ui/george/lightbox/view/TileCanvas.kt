package ui.george.lightbox.view

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.geometry.Orientation
import javafx.scene.image.ImageView
import javafx.scene.layout.FlowPane
import javafx.scene.layout.Pane
import ui.george.lightbox.model.Model

class TileCanvas(private val model: Model) : FlowPane(Orientation.VERTICAL), InvalidationListener {
    init {
        model.addListener(this)
        invalidated(null)
        setOnMouseClicked {
            model.setSelectedImage(null)
        }
    }

    override fun invalidated(observable: Observable?) {
        prefWidth = model.getStageSize().first - 20.0
        prefHeight = model.getStageSize().second - 86.0
        children.clear()
        model.getImages().forEach { (img, _) ->
            children.add(Pane().apply {
                children.add(ImageView(img).apply {
                    fitWidth = model.imageWidth
                    isPreserveRatio = true
                    relocate(4.0, 4.0)
                    setOnMouseClicked {
                        model.setSelectedImage(img)
                        it.consume()    // Consume before deselect
                    }
                })
                maxWidth = model.imageWidth + 8.0
                maxHeight = model.imageHeight(img.width, img.height) + 8.0
                style = if (model.getSelectedImage()?.first == img) {
                    "-fx-border-color: #00BFFF;\n" +
                            "-fx-border-style: solid;\n" +
                            "-fx-border-width: 4.0;"
                }
                else {
                    "-fx-border-color: transparent;\n" +
                            "-fx-border-style: solid;\n" +
                            "-fx-border-width: 4.0;"
                }
            })
        }
    }
}