package infogateway.sample.alkautherwater.model;

public class Notification {
    private String Title;
    private String Message;
    private String date;

    public Notification(String title, String message, String date) {
        Title = title;
        Message = message;
        this.date = date;
    }

    public Notification() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



}
