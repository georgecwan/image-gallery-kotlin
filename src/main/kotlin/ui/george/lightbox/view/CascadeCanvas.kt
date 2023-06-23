package ui.george.lightbox.view

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.scene.canvas.Canvas
import javafx.scene.image.Image
import javafx.scene.paint.Color
import ui.george.lightbox.model.Model
import kotlin.math.max

class CascadeCanvas(private val model: Model) : Canvas(), InvalidationListener {
    init {
        width = 680.0
        height = 300.0
        model.addListener(this)
        invalidated(null)
        setOnMouseClicked {
            var selectedImage: Image? = null
            for ((image, details) in model.getImages()) {
                if (details.x - image.width / 2 < it.x && it.x < details.x + image.width / 2 &&
                    details.y - image.height / 2 < it.y && it.y < details.y + image.height / 2) {
                    selectedImage = image
                }
            }
            model.setSelectedImage(selectedImage)
        }
    }

    override fun invalidated(observable: Observable?) {
        if (model.getStageSize().first > width || model.getStageSize().second > height) {
            width = model.getStageSize().first - 20.0
            height = model.getStageSize().second - 86.0
        }
        graphicsContext2D.clearRect(0.0, 0.0, width, height)
        graphicsContext2D.save()
        val imageMap = model.getImages()
        for ((image, details) in imageMap) {
            if (details.x + image.width / 2 > width) {
                width = details.x + image.width / 2 + 20.0
            }
            if (details.y + image.height / 2 > height) {
                height = details.y + image.height / 2 + 20.0
            }
            graphicsContext2D.apply {
                translate(details.x, details.y)
                rotate(details.angle)
                scale(details.scale, details.scale)
                drawImage(image, -image.width / 2, -image.height / 2, image.width, image.height)
                if (model.getSelectedImage() == image) {
                    stroke = Color.DEEPSKYBLUE
                    lineWidth = 5.0
                    strokeRect(-image.width / 2, -image.height / 2, image.width, image.height)
                }
                restore()
            }
        }
    }

    override fun isResizable(): Boolean {
        return true
    }
}