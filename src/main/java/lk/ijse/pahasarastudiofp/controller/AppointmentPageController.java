
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
import lk.ijse.pahasarastudiofp.dto.PackageDTO;
import lk.ijse.pahasarastudiofp.dto.tm.AppointmentTM;
import lk.ijse.pahasarastudiofp.model.AppointmentModel;
import lk.ijse.pahasarastudiofp.model.CustomerModel; // Needed for loading customer IDs
import lk.ijse.pahasarastudiofp.model.PackageModel;   // Needed for loading package IDs

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class AppointmentPageController implements Initializable {

    @FXML
    private ComboBox<Integer> cmbCustomerId;

    @FXML
    private ComboBox<Integer> cmbPackageId;

    @FXML
    private TextField txtName;

    @FXML
    private Button btnSaveAppointment;

    @FXML
    private Button btnUpdateAppointment;

    @FXML
    private Button btnDeleteAppointment;

    @FXML
    private Button btnResetAppointment;

    @FXML
    private TableView<AppointmentTM> tblAppointments;

    @FXML
    private TableColumn<AppointmentTM, Integer> colAppointmentId;

    @FXML
    private TableColumn<AppointmentTM, Integer> colCustomerId;

    @FXML
    private TableColumn<AppointmentTM, String> colCustomerName;

    @FXML
    private TableColumn<AppointmentTM, Integer> colPackageId;

    @FXML
    private TableColumn<AppointmentTM, String> colPackageName;

    @FXML
    private TableColumn<AppointmentTM, String> colName;


    private final AppointmentModel appointmentModel = new AppointmentModel();
    private final CustomerModel customerModel = new CustomerModel();
    private final PackageModel packageModel = new PackageModel();

    private ObservableList<AppointmentTM> obList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btnUpdateAppointment.setDisable(true);
        btnDeleteAppointment.setDisable(true);

        loadCustomerIds();
        loadPackageIds();
        setCellValueFactory();
        loadAllAppointments();


        tblAppointments.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillFields(newValue);
            }
        });
    }


    private void loadCustomerIds() {
        try {
            cmbCustomerId.getItems().clear();
            List<CustomerDTO> allCustomers = customerModel.getAllCustomers();
            for (CustomerDTO customerDTO : allCustomers) {
                cmbCustomerId.getItems().add(customerDTO.getCustomerId());
            }
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error loading customer IDs: " + e.getMessage());
        }
    }


    private void loadPackageIds() {
        try {
            cmbPackageId.getItems().clear();
            List<PackageDTO> allPackages = packageModel.getAllPackages(); // Assuming PackageModel has getAllPackages
            for (PackageDTO packageDTO : allPackages) {
                cmbPackageId.getItems().add(packageDTO.getPackageId());
            }
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error loading package IDs: " + e.getMessage());
        }
    }


    private void setCellValueFactory() {
        colAppointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colPackageId.setCellValueFactory(new PropertyValueFactory<>("packageId"));
        colPackageName.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
    }


    private void loadAllAppointments() {
        try {
            List<AppointmentDTO> allAppointments = appointmentModel.getAllAppointments();
            obList.clear();
            for (AppointmentDTO appointment : allAppointments) {

                String customerName = customerModel.getCustomerName(appointment.getCustomerId());
                String packageName = packageModel.getPackageName(appointment.getPackageId());


                obList.add(new AppointmentTM(
                        appointment.getAppointmentId(),
                        appointment.getCustomerId(),
                        appointment.getPackageId(),
                        appointment.getName(),
                        customerName,
                        packageName
                ));
            }
            tblAppointments.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error loading appointments: " + e.getMessage());
        }
    }


    private void fillFields(AppointmentTM tm) {
        cmbCustomerId.setValue(tm.getCustomerId());
        cmbPackageId.setValue(tm.getPackageId());
        txtName.setText(tm.getName());
        btnSaveAppointment.setDisable(true);
        btnUpdateAppointment.setDisable(false);
        btnDeleteAppointment.setDisable(false);
    }


    @FXML
    void btnSaveOnAction(ActionEvent event) {
        Integer customerId = cmbCustomerId.getValue();
        Integer packageId = cmbPackageId.getValue();
        String name = txtName.getText();

        // Input validation
        if (customerId == null || packageId == null || name.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Customer, Package, and Name are required.");
            return;
        }

        // Create a new AppointmentDTO (appointmentId is 0 for new records, will be auto-generated by DB)
        AppointmentDTO appointmentDTO = new AppointmentDTO(0, customerId, packageId, name);

        try {
            boolean isSaved = appointmentModel.saveAppointment(appointmentDTO);
            if (isSaved) {
                showAlert(Alert.AlertType.INFORMATION, "Appointment saved successfully.");
                loadAllAppointments(); // Refresh table
                clearFields();          // Clear input fields
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to save appointment.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
        }
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        AppointmentTM selectedAppointment = tblAppointments.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            showAlert(Alert.AlertType.WARNING, "Please select an appointment to update.");
            return;
        }

        Integer customerId = cmbCustomerId.getValue();
        Integer packageId = cmbPackageId.getValue();
        String name = txtName.getText();
        int appointmentId = selectedAppointment.getAppointmentId(); // Get ID from selected item

        // Input validation
        if (customerId == null || packageId == null || name.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Customer, Package, and Name are required.");
            return;
        }

        // Create an AppointmentDTO with updated data
        AppointmentDTO appointmentDTO = new AppointmentDTO(appointmentId, customerId, packageId, name);

        try {
            boolean isUpdated = appointmentModel.updateAppointment(appointmentDTO);
            if (isUpdated) {
                showAlert(Alert.AlertType.INFORMATION, "Appointment updated successfully.");
                loadAllAppointments(); // Refresh table
                clearFields();          // Clear input fields
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to update appointment.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
        }
    }


    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        AppointmentTM selectedAppointment = tblAppointments.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            showAlert(Alert.AlertType.WARNING, "Please select an appointment to delete.");
            return;
        }

        // Informative confirmation dialog about potential customer deletion
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this appointment? If this is the customer's ONLY appointment, the customer record will also be deleted.",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                // *** CALL THE NEW TRANSACTIONAL METHOD ***
                boolean isDeleted = appointmentModel.deleteAppointmentAndCustomerIfNoOtherAppointments(selectedAppointment.getAppointmentId());
                if (isDeleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Appointment (and potentially customer) deleted successfully.");
                    loadAllAppointments(); // Refresh table
                    clearFields();          // Clear input fields
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failed to delete appointment (or customer).");
                }
            } catch (SQLException | ClassNotFoundException e) {
                showAlert(Alert.AlertType.ERROR, "Database error during deletion: " + e.getMessage());
                e.printStackTrace(); // Print stack trace for debugging
            }
        }
    }


    @FXML
    void btnResetOnAction(ActionEvent event) {
        clearFields();
    }


    private void clearFields() {
        cmbCustomerId.setValue(null);
        cmbPackageId.setValue(null);
        txtName.clear();
        btnSaveAppointment.setDisable(false); // Enable save
        btnUpdateAppointment.setDisable(true); // Disable update
        btnDeleteAppointment.setDisable(true); // Disable delete
        tblAppointments.getSelectionModel().clearSelection(); // Clear table selection
    }


    private void showAlert(Alert.AlertType alertType, String message) {
        new Alert(alertType, message).show();
    }
}