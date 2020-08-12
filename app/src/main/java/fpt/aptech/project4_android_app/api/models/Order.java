package fpt.aptech.project4_android_app.api.models;

import com.github.nkzawa.emitter.Emitter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.goong.goongsdk.geometry.LatLng;

public class Order {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("restaurant")
    @Expose
    private Restaurant restaurant;
    @SerializedName("fee")
    @Expose
    private Double fee;
    @SerializedName("discount")
    @Expose
    private Double discount;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("products")
    @Expose
    private List<Map<String, ?>> products;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;

    @SerializedName("coupon")
    @Expose
    private Coupon coupon;

    @SerializedName("canceledBy")
    @Expose
    private String canceledBy;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("rated")
    @Expose
    private Map<String, ?> rated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Map<String, ?>> getProducts() {
        return products;
    }

    public void setProducts(List<Map<String, ?>> products) {
        this.products = products;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public String getCanceledBy() {
        return canceledBy;
    }

    public void setCanceledBy(String canceledBy) {
        this.canceledBy = canceledBy;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, ?> getRated() {
        return rated;
    }

    public void setRated(Map<String, ?> rated) {
        this.rated = rated;
    }
}
