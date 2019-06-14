package com.oleg.mir.israwind;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class ScrollingActivityModifyNotifications extends AppCompatActivity {

    DatabaseReference locationRef;
    Map<String,String> allNotifications;
    String locationNotification;
    Map<String,Object> taskMap;
    String locationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_modify_notifications);

        setTitle("Notifications");

        if(!isNetworkAvailable())
        {
            NoInternetAlert();
            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference windReportDatabase = database.getReference(IsraWindConsts.UsersInfoReference);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        locationRef = windReportDatabase.child(user.getUid()).child("notificationSettings");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allNotifications = (HashMap)dataSnapshot.getValue();

                ShowNotificationSettings();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean StringToBoolean(String s)
    {
        if(s != null)
        {
            if(s == "true")
            {
                return true;
            }
        }

        return false;
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void NoInternetAlert()
    {
        new AlertDialog.Builder(ScrollingActivityModifyNotifications.this)
                .setTitle("No Internet connection!")
                .setMessage("Please check your internet connection.")
                .setPositiveButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                //.setNegativeButton(android.R.string.no, null)
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

            if(allNotifications == null)
            {
                sw.setChecked(false);
            }
            else
            {
                Log.d("myTag", "location: "+ locationText);
                Object b=allNotifications.get(locationText);
                if(b == null)
                {
                    sw.setChecked(false);
                }
                else
                {
                    sw.setChecked( StringToBoolean(b.toString()));
                }

            }


            NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.ScrollViewID);

            LinearLayout linearLayout = findViewById(R.id.TogglesLinearLayoutID);
            // Add Switch to LinearLayout
            if (linearLayout != null) {
                linearLayout.addView(sw);
            }

            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    locationNotification = buttonView.getText().toString();

                    taskMap = new HashMap<String,Object>();
                    taskMap.put(locationNotification, isChecked);

                    locationId = IsraWindConsts.locationMap.get(locationNotification).toString();
                    if(isChecked)
                    {
                        FirebaseMessaging.getInstance().subscribeToTopic(locationId)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String msg;
                                    if(task.isSuccessful())
                                    {
                                        locationRef.updateChildren(taskMap);
                                        msg = "Subscribed to: "+locationNotification;
                                    }
                                    else
                                        {
                                        msg = "Failed to Subscribe";
                                    }
                                    Log.d("LocationNotification", msg);
                                    Toast.makeText(ScrollingActivityModifyNotifications.this, msg, Toast.LENGTH_SHORT).show();
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
                                        locationRef.updateChildren(taskMap);
                                        msg = "Unsubscribed from: "+locationNotification;
                                    }

                                    else{
                                        msg = "Failed to Unsubscribe";
                                    }
                                    Log.d("LocationNotification", msg);
                                    Toast.makeText(ScrollingActivityModifyNotifications.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            });
                    }
                }
            });
        }
    }
}