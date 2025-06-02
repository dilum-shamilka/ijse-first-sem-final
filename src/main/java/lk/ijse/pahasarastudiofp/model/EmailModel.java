package lk.ijse.pahasarastudiofp.model;

import lk.ijse.pahasarastudiofp.dto.EmailDTO;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailModel {

    private static final String MAILTRAP_HOST     = "sandbox.smtp.mailtrap.io";
    private static final int    MAILTRAP_PORT     = 2525;          // 2525 also works
    private static final String MAILTRAP_USERNAME = "136ca13b8e9b73";
    private static final String MAILTRAP_PASSWORD = "680bb52868bec0";

    public static void sendEmail(EmailDTO dto) throws MessagingException {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");   // TLS
        props.put("mail.smtp.host", MAILTRAP_HOST);
        props.put("mail.smtp.port", String.valueOf(MAILTRAP_PORT));

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MAILTRAP_USERNAME, MAILTRAP_PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("demo@pahasarastudio.local")); // any sender
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(dto.getTo()));
        message.setSubject(dto.getSubject());
        message.setText(dto.getMessage());

        Transport.send(message);
    }
}
