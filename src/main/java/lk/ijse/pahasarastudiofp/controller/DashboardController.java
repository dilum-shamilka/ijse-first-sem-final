package lk.ijse.pahasarastudiofp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.Platform; // IMPORTANT: Import Platform for exit()

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class DashboardController {


    public void openUserPage(ActionEvent actionEvent) throws IOException {
        loadAndShowStage("user.fxml", "User Management");
    }

    public void openCustomerPage(ActionEvent actionEvent) throws IOException {
        loadAndShowStage("customer.fxml", "Customer Management");
    }

    public void openAppointmentPage(ActionEvent actionEvent) throws IOException {
        loadAndShowStage("Appoinment.fxml", "Appointment Management");
    }

    public void openPackagePage(ActionEvent actionEvent) throws IOException {
        loadAndShowStage("package.fxml", "Package Management");
    }

    public void openPaymentDetailsPage(ActionEvent actionEvent) throws IOException {
        loadAndShowStage("paymentdetails.fxml", "Payment Details Management");
    }

    public void openInvoicePage(ActionEvent actionEvent) throws IOException {
        loadAndShowStage("Invoice.fxml", "Invoice Management");
    }

    public void openDiscountPage(ActionEvent actionEvent) throws IOException {
        loadAndShowStage("DiscountsPage.fxml", "Discount Management");
    }

    public void openServicePage(ActionEvent actionEvent) throws IOException {
        loadAndShowStage("service.fxml", "Service Management");
    }

    public void openEmployeePage(ActionEvent actionEvent) throws IOException {
        loadAndShowStage("employee.fxml", "Employee Management");
    }

    public void openExpensesPage(ActionEvent actionEvent) throws IOException {
        loadAndShowStage("Expenses.fxml", "Expenses Management");
    }

    public void openEventLocationPage(ActionEvent actionEvent) throws IOException {
        loadAndShowStage("eventlocation.fxml", "Event Location Management");
    }

    public void openCustomerFeedbackPage(ActionEvent actionEvent) throws IOException {
        loadAndShowStage("Customerfeedback.fxml", "Customer Feedback Management");
    }

    public void openAppoinmentPackagePage(ActionEvent actionEvent) throws IOException {
        loadAndShowStage("appoinmentpackage.fxml", "Appointment Packages");
    }

    public void openAppoinmentEventLocationPage(ActionEvent actionEvent) throws IOException {
        loadAndShowStage("apoinmenteventlocation.fxml", "Appointment Locations");
    }

    public void openPackageServicePage(ActionEvent actionEvent) throws IOException {
        loadAndShowStage("packageservice.fxml", "Package Services");
    }

    public void openEmployeeServicePage(ActionEvent actionEvent) throws IOException {
        loadAndShowStage("serviceemployee.fxml", "Employee Service Management");
    }

    // New method to open the Email Sender Form
    public void openEmailSenderPage(ActionEvent actionEvent) throws IOException {
        loadAndShowStage("email.fxml", "Send Email");
    }


    public void logout(ActionEvent event) {
        try {
            Stage currentDashboardStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            URL goodbyeFxmlUrl = getClass().getResource("/view/Goodbye.fxml");
            if (goodbyeFxmlUrl == null) {
                System.err.println("Error: Goodbye.fxml not found at /view/Goodbye.fxml. Check your resource path.");
                return;
            }
            Parent goodbyeRoot = FXMLLoader.load(goodbyeFxmlUrl);

            Stage goodbyeStage = new Stage();
            goodbyeStage.setTitle("Farewell!");
            goodbyeStage.setScene(new Scene(goodbyeRoot));
            goodbyeStage.initModality(Modality.APPLICATION_MODAL);
            goodbyeStage.initOwner(currentDashboardStage);
            System.out.println("Displaying goodbye window...");
            goodbyeStage.showAndWait();
            System.out.println("Goodbye window closed. Proceeding with application exit...");
            currentDashboardStage.close();
            System.out.println("Dashboard closed.");
            Platform.exit();
            System.out.println("Application exited.");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("An error occurred during logout sequence: " + e.getMessage());
        }
    }


    private void loadAndShowStage(String fxmlFileName, String title) throws IOException {
        try {
            // Ensure the path is correct, assuming FXML files are in src/main/resources/view/
            URL fxmlUrl = getClass().getResource("/view/" + fxmlFileName);
            if (fxmlUrl == null) {
                System.err.println("Error: FXML file not found at /view/" + fxmlFileName);
                throw new IOException("FXML file not found: " + fxmlFileName);
            }
            Parent root = FXMLLoader.load(Objects.requireNonNull(fxmlUrl));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + fxmlFileName);
            e.printStackTrace();
            throw e; // Re-throw the exception to indicate failure
        }
    }
}