package com.example.alkautherwater.model;

import java.io.Serializable;

/**
 * Created by Lincoln on 04/04/16.
 */
public class Image implements Serializable{
    private String name;
    private String image;
    private int id;
    private String specification;
    private String price;

    public Image(String name, String image, int id, String specification, String price) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.specification = specification;
        this.price = price;
    }

    public Image() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
