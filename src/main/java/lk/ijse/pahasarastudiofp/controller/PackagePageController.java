package lk.ijse.pahasarastudiofp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.pahasarastudiofp.dto.PackageDTO;
import lk.ijse.pahasarastudiofp.dto.tm.PackageTM;
import lk.ijse.pahasarastudiofp.model.PackageModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PackagePageController implements Initializable {

    @FXML
    private TextField txtName;

    @FXML
    private TextArea txtDescription;

    @FXML
    private Button btnSavePackage;

    @FXML
    private Button btnUpdatePackage;

    @FXML
    private Button btnDeletePackage;

    @FXML
    private Button btnResetPackage;

    @FXML
    private TableView<PackageTM> tblPackages;

    @FXML
    private TableColumn<PackageTM, Integer> colPackageId;

    @FXML
    private TableColumn<PackageTM, String> colName;

    @FXML
    private TableColumn<PackageTM, String> colDescription;

    private final PackageModel packageModel = new PackageModel();
    private final ObservableList<PackageTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        loadAllPackages();
        clearFields();

        tblPackages.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillFields(newValue);
            }
        });
    }

    private void setCellValueFactory() {
        colPackageId.setCellValueFactory(new PropertyValueFactory<>("packageId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    private void loadAllPackages() {
        try {
            List<PackageDTO> allPackages = packageModel.getAllPackages();
            obList.clear(); // clear existing items
            for (PackageDTO dto : allPackages) {
                obList.add(new PackageTM(dto.getPackageId(), dto.getName(), dto.getDescription()));
            }
            tblPackages.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading packages: " + e.getMessage()).show();
        }
    }

    private void fillFields(PackageTM tm) {
        txtName.setText(tm.getName());
        txtDescription.setText(tm.getDescription());
        btnSavePackage.setDisable(true);
        btnUpdatePackage.setDisable(false);
        btnDeletePackage.setDisable(false);
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String name = txtName.getText();
        String description = txtDescription.getText();

        if (name.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Package name is required.").show();
            return;
        }

        PackageDTO packageDTO = new PackageDTO(0, name, description);

        try {
            boolean isSaved = packageModel.savePackage(packageDTO);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Package saved successfully.").show();
                loadAllPackages();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save package.").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        PackageTM selected = tblPackages.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a package to update.").show();
            return;
        }

        String name = txtName.getText();
        String description = txtDescription.getText();

        if (name.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Package name is required.").show();
            return;
        }

        PackageDTO dto = new PackageDTO(selected.getPackageId(), name, description);

        try {
            boolean isUpdated = packageModel.updatePackage(dto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Package updated successfully.").show();
                loadAllPackages();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update package.").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        PackageTM selected = tblPackages.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a package to delete.").show();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this package?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = packageModel.deletePackage(selected.getPackageId());
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Package deleted successfully.").show();
                    loadAllPackages();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete package.").show();
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
        txtName.clear();
        txtDescription.clear();
        tblPackages.getSelectionModel().clearSelection();
        btnSavePackage.setDisable(false);
        btnUpdatePackage.setDisable(true);
        btnDeletePackage.setDisable(true);
    }
}
