package exercise.noteworth.com.places;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.List;

import exercise.noteworth.com.R;
import exercise.noteworth.com.model.SearchData;
import exercise.noteworth.com.util.LocationHelper;
import io.realm.Realm;
import pub.devrel.easypermissions.EasyPermissions;

public class PlacesActivity extends AppCompatActivity implements
        PlaceFragment.OnFragmentInteraction,
        EasyPermissions.PermissionCallbacks {

    private PlaceFragment placeFragment;
    private LocationHelper locationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_places);
        placeFragment = (PlaceFragment) getSupportFragmentManager().findFragmentById(R.id.container_place);
        initLocationAutoComplete();
        locationHelper = new LocationHelper(this, locationInterface);
        Realm mRealm = Realm.getDefaultInstance();
        SearchData searchInfo = mRealm.where(SearchData.class).findFirst();
        if (searchInfo == null) {
            locationHelper.connect();
        } else {
            if (savedInstanceState == null) {
                placeFragment.locationLoaded(searchInfo);
            }
        }
    }

    @Override
    protected void onStop() {
        if (locationHelper != null) {
            locationHelper.onStop();
            locationHelper = null;
        }
        super.onStop();
    }

    /**
     * DetailsLocation Interface {@link LocationHelper}
     * returns location and error handling
     */
    private LocationHelper.LocationInterface locationInterface = new LocationHelper.LocationInterface() {
        @Override
        public void onLocationLoaded(SearchData searchData) {
            saveLocation(searchData);
        }

        @Override
        public void onConnectionError() {
            showConnectionError(getString(R.string.error_connection));
        }

        @Override
        public void onLocationEmpty() {
            Toast.makeText(PlacesActivity.this, R.string.error_location_empty, Toast.LENGTH_SHORT).show();
            showSearchFragment();
        }
    };

    /**
     * Search DetailsLocation {@link PlaceAutocompleteFragment}
     * <p>
     * Set filter to United states only
     * Set filter to only return Locations
     */
    private void initLocationAutoComplete() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.container_search);
        autocompleteFragment.setFilter(new AutocompleteFilter.Builder()
                .setCountry(getString(R.string.country_united_states))
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_REGIONS)
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_GEOCODE)
                .build());
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                SearchData searchData = new SearchData(place.getName().toString(), place.getLatLng().latitude, place.getLatLng().longitude);
                saveLocation(searchData);
            }

            @Override
            public void onError(Status status) {
                showConnectionError(getString(R.string.error_location_search_empty));
            }
        });
    }

    /**
     * Cache Search data with Realm
     * Notify {@link PlaceFragment} that location has been updated
     *
     * @param searchData location name, lat, long
     */
    private void saveLocation(SearchData searchData) {
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.deleteAll();
        mRealm.copyToRealmOrUpdate(searchData);
        mRealm.commitTransaction();
        mRealm.close();
        placeFragment.locationLoaded(searchData);
    }

    /**
     * Show change location dialog
     * <p>
     * Choose between:
     * Current location
     * Search location
     */
    @Override
    public void showChangeLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.update_location);
        builder.setItems(getResources().getStringArray(R.array.spinner_location), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Current DetailsLocation
                        locationHelper.connect();
                        break;
                    case 1: // Search DetailsLocation
                        showSearchFragment();
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Show persistent SnackBar
     * onClick retry connection via {@link LocationHelper}
     *
     * @param errorMessage message to display in SnackBar
     */
    private void showConnectionError(String errorMessage) {
        if (placeFragment.getView() != null) {
            Snackbar.make(placeFragment.getView(), errorMessage, Snackbar.LENGTH_INDEFINITE).setAction(R.string.retry, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    locationHelper.connect();
                }
            }).show();
        }
    }

    /**
     * Handle Permissions for DetailsLocation
     * https://github.com/googlesamples/easypermissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /*
     * Permission granted
     * Load DetailsLocation {@link LocationHelper)
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        locationHelper.getLocation();
    }

    /*
     * Permission denied
     * Prompt user to search via {@link PlaceAutocompleteFragment}
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        showSearchFragment();
    }


    /**
     * Show Autocomplete Search Fragment
     * Hiding the original view and performing click programmatically
     */
    @Override
    public void showSearchFragment() {
        findViewById(R.id.place_autocomplete_search_input).performClick();
    }
}
