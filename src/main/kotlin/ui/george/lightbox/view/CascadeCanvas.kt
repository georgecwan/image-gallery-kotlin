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
                val imgWidth = model.imageWidth
                val imgHeight = model.imageHeight(image.width, image.height)
                if (details.x - imgWidth / 2 < it.x && it.x < details.x + imgWidth / 2 &&
                    details.y - imgHeight / 2 < it.y &&
                    it.y < details.y + imgHeight / 2) {
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
        val images = model.getImages()
        for ((image, details) in images) {
            val imageWidth = model.imageWidth
            val imageHeight = model.imageHeight(image.width, image.height)
            if (details.x + imageWidth / 2 > width) {
                width = details.x + imageWidth / 2 + 5.0
            }
            if (details.y + imageHeight / 2 > height) {
                height = details.y + imageHeight / 2 + 5.0
            }
            if (model.getSelectedImage() != Pair(image, details)) {
                graphicsContext2D.apply {
                    save()
                    translate(details.x, details.y)
                    rotate(details.angle)
                    scale(details.scale, details.scale)
                    drawImage(
                        image,
                        -imageWidth / 2,
                        -imageHeight / 2,
                        imageWidth,
                        imageHeight
                    )
                    restore()
                }
            }
        }
        if (model.getSelectedImage() != null) {
            val image = model.getSelectedImage()!!.first
            val details = model.getSelectedImage()!!.second
            graphicsContext2D.apply {
                val imageWidth = model.imageWidth
                val imageHeight = model.imageHeight(image.width, image.height)
                save()
                translate(details.x, details.y)
                rotate(details.angle)
                scale(details.scale, details.scale)
                drawImage(
                    image,
                    -imageWidth / 2,
                    -imageHeight / 2,
                    imageWidth,
                    imageHeight
                )
                stroke = Color.DEEPSKYBLUE
                lineWidth = 5.0
                strokeRect(
                    -imageWidth / 2,
                    -imageHeight / 2,
                    imageWidth,
                    imageHeight
                )
                restore()
            }
        }
    }

    override fun isResizable(): Boolean {
        return true
    }
}