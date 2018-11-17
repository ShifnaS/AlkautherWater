package infogateway.alkautherwater.model;

public class Notification {
    private String Title;
    private String Message;
    private String date;
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Notification(String title, String message, String date,int id) {
        Title = title;
        Message = message;
        this.date = date;
        this.id=id;
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
