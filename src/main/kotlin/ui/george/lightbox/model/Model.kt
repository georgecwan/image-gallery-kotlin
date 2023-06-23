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
    private var images: HashMap<Image, imageProperties> = HashMap()

    // Currently selected image as index of images.keys
    private var selectedImage: Int? = null

    /**
     * Returns the current list of images.
     */
    fun getImages(): HashMap<Image, imageProperties> {
        return images
    }

    /**
     * Adds an image to the list of images and invalidates all added listeners.
     */
    fun addImage(newImage: Image, x: Double, y: Double) {
        images[newImage] = object : imageProperties {
            override var x: Double = x
            override var y: Double = y
            override var angle: Double = 0.0
            override var scale: Double = 1.0
        }
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
    fun getSelectedImage(): Image? {
        if (selectedImage == null) {
            return null
        }
        return images.keys.elementAt(selectedImage!!)
    }

    /**
     * Sets the value of selectedImage and invalidates all added listeners.
     */
    fun setSelectedImage(newImage: Image?) {
        selectedImage = newImage?.let { images.keys.indexOf(it) }
        views.forEach { it?.invalidated(this) }
    }

    /**
     * Remove selected image from images
     */
    fun removeSelectedImage() {
        selectedImage?.let { images.remove(images.keys.elementAt(it)) }
        selectedImage = null
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
}