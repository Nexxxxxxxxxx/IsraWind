package com.oleg.mir.israwind;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ReportsPerLaction extends AppCompatActivity {

    TableLayout t1;
    TableRow tr;
    public String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_per_laction);

        Bundle b = getIntent().getExtras();
        location = b.getString("location");

        SetTitle();


    }

    private void SetTitle()
    {
        TextView textView =  (TextView)findViewById(R.id.locationTextId);
        textView.setText(location + " Reports");
    }

    private void SetReportsTable()
    {

    }
}
