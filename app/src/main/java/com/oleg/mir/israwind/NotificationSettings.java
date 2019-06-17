package com.oleg.mir.israwind;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class NotificationSettings extends AppCompatActivity {

    String locationNotification;
    String locationId;

    public SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        setTitle("Notifications");

        IsraWindUtils.SetAdMob(this);

        if(!isNetworkAvailable())
        {
            NoInternetAlert();
            return;
        }

        ShowNotificationSettings();
    }

    private void writeSharedPreference(String location, boolean flag)
    {
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(location,flag);
        editor.commit();
    }

    private boolean readSharedPreference(String location)
    {
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        boolean defaultValue = false;
        boolean flag = sharedPref.getBoolean(location, defaultValue);

        return flag;
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void NoInternetAlert()
    {
        new AlertDialog.Builder(NotificationSettings.this)
                .setTitle("No Internet connection!")
                .setMessage("Please check your internet connection.")
                .setPositiveButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.stat_notify_error)
                .show();
    }

    private void ShowNotificationSettings()
    {
        for(int j=0; j < IsraWindConsts.Location.length ; j++) {
            String locationText = IsraWindConsts.Location[j];
            final Switch sw = new Switch(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(30, 30, 30, 30);
            layoutParams.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            sw.setLayoutParams(layoutParams);
            sw.setText(locationText);
            sw.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

            Log.d("myTag", "location: "+ locationText);
            sw.setChecked(readSharedPreference(locationText));

            LinearLayout linearLayout = findViewById(R.id.NotificationslayoutID);

            if (linearLayout != null) {
                linearLayout.addView(sw);
            }

            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    locationNotification = buttonView.getText().toString();

                    locationId = IsraWindConsts.locationMap.get(locationNotification).toString();

                    writeSharedPreference(locationNotification, isChecked);
                    if(isChecked)
                    {
                        FirebaseMessaging.getInstance().subscribeToTopic(locationId)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        String msg;
                                        if(task.isSuccessful())
                                        {
                                            msg = "Subscribed to: "+locationNotification;
                                        }
                                        else
                                        {
                                            msg = "Failed to Subscribe";
                                        }
                                        Log.d("LocationNotification", msg);
                                        Toast.makeText(NotificationSettings.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    else
                    {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(locationId)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        String msg;
                                        if(task.isSuccessful())
                                        {
                                            msg = "Unsubscribed from: "+locationNotification;
                                        }

                                        else{
                                            msg = "Failed to Unsubscribe";
                                        }
                                        Log.d("LocationNotification", msg);
                                        Toast.makeText(NotificationSettings.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });
        }
    }
}