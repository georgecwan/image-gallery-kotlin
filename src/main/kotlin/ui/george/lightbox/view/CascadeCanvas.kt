package ui.george.lightbox.view

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.geometry.Point2D
import javafx.scene.canvas.Canvas
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.scene.transform.Rotate
import javafx.scene.transform.Scale
import kotlin.math.max
import ui.george.lightbox.model.Model

class CascadeCanvas(private val model: Model) : Canvas(), InvalidationListener {
    init {
        width = 680.0
        height = 300.0
        model.addListener(this)
        invalidated(null)
        var initialMousePos: Pair<Double, Double>? = null
        setOnMousePressed {
            val cursorLocation = Point2D(it.x, it.y)
            var selectedImage: Image? = null
            for ((image, details) in model.getImages()) {
                val rotation = Rotate(-details.angle, details.x, details.y)
                val scaling = Scale(1 / details.scale, 1 / details.scale, details.x, details.y)
                var transformedCursor = rotation.transform(scaling.transform(cursorLocation))
                val imgWidth = model.imageWidth
                val imgHeight = model.imageHeight(image.width, image.height)
                if (details.x - imgWidth / 2 < transformedCursor.x && transformedCursor.x < details.x + imgWidth / 2 &&
                    details.y - imgHeight / 2 < transformedCursor.y && transformedCursor.y < details.y + imgHeight / 2) {
                    selectedImage = image
                }
            }
            model.setSelectedImage(selectedImage)
            model.pushSelectedImageFront()
            initialMousePos = Pair(it.x, it.y)
        }
        setOnMouseDragged {
            model.moveSelectedImage(it.x - initialMousePos!!.first, it.y - initialMousePos!!.second)
            initialMousePos = Pair(it.x, it.y)
        }
    }

    override fun invalidated(observable: Observable?) {
        // Find limits of x and y needed to show images
//        var minX: Double = 0.0
//        var minY: Double = 0.0
        var maxX = 0.0
        var maxY = 0.0
        for ((img, details) in model.getImages()) {
            val imgWidth = model.imageWidth
            val imgHeight = model.imageHeight(img.width, img.height)
            val corners: Array<Point2D> = arrayOf(
                Point2D(details.x - imgWidth / 2, details.y - imgHeight / 2),
                Point2D(details.x + imgWidth / 2, details.y - imgHeight / 2),
                Point2D(details.x - imgWidth / 2, details.y + imgHeight / 2),
                Point2D(details.x + imgWidth / 2, details.y + imgHeight / 2)
            )
            val rotation = Rotate(details.angle, details.x, details.y)
            val scaling = Scale(details.scale, details.scale, details.x, details.y)
            corners.forEach {
                val transformedCorner = rotation.transform(scaling.transform(it))
//                if (transformedCorner.x < minX) {
//                    minX = transformedCorner.x
//                }
                if (transformedCorner.x > maxX) {
                    maxX = transformedCorner.x
                }
//                if (transformedCorner.y < minY) {
//                    minY = transformedCorner.y
//                }
                if (transformedCorner.y > maxY) {
                    maxY = transformedCorner.y
                }
            }
        }
//        maxX -= minX
//        maxY -= minY
        // Translate all images so that they fit in positive x and y
        // Decided not to do this because it would scroll the canvas infinitely when dragging into a corner
//        model.getImages().forEach {
//            it.second.x -= minX
//            it.second.y -= minY
//        }
        width = max(maxX, model.getStageSize().first - 20.0)
        height = max(maxY, model.getStageSize().second - 86.0)
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