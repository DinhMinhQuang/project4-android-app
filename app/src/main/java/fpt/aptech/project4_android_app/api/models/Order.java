package fpt.aptech.project4_android_app.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Order {
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("products")
    @Expose
    private List<String> products;
    @SerializedName("address")
    @Expose
    private Map<String, String> address;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("shipperId")
    @Expose
    private String shipperId;
    @SerializedName("status")
    @Expose
    private String status;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    public Map<String, String> getAddress() {
        return address;
    }

    public void setAddress(Map<String, String> address) {
        this.address = address;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getShipperId() {
        return shipperId;
    }

    public void setShipperId(String shipperId) {
        this.shipperId = shipperId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
