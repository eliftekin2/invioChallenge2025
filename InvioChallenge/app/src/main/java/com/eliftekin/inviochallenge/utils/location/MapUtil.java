package com.eliftekin.inviochallenge.utils.location;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class MapUtil {
    //drawable kaynağını bitmap yapma
    public static BitmapDescriptor bitmapDescriptor(Context context, int resId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, resId);
        if (vectorDrawable != null){
            Bitmap bitmap = Bitmap.createBitmap(
                    vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888
            );

            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);

            return BitmapDescriptorFactory.fromBitmap(bitmap);
        }
        else
            return null;
    }

    //google maps fragmentini oluşturur
    public static void setMapFragment(Fragment fragment, OnMapReadyCallback callback, int fragmentId) {
        FragmentManager childFragmentManager = fragment.getChildFragmentManager();

        SupportMapFragment mapFragment = (SupportMapFragment) childFragmentManager.findFragmentById(fragmentId);

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}
