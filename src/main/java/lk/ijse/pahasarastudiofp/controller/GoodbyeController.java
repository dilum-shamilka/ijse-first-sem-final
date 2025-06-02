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

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        System.out.println("Goodbye window was closed.");
    }
}