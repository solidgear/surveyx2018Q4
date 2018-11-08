package es.academy.solidgear.surveyx.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import es.academy.solidgear.surveyx.R;
import es.academy.solidgear.surveyx.api.APIService;
import es.academy.solidgear.surveyx.managers.ApiManager;
import es.academy.solidgear.surveyx.managers.DialogsManager;
import es.academy.solidgear.surveyx.managers.SharedPrefsManager;
import es.academy.solidgear.surveyx.managers.Utils;
import es.academy.solidgear.surveyx.models.SurveyModel;
import es.academy.solidgear.surveyx.models.SurveysModel;
import es.academy.solidgear.surveyx.ui.adapters.OnSelectedSurvey;
import es.academy.solidgear.surveyx.ui.adapters.SurveyListAdapter;
import es.academy.solidgear.surveyx.ui.fragments.InformationDialogFragment;

public class SurveysActivity extends BaseActivity {

    private static final String TAG = "SurveyListActivity";
    private static final String EXTRA_TOKEN = "token";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private final static int MY_PERMISSIONS_REQUEST_LOCATION = 123;
    private final static String ASKED_ENABLE_GPS_KEY = "AskedEnableGps";

    private RecyclerView surveysView;

    private boolean gpsDialogBeingShown = false;

    private Boolean gpsEnabled;
    private Boolean requestedRuntimePermissions = false;

    private String token;

    private FusedLocationProviderClient fusedLocationClient = null;
    private Location lastLocation = null;

    private final DialogsManager dialogsManager = DialogsManager.getInstance();

    private SharedPrefsManager sharedPrefsManager;

    private final OnSelectedSurvey onSelectedSurvey = new OnSelectedSurvey() {
        @Override
        public void onSelectSurvey(SurveyModel survey) {
            showSurveyPage(survey);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveys);
        initToolbar();

        sharedPrefsManager = SharedPrefsManager.getInstance(this);

        Bundle extras = getIntent().getExtras();
        token = extras.getString(EXTRA_TOKEN, null);

        surveysView = (RecyclerView) findViewById(R.id.surveysView);

        surveysView.setLayoutManager(new LinearLayoutManager(this));

        SurveyListAdapter adapter = new SurveyListAdapter(
                SurveysActivity.this,
                new ArrayList<SurveyModel>(),
                onSelectedSurvey);

        surveysView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        gpsEnabled = Utils.isGpsEnabled(this);
        Boolean askedEnableGps = sharedPrefsManager.getBoolean(ASKED_ENABLE_GPS_KEY);
        lastLocation = null;

        if (!gpsEnabled && !askedEnableGps) {
            if (!gpsDialogBeingShown) {

                DialogInterface.OnClickListener onAcceptGPS = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sharedPrefsManager.putBoolean(ASKED_ENABLE_GPS_KEY, true);

                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        dialog.dismiss();
                        gpsDialogBeingShown = false;
                    }
                };

                DialogInterface.OnClickListener onCancelGPS = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sharedPrefsManager.putBoolean(ASKED_ENABLE_GPS_KEY, true);
                        dialog.dismiss();
                        gpsDialogBeingShown = false;

                        SurveysActivity.this.onResume();
                    }
                };

                dialogsManager.showYesNoDialog(this,
                        R.string.dialog_gps_title,
                        R.string.dialog_gps_message,
                        R.string.dialog_gps_settings,
                        R.string.dialog_cancel,
                        onAcceptGPS,
                        onCancelGPS
                );

            }
        } else {
            boolean requestingRuntimePermissions = false;

            if (gpsEnabled && fusedLocationClient == null) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Utils.isLocationPermissionGranted(this) && !requestedRuntimePermissions) {
                        requestingRuntimePermissions = true;
                        requestLocationPermission();
                    }
                }
                // If the location permission has been requested the set-up will be done in the
                // async callback for the permission, otherwise it is done here.
                if (Utils.isLocationPermissionGranted(this) && !requestingRuntimePermissions) {
                    setUpLocationSettings();
                }

            }

            // If the location permission has been requested the initial fetch will be done in the
            // async callback for the permission, otherwise it is done here.
            if (!requestingRuntimePermissions) {
                fetchAllSurveys();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            doLogout();
        } else if (id == R.id.action_refresh) {
            doRefresh();
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("MissingPermission")
    private void setUpLocationSettings() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        final InformationDialogFragment dialog = dialogsManager.showInformationDialog(this, R.string.dialog_waiting_location);

        fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                lastLocation = location;
                                Log.d(TAG, "Location found: " +
                                        location.getLongitude()  + " ," + location.getLatitude());
                                fetchAllSurveys();
                            }
                            dialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Could not retrieve location", e);
                            dialog.dismiss();
                        }
                    });
    }

    private void doLogout() {
        //mAuthManager.setAuthCredentials("", null);
        sharedPrefsManager.remove(ASKED_ENABLE_GPS_KEY);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void doRefresh() {
        fetchAllSurveys();
    }

    private void fetchAllSurveys() {
        final InformationDialogFragment dialog = dialogsManager.showInformationDialog(this, R.string.dialog_getting_surveys);

        APIService.OnResponse<SurveysModel> onSurveysResponse = new APIService.OnResponse<SurveysModel>() {
            @Override
            public void onSuccess(SurveysModel surveysModel) {
                showSurveys(surveysModel.getSurveys());
                dialog.dismiss();
            }

            @Override
            public void onFailure() {
                dialog.dismiss();
                Utils.showUnexpectedNetworkError(SurveysActivity.this);
            }
        };

        new ApiManager(this).fetchSurveys(token, onSurveysResponse);
    }

    private void showSurveys(List<SurveyModel> surveys) {
        List<SurveyModel> surveysToShow = new ArrayList<>();
        for (SurveyModel survey : surveys) {
            if (survey.getAlreadyDone()) {
                // Already done surveys should not be shown.
                continue;
            }
            if (survey.hasCoordinates() && (!gpsEnabled ||
                    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ! Utils.isLocationPermissionGranted(this)))) {
                // Located surveys should not be shown when GPS is not enabled.
                continue;
            }
            // Add the survey to the list to show.
            surveysToShow.add(survey);
        }

        surveysView.setAdapter(new SurveyListAdapter(SurveysActivity.this, surveysToShow, onSelectedSurvey));

        Utils.applyFadeInAnimation(surveysView);
    }

    private void showSurveyPage(SurveyModel survey) {
        Intent intent = new Intent(this, SurveyActivity.class);
        intent.putExtra(SurveyActivity.EXTRA_TOKEN, token);
        intent.putExtra(SurveyActivity.SURVEY_ID, survey.getId());
        startActivity(intent);
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                                          new String[]{
                                                  Manifest.permission.ACCESS_FINE_LOCATION
                                          },
                                          MY_PERMISSIONS_REQUEST_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                requestedRuntimePermissions = true;
                if (Utils.isLocationPermissionGranted(this)) {
                    setUpLocationSettings();
                } else {
                    fetchAllSurveys();
                }
            }
        }
    }
}
