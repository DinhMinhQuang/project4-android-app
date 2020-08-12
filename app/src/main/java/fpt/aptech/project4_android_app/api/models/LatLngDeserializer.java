package fpt.aptech.project4_android_app.api.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import io.goong.goongsdk.geometry.LatLng;

public class LatLngDeserializer implements JsonDeserializer<LatLng> {
    @Override
    public LatLng deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jobject = json.getAsJsonObject();
        return new LatLng(
                jobject.get("lat").getAsDouble(),
                jobject.get("lng").getAsDouble());
    }
}
