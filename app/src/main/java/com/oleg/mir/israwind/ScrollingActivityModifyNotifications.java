package com.oleg.mir.israwind;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;

import com.oleg.mir.israwind.R;

public class ScrollingActivityModifyNotifications extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_modify_notifications);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        for(int j=0; j < IsraWindConsts.Location.length ; j++) {
            final Switch sw = new Switch(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(30, 30, 30, 30);
            layoutParams.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            sw.setLayoutParams(layoutParams);
            sw.setText(IsraWindConsts.Location[j]);
            sw.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

            NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.ScrollViewID);

            LinearLayout linearLayout = findViewById(R.id.TogglesLinearLayoutID);
            // Add Switch to LinearLayout
            if (linearLayout != null) {
                linearLayout.addView(sw);
            }

            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                }
            });
        }
    }
}
