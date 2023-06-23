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
                    Lightbox::class.java.getResource("new-file.png")!!.toString(),
                    16.0, 16.0, true, true
                )
            )
        minHeight = 28.0
        setOnAction {
            FileChooser().apply {
                title = "Select image"
                extensionFilters.add(FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.bmp"))
                val file = showOpenDialog(this@button.scene.window)
                if (file != null) {
                    val img = Image(file.toURI().toString())
                    // Find random x and y to place the image at that isn't off-screen
                    val canvas = ((this@button.scene.root as VBox).children[1] as LightboxCanvas).cascade
                    val wDiff = canvas.width - img.width
                    val hDiff = canvas.height - img.height
                    model.addImage(
                        img,
                        img.width / 2 + if (wDiff <= 0.0) 0.0 else Random.nextDouble() * wDiff,
                        img.width / 2 + if (hDiff <= 0.0) 0.0 else Random.nextDouble() * hDiff
                    )
                }
            }
        }
    }
    private val delButton = Button("Del image").apply {
        graphic =
            ImageView(
                Image(
                    Lightbox::class.java.getResource("delete-file.png")!!.toString(),
                    16.0, 16.0, true, true
                )
            )
        minHeight = 28.0
    }
    private val rotateLButton = Button("Rotate Left").apply {
        graphic =
            ImageView(
                Image(
                    Lightbox::class.java.getResource("rotate-left.png")!!.toString(),
                    16.0, 16.0, true, true
                )
            )
        minHeight = 28.0
    }
    private val rotateRButton = Button("Rotate Right").apply {
        graphic =
            ImageView(
                Image(
                    Lightbox::class.java.getResource("rotate-right.png")!!.toString(),
                    16.0, 16.0, true, true
                )
            )
        minHeight = 28.0
    }
    private val zoomIButton = Button("Zoom In").apply {
        graphic =
            ImageView(
                Image(
                    Lightbox::class.java.getResource("zoom-in.png")!!.toString(),
                    16.0, 16.0, true, true
                )
            )
        minHeight = 28.0
    }
    private val zoomOButton = Button("Zoom Out").apply {
        graphic =
            ImageView(
                Image(
                    Lightbox::class.java.getResource("zoom-out.png")!!.toString(),
                    16.0, 16.0, true, true
                )
            )
        minHeight = 28.0
    }
    private val resetButton = Button("Reset").apply {
        graphic =
            ImageView(
                Image(
                    Lightbox::class.java.getResource("reset.png")!!.toString(),
                    16.0, 16.0, true, true
                )
            )
        minHeight = 28.0
    }
    private val cascadeButton = ToggleButton("Cascade").apply {
        graphic =
            ImageView(
                Image(
                    Lightbox::class.java.getResource("cascade.png")!!.toString(),
                    16.0, 16.0, true, true
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
                    Lightbox::class.java.getResource("tile.png")!!.toString(),
                    16.0, 16.0, true, true
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
        rotateLButton.isDisable = !model.getViewMode() && model.getSelectedImage() == null
        rotateRButton.isDisable = !model.getViewMode() && model.getSelectedImage() == null
        zoomIButton.isDisable = !model.getViewMode() && model.getSelectedImage() == null
        zoomOButton.isDisable = !model.getViewMode() && model.getSelectedImage() == null
        resetButton.isDisable = !model.getViewMode() && model.getSelectedImage() == null
        cascadeButton.isSelected = model.getViewMode()
        tileButton.isSelected = !model.getViewMode()
    }
}