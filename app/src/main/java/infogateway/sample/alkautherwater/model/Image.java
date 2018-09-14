package infogateway.sample.alkautherwater.model;

import java.io.Serializable;

/**
 * Created by Lincoln on 04/04/16.
 */
public class Image implements Serializable{
    private String Productname;
    private String image;
    private int product_id;
    private String specification;
    private String price;
    private String Unit;

    public Image() {
    }

    public Image(String productname, String image, int product_id, String specification, String price, String unit) {
        Productname = productname;
        this.image = image;
        this.product_id = product_id;
        this.specification = specification;
        this.price = price;
        Unit = unit;
    }

    public String getProductname() {
        return Productname;
    }

    public void setProductname(String productname) {
        Productname = productname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
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

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }
}
