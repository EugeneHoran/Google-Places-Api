package exercise.noteworth.com.place.details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableField;

import exercise.noteworth.com.api.PlacesClient;
import exercise.noteworth.com.model.Details;
import exercise.noteworth.com.util.Helper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class DetailsViewModel extends ViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final LiveData<Details> detailsObservable;
    public ObservableField<Details> details = new ObservableField<>();
    public ObservableField<String> ratingText = new ObservableField<>(null);
    public ObservableField<Float> ratingNum = new ObservableField<>();
    public ObservableField<String> priceLevel = new ObservableField<>();

    public DetailsViewModel(String placeId) {
        detailsObservable = getPlaceDetails(placeId);
    }

    public LiveData<Details> getObservableDetails() {
        return detailsObservable;
    }

    public void setDetails(Details project) {
        this.details.set(project);
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }

    /**
     * Load Place Details
     *
     * @param placeId placeId
     * @return LiveData<Details>
     */
    private LiveData<Details> getPlaceDetails(String placeId) {
        final MutableLiveData<Details> data = new MutableLiveData<>();
        disposables.add(PlacesClient.create(PlacesClient.CLIENT.PLACE)
                .getRestaurantsDetails(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<Details>>() {
                    @Override
                    public void onNext(@NonNull final Response<Details> searchResultResponse) {
                        data.setValue(searchResultResponse.body());
                        setDetails(searchResultResponse.body());
                    }

                    @Override
                    public void onComplete() {
                        Details detailsFinal = details.get();
                        ratingText.set(detailsFinal.getResult().getRating() + "");
                        if (detailsFinal.getResult().getRating() != null) {
                            float f = (float) ((double) detailsFinal.getResult().getRating());
                            ratingNum.set(f);
                        }
                        if (detailsFinal.getResult().getPriceLevel() != null) {
                            priceLevel.set(Helper.getRating(detailsFinal.getResult().getPriceLevel()));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        data.setValue(null);
                    }
                }));
        return data;
    }

    /**
     * A creator is used to inject the placeId into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private final String projectID;

        public Factory(String projectID) {
            this.projectID = projectID;
        }

        @android.support.annotation.NonNull
        @Override
        public <T extends ViewModel> T create(@android.support.annotation.NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new DetailsViewModel(projectID);
        }
    }
}
