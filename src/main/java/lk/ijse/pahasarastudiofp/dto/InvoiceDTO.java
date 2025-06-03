package lk.ijse.pahasarastudiofp.dto;

import java.sql.Date;

public class InvoiceDTO {
    private int invoiceId;
    private int appointmentId;
    private Date date;
    private double amount;
    private Integer reportId;

    public InvoiceDTO(int invoiceId, int appointmentId, Date date, double amount, Integer reportId) { // Changed parameter type
        this.invoiceId = invoiceId;
        this.appointmentId = appointmentId;
        this.date = date;
        this.amount = amount;
        this.reportId = reportId;
    }

    public InvoiceDTO() {
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Integer getReportId() { // Changed return type
        return reportId;
    }

    public void setReportId(Integer reportId) { // Changed parameter type
        this.reportId = reportId;
    }
}