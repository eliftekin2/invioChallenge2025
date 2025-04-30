package com.eliftekin.inviochallenge.utils.location;

import static com.eliftekin.inviochallenge.utils.location.LocationSettingUtil.openAppSettings;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class PermissionUtil {
    //izin var mı yok mu
    public static boolean checkLocationPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    //izin ister
    public static void requestLocationPermission(ActivityResultLauncher<String> launcher) {
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    //konum izni almak için alert
    public static void showAlert(Fragment fragment, ActivityResultLauncher<String> launcher) {
        new AlertDialog.Builder(fragment.requireContext())
                .setMessage("Kendi konumunu haritada görmek ister misin?")
                .setPositiveButton("Evet", (dialog, which) -> {
                    if(fragment.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
                        openAppSettings(fragment.requireContext());

                    else
                        requestLocationPermission(launcher); //ilk kez izin istiyorsa
                })
                .setNegativeButton("Hayır", (dialog, which) -> {
                    //pencere kapanır
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }
}
