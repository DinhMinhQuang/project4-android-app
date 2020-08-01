package fpt.aptech.project4_android_app.api.models.Place;

import com.google.gson.annotations.SerializedName;
import com.goong.geocoder.places.data.remote.entity.Place;

public class ChildPlace implements Place {
    @SerializedName("pid")
    public String placeId;
    @SerializedName("content")
    public String content;
    @SerializedName("address")
    public String address;
    @SerializedName("lon")
    public double longitude;
    @SerializedName("lat")
    public double latitude;

    @Override
    public String toString() {
        return "ChildPlace{" +
                "placeId='" + placeId + '\'' +
                ", content='" + content + '\'' +
                ", address='" + address + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSubTitle() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }
}
