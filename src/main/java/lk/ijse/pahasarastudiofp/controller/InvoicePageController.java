package lk.ijse.pahasarastudiofp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.pahasarastudiofp.dto.InvoiceDTO;
import lk.ijse.pahasarastudiofp.dto.tm.InvoiceTM;
import lk.ijse.pahasarastudiofp.model.InvoiceModel;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class InvoicePageController implements Initializable {

    @FXML
    private TableColumn<InvoiceTM, Double> colAmount;
    @FXML
    private TableColumn<InvoiceTM, Integer> colAppointmentId;
    @FXML
    private TableColumn<InvoiceTM, String> colAppointmentName;
    @FXML
    private TableColumn<InvoiceTM, Date> colDate;
    @FXML
    private TableColumn<InvoiceTM, Integer> colInvoiceId;
    @FXML
    private TableColumn<InvoiceTM, Integer> colReportId;

    @FXML
    private TableView<InvoiceTM> tblInvoices;

    @FXML
    private ComboBox<Integer> cmbAppointmentId;
    @FXML
    private DatePicker dpDate;
    @FXML
    private Spinner<Double> spnAmount;
    @FXML
    private Spinner<Integer> spnReportId;
    @FXML
    private TextField txtInvoiceId;

    private final InvoiceModel invoiceModel = new InvoiceModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        loadAppointmentIds();
        loadAllInvoices();

        spnAmount.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1_000_000, 0, 100));
        spnReportId.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1_000_000, 0));
    }

    private void initializeTable() {
        colInvoiceId.setCellValueFactory(new PropertyValueFactory<>("invoiceId"));
        colAppointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        colAppointmentName.setCellValueFactory(new PropertyValueFactory<>("appointmentName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colReportId.setCellValueFactory(new PropertyValueFactory<>("reportId"));
    }

    private void loadAppointmentIds() {
        try {
            List<Integer> appointmentIds = invoiceModel.getAppointmentIds();
            cmbAppointmentId.setItems(FXCollections.observableArrayList(appointmentIds));
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load appointment IDs: " + e.getMessage()).showAndWait();
        }
    }

    private void loadAllInvoices() {
        try {
            List<InvoiceDTO> invoiceDTOList = invoiceModel.getAllInvoices();
            ObservableList<InvoiceTM> invoiceTMList = FXCollections.observableArrayList();

            for (InvoiceDTO dto : invoiceDTOList) {
                String appointmentName = invoiceModel.getAppointmentName(dto.getAppointmentId());
                invoiceTMList.add(new InvoiceTM(
                        dto.getInvoiceId(),
                        dto.getAppointmentId(),
                        dto.getDate(),
                        dto.getAmount(),
                        dto.getReportId(),
                        appointmentName
                ));
            }

            tblInvoices.setItems(invoiceTMList);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load invoices: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void btnSaveOnAction(ActionEvent event) {
        try {
            Integer appointmentId = cmbAppointmentId.getValue();
            if (appointmentId == null || dpDate.getValue() == null) {
                new Alert(Alert.AlertType.WARNING, "Please fill in all required fields.").showAndWait();
                return;
            }

            Date date = Date.valueOf(dpDate.getValue());
            double amount = spnAmount.getValue();
            int reportId = spnReportId.getValue();

            InvoiceDTO dto = new InvoiceDTO(0, appointmentId, date, amount, reportId);
            boolean isSaved = invoiceModel.saveInvoice(dto);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Invoice saved successfully.").showAndWait();
                loadAllInvoices();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save invoice.").showAndWait();
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void btnUpdateOnAction(ActionEvent event) {
        try {
            int invoiceId = Integer.parseInt(txtInvoiceId.getText());
            Integer appointmentId = cmbAppointmentId.getValue();

            if (appointmentId == null || dpDate.getValue() == null) {
                new Alert(Alert.AlertType.WARNING, "Please fill in all required fields.").showAndWait();
                return;
            }

            Date date = Date.valueOf(dpDate.getValue());
            double amount = spnAmount.getValue();
            int reportId = spnReportId.getValue();

            InvoiceDTO dto = new InvoiceDTO(invoiceId, appointmentId, date, amount, reportId);
            boolean isUpdated = invoiceModel.updateInvoice(dto);

            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Invoice updated successfully.").showAndWait();
                loadAllInvoices();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update invoice.").showAndWait();
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void btnDeleteOnAction(ActionEvent event) {
        try {
            int invoiceId = Integer.parseInt(txtInvoiceId.getText());

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this invoice?", ButtonType.YES, ButtonType.NO);
            confirm.showAndWait();

            if (confirm.getResult() == ButtonType.YES) {
                boolean isDeleted = invoiceModel.deleteInvoice(invoiceId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Invoice deleted successfully.").showAndWait();
                    loadAllInvoices();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete invoice.").showAndWait();
                }
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void btnResetOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtInvoiceId.clear();
        cmbAppointmentId.setValue(null);
        dpDate.setValue(null);
        spnAmount.getValueFactory().setValue(0.0);
        spnReportId.getValueFactory().setValue(0);
        tblInvoices.getSelectionModel().clearSelection();
    }

    @FXML
    private void tblInvoicesOnMouseClicked(MouseEvent event) {
        InvoiceTM selected = tblInvoices.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtInvoiceId.setText(String.valueOf(selected.getInvoiceId()));
            cmbAppointmentId.setValue(selected.getAppointmentId());
            dpDate.setValue(selected.getDate().toLocalDate());
            spnAmount.getValueFactory().setValue(selected.getAmount());
            spnReportId.getValueFactory().setValue(selected.getReportId());
        }
    }
}
