package lk.ijse.pahasarastudiofp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.pahasarastudiofp.dto.EmployeeDTO;
import lk.ijse.pahasarastudiofp.dto.tm.EmployeeTM;
import lk.ijse.pahasarastudiofp.model.EmployeeModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeePageController implements Initializable {

    @FXML
    private TextField txtName;

    @FXML
    private TextArea txtDescription;

    @FXML
    private Button btnSaveEmployee;

    @FXML
    private Button btnUpdateEmployee;

    @FXML
    private Button btnDeleteEmployee;

    @FXML
    private Button btnResetEmployee;

    @FXML
    private TableView<EmployeeTM> tblEmployees;

    @FXML
    private TableColumn<EmployeeTM, Integer> colEmployeeId;

    @FXML
    private TableColumn<EmployeeTM, String> colName;

    @FXML
    private TableColumn<EmployeeTM, String> colDescription;

    private final EmployeeModel employeeModel = new EmployeeModel();
    private final ObservableList<EmployeeTM> obList = FXCollections.observableArrayList();


    private int selectedEmployeeId = -1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setCellValueFactory();


        loadAllEmployees();


        tblEmployees.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillFields(newValue);
            } else {
                clearFields();
            }
        });


        btnUpdateEmployee.setDisable(true);
        btnDeleteEmployee.setDisable(true);
    }

    private void setCellValueFactory() {
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    private void loadAllEmployees() {
        try {
            obList.clear();
            List<EmployeeDTO> allEmployees = employeeModel.getAllEmployees();
            for (EmployeeDTO employee : allEmployees) {
                obList.add(new EmployeeTM(
                        employee.getEmployeeId(),
                        employee.getName(),
                        employee.getDescription()
                ));
            }
            tblEmployees.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading employees: " + e.getMessage()).show();
        }
    }

    private void fillFields(EmployeeTM tm) {
        selectedEmployeeId = tm.getEmployeeId();
        txtName.setText(tm.getName());
        txtDescription.setText(tm.getDescription());
        btnSaveEmployee.setDisable(true);
        btnUpdateEmployee.setDisable(false);
        btnDeleteEmployee.setDisable(false);
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String name = txtName.getText().trim();
        String description = txtDescription.getText().trim();

        if (name.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Employee name is required.").show();
            return;
        }

        EmployeeDTO employeeDTO = new EmployeeDTO(0, name, description);

        try {
            boolean isSaved = employeeModel.saveEmployee(employeeDTO);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Employee saved successfully.").show();
                loadAllEmployees();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save employee.").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (selectedEmployeeId == -1) {
            new Alert(Alert.AlertType.WARNING, "No employee selected to update.").show();
            return;
        }

        String name = txtName.getText().trim();
        String description = txtDescription.getText().trim();

        if (name.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Employee name is required.").show();
            return;
        }

        EmployeeDTO employeeDTO = new EmployeeDTO(selectedEmployeeId, name, description);

        try {
            boolean isUpdated = employeeModel.updateEmployee(employeeDTO);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Employee updated successfully.").show();
                loadAllEmployees();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update employee.").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        EmployeeTM selectedEmployee = tblEmployees.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            new Alert(Alert.AlertType.WARNING, "Please select an employee to delete.").show();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this employee?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = employeeModel.deleteEmployee(selectedEmployee.getEmployeeId());
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Employee deleted successfully.").show();
                    loadAllEmployees();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete employee.").show();
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
        tblEmployees.getSelectionModel().clearSelection();
        selectedEmployeeId = -1;
        btnSaveEmployee.setDisable(false);
        btnUpdateEmployee.setDisable(true);
        btnDeleteEmployee.setDisable(true);
    }
}
