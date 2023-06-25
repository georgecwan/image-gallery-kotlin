package ui.george.lightbox.view

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.geometry.Orientation
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.layout.TilePane
import javafx.scene.layout.Pane
import ui.george.lightbox.model.Model

class TileCanvas(private val model: Model) : TilePane(Orientation.HORIZONTAL), InvalidationListener {
    private var columns: ArrayList<VBox> = ArrayList()

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
        updateColumns()
    }

    private fun updateColumns() {
        // Get correct number of columns
        columns.clear()
        val columnWidth = model.imageWidth + 8.0
        while ((columns.size + 1) * columnWidth <= prefWidth) {
            columns.add(VBox())
        }
        val columnHeights: MutableMap<VBox, Double> = columns.associateWith { 0.0 }.toMutableMap()
        children.addAll(columns)
        model.getImages().forEach { (img, _) ->
            // Find the shortest column
            val shortestColumn: VBox = columns.minBy {
                columnHeights[it]!!
            }
            shortestColumn.children.add(Pane().apply {
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
                columnHeights[shortestColumn] = columnHeights[shortestColumn]!! + maxHeight
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