package com.eliftekin.inviochallenge.utils.location;

import static com.eliftekin.inviochallenge.constants.AppConstants.GoogleMapsUrl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class LocationRouteUtil {

    //rota oluşturma
    public static void createRoute(Context context,double startLat, double startLng, double endLat, double endLng) {
        Uri gmmIntentUri = Uri.parse(GoogleMapsUrl
                + startLat + "," + startLng + "&destination="
                + endLat + "," + endLng + "&travelmode=driving");

        Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        intent.setPackage("com.google.android.apps.maps"); //google maps uygulaması

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "Google Maps yüklü değil", Toast.LENGTH_SHORT).show();
        }
    }

}
