package com.example.alkautherwater.model;

import com.google.gson.annotations.SerializedName;

public class Results {
    @SerializedName("message")
    private String message;
    @SerializedName("error")
    private boolean error;

    public Results() {
    }

    public Results(String message, boolean error) {
        this.message = message;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }





}
