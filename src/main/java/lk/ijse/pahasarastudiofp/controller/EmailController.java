package lk.ijse.pahasarastudiofp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import lk.ijse.pahasarastudiofp.dto.EmailDTO;
import lk.ijse.pahasarastudiofp.model.EmailModel;

public class EmailController {  // <-- renamed class

    @FXML
    private TextField txtTo;

    @FXML
    private TextField txtSubject;

    @FXML
    private TextArea txtMessage;

    @FXML
    private Label lblStatus;

    @FXML
    private void onSendEmail() {
        String to = txtTo.getText().trim();
        String subject = txtSubject.getText().trim();
        String message = txtMessage.getText().trim();

        if (to.isEmpty() || subject.isEmpty() || message.isEmpty()) {
            lblStatus.setText("❌ Please fill all fields.");
            return;
        }

        EmailDTO emailDTO = new EmailDTO(to, subject, message);
        try {
            EmailModel.sendEmail(emailDTO);
            lblStatus.setText("✅ Email sent successfully.");
        } catch (Exception e) {
            lblStatus.setText("❌ Failed to send email.");
            e.printStackTrace();
        }
    }
}
