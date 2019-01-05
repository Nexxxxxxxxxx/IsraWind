package com.oleg.mir.israwind;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class report_wind extends AppCompatActivity {
    EditText windSpeedEditText;
    EditText gustEditText;
    Spinner locationsDropdown;
    Spinner directionsDropdown;
    EditText commentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_wind);

        directionsDropdown = findViewById(R.id.spinner_wind_direction);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, IsraWindConsts.WindDirections);
        directionsDropdown.setAdapter(adapter);

        locationsDropdown = findViewById(R.id.spinner_location_dropdown);
        ArrayAdapter<String> locationsDropdownAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, IsraWindConsts.Location);
        locationsDropdown.setAdapter(locationsDropdownAdapter);

        windSpeedEditText = findViewById(R.id.windSpeedID);
        gustEditText = findViewById(R.id.gustID);
        commentEditText = findViewById(R.id.commentsID2);

    }

    public void ReportWind(View v)
    {
        int windSpeed = Integer.parseInt(windSpeedEditText.getText().toString());
        int gust = Integer.parseInt(gustEditText.getText().toString());
        String location = locationsDropdown.getSelectedItem().toString();
        String direction = directionsDropdown.getSelectedItem().toString();
        String comment = commentEditText.getText().toString();

        WindReportDTO windReport = new WindReportDTO(windSpeed, direction, location, gust, comment);

        UpdateLastWindReport(windReport);
    }

    public void UpdateLastWindReport(WindReportDTO windReport)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference windReportDatabase = database.getReference("LastWindReport");

        //String id = windReportDatabase.push().getKey();
        //windReportDatabase.child(id).setValue(windReport);

        DatabaseReference locationRef = windReportDatabase.child(Integer.toString(IsraWindUtils.GetLocationID(windReport.location)));

        Map<String,Object> taskMap = new HashMap<String,Object>();

        taskMap.put("windSpeed", windReport.windSpeed);
        locationRef.updateChildren(taskMap);

        taskMap.put("gustSpeed", windReport.gustSpeed);
        locationRef.updateChildren(taskMap);

        taskMap.put("reportTime", windReport.reportTime);
        locationRef.updateChildren(taskMap);

        taskMap.put("comment", windReport.comment);
        locationRef.updateChildren(taskMap);

        taskMap.put("windDirection", windReport.windDirection);
        locationRef.updateChildren(taskMap);

        taskMap.put("location", windReport.location);
        locationRef.updateChildren(taskMap);
    }
}
