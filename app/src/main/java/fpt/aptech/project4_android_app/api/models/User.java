package fpt.aptech.project4_android_app.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class User {
    @SerializedName("restaurants")
    @Expose
    private List<String> restaurants;
    @SerializedName("comments")
    @Expose
    private List<Object> comments;
    @SerializedName("orders")
    @Expose
    private List<Object> orders;
    @SerializedName("newfeeds")
    @Expose
    private List<Object> newfeeds;
    @SerializedName("followers")
    @Expose
    private List<Object> followers;
    @SerializedName("following")
    @Expose
    private Map<String, ?> following;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("gender")
    @Expose
    private Boolean gender;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("friends")
    @Expose
    private List<Object> friends;
    @SerializedName("conversations")
    @Expose
    private List<Object> conversations;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("cart")
    @Expose
    private List<Object> cart;
    @SerializedName("notifications")
    @Expose
    private List<Map<String, ?>> notifications;
    @SerializedName("active")
    @Expose
    private Boolean active;

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<String> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<String> restaurants) {
        this.restaurants = restaurants;
    }

    public List<Object> getComments() {
        return comments;
    }

    public void setComments(List<Object> comments) {
        this.comments = comments;
    }

    public List<Object> getOrders() {
        return orders;
    }

    public void setOrders(List<Object> orders) {
        this.orders = orders;
    }

    public List<Object> getNewfeeds() {
        return newfeeds;
    }

    public void setNewfeeds(List<Object> newfeeds) {
        this.newfeeds = newfeeds;
    }

    public List<Object> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Object> followers) {
        this.followers = followers;
    }

    public Map<String, ?> getFollowing() {
        return following;
    }

    public void setFollowing(Map<String, ?> following) {
        this.following = following;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Object> getFriends() {
        return friends;
    }

    public void setFriends(List<Object> friends) {
        this.friends = friends;
    }

    public List<Object> getConversations() {
        return conversations;
    }

    public void setConversations(List<Object> conversations) {
        this.conversations = conversations;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public List<Object> getCart() {
        return cart;
    }

    public void setCart(List<Object> cart) {
        this.cart = cart;
    }

    public List<Map<String, ?>> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Map<String, ?>> notifications) {
        this.notifications = notifications;
    }
}
