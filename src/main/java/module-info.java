module ui.george.lightbox {
    requires javafx.controls;
    requires javafx.fxml;
                requires kotlin.stdlib;
    
                            
    opens ui.george.lightbox to javafx.fxml;
    exports ui.george.lightbox;
}