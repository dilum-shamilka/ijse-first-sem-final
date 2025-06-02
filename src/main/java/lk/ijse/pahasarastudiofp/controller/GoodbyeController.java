package lk.ijse.pahasarastudiofp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Node;

public class GoodbyeController {

    @FXML
    private Button btnOk;


    @FXML
    void handleOkButton(ActionEvent event) {
        // Get the stage (window) from the button that triggered the event
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close(); // Close the goodbye window
        System.out.println("Goodbye window was closed."); // For debugging
    }
}