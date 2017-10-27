package exercise.noteworth.com.places;


import android.databinding.ObservableField;

import exercise.noteworth.com.api.PlacesClient;
import exercise.noteworth.com.base.nullpresenters.PlacePresenterNullCheck;
import exercise.noteworth.com.model.SearchData;
import exercise.noteworth.com.model.SearchResult;
import exercise.noteworth.com.util.Helper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import retrofit2.Response;

/**
 * No reference to Activity other than interface
 */

public class PlacePresenter extends PlacePresenterNullCheck implements PlaceContract.Presenter {
    private Realm realm;
    private CompositeDisposable disposables;
    /**
     * Data and Query params
     */
    private SearchData searchData;
    private SearchResult searchResult;
    // Filter data
    private Double distance;
    private String keyword;
    private Integer priceRange;
    private String rankBy;
    // Observe DataBinding
    public ObservableField<String> toolbarSubTitle = new ObservableField<>(null);
    public ObservableField<Boolean> showRecycler = new ObservableField<>(false);
    public ObservableField<Boolean> showLoading = new ObservableField<>(true);
    public ObservableField<Boolean> showRadius = new ObservableField<>(true);
    public ObservableField<Boolean> showNoResults = new ObservableField<>(false);

    @Override
    public void onStart() {
    }

    /**
     * Init
     *
     * @param view PlaceContract.View
     */
    PlacePresenter(PlaceContract.View view) {
        onAttachView(view);
        getView().setPresenter(this);
        this.realm = Realm.getDefaultInstance();
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void setFilterData(
            Integer sortByPosition,
            Integer distancePosition,
            Integer keywordPosition,
            Integer priceRange) {
        // Will be null on screen rotation
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

    @Override
    public void initSearchCred(SearchData searchData) {
        this.searchData = searchData;
    }

    /**
     *
     */
    @Override
    public void loadRestaurantList() {
        showNoResults.set(false);
        if (searchData == null) { // called before location found (Shouldn't be called, fail safe)
            showRecycler.set(false);
            showLoading.set(true);
            return;
        }
        showRecycler.set(false);
        showLoading.set(true);
        searchResult = null;
        toolbarSubTitle.set(searchData.subtitleFormatted());
        showLoading.set(true);
        clearSubscriptions();
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
                    public void onNext(@NonNull final Response<SearchResult> searchResultResponse) {
                        searchResult = searchResultResponse.body();
                    }

                    @Override
                    public void onComplete() {
                        showRecycler.set(true);
                        getView().initPlaceAdapter(searchResult);
                        // No more pages
                        if (searchResult.getNextPageToken() == null) {
                            showLoading.set(false);
                        }
                        if (searchResult.getResults().size() == 0) {
                            showNoResults.set(true);
                        }
                        freeMemory();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        // TODO
                    }
                }));
    }

    /**
     * Load Restaurant List
     * <p>
     * <p>
     * ++page
     */
    @Override
    public void loadRestaurantListPage() {
        if (searchResult == null) {
            return;
        }
        if (searchResult.getNextPageToken() == null) {
            showLoading.set(false);
            return;
        }
        showLoading.set(true);
        showRecycler.set(true);
        clearSubscriptions();
        disposables.add(PlacesClient.create(PlacesClient.CLIENT.PLACE)
                .getRestaurantsPage(searchResult.getNextPageToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<SearchResult>>() {
                    @Override
                    public void onNext(@NonNull final Response<SearchResult> searchResultResponse) {
                        searchResult = searchResultResponse.body();
                    }

                    @Override
                    public void onComplete() {
                        getView().addPlaceAdapterItems(searchResult);
                        // No more pages
                        if (searchResult.getNextPageToken() == null) {
                            showLoading.set(false);
                        }
                        if (searchResult.getResults().size() == 0) {
                            showNoResults.set(true);
                        }
                        freeMemory();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        // TODO
                    }
                }));
    }

    /**
     * Clear memory after loading of data.
     * Garbage Collector
     */
    private void freeMemory() {
        clearSubscriptions();
        searchData = null;
        distance = null;
        keyword = null;
        priceRange = null;
        rankBy = null;
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    @Override
    public void onDestroy() {
        if (!realm.isClosed()) {
            realm.close();
        }
        clearSubscriptions();
        onDetachView();
    }

    /**
     * Cancel any Retrofit callbacks
     */
    private void clearSubscriptions() {
        if (disposables != null) {
            disposables.clear();
        }
    }
}
