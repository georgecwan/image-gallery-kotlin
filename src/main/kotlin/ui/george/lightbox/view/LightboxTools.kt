package ui.george.lightbox.view

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.geometry.Orientation
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import ui.george.lightbox.Lightbox
import ui.george.lightbox.model.Model
import kotlin.random.Random

class LightboxTools(private val model: Model) : ToolBar(), InvalidationListener {
    private val addButton = Button("Add image").apply button@{
        graphic =
            ImageView(
                Image(
                    "new-file.png", 16.0, 16.0, true, true
                )
            )
        minHeight = 28.0
        setOnAction {
            FileChooser().apply {
                title = "Select image"
                extensionFilters.add(FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp"))
                val file = showOpenDialog(this@button.scene.window)
                if (file != null) {
                    val img = Image(file.toURI().toString())
                    val imgWidth = model.imageWidth
                    val imgHeight = model.imageHeight(img.width, img.height)
                    // Find random x and y to place the image at that isn't off-screen
                    val canvas = ((this@button.scene.root as VBox).children[1] as LightboxCanvas).cascade
                    val wDiff = canvas.width - imgWidth - 10.0
                    val hDiff = canvas.height - imgHeight - 10.0
                    model.addImage(
                        img,
                        5.0 + imgWidth / 2 + if (wDiff <= 0.0) 0.0 else Random.nextDouble() * wDiff,
                        5.0 + imgHeight / 2 + if (hDiff <= 0.0) 0.0 else Random.nextDouble() * hDiff
                    )
                }
            }
        }
    }

    private val delButton = Button("Del image").apply {
        graphic =
            ImageView(
                Image(
                    "delete-file.png", 16.0, 16.0, true, true
                )
            )
        minHeight = 28.0
        setOnAction { model.removeSelectedImage() }
    }

    private val rotateLButton = Button("Rotate Left").apply {
        graphic =
            ImageView(
                Image(
                    "rotate-left.png", 16.0, 16.0, true, true
                )
            )
        minHeight = 28.0
        setOnAction { model.rotateSelectedImage(-10.0) }
    }

    private val rotateRButton = Button("Rotate Right").apply {
        graphic =
            ImageView(
                Image(
                    "rotate-right.png", 16.0, 16.0, true, true
                )
            )
        minHeight = 28.0
        setOnAction { model.rotateSelectedImage(10.0) }
    }

    private val zoomIButton = Button("Zoom In").apply {
        graphic =
            ImageView(
                Image(
                    "zoom-in.png", 16.0, 16.0, true, true
                )
            )
        minHeight = 28.0
        setOnAction { model.scaleSelectedImage(1.25) }
    }

    private val zoomOButton = Button("Zoom Out").apply {
        graphic =
            ImageView(
                Image(
                    "zoom-out.png", 16.0, 16.0, true, true
                )
            )
        minHeight = 28.0
        setOnAction { model.scaleSelectedImage(0.75) }
    }

    private val resetButton = Button("Reset").apply {
        graphic =
            ImageView(
                Image(
                    "reset.png", 16.0, 16.0, true, true
                )
            )
        minHeight = 28.0
        setOnAction { model.resetSelectedImage() }
    }

    private val cascadeButton = ToggleButton("Cascade").apply {
        graphic =
            ImageView(
                Image(
                    "cascade.png", 16.0, 16.0, true, true
                )
            )
        minHeight = 28.0
        setOnAction {
            model.setViewMode(true)
        }
    }

    private val tileButton = ToggleButton("Tile").apply {
        graphic =
            ImageView(
                Image(
                    "tile.png", 16.0, 16.0, true, true
                )
            )
        minHeight = 28.0
        setOnAction {
            model.setViewMode(false)
        }
    }

    init {
        items.addAll(
            addButton,
            delButton,
            Separator(Orientation.VERTICAL),
            rotateLButton,
            rotateRButton,
            zoomIButton,
            zoomOButton,
            resetButton,
            Separator(Orientation.VERTICAL),
            cascadeButton,
            tileButton
        )

        model.addListener(this)
        this.invalidated(null)
    }

    // Update enabled status of the toolbar buttons
    override fun invalidated(observable: Observable?) {
        delButton.isDisable = model.getSelectedImage() == null
        rotateLButton.isDisable = !model.getViewMode() || model.getSelectedImage() == null
        rotateRButton.isDisable = !model.getViewMode() || model.getSelectedImage() == null
        zoomIButton.isDisable = !model.getViewMode() || model.getSelectedImage() == null
        zoomOButton.isDisable = !model.getViewMode() || model.getSelectedImage() == null
        resetButton.isDisable = !model.getViewMode() || model.getSelectedImage() == null
        cascadeButton.isSelected = model.getViewMode()
        tileButton.isSelected = !model.getViewMode()
    }
}