package infogateway.alkautherwater.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by pc on 02-Aug-18.
 */

public class Result {


    @SerializedName("item")
    private ArrayList<Image> item;

    public ArrayList<Image> getItem() {
        return item;
    }

    public void setItem(ArrayList<Image> item) {
        this.item = item;
    }
}
