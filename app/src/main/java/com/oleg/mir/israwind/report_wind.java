package com.oleg.mir.israwind;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
        setTitle("Report a Wind");

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

        WindReportDTO windReport = new WindReportDTO(windSpeed, direction,null, location, gust, comment);

        UpdateLastWindReport(windReport);
        UpdateAllWindReports(windReport);

        ShowReportStatus();
    }

    private void ShowReportStatus()
    {
        new AlertDialog.Builder(report_wind.this)
                .setTitle("Report Status")
                .setMessage("Your Report was added successfully!")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                //.setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    public void UpdateAllWindReports(WindReportDTO windReport)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference windReportDatabase = database.getReference(IsraWindConsts.AllWindReportsReference);
        DatabaseReference locationRef = windReportDatabase.child(windReport.location);

        Map<String,Object> taskMap = new HashMap<String,Object>();

        taskMap.put("windSpeed", windReport.windSpeed);
        taskMap.put("gustSpeed", windReport.gustSpeed);
        taskMap.put("reportTime", windReport.reportTime);
        taskMap.put("comment", windReport.comment);
        taskMap.put("windDirection", windReport.windDirection);
        taskMap.put("location", windReport.location);

        String id = locationRef.push().getKey();
        locationRef.child(id).setValue(taskMap);
    }


    public void UpdateLastWindReport(WindReportDTO windReport)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference windReportDatabase = database.getReference(IsraWindConsts.LastWindReportReference);

        DatabaseReference locationRef = windReportDatabase.child(windReport.location);

        Map<String,Object> taskMap = new HashMap<String,Object>();

        taskMap.put("windSpeed", windReport.windSpeed);
        taskMap.put("gustSpeed", windReport.gustSpeed);
        taskMap.put("reportTime", windReport.reportTime);
        taskMap.put("comment", windReport.comment);
        taskMap.put("windDirection", windReport.windDirection);
        taskMap.put("location", windReport.location);
        
        locationRef.updateChildren(taskMap);
    }
}
