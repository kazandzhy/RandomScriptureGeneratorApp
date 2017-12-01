package com.example.randomscripturegeneratorapp;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import static android.support.v4.content.ContextCompat.startActivity;
import static com.example.randomscripturegeneratorapp.MainActivity.sharedPrefs;


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {



        String randomizeOption = sharedPrefs.getString("randomizeOption", "Weighted Random");
        RandomizeVerse randomizeVerse = new RandomizeVerse();

        ScriptureData verse;

        if (randomizeOption.equals("Weighted Random")) {
            verse = randomizeVerse.weightedRandomizeFromAllWorks();
        } else {
            verse = randomizeVerse.pureRandomizeFromAllWorks();
        }

        Intent displayIntent = new Intent(context, ShowScriptureActivity.class);

        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString("verse_id", Integer.toString(verse.getVerse_id()));
        editor.putString("verse_title", verse.getVerse_title());
        editor.putString("scripture_text", verse.getScripture_text());
        editor.putString("url", URL.createURL(verse));
        editor.putString("activity", "AlarmReceiver");
        editor.apply();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.rsgicon)
                        .setContentTitle("SCRIPTURE OF THE DAY!")
                        .setContentIntent(PendingIntent.getActivity(context, 0, displayIntent,0))
                        .setSound(Settings.System.DEFAULT_ALARM_ALERT_URI)
                        .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                        .setContentText("Tap to see");

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

}
