package lk.ijse.pahasarastudiofp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.pahasarastudiofp.dto.AppointmentDTO;
import lk.ijse.pahasarastudiofp.dto.CustomerDTO;
import lk.ijse.pahasarastudiofp.dto.PaymentDetailsDTO;
import lk.ijse.pahasarastudiofp.dto.tm.PaymentDetailsTM;
import lk.ijse.pahasarastudiofp.model.AppointmentModel;
import lk.ijse.pahasarastudiofp.model.PaymentDetailsModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PaymentDetailsPageController implements Initializable {

    @FXML
    private ComboBox<Integer> cmbAppointmentId;
    @FXML
    private TextField txtAmount;
    @FXML
    private Button btnSavePayment;
    @FXML
    private Button btnUpdatePayment;
    @FXML
    private Button btnDeletePayment;
    @FXML
    private Button btnResetPayment;
    @FXML
    private TableView<PaymentDetailsTM> tblPayments;
    @FXML
    private TableColumn<PaymentDetailsTM, Integer> colPaymentId;
    @FXML
    private TableColumn<PaymentDetailsTM, Integer> colAppointmentId;
    @FXML
    private TableColumn<PaymentDetailsTM, String> colAppointmentName;
    @FXML
    private TableColumn<PaymentDetailsTM, Double> colAmount;

    private final PaymentDetailsModel paymentDetailsModel = new PaymentDetailsModel();
    private final AppointmentModel appointmentModel = new AppointmentModel();
    private ObservableList<PaymentDetailsTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeComboBoxes(); //load appointment ids
        setCellValueFactory();
        loadAllPayments();
        addTableSelectionListener();
        btnUpdatePayment.setDisable(true);
        btnDeletePayment.setDisable(true);
    }

    private void initializeComboBoxes() {
        try {
            List<AppointmentDTO> allAppointments = appointmentModel.getAllAppointments();
            cmbAppointmentId.getItems().clear(); // Clear any previous items
            for (AppointmentDTO appointmentDTO : allAppointments) {
                cmbAppointmentId.getItems().add(appointmentDTO.getAppointmentId());
            }
        } catch (SQLException | ClassNotFoundException e) {
            showErrorAlert("Error loading appointment IDs: " + e.getMessage());
        }
    }

    private void setCellValueFactory() {
        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colAppointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        colAppointmentName.setCellValueFactory(new PropertyValueFactory<>("appointmentName"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        tblPayments.setItems(obList);
    }

    private void loadAllPayments() {
        obList.clear();
        try {
            List<PaymentDetailsDTO> allPayments = paymentDetailsModel.getAllPayments();

            for (PaymentDetailsDTO payment : allPayments) {
                String appointmentName = paymentDetailsModel.getAppointmentName(payment.getAppointmentId());
                obList.add(new PaymentDetailsTM(
                        payment.getPaymentId(),
                        payment.getAppointmentId(),
                        payment.getAmount(),
                        appointmentName
                ));
            }
            tblPayments.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            showErrorAlert("Error loading payments: " + e.getMessage());
        }
    }

    private void addTableSelectionListener() {
        tblPayments.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillFields(newValue);
            } else {
                btnSavePayment.setDisable(false);
                btnUpdatePayment.setDisable(true);
                btnDeletePayment.setDisable(true);
            }
        });
    }

    private void fillFields(PaymentDetailsTM tm) {
        cmbAppointmentId.setValue(tm.getAppointmentId());
        txtAmount.setText(String.valueOf(tm.getAmount()));
        btnSavePayment.setDisable(true);
        btnUpdatePayment.setDisable(false);
        btnDeletePayment.setDisable(false);
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        Integer appointmentId = cmbAppointmentId.getValue();
        String amountText = txtAmount.getText();

        if (appointmentId == null || amountText.isEmpty()) {
            showWarningAlert("Appointment and Amount are required.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            PaymentDetailsDTO paymentDetailsDTO = new PaymentDetailsDTO(0, appointmentId, amount); // Let the model handle the ID
            boolean isSaved = paymentDetailsModel.savePayment(paymentDetailsDTO);
            if (isSaved) {
                showInformationAlert("Payment saved successfully.");
                loadAllPayments();
                clearFields();
            } else {
                showErrorAlert("Failed to save payment.");
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid amount format.");
        } catch (SQLException | ClassNotFoundException e) {
            showErrorAlert("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        PaymentDetailsTM selectedPayment = tblPayments.getSelectionModel().getSelectedItem();
        if (selectedPayment == null) {
            showWarningAlert("Please select a payment to update.");
            return;
        }

        Integer appointmentId = cmbAppointmentId.getValue();
        String amountText = txtAmount.getText();
        int paymentId = selectedPayment.getPaymentId();

        if (appointmentId == null || amountText.isEmpty()) {
            showWarningAlert("Appointment and Amount are required.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            PaymentDetailsDTO paymentDetailsDTO = new PaymentDetailsDTO(paymentId, appointmentId, amount);
            boolean isUpdated = paymentDetailsModel.updatePayment(paymentDetailsDTO);
            if (isUpdated) {
                showInformationAlert("Payment updated successfully.");
                loadAllPayments();
                clearFields();
            } else {
                showErrorAlert("Failed to update payment.");
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid amount format.");
        } catch (SQLException | ClassNotFoundException e) {
            showErrorAlert("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        PaymentDetailsTM selectedPayment = tblPayments.getSelectionModel().getSelectedItem();
        if (selectedPayment == null) {
            showWarningAlert("Please select a payment to delete.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this payment?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = paymentDetailsModel.deletePayment(selectedPayment.getPaymentId());
                if (isDeleted) {
                    showInformationAlert("Payment deleted successfully.");
                    loadAllPayments();
                    clearFields();
                } else {
                    showErrorAlert("Failed to delete payment.");
                }
            } catch (SQLException | ClassNotFoundException e) {
                showErrorAlert("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        cmbAppointmentId.setValue(null);
        txtAmount.clear();
        btnSavePayment.setDisable(false);
        btnUpdatePayment.setDisable(true);
        btnDeletePayment.setDisable(true);
    }






    private void showInformationAlert(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).show();
    }

    private void showErrorAlert(String message) {
        new Alert(Alert.AlertType.ERROR, message).show();
    }

    private void showWarningAlert(String message) {
        new Alert(Alert.AlertType.WARNING, message).show();
    }
}

