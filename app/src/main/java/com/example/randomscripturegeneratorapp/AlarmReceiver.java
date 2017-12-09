package com.example.randomscripturegeneratorapp;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

/**
 * Class that recieves alarm broadcast from AlarmClockAcitivity
 *
 * This class creates a notification to alert user of of the daily
 * alarm they set.
 * This class also creates a random scripture for the user to view
 * when they tap the notification
 *
 * @author Tyler Braithwaite, Nathan Tagg, Vlad Kazandzhy
 */
public class AlarmReceiver extends BroadcastReceiver {

    public static final String APP_PREFS = "APPLICATION_PREFERENCES";

    /**
     * This function recieves the broadcast from  AlarmClockAcivity
     * and creates a notification and random scripture
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        //PowerManager allows the alarm to run in the background
        PowerManager pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl= pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"My TAG");
        wl.acquire();

        NotificationManager mNotificationManager;
        Intent displayIntent;
        SharedPreferences.Editor editor;
        NotificationCompat.Builder mBuilder;

        //determines if user has randomize option set to weighted or pure random in settings
        SharedPreferences sharedPrefs = context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        String randomizeOption = sharedPrefs.getString("randomizeOption", "Weighted Random");

        ScriptureData[] scriptureArray = new WorkWithJSON().getScriptureArray();

        // we want to do this only if alarm is called when app is closed
        if (scriptureArray == null) {
            WorkWithJSON.deserializeJSON(context);
        }

        //find a random verse
        RandomizeVerse randomizeVerse = new RandomizeVerse();
        ScriptureData verse;

        if (randomizeOption.equals("Weighted Random")) {
            verse = randomizeVerse.weightedRandomizeFromAllWorks();
        } else {
            verse = randomizeVerse.pureRandomizeFromAllWorks();
        }


        //create intent that leads to ShowScriptureActivity
        displayIntent = new Intent(context, ShowScriptureActivity.class);
        displayIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        //create pending intent to be used in the notification
        PendingIntent pending = PendingIntent.getActivity(context, 1, displayIntent,0);

        //put new random scripture in shared preferences to be passed to ShowScriptureActivity
        editor = sharedPrefs.edit();
        editor.putString("verse_id", Integer.toString(verse.getVerse_id()));
        editor.putString("verse_title", verse.getVerse_title());
        editor.putString("scripture_text", verse.getScripture_text());
        editor.putString("url", GenerateURL.createURL(verse));
        editor.putString("activity", "AlarmReceiver");
        editor.apply();

        //creates notificationcompat.builder so construct new notification
        mBuilder = new NotificationCompat.Builder(context.getApplicationContext());
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //set up params of notification
         mBuilder.setSmallIcon(R.drawable.rsgicon)
                 .setContentTitle("Scripture of the Day!")
                 .setContentIntent(pending)
                 .setSound(Settings.System.DEFAULT_ALARM_ALERT_URI)
                 .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                 .setAutoCancel(true)
                 .setContentText("Tap to see");

        //notify the user
        mNotificationManager.notify(9, mBuilder.build());
        wl.release();
    }

}
