package es.academy.solidgear.surveyx.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import es.academy.solidgear.surveyx.R;
import es.academy.solidgear.surveyx.ui.activities.MainActivity;
import es.academy.solidgear.surveyx.ui.activities.SurveyActivity;

public class ReceiveTransitionsIntentService extends IntentService {
    private final static String TAG = ReceiveTransitionsIntentService.class.getCanonicalName();

    public ReceiveTransitionsIntentService() {
        super("ReceiveTransitionsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if (event != null){

            if (event.hasError()){
                Log.d(TAG, String.valueOf(event.getErrorCode()));
            } else {
                int transition = event.getGeofenceTransition();
                if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                    for (int index = 0; index < event.getTriggeringGeofences().size(); index++) {
                        sendNotification(event.getTriggeringGeofences().get(index).getRequestId());
                    }
                }
            }

        }
    }

    private void sendNotification(String surveyId) {
        Intent notificationIntent = new Intent(getApplicationContext(), SurveyActivity.class);
        notificationIntent.putExtra(SurveyActivity.SURVEY_ID, Integer.valueOf(surveyId));

        // Construct a task stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the main Activity to the task stack as the parent
        stackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Set the notification contents
        builder.setSmallIcon(R.drawable.logo_notification)
                .setContentTitle(this.getString(R.string.notification_title))
                //.setContentText(surveyId)
                .setContentIntent(notificationPendingIntent);

        // Get an instance of the Notification manager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        // Issue the notifications
        notificationManager.notify(Integer.valueOf(surveyId), notification);
    }
}
