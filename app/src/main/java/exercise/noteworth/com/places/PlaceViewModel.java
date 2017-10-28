package exercise.noteworth.com.places;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import java.util.ArrayList;
import java.util.List;

import exercise.noteworth.com.api.PlacesClient;
import exercise.noteworth.com.model.Result;
import exercise.noteworth.com.model.SearchData;
import exercise.noteworth.com.model.SearchResult;
import exercise.noteworth.com.util.Helper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import retrofit2.Response;

public class PlaceViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final Realm realm = Realm.getDefaultInstance();

    // Mutable Listen for live data
    private MutableLiveData<List<Result>> listResults;
    // Location
    private SearchData searchData;
    // Observe DataBinding
    public ObservableField<String> toolbarSubTitle = new ObservableField<>(null);
    public ObservableField<Boolean> showRecycler = new ObservableField<>(false);
    public ObservableField<Boolean> showLoading = new ObservableField<>(false);
    public ObservableField<Boolean> showRadius = new ObservableField<>(true);
    public ObservableField<Boolean> showNoResults = new ObservableField<>(false);
    // Filter data
    private Double distance;
    private String keyword;
    private Integer priceRange;
    private String rankBy;
    private String nextPageToken;


    public PlaceViewModel() {
    }

    MutableLiveData<List<Result>> getPlacesList() {
        if (listResults == null) {
            listResults = new MutableLiveData<>();
        }
        return listResults;
    }

    /**
     * Set the searchData from Context
     * Required to make api call (lat,long)
     *
     * @param searchData name, lat, long
     */
    void setSearchData(SearchData searchData) {
        this.searchData = searchData;
        toolbarSubTitle.set(searchData.subtitleFormatted());
    }

    /**
     * Filter Data Changed
     *
     * @param sortByPosition   Query by Sort by (prominence, distance)
     * @param distancePosition Query by distance (meters)
     * @param keywordPosition  Query by keyword (pizza, chines, ...)
     * @param priceRange       Query by price range
     */
    void setFilterData(
            Integer sortByPosition,
            Integer distancePosition,
            Integer keywordPosition,
            Integer priceRange) {
        if (searchData == null) {
            searchData = realm.where(SearchData.class).findFirst();
            if (searchData == null) {
                showRecycler.set(false);
                showLoading.set(false);
                return;
            }
        }
        this.rankBy = Helper.getSortBy(sortByPosition);
        // Hide radius filter
        if (rankBy == null) {
            showRadius.set(true);
        } else {
            showRadius.set(false);
        }
        this.distance = Helper.getMeters(distancePosition);
        this.keyword = Helper.getKeyword(keywordPosition);
        this.priceRange = Helper.getPriceRange(priceRange);
        loadRestaurantList();
    }

    private void loadRestaurantList() {
        resetData();
        setViewVisibility(false, true, false);
        if (searchData == null) { // called before location found (Shouldn't be called, fail safe)
            return;
        }
        disposables.add(PlacesClient.create(PlacesClient.CLIENT.PLACE).getRestaurants(
                rankBy,
                searchData.locationFormatted(),
                distance,
                keyword,
                priceRange,
                priceRange)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<SearchResult>>() {
                    @Override
                    public void onNext(final Response<SearchResult> searchResultResponse) {
                        if (searchResultResponse.isSuccessful()) {
                            SearchResult searchResult = searchResultResponse.body();
                            if (searchResult != null) {
                                nextPageToken = searchResult.getNextPageToken();
                                listResults.setValue(searchResult.getResults());
                                int searchResultSize = searchResult.getResults().size();
                                setViewVisibility(
                                        searchResultSize > 0,
                                        nextPageToken != null, // No more pages
                                        searchResultSize == 0
                                );
                            } else {
                                setViewVisibility(
                                        false,
                                        false, // No more pages
                                        true
                                );
                            }
                            freeMemory();
                        }
                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                }));
    }

    /**
     * Load Restaurant List
     * <p>
     * <p>
     * ++page
     */
    void loadRestaurantListPage() {
        if (nextPageToken == null) {
            setViewVisibility(true, false, false);
            return;
        }
        setViewVisibility(true, true, false);
        disposables.add(PlacesClient.create(PlacesClient.CLIENT.PLACE)
                .getRestaurantsPage(nextPageToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<SearchResult>>() {
                    @Override
                    public void onNext(final Response<SearchResult> searchResultResponse) {
                        SearchResult searchResult = searchResultResponse.body();
                        if (searchResult != null && listResults.getValue() != null) {
                            nextPageToken = searchResult.getNextPageToken();
                            List<Result> addingList = new ArrayList<>(listResults.getValue());
                            addingList.addAll(searchResult.getResults());
                            listResults.setValue(addingList);
                            int searchResultSize = listResults.getValue().size();
                            setViewVisibility(
                                    searchResultSize > 0,
                                    nextPageToken != null, // No more pages
                                    searchResultSize == 0
                            );
                        } else {
                            setViewVisibility(
                                    false,
                                    false, // No more pages
                                    true
                            );
                        }
                        freeMemory();
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                }));
    }

    /**
     * handle view visibility
     *
     * @param recycler  recycler
     * @param loading   progress
     * @param noResults no results
     */
    private void setViewVisibility(Boolean recycler, Boolean loading, Boolean noResults) {
        if (recycler != null) {
            if (showRecycler.get() != recycler) {
                showRecycler.set(recycler);
            }
        }
        if (loading != null) {
            if (showLoading.get() != loading) {
                showLoading.set(loading);
            }
        }
        if (noResults != null) {
            if (showNoResults.get() != noResults) {
                showNoResults.set(noResults);
            }
        }
    }

    /**
     * Clear memory after loading of data.
     * Garbage Collector
     */
    private void freeMemory() {
        distance = null;
        keyword = null;
        priceRange = null;
        rankBy = null;
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    /**
     * New request being called
     */
    private void resetData() {
        nextPageToken = null;
        listResults.setValue(null);
    }

    /**
     * Fragment on Destroy
     */
    @Override
    protected void onCleared() {
        clearSubscriptions();
        if (!realm.isClosed()) {
            realm.close();
        }
    }

    /**
     * Cancel any Retrofit callbacks
     */
    private void clearSubscriptions() {
        disposables.clear();
    }
}
