package com.oleg.mir.israwind;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

public class ScrollingActivityModifyNotifications extends AppCompatActivity {

    DatabaseReference locationRef;
    Map<String,String> allNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_modify_notifications);

        setTitle("Notification Settings");

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
                sw.setChecked( StringToBoolean(b.toString()));
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
                    String locationNotification = buttonView.getText().toString();

                    Map<String,Object> taskMap = new HashMap<String,Object>();
                    taskMap.put(locationNotification, isChecked);

                    locationRef.updateChildren(taskMap);
                }
            });
        }
    }
}