package lk.ijse.pahasarastudiofp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Initializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {

            // Try different paths to see which one works
            URL dashboardURL1 = getClass().getResource("/view/dashboard.fxml");
            System.out.println("Trying path 1: " + dashboardURL1);
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/login.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Pahasara Studio Management System");
            primaryStage.setScene(scene);
            primaryStage.setMaximized(false);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();

            // Load the login screen first
            URL loginURL = getClass().getResource("/view/login.fxml"); // Assuming your login FXML is named login.fxml
            if (loginURL == null) {
                System.err.println("Error: login.fxml not found. Please ensure it's in src/main/resources/view/");
                return;
            }

            Parent root = FXMLLoader.load(Objects.requireNonNull(loginURL));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Pahasara Studio Management System - Login"); // Set a title for the login window
            primaryStage.setScene(scene);
            primaryStage.setResizable(false); // Often good to make login windows not resizable
            primaryStage.show();



        }
    }
}