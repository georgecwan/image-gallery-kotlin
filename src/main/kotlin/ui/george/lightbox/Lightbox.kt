package ui.george.lightbox

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import ui.george.lightbox.model.Model
import ui.george.lightbox.view.LightboxCanvas
import ui.george.lightbox.view.LightboxTools
import ui.george.lightbox.view.StatusBar

class Lightbox : Application() {
    override fun start(stage: Stage) {
        val myModel = Model

        stage.apply {
            title = "Lightbox by g8wan"
            scene = Scene(BorderPane().apply {
                top = LightboxTools(myModel)
                center = LightboxCanvas(myModel)
                bottom = StatusBar(myModel)
                minWidth = 550.0
                minHeight = 360.0

            }, 850.0, 400.0)
//            VBox.setVgrow(LightboxTools(myModel), Priority.NEVER)
//            VBox.setVgrow(LightboxCanvas(myModel), Priority.ALWAYS)
//            VBox.setVgrow(StatusBar(myModel), Priority.NEVER)
            minWidth = 550.0
            minHeight = 400.0
            widthProperty().addListener { _, _, _ ->
                myModel.setStageSize(width, height)
            }
            heightProperty().addListener { _, _, _ ->
                myModel.setStageSize(width, height)
            }
        }.show()
    }
}

fun main() {
    Application.launch(Lightbox::class.java)
}