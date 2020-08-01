package fpt.aptech.project4_android_app.api.service.MapService;

import com.goong.geocoder.places.data.remote.entity.AutoCompleteResponse;
import com.goong.geocoder.places.data.remote.entity.Place;
import com.goong.geocoder.places.data.remote.entity.PlaceDetailsResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface MapClient {

    @GET(ServerConstants.AUTO_COMPLETE_PATH)
    @Headers({"Content-Type:Application/Json", "Accept:Application/Json"})
    Call<AutoCompleteResponse> getPlaceId(@Query("api_key") String apiKey,
                                                 @Query("input") String keyword,
                                                 @Query("limit") int limit,
                                                 @Query("radius") int radius);
    @GET(ServerConstants.PLACE_DETAIL_PATH)
    @Headers({"Content-Type:Application/Json", "Accept:Application/Json"})
    Call<Map<String, ?>> getPlaceDetails(@Query("api_key") String apiKey,
                                         @Query("placeid") String placeId);

    @GET(ServerConstants.DIRECTION)
    @Headers({"Content-Type:Application/Json", "Accept:Application/Json"})
    Call<Map<String, ?>> getDirection(@Query("api_key") String apikey,
                                      @Query("origin") String origin,
                                      @Query("destination") String destination,
                                      @Query("vehicle") String vehicle);
}
