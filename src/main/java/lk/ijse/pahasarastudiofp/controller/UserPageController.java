package lk.ijse.pahasarastudiofp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.pahasarastudiofp.dto.UserDTO;
import lk.ijse.pahasarastudiofp.dto.tm.UserTM;
import lk.ijse.pahasarastudiofp.model.UserModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserPageController implements Initializable {

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private ComboBox<String> cmbRole;

    @FXML
    private Button btnSaveUser;

    @FXML
    private Button btnUpdateUser;

    @FXML
    private Button btnDeleteUser;

    @FXML
    private Button btnResetUser;

    @FXML
    private TableView<UserTM> tblUsers;

    @FXML
    private TableColumn<UserTM, Integer> colId;

    @FXML
    private TableColumn<UserTM, String> colName;

    @FXML
    private TableColumn<UserTM, String> colEmail;

    @FXML
    private TableColumn<UserTM, String> colRole;

    private final UserModel userModel = new UserModel();
    private final ObservableList<UserTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbRole.getItems().addAll("owner", "cashier");
        setCellValueFactory();
        loadAllUsers();
        clearFields();

        tblUsers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillFields(newValue);
            }
        });
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
    }

    private void loadAllUsers() {
        try {
            obList.clear();
            List<UserDTO> allUsers = userModel.getAllUsers();
            for (UserDTO user : allUsers) {
                obList.add(new UserTM(
                        user.getUserId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole()
                ));
            }
            tblUsers.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading users: " + e.getMessage()).show();
        }
    }

    private void fillFields(UserTM tm) {
        txtName.setText(tm.getName());
        txtEmail.setText(tm.getEmail());
        cmbRole.setValue(tm.getRole());
        btnSaveUser.setDisable(true);
        btnUpdateUser.setDisable(false);
        btnDeleteUser.setDisable(false);
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String name = txtName.getText();
        String email = txtEmail.getText();
        String role = cmbRole.getValue();

        if (name.isEmpty() || email.isEmpty() || role == null) {
            new Alert(Alert.AlertType.WARNING, "Name, Email, and Role are required.").show();
            return;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            new Alert(Alert.AlertType.WARNING, "Please enter a valid email address.").show();
            return;
        }

        UserDTO userDTO = new UserDTO(0, name, email, role);

        try {
            boolean isSaved = userModel.saveUser(userDTO);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "User saved successfully.").show();
                loadAllUsers();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save user.").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        UserTM selectedUser = tblUsers.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a user to update.").show();
            return;
        }

        int id = selectedUser.getUserId();
        String name = txtName.getText();
        String email = txtEmail.getText();
        String role = cmbRole.getValue();

        if (name.isEmpty() || email.isEmpty() || role == null) {
            new Alert(Alert.AlertType.WARNING, "Name, Email, and Role are required.").show();
            return;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            new Alert(Alert.AlertType.WARNING, "Please enter a valid email address.").show();
            return;
        }

        UserDTO userDTO = new UserDTO(id, name, email, role);

        try {
            boolean isUpdated = userModel.updateUser(userDTO);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "User updated successfully.").show();
                loadAllUsers();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update user.").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        UserTM selectedUser = tblUsers.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a user to delete.").show();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this user?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = userModel.deleteUser(selectedUser.getUserId());
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "User deleted successfully.").show();
                    loadAllUsers();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete user.").show();
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
        txtEmail.clear();
        cmbRole.setValue(null);
        tblUsers.getSelectionModel().clearSelection();
        btnSaveUser.setDisable(false);
        btnUpdateUser.setDisable(true);
        btnDeleteUser.setDisable(true);
    }
}
