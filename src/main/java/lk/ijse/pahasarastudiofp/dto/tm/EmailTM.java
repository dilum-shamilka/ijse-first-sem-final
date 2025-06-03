package lk.ijse.pahasarastudiofp.dto.tm;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EmailTM {

    private final StringProperty to;
    private final StringProperty subject;
    private final StringProperty message;

    public EmailTM(String to, String subject, String message) {
        this.to = new SimpleStringProperty(to);
        this.subject = new SimpleStringProperty(subject);
        this.message = new SimpleStringProperty(message);
    }


    public StringProperty toProperty() {
        return to;
    }

    public StringProperty subjectProperty() {
        return subject;
    }

    public StringProperty messageProperty() {
        return message;
    }


    public String getTo() {
        return to.get();
    }

    public void setTo(String to) {
        this.to.set(to);
    }

    public String getSubject() {
        return subject.get();
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public String getMessage() {
        return message.get();
    }

    public void setMessage(String message) {
        this.message.set(message);
    }
}
