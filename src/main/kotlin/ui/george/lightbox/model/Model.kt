package ui.george.lightbox.model

import javafx.beans.InvalidationListener
import javafx.beans.Observable


// Template for model class from "Extended MVC with JavaFX" sample code
class Model : Observable {
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
}