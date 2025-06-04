package lk.ijse.pahasarastudiofp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.pahasarastudiofp.dto.PackageDTO;
import lk.ijse.pahasarastudiofp.dto.ServiceDTO;
import lk.ijse.pahasarastudiofp.dto.tm.ServiceTM;
import lk.ijse.pahasarastudiofp.model.PackageModel;
import lk.ijse.pahasarastudiofp.model.ServiceModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ServicePageController implements Initializable {

    @FXML
    private ComboBox<Integer> cmbPackageId;

    @FXML
    private TextField txtServiceName;

    @FXML
    private TextField txtPrice;

    @FXML
    private Button btnSaveService;

    @FXML
    private Button btnUpdateService;

    @FXML
    private Button btnDeleteService;

    @FXML
    private Button btnResetService;

    @FXML
    private TableView<ServiceTM> tblServices;

    @FXML
    private TableColumn<ServiceTM, Integer> colServiceId;

    @FXML
    private TableColumn<ServiceTM, Integer> colPackageId;

    @FXML
    private TableColumn<ServiceTM, String> colServiceName;

    @FXML
    private TableColumn<ServiceTM, Double> colPrice;

    private final ServiceModel serviceModel = new ServiceModel();
    private final PackageModel packageModel = new PackageModel();
    private ObservableList<ServiceTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadPackageIds();
        setCellValueFactory();
        loadAllServices();


        btnUpdateService.setDisable(true);
        btnDeleteService.setDisable(true);

        tblServices.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillFields(newValue);
            } else {
                // If selection is cleared, reset button states and clear fields
                clearFields();
                btnSaveService.setDisable(false);
                btnUpdateService.setDisable(true);
                btnDeleteService.setDisable(true);
            }
        });
    }

    private void loadPackageIds() {
        try {
            List<PackageDTO> allPackages = packageModel.getAllPackages();
            cmbPackageId.getItems().clear();
            for (PackageDTO packageDTO : allPackages) {
                cmbPackageId.getItems().add(packageDTO.getPackageId());
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading package IDs: " + e.getMessage()).show();
        }
    }

    private void setCellValueFactory() {
        colServiceId.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        colPackageId.setCellValueFactory(new PropertyValueFactory<>("packageId"));
        colServiceName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    private void loadAllServices() {
        obList.clear();
        try {
            List<ServiceDTO> allServices = serviceModel.getAllServices();
            for (ServiceDTO service : allServices) {
                obList.add(new ServiceTM(
                        service.getServiceId(),
                        service.getPackageId(),
                        service.getName(),
                        service.getPrice()
                ));
            }
            tblServices.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading services: " + e.getMessage()).show();
        }
    }

    private void fillFields(ServiceTM tm) {
        cmbPackageId.setValue(tm.getPackageId());
        txtServiceName.setText(tm.getName());
        txtPrice.setText(String.valueOf(tm.getPrice()));
        btnSaveService.setDisable(true);
        btnUpdateService.setDisable(false);
        btnDeleteService.setDisable(false);
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        Integer packageId = cmbPackageId.getValue();
        String name = txtServiceName.getText();
        String priceText = txtPrice.getText();

        if (packageId == null || name.isEmpty() || priceText.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Package ID, Name, and Price are required.").show();
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            ServiceDTO serviceDTO = new ServiceDTO(0, packageId, name, price);
            boolean isSaved = serviceModel.saveService(serviceDTO);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Service saved successfully.").show();
                loadAllServices();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save service.").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid price format.").show();
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        ServiceTM selectedService = tblServices.getSelectionModel().getSelectedItem();
        if (selectedService == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a service to update.").show();
            return;
        }

        Integer packageId = cmbPackageId.getValue();
        String name = txtServiceName.getText();
        String priceText = txtPrice.getText();
        int serviceId = selectedService.getServiceId();

        if (packageId == null || name.isEmpty() || priceText.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Package ID, Name, and Price are required.").show();
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            ServiceDTO serviceDTO = new ServiceDTO(serviceId, packageId, name, price);
            boolean isUpdated = serviceModel.updateService(serviceDTO);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Service updated successfully.").show();
                loadAllServices();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update service.").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid price format.").show();
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        ServiceTM selectedService = tblServices.getSelectionModel().getSelectedItem();
        if (selectedService == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a service to delete.").show();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this service?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = serviceModel.deleteService(selectedService.getServiceId());
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Service deleted successfully.").show();
                    loadAllServices();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete service.").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        clearFields();
        tblServices.getSelectionModel().clearSelection();
        btnSaveService.setDisable(false);
        btnUpdateService.setDisable(true);
        btnDeleteService.setDisable(true);
    }

    private void clearFields() {
        cmbPackageId.setValue(null);
        txtServiceName.clear();
        txtPrice.clear();

    }
}