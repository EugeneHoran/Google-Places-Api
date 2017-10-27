package exercise.noteworth.com.places;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import exercise.noteworth.com.R;
import exercise.noteworth.com.databinding.FragmentPlaceBinding;
import exercise.noteworth.com.model.SearchData;
import exercise.noteworth.com.model.SearchResult;
import exercise.noteworth.com.place.details.DetailsActivity;
import exercise.noteworth.com.util.AppBarCollapsedListener;
import exercise.noteworth.com.util.Common;
import exercise.noteworth.com.util.EndlessParentScrollListener;
import exercise.noteworth.com.util.Helper;
import exercise.noteworth.com.util.SwitchMultiButton;

import static android.view.View.VISIBLE;


public class PlaceFragment extends Fragment implements PlaceContract.View, Toolbar.OnMenuItemClickListener {

    private static final String STATE_KEYWORD_POS = "state_key_pos";
    public ObservableField<String> filterString = new ObservableField<>("Filtered by: ");

    /**
     * Set Presenter
     *
     * @param presenter PlacePresenter
     */
    @Override
    public void setPresenter(PlaceContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private FragmentPlaceBinding binding;
    private PlaceContract.Presenter presenter;
    private ArrayAdapter<String> keywordAdapter;
    private PlacesRecyclerAdapter placesRecyclerAdapter;
    private EndlessParentScrollListener endlessParentScrollListener;
    private int intKeywordPosition = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placesRecyclerAdapter = new PlacesRecyclerAdapter();
        keywordAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, getResources().getStringArray(R.array.spinner_keyword));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_place, container, false);
        binding.setFragment(this);
        binding.setPresenter(new PlacePresenter(this));
        binding.nestedScroll.invalidate();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        // Toolbar
        binding.toolbar.inflateMenu(R.menu.menu_places);
        binding.toolbar.setNavigationIcon(R.drawable.ic_restaurant);
        binding.toolbar.setOnMenuItemClickListener(this);
        // ListView Keyword
        binding.listViewKeyword.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        binding.listViewKeyword.setAdapter(keywordAdapter);
        Helper.getListViewSize(binding.listViewKeyword);
        // Recycler Restaurants
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recycler.setLayoutManager(layoutManager);
        binding.recycler.setNestedScrollingEnabled(false);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        binding.recycler.addItemDecoration(itemDecorator);
        binding.recycler.setAdapter(placesRecyclerAdapter);
        placesRecyclerAdapter.setListener(new PlacesRecyclerAdapter.PlaceRecyclerInterface() {
            @Override
            public void onPlaceClicked(String placeId, View view) {
                Intent intentPlaceDetails = new Intent(getActivity(), DetailsActivity.class);
                Bundle bundlePlaceDetails = new Bundle();
                bundlePlaceDetails.putString(Common.ARG_PLACE_ID, placeId);
                intentPlaceDetails.putExtras(bundlePlaceDetails);
                startActivity(intentPlaceDetails);
            }
        });
        // Nested scrolling
        endlessParentScrollListener = new EndlessParentScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                presenter.loadRestaurantListPage();
            }
        };
        binding.nestedScroll.setOnScrollChangeListener(endlessParentScrollListener);
        // DrawerLayout
        binding.drawerLayout.setNestedScrollingEnabled(true);
        if (savedInstanceState != null) {
            intKeywordPosition = savedInstanceState.getInt(STATE_KEYWORD_POS, -1);
            filterChanged();
        }
        initFilterListeners();
        // Filter ModelTextHeader
        if (Helper.isL()) {
            initCustomBar();
        }
        showCustomHeader();
        setFilterString();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_KEYWORD_POS, intKeywordPosition);
    }

    /**
     * Handle Filtering Listeners
     * <p>
     * - Filter Distance
     * - Filter Keyword
     * - Filter Price Range
     * <p>
     * Separated for readability
     */
    private void initFilterListeners() {
        // Filter Sort
        binding.spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        // Filter Distance
        binding.spinnerRadius.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        // Filter Keyword
        binding.listViewKeyword.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (intKeywordPosition == i) {
                    if (binding.listViewKeyword.isItemChecked(intKeywordPosition)) {
                        binding.listViewKeyword.setItemChecked(i, false);
                    } else {
                        binding.listViewKeyword.setItemChecked(i, true);
                    }
                } else {
                    binding.listViewKeyword.setItemChecked(i, true);
                }
                intKeywordPosition = binding.listViewKeyword.getCheckedItemPosition();
                filterChanged();
            }
        });
        // Filter Price Range
        binding.switchMoney.setText(getActivity().getResources().getStringArray(R.array.switch_money)).setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                filterChanged();
            }
        });
    }

    /**
     * Handle Filtered Data
     * <p>
     * Pass filtered data to {@link PlacePresenter}
     * Refresh Places
     */
    private void filterChanged() {
        endlessParentScrollListener.resetState();
        Integer sortByPosition = binding.spinnerFilter.getSelectedItemPosition();
        Integer radiusPosition = null;
        // Sort by only works if radius is null
        // Set sortBy to null if pos == 0 because of default value
        if (sortByPosition == 0) {
            radiusPosition = binding.spinnerRadius.getSelectedItemPosition();
            sortByPosition = null;
        }
        Integer keywordPosition = binding.listViewKeyword.getCheckedItemPosition();
        Integer priceRange = binding.switchMoney.getSelectedTab();
        presenter.setFilterData(sortByPosition, radiusPosition, keywordPosition, priceRange);
        setFilterString();

    }

    private void setFilterString() {
        filterString.set(Helper.getHeaderString(
                getActivity(),
                binding.spinnerFilter.getSelectedItemPosition(),
                binding.spinnerRadius.getSelectedItemPosition(),
                binding.switchMoney.getSelectedTab(),
                binding.listViewKeyword.getCheckedItemPosition()));
    }

    /**
     * Called from {@link PlacesActivity} after location acquired
     *
     * @param searchData location name, lat, lon
     */
    public void locationLoaded(SearchData searchData) {
        presenter.initSearchCred(searchData);
        filterChanged();
    }

    /**
     * DetailsLocation missing from Presenter
     * Ask user to select location
     */
    @Override
    public void locationMissing() {
        listener.showChangeLocationDialog();
    }

    /**
     * Set Items to Place Adapter (clears list)
     * from {@link PlacePresenter}
     * Called after data complete
     *
     * @param searchResult SearchResult
     */
    @Override
    public void initPlaceAdapter(SearchResult searchResult) {
        placesRecyclerAdapter.setItems(searchResult.getResults());

    }

    /**
     * Add Items to Place Adapter (adds items to list)
     * from {@link PlacePresenter}
     *
     * @param searchResult SearchResult
     */
    @Override
    public void addPlaceAdapterItems(SearchResult searchResult) {
        placesRecyclerAdapter.addItems(searchResult.getResults());
    }

    /**
     * Toolbar Menu
     * Open Filter Drawer
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            binding.drawer.openDrawer(GravityCompat.END);
        }
        return false;
    }

    // TODO remove
    private String getKeyword() {
        if (intKeywordPosition == -1) {
            return null;
        } else {
            return keywordAdapter.getItem(intKeywordPosition);
        }
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    /**
     * Change DetailsLocation Clicked
     * <p>
     * Show which type of location
     */
    @SuppressWarnings("unused")
    public void onChangeLocationClicked(View view) {
        binding.drawer.closeDrawer(GravityCompat.END);
        listener.showChangeLocationDialog();
    }

    /**
     * Calculate offset for text header
     */
    private boolean customBarInitiated = false;
    private final Rect mFiltersBarClip = new Rect();

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initCustomBar() {
        customBarInitiated = true;
        final AppBarCollapsedListener appBarCollapsedListener = new AppBarCollapsedListener();
        binding.appBar.addOnOffsetChangedListener(appBarCollapsedListener);
        ((CoordinatorLayout.LayoutParams) binding.nestedScroll.getLayoutParams()).setBehavior(new AppBarLayout.ScrollingViewBehavior() {
            @Override
            public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
                return (axes & View.SCROLL_AXIS_VERTICAL) != 0 && binding.headerFilter.getVisibility() == VISIBLE;
            }

            @Override
            public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
                float filtersOffset = binding.headerFilter.getTranslationY();
                int filtersHeight = binding.headerFilter.getHeight();
                if (dyConsumed > 0 && appBarCollapsedListener.isCollapsed() && filtersOffset > -filtersHeight) {
                    int offset = (int) Math.max(filtersOffset - dyConsumed, -filtersHeight);
                    offsetFilters(filtersHeight, offset);
                } else if (dyConsumed < 0 && filtersOffset < 0f) {
                    int offset = (int) Math.min(filtersOffset - dyConsumed, 0f);
                    offsetFilters(filtersHeight, offset);
                }
            }
        });
    }

    private void showCustomHeader() {
        if (!customBarInitiated) {
            binding.headerFilter.setVisibility(View.GONE);
            return;
        }
        binding.headerFilter.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.headerFilter.invalidate();
                binding.nestedScroll.setTranslationY(binding.headerFilter.getHeight());
                offsetFilters(binding.headerFilter.getHeight(), 0);
            }
        }, 1);
    }

    private void offsetFilters(int filtersHeight, int offset) {
        binding.headerFilter.setTranslationY(offset);
        mFiltersBarClip.set(0, -offset, binding.headerFilter.getWidth(), filtersHeight);
        binding.nestedScroll.setTranslationY(filtersHeight + offset);
    }


    /**
     * Interface between fragment and activity
     */
    private OnFragmentInteraction listener;

    interface OnFragmentInteraction {
        void showSearchFragment();

        void showChangeLocationDialog();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteraction) {
            listener = (OnFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    /**
     * Currently Unused
     */
    @SuppressWarnings("unused")
    private void resetFilter() {
        binding.spinnerRadius.setSelection(0);
        binding.listViewKeyword.setItemChecked(intKeywordPosition, false);
        binding.switchMoney.setSelectedTab(-1);
    }
}
