package lk.ijse.pahasarastudiofp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.pahasarastudiofp.dto.DiscountDTO;
import lk.ijse.pahasarastudiofp.dto.tm.DiscountTM;
import lk.ijse.pahasarastudiofp.model.DiscountModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DiscountPageController implements Initializable {

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPercentage;

    @FXML
    private TextField txtAmount;

    @FXML
    private Button btnSaveDiscount;

    @FXML
    private Button btnUpdateDiscount;

    @FXML
    private Button btnDeleteDiscount;

    @FXML
    private Button btnResetDiscount;

    @FXML
    private TableView<DiscountTM> tblDiscounts;

    @FXML
    private TableColumn<DiscountTM, Integer> colDiscountId;

    @FXML
    private TableColumn<DiscountTM, String> colName;

    @FXML
    private TableColumn<DiscountTM, Double> colPercentage;

    @FXML
    private TableColumn<DiscountTM, Double> colAmount;

    private final DiscountModel discountModel = new DiscountModel();
    private final ObservableList<DiscountTM> obList = FXCollections.observableArrayList();

    private int selectedDiscountId = -1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        loadAllDiscounts();

        tblDiscounts.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillFields(newValue);
            }
        });

        btnUpdateDiscount.setDisable(true);
        btnDeleteDiscount.setDisable(true);
    }

    private void setCellValueFactory() {
        colDiscountId.setCellValueFactory(new PropertyValueFactory<>("discountId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPercentage.setCellValueFactory(new PropertyValueFactory<>("percentage"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    }

    private void loadAllDiscounts() {
        try {
            List<DiscountDTO> allDiscounts = discountModel.getAllDiscounts();
            obList.clear();
            for (DiscountDTO discount : allDiscounts) {
                obList.add(new DiscountTM(
                        discount.getDiscountId(),
                        discount.getName(),
                        discount.getPercentage(),
                        discount.getAmount()
                ));
            }
            tblDiscounts.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading discounts: " + e.getMessage()).show();
        }
    }

    private void fillFields(DiscountTM tm) {
        selectedDiscountId = tm.getDiscountId();
        txtName.setText(tm.getName());
        txtPercentage.setText(tm.getPercentage() != null ? String.valueOf(tm.getPercentage()) : "");
        txtAmount.setText(tm.getAmount() != null ? String.valueOf(tm.getAmount()) : "");
        btnSaveDiscount.setDisable(true);
        btnUpdateDiscount.setDisable(false);
        btnDeleteDiscount.setDisable(false);
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String name = txtName.getText().trim();
        String percentageText = txtPercentage.getText().trim();
        String amountText = txtAmount.getText().trim();

        if (name.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Discount Name is required.").show();
            return;
        }

        Double percentage = null;
        if (!percentageText.isEmpty()) {
            try {
                percentage = Double.parseDouble(percentageText);
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid percentage format.").show();
                return;
            }
        }

        Double amount = null;
        if (!amountText.isEmpty()) {
            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid amount format.").show();
                return;
            }
        }

        DiscountDTO discountDTO = new DiscountDTO(0, name, percentage, amount);

        try {
            boolean isSaved = discountModel.saveDiscount(discountDTO);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Discount saved successfully.").show();
                loadAllDiscounts();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save discount.").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (selectedDiscountId == -1) {
            new Alert(Alert.AlertType.WARNING, "Please select a discount to update.").show();
            return;
        }

        String name = txtName.getText().trim();
        String percentageText = txtPercentage.getText().trim();
        String amountText = txtAmount.getText().trim();

        if (name.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Discount Name is required.").show();
            return;
        }

        Double percentage = null;
        if (!percentageText.isEmpty()) {
            try {
                percentage = Double.parseDouble(percentageText);
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid percentage format.").show();
                return;
            }
        }

        Double amount = null;
        if (!amountText.isEmpty()) {
            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid amount format.").show();
                return;
            }
        }

        DiscountDTO discountDTO = new DiscountDTO(selectedDiscountId, name, percentage, amount);

        try {
            boolean isUpdated = discountModel.updateDiscount(discountDTO);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Discount updated successfully.").show();
                loadAllDiscounts();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update discount.").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (selectedDiscountId == -1) {
            new Alert(Alert.AlertType.WARNING, "Please select a discount to delete.").show();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this discount?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = discountModel.deleteDiscount(selectedDiscountId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Discount deleted successfully.").show();
                    loadAllDiscounts();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete discount.").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
                e.printStackTrace();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtName.clear();
        txtPercentage.clear();
        txtAmount.clear();
        selectedDiscountId = -1;
        tblDiscounts.getSelectionModel().clearSelection();
        btnSaveDiscount.setDisable(false);
        btnUpdateDiscount.setDisable(true);
        btnDeleteDiscount.setDisable(true);
    }
}
