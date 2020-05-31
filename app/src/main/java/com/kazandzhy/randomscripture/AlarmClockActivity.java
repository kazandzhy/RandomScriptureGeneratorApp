package com.kazandzhy.randomscripture;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;

/**
 * Class for creating a daily alarm set by the user
 *
 * This class will allow the user to create an alarm
 * set by the user that repeats daily
 *
 * @author Tyler Braithwaite Nathan Tagg Vlad Kazandzhy
 */
public class AlarmClockActivity extends AppCompatActivity {

    TimePicker alarmTime;
    Switch alarmSwitch;
    PendingIntent pendingintent;

    /**
     * retrieves shared preferences and sets alarmTime and on/off switch to them
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_clock_activity);
        alarmTime = findViewById(R.id.timePicker);
        alarmSwitch = findViewById((R.id.switch1));

        // retrieve shared preferences and set alarmTime and switch to them
        SharedPreferences sharedpref = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        alarmTime.setCurrentHour(sharedpref.getInt("Hour", 12));
        alarmTime.setCurrentMinute(sharedpref.getInt("Minute", 00));
        alarmSwitch.setChecked(sharedpref.getBoolean("Alarm", false));

        // create listener for when the switch is triggered
        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switch_change();
            }
        });

    }



    /**
     * Determines if User is logged in or not
     * @param menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu) {

        if (MainActivity.userId == null) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_other_loggedout, menu);
        } else {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_other_loggedin, menu);
        }
        return true;
    }

    /**
     * Creates the options in the menu dropdown
     *
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.action_login:
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            case R.id.action_signup:
                startActivity(new Intent(this, SignupActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_logout:
                LogoutOption.logOut(getApplicationContext());
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Checks if alarm on/off Switch is set to on or off and displays method
     */
    public void switch_change()
    {
        if(alarmSwitch.isChecked() == false) {
            //if alarm is off cancel pending alarm intent
            setAlarm(0);
            Toast.makeText(this,"Alarm off", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(this,"Alarm on", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Creates an alarm with a pending intent when the setAlarm button is pressed
     *
     * @param view
     */
    public void alarmSet(View view)
    {

        int x = 0;

        //check if alarm switch is on or off
        if(alarmSwitch.isChecked())
        {
            x = 1;
        }
        else
        {
            x = 0;
        }

        //creates a calendar instance
        Calendar calendar = Calendar.getInstance();

        //determines android version and sets the calendar to alarmTime
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    alarmTime.getHour(),
                    alarmTime.getMinute(),
                    0
            );


        }
        else
        {
            calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    alarmTime.getCurrentHour(),
                    alarmTime.getCurrentMinute(),
                    0
            );

        }

        //Creates a calendar instance and sets it to the current time
        Calendar now = Calendar.getInstance();

        //if alarmTime is set to a time earlier then the current time. Set alarm for following day
        if (now.after(calendar))
            calendar.add(Calendar.DAY_OF_YEAR, 1);

        //set hour, minute, and current state of on/off switch to sharedpreferences
        SharedPreferences sharedPrefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt("Hour",alarmTime.getCurrentHour());
        editor.putInt("Minute", alarmTime.getCurrentMinute());
        editor.putBoolean("Alarm", alarmSwitch.isChecked());
        editor.apply();

        //if on/off switch is checked Call setAlarm function, if not, turn alarm off
        switch(x)
        {
            case 1:
            {
            setAlarm(calendar.getTimeInMillis());
            Toast.makeText(this,"Alarm set", Toast.LENGTH_SHORT).show();
            }
            break;
            case 0:
            {
            setAlarm(0);
            }
            break;
        }

    }

    /**
     * Creates instance of alarmManager and creates the alarm
     *
     * @param timeInMillis
     */
    public void setAlarm(long timeInMillis) {

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingintent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //if timeInMillis != 0 then set the alarm with a pending intent
        if (timeInMillis != 0) {
            //create alarm to repeat daily
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, AlarmManager.INTERVAL_DAY, pendingintent);

        } else
        {
            //cancel pending intent and cancel any existing alarm
            alarm.cancel(pendingintent);
        }
    }

}
