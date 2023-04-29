module p.lodz.dsa {
    requires javafx.controls;
    requires javafx.fxml;


    opens p.lodz.dsa to javafx.fxml;
    exports p.lodz.dsa;
}