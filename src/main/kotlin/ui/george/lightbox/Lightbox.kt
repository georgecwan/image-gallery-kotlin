package ui.george.lightbox

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import ui.george.lightbox.model.Model

class Lightbox : Application() {
    override fun start(stage: Stage) {
        val myModel = Model()

        stage.apply {
            title = "Lightbox by George Wan"
            scene = Scene(BorderPane().apply {
                center = Button("Click me!")
            }, 320.0, 240.0)
        }.show()
    }
}

fun main() {
    Application.launch(Lightbox::class.java)
}