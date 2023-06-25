George Wan  
20948342 g8wan

kotlinc-jvm 1.8.21 (jre 17.0.4)  
macOS Ventura 13.4.1

Class structure for the MVC classes were taken from the "Extended MVC with JavaFX" project in the cs349-sample-code
repository.

The minimize, maximize, and restore buttons work as expected on my Mac, but I am not sure if they will work on Windows
machines since those buttons have different behaviours on Windows.

All images used in this project (on the toolbar buttons) were sourced from [icons8](https://icons8.com/).

The macOS issue that causes the error `java[70573:2259403] +[CATransaction synchronize] called within transaction` that
appeared in A1 still appears to be unresolved and appears whenever the FileChooser dialog is used.

Cascade mode remembers the positions and transformations of images, but the order (z-index) of the images are not
remembered (it depends on how recently the image was selected, which is shared between the cascade and tile modes).

Some dimensions were hard-coded since I couldn't find a reference unit, I am not sure if everything will display
correctly on differently-sized screens and resolutions. I can supply videos/screenshots of the app working on my device
if it is requested.

On initial load, images are added with the same width (250.0) and the height is scaled according to the original aspect
ratio of the image.

The status bar simply outputs the direct source URL as given by the Java image class, which uses encoded symbols (
e.g. `%20` for spaces). Since I allow duplicate images to be added to the Lightbox and transformed independently, the
status bar will show the same name for both images since they came from different files although they behave
independently of each other.
