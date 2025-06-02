package lk.ijse.pahasarastudiofp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.pahasarastudiofp.dto.PackageDTO;
import lk.ijse.pahasarastudiofp.dto.PackageServiceDTO;
import lk.ijse.pahasarastudiofp.dto.ServiceDTO;
import lk.ijse.pahasarastudiofp.dto.tm.PackageServiceTM;
import lk.ijse.pahasarastudiofp.model.PackageModel;
import lk.ijse.pahasarastudiofp.model.PackageServiceModel;
import lk.ijse.pahasarastudiofp.model.ServiceModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class PackageServicePageController implements Initializable {

    @FXML
    private ComboBox<Integer> cmbPackageId;

    @FXML
    private ComboBox<Integer> cmbServiceId;

    @FXML
    private Button btnAddService;

    @FXML
    private Button btnRemoveService;

    @FXML
    private Button btnReset;

    @FXML
    private TableView<PackageServiceTM> tblPackageServices;

    @FXML
    private TableColumn<PackageServiceTM, Integer> colPackageServiceId;

    @FXML
    private TableColumn<PackageServiceTM, Integer> colPackageId;

    @FXML
    private TableColumn<PackageServiceTM, String> colPackageName;

    @FXML
    private TableColumn<PackageServiceTM, Integer> colServiceId;

    @FXML
    private TableColumn<PackageServiceTM, String> colServiceName;

    private final PackageServiceModel packageServiceModel = new PackageServiceModel();
    private final PackageModel packageModel = new PackageModel();
    private final ServiceModel serviceModel = new ServiceModel();
    private final ObservableList<PackageServiceTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadPackageIds();
        loadServiceIds();
        setCellValueFactory();
        loadAllPackageServices();

        tblPackageServices.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                cmbPackageId.setValue(newValue.getPackageId());
                cmbServiceId.setValue(newValue.getServiceId());
            } else {
                clearFields();
            }
        });
    }

    private void loadPackageIds() {
        try {
            cmbPackageId.getItems().clear();
            List<PackageDTO> allPackages = packageModel.getAllPackages();
            for (PackageDTO packageDTO : allPackages) {
                cmbPackageId.getItems().add(packageDTO.getPackageId());
            }
        } catch (SQLException | ClassNotFoundException e) {
            showErrorAlert("Error loading package IDs: " + e.getMessage());
        }
    }

    private void loadServiceIds() {
        try {
            cmbServiceId.getItems().clear();
            List<ServiceDTO> allServices = serviceModel.getAllServices();
            for (ServiceDTO serviceDTO : allServices) {
                cmbServiceId.getItems().add(serviceDTO.getServiceId());
            }
        } catch (SQLException | ClassNotFoundException e) {
            showErrorAlert("Error loading service IDs: " + e.getMessage());
        }
    }

    private void setCellValueFactory() {
        colPackageServiceId.setCellValueFactory(new PropertyValueFactory<>("packageServiceId"));
        colPackageId.setCellValueFactory(new PropertyValueFactory<>("packageId"));
        colPackageName.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        colServiceId.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        colServiceName.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        tblPackageServices.setItems(obList);
    }

    private void loadAllPackageServices() {
        obList.clear();
        try {
            List<PackageServiceDTO> allPackageServices = packageServiceModel.getAllPackageServices();
            for (PackageServiceDTO ps : allPackageServices) {
                PackageDTO pkg = packageModel.getPackage(ps.getPackageId());
                ServiceDTO svc = serviceModel.getService(ps.getServiceId());

                if (pkg != null && svc != null) {
                    obList.add(new PackageServiceTM(
                            ps.getPackageServiceId(),
                            ps.getPackageId(),
                            ps.getServiceId(),
                            pkg.getName(),
                            svc.getName()
                    ));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            showErrorAlert("Error loading package services: " + e.getMessage());
        }
    }

    @FXML
    void btnAddServiceOnAction(ActionEvent event) {
        Integer packageId = cmbPackageId.getValue();
        Integer serviceId = cmbServiceId.getValue();

        if (packageId == null || serviceId == null) {
            showWarningAlert("Please select both Package and Service.");
            return;
        }

        try {
            PackageServiceDTO dto = new PackageServiceDTO(0, packageId, serviceId);
            boolean isSaved = packageServiceModel.savePackageService(dto);
            if (isSaved) {
                showInfoAlert("Service added to package successfully.");
                loadAllPackageServices();
                clearFields();
            } else {
                showErrorAlert("Failed to add service to package.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            showErrorAlert("Database error: " + e.getMessage());
        }
    }

    @FXML
    void btnRemoveServiceOnAction(ActionEvent event) {
        Integer packageId = cmbPackageId.getValue();
        Integer serviceId = cmbServiceId.getValue();

        if (packageId == null || serviceId == null) {
            showWarningAlert("Please select both Package and Service to remove.");
            return;
        }

        try {
            boolean isDeleted = packageServiceModel.deletePackageService(packageId, serviceId);
            if (isDeleted) {
                showInfoAlert("Service removed from package successfully.");
                loadAllPackageServices();
                clearFields();
            } else {
                showErrorAlert("Failed to remove service from package.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            showErrorAlert("Database error: " + e.getMessage());
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        cmbPackageId.setValue(null);
        cmbServiceId.setValue(null);
        tblPackageServices.getSelectionModel().clearSelection();
    }

    private void showInfoAlert(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).show();
    }

    private void showWarningAlert(String message) {
        new Alert(Alert.AlertType.WARNING, message).show();
    }

    private void showErrorAlert(String message) {
        new Alert(Alert.AlertType.ERROR, message).show();
    }
}
