package lk.ijse.pahasarastudiofp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.pahasarastudiofp.dto.CustomerDTO;
import lk.ijse.pahasarastudiofp.dto.tm.CustomerTM;
import lk.ijse.pahasarastudiofp.model.CustomerModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);


    private static final String PHONE_REGEX = "^\\d{10}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

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
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String contact = txtContact.getText().trim();
        String address = txtAddress.getText().trim();

        if (name.isEmpty() || email.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Name and Email are required.").show();
            return;
        }

        if (!isValidEmail(email)) {
            new Alert(Alert.AlertType.WARNING, "Please enter a valid email address.").show();
            return;
        }


        if (!contact.isEmpty() && !isValidContact(contact)) {
            new Alert(Alert.AlertType.WARNING, "Please enter a valid 10-digit phone number for contact.").show();
            return;
        }

        CustomerDTO customerDTO = new CustomerDTO(0, name, email, contact, address);

        try {
            boolean isSaved = customerModel.saveCustomer(customerDTO);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Customer saved successfully.").show();
                loadTableData();
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
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String contact = txtContact.getText().trim();
        String address = txtAddress.getText().trim();

        if (name.isEmpty() || email.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Name and Email are required.").show();
            return;
        }

        if (!isValidEmail(email)) {
            new Alert(Alert.AlertType.WARNING, "Please enter a valid email address.").show();
            return;
        }


        if (!contact.isEmpty() && !isValidContact(contact)) {
            new Alert(Alert.AlertType.WARNING, "Please enter a valid 10-digit phone number for contact.").show();
            return;
        }

        CustomerDTO customerDTO = new CustomerDTO(id, name, email, contact, address);

        try {
            boolean isUpdated = customerModel.updateCustomer(customerDTO);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Customer updated successfully.").show();
                loadTableData();
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
                    loadTableData();
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
            clearFields();
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

    private boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }


    private boolean isValidContact(String contact) {
        if (contact == null) {
            return false;
        }
        Matcher matcher = PHONE_PATTERN.matcher(contact);
        return matcher.matches();
    }

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