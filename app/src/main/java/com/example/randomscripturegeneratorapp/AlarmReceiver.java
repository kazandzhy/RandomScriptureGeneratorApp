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

public class AlarmReceiver extends BroadcastReceiver {

    public static final String APP_PREFS = "APPLICATION_PREFERENCES";

    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl= pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"My TAG");
        wl.acquire();
        NotificationManager mNotificationManager;
        Intent displayIntent;
        SharedPreferences.Editor editor;
        NotificationCompat.Builder mBuilder;

        SharedPreferences sharedPrefs = context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        String randomizeOption = sharedPrefs.getString("randomizeOption", "Weighted Random");

        ScriptureData[] scriptureArray = new WorkWithJSON().getScriptureArray();

        // we want to do this only if alarm is called when app is closed
        if (scriptureArray == null) {
            WorkWithJSON.deserializeJSON(context);
        }

        RandomizeVerse randomizeVerse = new RandomizeVerse();
        ScriptureData verse;

        if (randomizeOption.equals("Weighted Random")) {
            verse = randomizeVerse.weightedRandomizeFromAllWorks();
        } else {
            verse = randomizeVerse.pureRandomizeFromAllWorks();
        }



        displayIntent = new Intent(context, ShowScriptureActivity.class);
        displayIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pending = PendingIntent.getActivity(context, 1, displayIntent,0);

        editor = sharedPrefs.edit();
        editor.putString("verse_id", Integer.toString(verse.getVerse_id()));
        editor.putString("verse_title", verse.getVerse_title());
        editor.putString("scripture_text", verse.getScripture_text());
        editor.putString("url", GenerateURL.createURL(verse));
        editor.putString("activity", "AlarmReceiver");
        editor.apply();

        mBuilder = new NotificationCompat.Builder(context.getApplicationContext());
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

         mBuilder.setSmallIcon(R.drawable.rsgicon)
                 .setContentTitle("Scripture of the Day!")
                 .setContentIntent(pending)
                 .setSound(Settings.System.DEFAULT_ALARM_ALERT_URI)
                 .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                 .setAutoCancel(true)
                 .setContentText("Tap to see");

        mNotificationManager.notify(9, mBuilder.build());
        wl.release();
    }

}
