package p.lodz.dsa;

import javafx.scene.control.Alert;

public class AlertWindow {

    static public void messageWindow(String title, String msg, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
