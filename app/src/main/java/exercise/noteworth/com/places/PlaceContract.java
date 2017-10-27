package exercise.noteworth.com.places;


import exercise.noteworth.com.base.BasePresenter;
import exercise.noteworth.com.base.BaseView;
import exercise.noteworth.com.model.SearchData;
import exercise.noteworth.com.model.SearchResult;

public class PlaceContract {
    public interface View extends BaseView<Presenter> {
        void initPlaceAdapter(SearchResult searchResult);

        void addPlaceAdapterItems(SearchResult searchResult);

        void locationMissing();
    }

    public interface Presenter extends BasePresenter {
        void initSearchCred(SearchData searchData);

        void setFilterData(Integer sortByPosition, Integer distancePosition, Integer keywordPosition, Integer priceRange);

        void loadRestaurantList();

        void loadRestaurantListPage();
    }
}
