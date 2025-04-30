package com.eliftekin.inviochallenge.utils.location;

import static com.eliftekin.inviochallenge.utils.location.PermissionUtil.checkLocationPermission;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class LocationUtil {

    //gps açık mı kapalı mı
    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    //kullanıcının konumunu aktif eder
    public static void enableUserLocation(Context context, GoogleMap map) {
        if (checkLocationPermission(context) && map != null) {
            Toast.makeText(context, "Konumunuz alınıyor", Toast.LENGTH_SHORT).show();
            map.setMyLocationEnabled(true);
        }
    }

    public static void zoomToUserLocation(Fragment fragment, Context context, FusedLocationProviderClient client, GoogleMap map, Runnable onPermissionDenied, Runnable onGpsDisabled) {

        //izin kontrolü
        if (PermissionUtil.checkLocationPermission(context)) {
            //gps açık mı kapalı mı
            if (LocationUtil.isGpsEnabled(context)) {
                getUserLocation(context, client, (lat, lng) -> {
                    LatLng userLocation = new LatLng(lat, lng);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                });
            } else {
                onGpsDisabled.run();
            }
        } else {
            onPermissionDenied.run();
        }
    }

    public static void getUserLocation(Context context, FusedLocationProviderClient client, LocationCallback callback) {
        if(checkLocationPermission(context)){
            client.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    callback.onLocationReceived(location.getLatitude(), location.getLongitude());
                } else {
                    Toast.makeText(context, "Konum alınamadı", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public interface LocationCallback {
        void onLocationReceived(double latitude, double longitude);
    }

}
