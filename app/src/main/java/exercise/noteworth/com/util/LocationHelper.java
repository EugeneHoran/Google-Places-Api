package exercise.noteworth.com.util;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import exercise.noteworth.com.R;
import exercise.noteworth.com.model.SearchData;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LocationHelper {

    private static final String[] locationPermission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int RC_LOCATION_PERMISSIONS = 102;

    private LocationInterface listener;
    private Context context;
    private GoogleApiClient mGoogleApiClient;

    public LocationHelper(Context context, LocationInterface locationInterface) {
        this.context = context;
        this.listener = locationInterface;
    }

    public void connect() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(@Nullable Bundle bundle) {
                            getLocation();
                            mGoogleApiClient.disconnect();
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            if (listener != null) {
                                listener.onConnectionError();
                            }
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            if (listener != null) {
                                listener.onConnectionError();
                            }
                        }
                    })
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    public void onStop() {
        listener = null;
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        }
    }

    @AfterPermissionGranted(RC_LOCATION_PERMISSIONS)
    public void getLocation() {
        if (!EasyPermissions.hasPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            EasyPermissions.requestPermissions(context, context.getString(R.string.location_required), RC_LOCATION_PERMISSIONS, locationPermission);
            return;
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        FusedLocationProviderClient mLastLocation = LocationServices.getFusedLocationProviderClient(context);
        mLastLocation.getLastLocation()
                .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // May be null on Emulators
                        if (location == null) {
                            if (listener != null) {
                                listener.onLocationEmpty();
                            }
                        } else {
                            SearchData searchInfo = new SearchData(context.getString(R.string.current_location), location.getLatitude(), location.getLongitude());
                            if (listener != null) {
                                listener.onLocationLoaded(searchInfo);
                            }
                        }
                    }
                });
    }

    /**
     * Interface
     */
    public interface LocationInterface {
        void onLocationLoaded(SearchData searchInfo);

        void onConnectionError();

        void onLocationEmpty();
    }
}
