package es.academy.solidgear.surveyx.managers;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import java.text.DecimalFormat;

import es.academy.solidgear.surveyx.R;

public class Utils {
    public static float dimenToPixels(Context context, int complexUnit, float units) {
        return TypedValue.applyDimension(complexUnit, units, context.getResources().getDisplayMetrics());
    }

    public static boolean isGpsEnabled(Context ctx) {
        LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean isLocationPermissionGranted(Context ctx) {
        int locationPermissionStatus = ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION);
        return locationPermissionStatus == PackageManager.PERMISSION_GRANTED;

    }

    public static void applyFadeInAnimation(final View view) {
        Animation fadeInAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_in);
        view.startAnimation(fadeInAnimation);
        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    public static String getDistanceString(float distance) {
        String unit = "m";
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        if (distance >= 1000) {
            distance /= 1000;
            unit = "km";
        }

        return decimalFormat.format(distance) + " " + unit;
    }

    public static void showUnexpectedNetworkError(Context ctx) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(ctx, R.string.unexpected_network_error, duration);
        toast.show();
    }

}
