package firebase.gopool.model.request;

import android.app.Notification;

public class Sender {

    public Notification data;
    public String to;

    public Sender() {
    }

    public Sender(Notification data, String to) {
        this.data = data;
        this.to = to;
    }

    public Notification getData() {
        return data;
    }

    public void setData(Notification data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
