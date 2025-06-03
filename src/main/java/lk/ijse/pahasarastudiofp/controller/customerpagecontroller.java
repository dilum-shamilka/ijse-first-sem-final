// File: src/main/java/lk/ijse/pahasarastudiofp/controller/customerpagecontroller.java

package lk.ijse.pahasarastudiofp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import  lk.ijse.pahasarastudiofp.dto.CustomerDTO;
import lk.ijse.pahasarastudiofp.dto.tm.CustomerTM;
import lk.ijse.pahasarastudiofp.model.CustomerModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class customerpagecontroller implements Initializable {

    public Label lblId;
    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtAddress;

    @FXML
    private Button btnSaveClient;

    @FXML
    private Button btnUpdateClient;

    @FXML
    private Button btnDeleteClient;

    @FXML
    private Button btnResetClient;

    @FXML
    private TableView<CustomerTM> tblCustomers;

    @FXML
    private TableColumn<CustomerTM, Integer> colId;

    @FXML
    private TableColumn<CustomerTM, String> colName;

    @FXML
    private TableColumn<CustomerTM, String> colEmail;

    @FXML
    private TableColumn<CustomerTM, String> colContact;

    @FXML
    private TableColumn<CustomerTM, String> colAddress;

    private final CustomerModel customerModel = new CustomerModel();
    private ObservableList<CustomerTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        try {
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to initialize Customer Page", ButtonType.OK).show();
        }
    }


    private void fillFields(CustomerTM tm) {
        txtName.setText(tm.getName());
        txtEmail.setText(tm.getEmail());
        txtContact.setText(tm.getContact());
        txtAddress.setText(tm.getAddress());
        btnSaveClient.setDisable(true);
        btnUpdateClient.setDisable(false);
        btnDeleteClient.setDisable(false);
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String name = txtName.getText();
        String email = txtEmail.getText();
        String contact = txtContact.getText();
        String address = txtAddress.getText();

        if (name.isEmpty() || email.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Name and Email are required.").show();
            return;
        }

        CustomerDTO customerDTO = new CustomerDTO(0, name, email, contact, address);

        try {
            boolean isSaved = customerModel.saveCustomer(customerDTO);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Customer saved successfully.").show();
                loadTableData(); // Reload the table to show the new customer
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save customer.").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        }
    }

    private void clearFields() {
        txtName.clear();
        txtEmail.clear();
        txtContact.clear();
        txtAddress.clear();
        btnSaveClient.setDisable(false);
        btnUpdateClient.setDisable(true);
        btnDeleteClient.setDisable(true);
        tblCustomers.getSelectionModel().clearSelection();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        CustomerTM selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a customer to update.").show();
            return;
        }

        int id = selectedCustomer.getCustomerId();
        String name = txtName.getText();
        String email = txtEmail.getText();
        String contact = txtContact.getText();
        String address = txtAddress.getText();

        if (name.isEmpty() || email.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Name and Email are required.").show();
            return;
        }

        CustomerDTO customerDTO = new CustomerDTO(id, name, email, contact, address);

        try {
            boolean isUpdated = customerModel.updateCustomer(customerDTO);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Customer updated successfully.").show();
                loadTableData(); // Reload the table to show the updated customer
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update customer.").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        CustomerTM selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a customer to delete.").show();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = customerModel.deleteCustomer(selectedCustomer.getCustomerId());
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer deleted successfully.").show();
                    loadTableData(); // Reload the table after deletion
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete customer.").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
            }
        }
    }


    private void loadTableData() throws SQLException, ClassNotFoundException {
        ArrayList<CustomerDTO> customerDtos=customerModel.getAllCustomers();

        ObservableList<CustomerTM> customerTMS=FXCollections.observableArrayList();

        for(CustomerDTO customerDto:customerDtos){
            CustomerTM customerTM=new CustomerTM(customerDto.getCustomerId(), customerDto.getName(), customerDto.getEmail(), customerDto.getContact(), customerDto.getAddress());
            customerTMS.add(customerTM);
        }
        tblCustomers.setItems(customerTMS);
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        resetPage();
    }

    public void resetPage(){
        try {
            loadTableData();
            clearFields(); // Use existing clearFields method
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to Reset Customer Page").show();
        }
    }

    public void onClickTable(MouseEvent mouseEvent) {
        CustomerTM selectedItem=tblCustomers.getSelectionModel().getSelectedItem();

        if(selectedItem!=null){
            fillFields(selectedItem);
        }
    }

    // Getters and Setters for buttons (if needed by FXML or other parts of the app)
    public Button getBtnSaveClient() {
        return btnSaveClient;
    }
    public void setBtnSaveClient(Button btnSaveClient) {
        this.btnSaveClient = btnSaveClient;
    }
    public Button getBtnUpdateClient() {
        return btnUpdateClient;
    }
    public void setBtnUpdateClient(Button btnUpdateClient) {
        this.btnUpdateClient = btnUpdateClient;
    }
    public Button getBtnDeleteClient() {
        return btnDeleteClient;
    }
    public void setBtnDeleteClient(Button btnDeleteClient) {
        this.btnDeleteClient = btnDeleteClient;
    }
    public Button getBtnResetClient() {
        return btnResetClient;
    }
    public void setBtnResetClient(Button btnResetClient) {
        this.btnResetClient = btnResetClient;
    }
}