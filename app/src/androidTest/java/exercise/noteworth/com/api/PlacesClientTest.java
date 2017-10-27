package exercise.noteworth.com.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.junit.Test;

import exercise.noteworth.com.model.Details;
import exercise.noteworth.com.model.SearchResult;
import io.reactivex.Observable;
import retrofit2.Response;

public class PlacesClientTest implements PlaceService {
    @Test
    public void create() throws Exception {

    }

    @Override
    public Observable<Response<SearchResult>> getRestaurants(String rank, @NonNull String latLong, Double radius, String keyword, @Nullable Integer minPrice, @Nullable Integer maxPrice) {
        return null;
    }

    @Override
    public Observable<Response<SearchResult>> getRestaurantsPage(@Nullable String pageToken) {
        return null;
    }

    @Override
    public Observable<Response<Details>> getRestaurantsDetails(@Nullable String placeid) {
        return null;
    }
}