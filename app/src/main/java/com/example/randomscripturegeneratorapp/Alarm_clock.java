package com.example.randomscripturegeneratorapp;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;


public class Alarm_clock extends AppCompatActivity {

    TimePicker timepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_clock_activity);
        timepicker = findViewById(R.id.timePicker);
    }

    public void alarmSwitch(View view)
    {
        Switch alarm = findViewById((R.id.switch1));
        int x = 0;
        if(alarm.isChecked())
        {
            x = 1;
        }
        else
        {
            x = 0;
        }
        Calendar calendar = Calendar.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    timepicker.getHour(),
                    timepicker.getMinute(),
                    0
            );
        }
        else
        {
            calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    timepicker.getCurrentHour(),
                    timepicker.getCurrentMinute(),
                    0
            );
        }

        switch(x)
        {
            case 1:
            {
            setAlarm(calendar.getTimeInMillis());
            Toast.makeText(this,"Alarm on", Toast.LENGTH_SHORT).show();
            }
            break;
            case 0:
            {
            Toast.makeText(this,"Alarm off", Toast.LENGTH_SHORT).show();
            setAlarm(0);
            }
            break;
        }

    }

    public void setAlarm(long timeInMillis) {
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class );
        PendingIntent pendingintent = PendingIntent.getBroadcast(this,0, intent,0);

        alarm.setRepeating(AlarmManager.RTC  ,timeInMillis,AlarmManager.INTERVAL_DAY, pendingintent);
    }

}
