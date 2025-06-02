package lk.ijse.pahasarastudiofp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.pahasarastudiofp.dto.EmployeeDTO;
import lk.ijse.pahasarastudiofp.dto.ExpensesDTO;
import lk.ijse.pahasarastudiofp.dto.tm.ExpensesTM;
import lk.ijse.pahasarastudiofp.model.EmployeeModel;
import lk.ijse.pahasarastudiofp.model.ExpensesModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ExpensesPageController implements Initializable {

    @FXML
    private ComboBox<Integer> cmbEmployeeId;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextArea txtDescription;

    @FXML
    private Button btnSaveExpense;

    @FXML
    private Button btnUpdateExpense;

    @FXML
    private Button btnDeleteExpense;

    @FXML
    private Button btnResetExpense;

    @FXML
    private TableView<ExpensesTM> tblExpenses;

    @FXML
    private TableColumn<ExpensesTM, Integer> colExpensesId;

    @FXML
    private TableColumn<ExpensesTM, Integer> colEmployeeId;

    @FXML
    private TableColumn<ExpensesTM, String> colEmployeeName;

    @FXML
    private TableColumn<ExpensesTM, Double> colAmount;

    @FXML
    private TableColumn<ExpensesTM, String> colDescription;

    private final ExpensesModel expensesModel = new ExpensesModel();
    private final EmployeeModel employeeModel = new EmployeeModel();
    private final ObservableList<ExpensesTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadEmployeeIds();
        setCellValueFactory();
        loadAllExpenses();


        btnUpdateExpense.setDisable(true);
        btnDeleteExpense.setDisable(true);

        tblExpenses.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillFields(newValue);
            }
        });
    }

    private void loadEmployeeIds() {
        try {
            List<EmployeeDTO> allEmployees = employeeModel.getAllEmployees();
            cmbEmployeeId.setItems(FXCollections.observableArrayList(
                    allEmployees.stream().map(EmployeeDTO::getEmployeeId).collect(Collectors.toList())
            ));
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error loading employee IDs: " + e.getMessage());
        }
    }

    private void setCellValueFactory() {
        colExpensesId.setCellValueFactory(new PropertyValueFactory<>("expensesId"));
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colEmployeeName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    private void loadAllExpenses() {
        try {
            List<ExpensesDTO> allExpenses = expensesModel.getAllExpenses();
            obList.clear();
            for (ExpensesDTO expense : allExpenses) {
                String employeeName = expensesModel.getEmployeeName(expense.getEmployeeId());
                obList.add(new ExpensesTM(
                        expense.getExpensesId(),
                        expense.getEmployeeId(),
                        expense.getAmount(),
                        expense.getDescription(),
                        employeeName
                ));
            }
            tblExpenses.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error loading expenses: " + e.getMessage());
        }
    }

    private void fillFields(ExpensesTM tm) {
        cmbEmployeeId.setValue(tm.getEmployeeId());
        txtAmount.setText(String.valueOf(tm.getAmount()));
        txtDescription.setText(tm.getDescription());
        btnSaveExpense.setDisable(true);
        btnUpdateExpense.setDisable(false);
        btnDeleteExpense.setDisable(false);
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        Integer employeeId = cmbEmployeeId.getValue();
        String amountText = txtAmount.getText();
        String description = txtDescription.getText();

        if (employeeId == null || amountText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Employee and Amount are required.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            ExpensesDTO expensesDTO = new ExpensesDTO(0, employeeId, amount, description);
            boolean isSaved = expensesModel.saveExpense(expensesDTO);
            if (isSaved) {
                showAlert(Alert.AlertType.INFORMATION, "Expense saved successfully.");
                loadAllExpenses();
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to save expense.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid amount format.");
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        ExpensesTM selectedExpense = tblExpenses.getSelectionModel().getSelectedItem();
        if (selectedExpense == null) {
            showAlert(Alert.AlertType.WARNING, "Please select an expense to update.");
            return;
        }

        Integer employeeId = cmbEmployeeId.getValue();
        String amountText = txtAmount.getText();
        String description = txtDescription.getText();
        int expenseId = selectedExpense.getExpensesId();

        if (employeeId == null || amountText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Employee and Amount are required.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            ExpensesDTO expensesDTO = new ExpensesDTO(expenseId, employeeId, amount, description);
            boolean isUpdated = expensesModel.updateExpense(expensesDTO);
            if (isUpdated) {
                showAlert(Alert.AlertType.INFORMATION, "Expense updated successfully.");
                loadAllExpenses();
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to update expense.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid amount format.");
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        ExpensesTM selectedExpense = tblExpenses.getSelectionModel().getSelectedItem();
        if (selectedExpense == null) {
            showAlert(Alert.AlertType.WARNING, "Please select an expense to delete.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this expense?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = expensesModel.deleteExpense(selectedExpense.getExpensesId());
                if (isDeleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Expense deleted successfully.");
                    loadAllExpenses();
                    clearFields();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failed to delete expense.");
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

    private void clearFields() {
        cmbEmployeeId.getSelectionModel().clearSelection();
        txtAmount.clear();
        txtDescription.clear();
        btnSaveExpense.setDisable(false);
        btnUpdateExpense.setDisable(true);
        btnDeleteExpense.setDisable(true);
        tblExpenses.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String message) {
        new Alert(type, message).show();
    }
}
