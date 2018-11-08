package es.academy.solidgear.surveyx.models;

import com.google.android.gms.location.Geofence;

public class GeofenceModel {
    private final String id;
    private final double latitude;
    private final double longitude;
    private final float radius;
    private final long expirationDuration;
    private int transitionType;

    public static final float RADIUS_BY_DEFAULT = 100; // meters
    public static final long EXPIRATION_DURATION_BY_DEFAULT = 1000*60*60*24*7; // 1 week

    /**
     * @param geofenceId The Geofence's request ID
     * @param latitude Latitude of the Geofence's center. The value is not checked for validity.
     * @param longitude Longitude of the Geofence's center. The value is not checked for validity.
     * @param radius Radius of the geofence circle. The value is not checked for validity
     * @param expiration Geofence expiration duration in milliseconds The value is not checked for
     * validity.
     * @param transition Type of Geofence transition. The value is not checked for validity.
     */
    public GeofenceModel(
            String geofenceId,
            double latitude,
            double longitude,
            float radius,
            long expiration,
            int transition) {
        // Set the instance fields from the constructor

        // An identifier for the geofence
        this.id = geofenceId;

        // Center of the geofence
        this.latitude = latitude;
        this.longitude = longitude;

        // Radius of the geofence, in meters
        this.radius = radius;

        // Expiration time in milliseconds
        this.expirationDuration = expiration;

        // Transition type
        this.transitionType = transition;
    }
    // Instance field getters

    public String getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getRadius() {
        return radius;
    }

    public long getExpirationDuration() {
        return expirationDuration;
    }

    public int getTransitionType() {
        return transitionType;
    }

    /**
     * Creates a Location Services Geofence object from a SimpleGeofence
     *
     * @return A Geofence object
     */
    public Geofence toGeofence() {
        // Build a new Geofence object
        return new Geofence.Builder()
                .setRequestId(getId())
                .setTransitionTypes(transitionType)
                .setCircularRegion(
                        getLatitude(),
                        getLongitude(),
                        getRadius())
                .setExpirationDuration(expirationDuration)
                .build();
    }
}
