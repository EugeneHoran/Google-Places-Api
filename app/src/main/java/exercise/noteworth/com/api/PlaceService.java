package exercise.noteworth.com.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import exercise.noteworth.com.model.Details;
import exercise.noteworth.com.model.SearchResult;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

@SuppressWarnings("all") // Spelling
public interface PlaceService {
    /**
     * Get Restaurants
     *
     * @param rank     Query by (Best Match, Distance)
     * @param latLong  Query location (lat,long)
     * @param radius   Query radius in meters
     * @param keyword  Query keyword (food type: Chinese, Italian,...)
     * @param minPrice Query min price range
     * @param maxPrice Query max price range
     * @return {@link SearchResult}
     */
    @GET("nearbysearch/")
    Observable<Response<SearchResult>> getRestaurants(
            @Query("rankby") String rank,
            @NonNull @Query("location") String latLong,
            @Query("radius") Double radius,
            @Query("keyword") String keyword,
            @Nullable @Query("minprice") Integer minPrice,
            @Nullable @Query("maxprice") Integer maxPrice);

    /**
     * Get Resturants ++Page
     *
     * @param pageToken Query Previous Search Token
     * @return {@link SearchResult}
     */
    @GET("nearbysearch/")
    Observable<Response<SearchResult>> getRestaurantsPage(
            @Nullable @Query("pagetoken") String pageToken);

    /**
     * Get Place Details
     *
     * @param placeid placeId
     * @return return {@link Details}
     */
    @GET("details/")
    Observable<Response<Details>> getRestaurantsDetails(
            @Nullable @Query("placeid") String placeid);

}
