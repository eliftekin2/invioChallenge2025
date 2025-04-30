package com.eliftekin.inviochallenge.utils.location;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

public class LocationSettingUtil {

    //ayarlara yönlendirir
    public static void openAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    //gps açma için yönlendirme
    public static void enableGps(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }
}
