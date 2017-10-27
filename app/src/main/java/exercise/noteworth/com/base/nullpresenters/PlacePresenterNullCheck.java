package exercise.noteworth.com.base.nullpresenters;

import exercise.noteworth.com.base.BasePresenterNullCheck;
import exercise.noteworth.com.model.SearchResult;
import exercise.noteworth.com.places.PlaceContract;

/**
 * Dummy Views to prevent null exceptions.
 * Really shouldn't be called but use to be safe
 */
public abstract class PlacePresenterNullCheck extends BasePresenterNullCheck<PlaceContract.View> {

    @Override
    public PlaceContract.View createNullView() {
        return new PlaceContract.View() {

            @Override
            public void setPresenter(PlaceContract.Presenter presenter) {

            }

            @Override
            public void initPlaceAdapter(SearchResult searchResult) {

            }

            @Override
            public void addPlaceAdapterItems(SearchResult searchResult) {

            }

            @Override
            public void locationMissing() {

            }
        };
    }
}
