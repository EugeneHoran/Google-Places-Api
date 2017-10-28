package exercise.noteworth.com.places;

import android.annotation.TargetApi;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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

import java.util.List;

import exercise.noteworth.com.R;
import exercise.noteworth.com.databinding.FragmentPlaceBinding;
import exercise.noteworth.com.model.Result;
import exercise.noteworth.com.model.SearchData;
import exercise.noteworth.com.util.AppBarCollapsedListener;
import exercise.noteworth.com.util.EndlessParentScrollListener;
import exercise.noteworth.com.util.Helper;
import exercise.noteworth.com.util.SwitchMultiButton;

import static android.view.View.VISIBLE;


public class PlaceFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private static final String STATE_KEYWORD_POS = "state_key_pos";
    public ObservableField<String> filterString = new ObservableField<>("Filtered by: ");

    private FragmentPlaceBinding binding;
    private ArrayAdapter<String> keywordAdapter;
    private PlacesRecyclerAdapter placesRecyclerAdapter;
    private int intKeywordPosition = -1;
    private PlaceViewModel viewModel;
    // Prevent spinners from calling view model on creation
    private boolean spinnerFilterInit = false;
    private boolean spinnerRadiusInit = false;
    private int filterPos = 0;
    private int radiusPos = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            intKeywordPosition = savedInstanceState.getInt(STATE_KEYWORD_POS, -1);
        }
        viewModel = ViewModelProviders.of(this).get(PlaceViewModel.class);
        placesRecyclerAdapter = new PlacesRecyclerAdapter();
        keywordAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, getResources().getStringArray(R.array.spinner_keyword));
        spinnerFilterInit = false;
        spinnerRadiusInit = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_place, container, false);
        binding.setFragment(this);
        binding.setModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        // Toolbar
        binding.toolbar.inflateMenu(R.menu.menu_places);
        binding.toolbar.setOnMenuItemClickListener(this);
        // DrawerLayout
        binding.drawerLayout.setNestedScrollingEnabled(true);
        // ListView Keyword
        binding.listViewKeyword.setAdapter(keywordAdapter);
        Helper.getListViewSize(binding.listViewKeyword);
        // Recycler Restaurants
        binding.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recycler.setNestedScrollingEnabled(false);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        binding.recycler.addItemDecoration(itemDecorator);
        binding.recycler.setAdapter(placesRecyclerAdapter);
        // Nested scrolling
        endlessParentScrollListener = new EndlessParentScrollListener(binding.recycler.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                viewModel.loadRestaurantListPage();
            }
        };
        binding.nestedScroll.setOnScrollChangeListener(endlessParentScrollListener);
        // Live data listener
        observeViewModel(viewModel);

        // Listeners
        initFilterListeners();

        // Filter header
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
     * Observe Live data @ {@link PlaceViewModel}
     *
     * @param viewModel PlaceViewModel
     */
    private void observeViewModel(final PlaceViewModel viewModel) {
        viewModel.getPlacesList().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(@Nullable List<Result> resultList) {
                if (resultList != null) {
                    placesRecyclerAdapter.setItems(resultList);
                    endlessParentScrollListener.resetState();
                    setFilterString();
                } else {
                    placesRecyclerAdapter.clearItems();
                }
            }
        });
    }

    /**
     * Endless Scroll Listener
     * <p>
     * Load More Places @ {@link PlaceViewModel}
     */
    private EndlessParentScrollListener endlessParentScrollListener = new EndlessParentScrollListener() {
        @Override
        public void onLoadMore(int page, int totalItemsCount) {
            viewModel.loadRestaurantListPage();
        }
    };

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
                if (spinnerFilterInit) {
                    if (filterPos != i) {
                        filterChanged();
                    }
                }
                filterPos = i;
                spinnerFilterInit = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        // Filter Distance
        binding.spinnerRadius.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerRadiusInit) {
                    if (i != radiusPos) {
                        filterChanged();
                    }
                }
                radiusPos = i;
                spinnerRadiusInit = true;
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
     * Pass filtered data to {@link PlaceViewModel}
     * Refresh Places
     */
    private void filterChanged() {
        binding.drawer.closeDrawer(GravityCompat.END);
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
        viewModel.setFilterData(sortByPosition, radiusPosition, keywordPosition, priceRange);
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
        viewModel.setSearchData(searchData);
        filterChanged();
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

    /**
     * Show header
     */
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
