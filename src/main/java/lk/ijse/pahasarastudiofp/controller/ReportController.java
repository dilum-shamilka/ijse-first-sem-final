package lk.ijse.pahasarastudiofp.controller;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Connection;

public class ReportController {

    public static void showOrderReport(Connection connection) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/Report/customer.jrxml");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, connection);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
}
}