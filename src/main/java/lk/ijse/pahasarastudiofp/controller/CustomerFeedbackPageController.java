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
import lk.ijse.pahasarastudiofp.dto.CustomerFeedbackDTO;
import lk.ijse.pahasarastudiofp.dto.tm.CustomerFeedbackTM;
import lk.ijse.pahasarastudiofp.model.AppointmentModel;
import lk.ijse.pahasarastudiofp.model.CustomerFeedbackModel;
import lk.ijse.pahasarastudiofp.model.CustomerModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerFeedbackPageController implements Initializable {

    @FXML
    private ComboBox<Integer> cmbCustomerId;

    @FXML
    private ComboBox<Integer> cmbAppointmentId;

    @FXML
    private Spinner<Integer> spnRating;

    @FXML
    private Button btnSaveFeedback;

    @FXML
    private Button btnUpdateFeedback;

    @FXML
    private Button btnDeleteFeedback;

    @FXML
    private Button btnResetFeedback;

    @FXML
    private TableView<CustomerFeedbackTM> tblFeedback;

    @FXML
    private TableColumn<CustomerFeedbackTM, Integer> colFeedbackId;

    @FXML
    private TableColumn<CustomerFeedbackTM, Integer> colCustomerId;

    @FXML
    private TableColumn<CustomerFeedbackTM, String> colCustomerName;

    @FXML
    private TableColumn<CustomerFeedbackTM, Integer> colAppointmentId;

    @FXML
    private TableColumn<CustomerFeedbackTM, String> colAppointmentName;

    @FXML
    private TableColumn<CustomerFeedbackTM, Integer> colRating;

    private final CustomerFeedbackModel feedbackModel = new CustomerFeedbackModel();
    private final CustomerModel customerModel = new CustomerModel();
    private final AppointmentModel appointmentModel = new AppointmentModel();
    private ObservableList<CustomerFeedbackTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupRatingSpinner();
        setCellValueFactory();
        loadCustomerIds();
        loadAppointmentIds();
        loadAllFeedback();


        tblFeedback.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillFields(newValue);
            }
        });


        btnSaveFeedback.setDisable(false);
        btnUpdateFeedback.setDisable(true);
        btnDeleteFeedback.setDisable(true);
    }

    private void loadCustomerIds() {
        try {
            cmbCustomerId.getItems().clear();
            List<CustomerDTO> allCustomers = customerModel.getAllCustomers();
            for (CustomerDTO customerDTO : allCustomers) {
                cmbCustomerId.getItems().add(customerDTO.getCustomerId());
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading customer IDs: " + e.getMessage()).show();
        }
    }

    private void loadAppointmentIds() {
        try {
            cmbAppointmentId.getItems().clear();
            List<AppointmentDTO> allAppointments = appointmentModel.getAllAppointments();
            for (AppointmentDTO appointmentDTO : allAppointments) {
                cmbAppointmentId.getItems().add(appointmentDTO.getAppointmentId());
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading appointment IDs: " + e.getMessage()).show();
        }
    }

    private void setupRatingSpinner() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 3);
        spnRating.setValueFactory(valueFactory);
    }

    private void setCellValueFactory() {
        colFeedbackId.setCellValueFactory(new PropertyValueFactory<>("feedbackId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colAppointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        colAppointmentName.setCellValueFactory(new PropertyValueFactory<>("appointmentName"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
    }

    private void loadAllFeedback() {
        try {
            List<CustomerFeedbackDTO> allFeedback = feedbackModel.getAllFeedback();
            obList.clear();
            for (CustomerFeedbackDTO feedback : allFeedback) {
                String customerName = feedbackModel.getCustomerName(feedback.getCustomerId());
                String appointmentName = feedbackModel.getAppointmentName(feedback.getAppointmentId());
                obList.add(new CustomerFeedbackTM(
                        feedback.getFeedbackId(),
                        feedback.getCustomerId(),
                        feedback.getAppointmentId(),
                        feedback.getRating(),
                        customerName,
                        appointmentName
                ));
            }
            tblFeedback.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading feedback: " + e.getMessage()).show();
        }
    }

    private void fillFields(CustomerFeedbackTM tm) {
        cmbCustomerId.setValue(tm.getCustomerId());
        cmbAppointmentId.setValue(tm.getAppointmentId());
        spnRating.getValueFactory().setValue(tm.getRating());
        btnSaveFeedback.setDisable(true);
        btnUpdateFeedback.setDisable(false);
        btnDeleteFeedback.setDisable(false);
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        Integer customerId = cmbCustomerId.getValue();
        Integer appointmentId = cmbAppointmentId.getValue();
        int rating = spnRating.getValue();

        if (customerId == null || appointmentId == null) {
            new Alert(Alert.AlertType.WARNING, "Customer and Appointment are required.").show();
            return;
        }

        CustomerFeedbackDTO feedbackDTO = new CustomerFeedbackDTO(0, customerId, appointmentId, rating);

        try {
            boolean isSaved = feedbackModel.saveFeedback(feedbackDTO);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Feedback saved successfully.").show();
                loadAllFeedback();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save feedback.").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        CustomerFeedbackTM selectedFeedback = tblFeedback.getSelectionModel().getSelectedItem();
        if (selectedFeedback == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a feedback to update.").show();
            return;
        }

        Integer customerId = cmbCustomerId.getValue();
        Integer appointmentId = cmbAppointmentId.getValue();
        int rating = spnRating.getValue();
        int feedbackId = selectedFeedback.getFeedbackId();

        if (customerId == null || appointmentId == null) {
            new Alert(Alert.AlertType.WARNING, "Customer and Appointment are required.").show();
            return;
        }

        CustomerFeedbackDTO feedbackDTO = new CustomerFeedbackDTO(feedbackId, customerId, appointmentId, rating);

        try {
            boolean isUpdated = feedbackModel.updateFeedback(feedbackDTO);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Feedback updated successfully.").show();
                loadAllFeedback();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update feedback.").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        CustomerFeedbackTM selectedFeedback = tblFeedback.getSelectionModel().getSelectedItem();
        if (selectedFeedback == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a feedback to delete.").show();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this feedback?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = feedbackModel.deleteFeedback(selectedFeedback.getFeedbackId());
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Feedback deleted successfully.").show();
                    loadAllFeedback();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete feedback.").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        cmbCustomerId.setValue(null);
        cmbAppointmentId.setValue(null);
        spnRating.getValueFactory().setValue(3); // Reset to default rating
        btnSaveFeedback.setDisable(false);
        btnUpdateFeedback.setDisable(true);
        btnDeleteFeedback.setDisable(true);
    }
}
