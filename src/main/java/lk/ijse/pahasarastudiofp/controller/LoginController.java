package lk.ijse.pahasarastudiofp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.util.Objects;

public class LoginController {

    public TextField txtUsername;
    public PasswordField txtPassword;

    public void btnLoginOnAction(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();


        if (username.equals("dilu") && password.equals("1")) {
            // Successful login
            try {

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/dashboard.fxml")));
                Scene scene = new Scene(root);
                stage.setTitle("Pahasara Studio Management System");
                stage.setScene(scene);
                stage.setMaximized(true); // Maximize the dashboard
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to load dashboard!").show();
            }
        } else {
            // Failed login
            new Alert(Alert.AlertType.ERROR, "Invalid Username or Password!").show();
        }
    }
}