package red.com.zones;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * Created by Binil Mathew on 07-Jan-17.
 */
public class LocationServices extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    @Nullable
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    SharedPreferences sharedPreferences;
    Location locationlast;
    Location locationtarget;
    Double radius;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(com.google.android.gms.location.LocationServices.API).build();
        }
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        final PendingResult<LocationSettingsResult> result =
                com.google.android.gms.location.LocationServices
                        .SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                /*final Status status = locationSettingsResult.getStatus();
                final LocationSettingsStates = locationSettingsResult.getLocationSettingsStates();
                if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {

                }*/
            }
        });
        googleApiClient.connect();
        sharedPreferences = getSharedPreferences("loc_data", MODE_PRIVATE);
        locationtarget = new Location("");
        radius = Double.valueOf(sharedPreferences.getString("rad", ""));
        locationtarget.setLatitude(Double.parseDouble(sharedPreferences.getString("lat", "")));
        locationtarget.setLongitude(Double.parseDouble(sharedPreferences.getString("lng", "")));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationlast = com.google.android.gms.location.LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        com.google.android.gms.location.LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        locationlast = location;
        float distance;
        distance = locationlast.distanceTo(locationtarget);
        if (distance < radius ){
            Intent intent = new Intent(this, Alert.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            stopService(new Intent(this, LocationServices.class));
            startActivity(intent);
            Log.e("Radius", radius.toString());
        }
    }
}
