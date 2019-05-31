package com.oleg.mir.israwind;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ReportsPerLaction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_per_laction);

        Bundle b = getIntent().getExtras();
        String location = b.getString("location");

        TextView textView =  (TextView)findViewById(R.id.locationTextId);
        textView.setText(location);
    }
}
