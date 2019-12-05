package shell_track.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Client {

    private StringProperty clientName;
    private StringProperty address;
    private StringProperty contactInfo;

    public Client(String clientName, String address, String contactInfo){
        this.clientName = new SimpleStringProperty(clientName);
        this.address = new SimpleStringProperty(address);
        this.contactInfo = new SimpleStringProperty(contactInfo);
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public void setClientName(String clientName) {
        this.clientName.set(clientName);
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo.set(contactInfo);
    }

    public String getAddress() {
        return address.get();
    }

    public String getClientName() {
        return clientName.get();
    }

    public String getContactInfo() {
        return contactInfo.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public StringProperty clientNameProperty() {
        return clientName;
    }

    public StringProperty contactInfoProperty() {
        return contactInfo;
    }
}
