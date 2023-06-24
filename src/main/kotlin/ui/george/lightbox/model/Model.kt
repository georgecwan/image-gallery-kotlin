package ui.george.lightbox.model

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.scene.image.Image


// Template for model class from "Extended MVC with JavaFX" sample code
object Model : Observable {

    private val views = mutableListOf<InvalidationListener?>()

    /**
     * Add listener to receive notifications about changes in the [Model].
     * @param listener the listener that is added to the [Model]
     */
    override fun addListener(listener: InvalidationListener?) {
        views.add(listener)
    }

    /**
     * Remove listener to stop receiving notifications about changes in the [Model].
     * @param listener the listener that is removed from the [Model]
     */
    override fun removeListener(listener: InvalidationListener?) {
        views.remove(listener)
    }

    // True in cascade mode, false in tile mode
    private var cascadeView: Boolean = true

    /**
     * Returns the current value of cascadeView.
     */
    fun getViewMode(): Boolean {
        return cascadeView
    }

    /**
     * Sets the value of cascadeView and invalidates all added listeners.
     */
    fun setViewMode(newMode: Boolean) {
        cascadeView = newMode
        views.forEach { it?.invalidated(this) }
    }

    interface imageProperties {
        var x: Double
        var y: Double
        var angle: Double
        var scale: Double
    }

    // List of images currently on canvas
    private val images: MutableList<Pair<Image, imageProperties>> = mutableListOf()

    // Currently selected image as index of images.keys
    private var selectedImage: Int? = null

    /**
     * Returns the current list of images.
     */
    fun getImages(): List<Pair<Image, imageProperties>> {
        return images
    }

    /**
     * Adds an image to the list of images and invalidates all added listeners.
     */
    fun addImage(newImage: Image, x: Double, y: Double) {
        images.add(Pair(newImage, object : imageProperties {
            override var x: Double = x
            override var y: Double = y
            override var angle: Double = 0.0
            override var scale: Double = 1.0
        }))
        views.forEach {
            it?.invalidated(this)
        }
    }

    /**
     * Returns the number of images in the list.
     */
    fun getImageCount(): Int {
        return images.size
    }

    /**
     * Get the currently selected image
     */
    fun getSelectedImage(): Pair<Image, imageProperties>? {
        return images[selectedImage ?: return null]
    }

    /**
     * Sets the value of selectedImage and invalidates all added listeners.
     */
    fun setSelectedImage(newImage: Image?) {
        selectedImage = newImage?.let {
            // Place selected image at end of list
            images.indexOfFirst { it.first == newImage }
        }
        views.forEach { it?.invalidated(this) }
    }

    fun pushSelectedImageFront() {
        if (selectedImage != null) {
            val selectedPair = images[selectedImage!!]
            images.remove(selectedPair)
            images.add(selectedPair)
            selectedImage = images.size - 1
        }
    }

    /**
     * Move selected image by amount x and y
     */
    fun moveSelectedImage(x: Double, y: Double) {
        if (selectedImage == null) {
            return
        }
        images[selectedImage!!].second.x += x
        images[selectedImage!!].second.y += y
        views.forEach { it?.invalidated(this) }
    }

    /**
     * Remove selected image from images
     */
    fun removeSelectedImage() {
        selectedImage?.let { images.remove(images[it]) }
        selectedImage = null
        views.forEach { it?.invalidated(this) }
    }

    /**
     * Rotate selected image by specified angle
     */
    fun rotateSelectedImage(rotation: Double) {
        selectedImage?.let {
            images[it].second.angle += rotation
        }
        views.forEach { it?.invalidated(this) }
    }

    /**
     * Scale selected image by specified factor
     */
    fun scaleSelectedImage(factor: Double) {
        selectedImage?.let {
            images[it].second.scale *= factor
        }
        views.forEach { it?.invalidated(this) }
    }

    /**
     * Reset selected image to original size and orientation
     */
    fun resetSelectedImage() {
        selectedImage?.let {
            images[it].second.angle = 0.0
            images[it].second.scale = 1.0
        }
        views.forEach { it?.invalidated(this) }
    }

    // Tuple of current stage width, height
    private var stageSize: Pair<Double, Double> = Pair(0.0, 0.0)

    /**
     * Returns current stage width and height
     */
    fun getStageSize(): Pair<Double, Double> {
        return stageSize
    }

    /**
     * Sets the value of stageSize and invalidates all added listeners.
     */
    fun setStageSize(width: Double, height: Double) {
        stageSize = Pair(width, height)
        views.forEach { it?.invalidated(this) }
    }


    /**
     * Image dimensions for canvas mode
     */
    const val imageWidth: Double = 250.0

    fun imageHeight(width: Double, height: Double): Double {
        return imageWidth / width * height
    }
}