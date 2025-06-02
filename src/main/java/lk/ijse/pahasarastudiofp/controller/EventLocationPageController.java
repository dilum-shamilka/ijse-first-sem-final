package lk.ijse.pahasarastudiofp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.pahasarastudiofp.dto.EventLocationDTO;
import lk.ijse.pahasarastudiofp.dto.tm.EventLocationTM;
import lk.ijse.pahasarastudiofp.model.EventLocationModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EventLocationPageController implements Initializable {

    @FXML
    private TextArea txtAddress;

    @FXML
    private TextField txtLocationType;

    @FXML
    private TextField txtVenueType;

    @FXML
    private Button btnSaveLocation;

    @FXML
    private Button btnUpdateLocation;

    @FXML
    private Button btnDeleteLocation;

    @FXML
    private Button btnResetLocation;

    @FXML
    private TableView<EventLocationTM> tblLocations;

    @FXML
    private TableColumn<EventLocationTM, Integer> colEventId;

    @FXML
    private TableColumn<EventLocationTM, String> colAddress;

    @FXML
    private TableColumn<EventLocationTM, String> colLocationType;

    @FXML
    private TableColumn<EventLocationTM, String> colVenueType;

    private final EventLocationModel locationModel = new EventLocationModel();
    private final ObservableList<EventLocationTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        loadAllLocations();
        clearFields();

        // Set listener for table selection to fill form fields
        tblLocations.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillFields(newValue);
            }
        });
    }

    // Initialize table columns mapping
    private void setCellValueFactory() {
        colEventId.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colLocationType.setCellValueFactory(new PropertyValueFactory<>("locationType"));
        colVenueType.setCellValueFactory(new PropertyValueFactory<>("venueType"));
    }

    // Load all event locations from DB and populate table
    private void loadAllLocations() {
        try {
            List<EventLocationDTO> allLocations = locationModel.getAllLocations();
            obList.clear();
            for (EventLocationDTO location : allLocations) {
                obList.add(new EventLocationTM(
                        location.getEventId(),
                        location.getAddress(),
                        location.getLocationType(),
                        location.getVenueType()
                ));
            }
            tblLocations.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error loading event locations: " + e.getMessage());
        }
    }

    // Fill form fields when a table row is selected
    private void fillFields(EventLocationTM tm) {
        txtAddress.setText(tm.getAddress());
        txtLocationType.setText(tm.getLocationType());
        txtVenueType.setText(tm.getVenueType());

        btnSaveLocation.setDisable(true);
        btnUpdateLocation.setDisable(false);
        btnDeleteLocation.setDisable(false);
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String address = txtAddress.getText().trim();
        String locationType = txtLocationType.getText().trim();
        String venueType = txtVenueType.getText().trim();

        if (address.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Address is required.");
            return;
        }

        EventLocationDTO locationDTO = new EventLocationDTO(0, address, locationType, venueType);

        try {
            boolean isSaved = locationModel.saveLocation(locationDTO);
            if (isSaved) {
                showAlert(Alert.AlertType.INFORMATION, "Event location saved successfully.");
                loadAllLocations();
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to save event location.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        EventLocationTM selectedLocation = tblLocations.getSelectionModel().getSelectedItem();
        if (selectedLocation == null) {
            showAlert(Alert.AlertType.WARNING, "Please select an event location to update.");
            return;
        }

        String address = txtAddress.getText().trim();
        String locationType = txtLocationType.getText().trim();
        String venueType = txtVenueType.getText().trim();
        int eventId = selectedLocation.getEventId();

        if (address.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Address is required.");
            return;
        }

        EventLocationDTO locationDTO = new EventLocationDTO(eventId, address, locationType, venueType);

        try {
            boolean isUpdated = locationModel.updateLocation(locationDTO);
            if (isUpdated) {
                showAlert(Alert.AlertType.INFORMATION, "Event location updated successfully.");
                loadAllLocations();
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to update event location.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        EventLocationTM selectedLocation = tblLocations.getSelectionModel().getSelectedItem();
        if (selectedLocation == null) {
            showAlert(Alert.AlertType.WARNING, "Please select an event location to delete.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this event location?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = locationModel.deleteLocation(selectedLocation.getEventId());
                if (isDeleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Event location deleted successfully.");
                    loadAllLocations();
                    clearFields();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failed to delete event location.");
                }
            } catch (SQLException | ClassNotFoundException e) {
                showAlert(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        clearFields();
    }

    // Clear all input fields and reset buttons and table selection
    private void clearFields() {
        txtAddress.clear();
        txtLocationType.clear();
        txtVenueType.clear();

        btnSaveLocation.setDisable(false);
        btnUpdateLocation.setDisable(true);
        btnDeleteLocation.setDisable(true);

        tblLocations.getSelectionModel().clearSelection();
    }

    // Helper method to show alerts
    private void showAlert(Alert.AlertType type, String message) {
        new Alert(type, message).show();
    }
}
