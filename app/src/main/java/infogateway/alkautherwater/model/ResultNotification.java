package infogateway.alkautherwater.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResultNotification {

    @SerializedName("item")
    private ArrayList<Notification> item;

    public ArrayList<Notification> getItem() {
        return item;
    }

    public void setItem(ArrayList<Notification> item) {
        this.item = item;
    }

    public ResultNotification(ArrayList<Notification> item) {
        this.item = item;
    }
}
