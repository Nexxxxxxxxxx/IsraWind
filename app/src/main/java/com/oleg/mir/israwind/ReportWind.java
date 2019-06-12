package com.oleg.mir.israwind;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class ReportWind extends AppCompatActivity {
    EditText windSpeedEditText;
    EditText gustEditText;
    Spinner locationsDropdown;
    EditText commentEditText;
    private String auth;
    Drawable mDefaultButtonColor;

    private boolean nwselected=false,nselected=false,neselected=false,wselected=false,eselected=false,swselected=false,sselected=false,seselected=false;
    private String currentDirection="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_wind);
        setTitle("Report a Wind");

        locationsDropdown = findViewById(R.id.spinner_location_dropdown);
        ArrayAdapter<String> locationsDropdownAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, IsraWindConsts.Location);
        locationsDropdownAdapter.setDropDownViewResource(R.layout.spinner_item);
        locationsDropdown.setAdapter(locationsDropdownAdapter);

        windSpeedEditText = findViewById(R.id.windSpeedID);
        gustEditText = findViewById(R.id.gustID);
        commentEditText = findViewById(R.id.commentsID2);

        Button nw = findViewById(R.id.NWID);
        mDefaultButtonColor = ((Drawable) nw.getBackground());

    }

    public void NWSelection(View v)
    {
        Button nw = findViewById(R.id.NWID);

        if(!nwselected)
        {
            SetAllDirectionsToFalse();
            currentDirection = "NW ↘";
            nwselected = true;
        }
        else
        {
            nwselected = false;
        }

        SetButtonsCollor(nw,!nwselected);
    }

    public void NSelection(View v)
    {
        Button n = findViewById(R.id.NID);

        if(!nselected)
        {
            SetAllDirectionsToFalse();
            currentDirection = "N ↓";
            nselected = true;
        }
        else
        {
            nselected = false;
        }

        SetButtonsCollor(n,!nselected);
    }

    public void NESelection(View v)
    {
        Button n = findViewById(R.id.NEID);

        if(!neselected)
        {
            SetAllDirectionsToFalse();
            currentDirection = "NE ↙";
            neselected = true;
        }
        else
        {
            neselected = false;
        }

        SetButtonsCollor(n,!neselected);
    }

    public void WSelection(View v)
    {
        Button n = findViewById(R.id.WID);

        if(!wselected)
        {
            SetAllDirectionsToFalse();
            currentDirection = "W →";
            wselected = true;
        }
        else
        {
            wselected = false;
        }

        SetButtonsCollor(n,!wselected);
    }

    public void ESelection(View v)
    {
        Button n = findViewById(R.id.EID);

        if(!eselected)
        {
            SetAllDirectionsToFalse();
            currentDirection = "E ←";
            eselected = true;
        }
        else
        {
            eselected = false;
        }

        SetButtonsCollor(n,!eselected);
    }

    public void SWSelection(View v)
    {
        Button n = findViewById(R.id.SWID);

        if(!swselected)
        {
            SetAllDirectionsToFalse();
            currentDirection = "SW ↗";
            swselected = true;
        }
        else
        {
            swselected = false;
        }

        SetButtonsCollor(n,!swselected);
    }

    public void SSelection(View v)
    {
        Button n = findViewById(R.id.SID);

        if(!sselected)
        {
            SetAllDirectionsToFalse();
            currentDirection = "S ↑";
            sselected = true;
        }
        else
        {
            sselected = false;
        }

        SetButtonsCollor(n,!sselected);
    }

    public void SESelection(View v)
    {
        Button n = findViewById(R.id.SEID);

        if(!seselected)
        {
            SetAllDirectionsToFalse();
            currentDirection = "SE ↖";
            seselected = true;
        }
        else
        {
            seselected = false;
        }

        SetButtonsCollor(n,!seselected);
    }

    private void SetAllDirectionsToFalse()
    {
        nwselected=false;
        nselected=false;
        neselected=false;
        wselected=false;
        eselected=false;
        swselected=false;
        sselected=false;
        seselected=false;
    }

    private void SetButtonsCollor(Button b, boolean selection)
    {
        if(!selection)
        {
            SetBackgroundForAllDirectionButtons();
            b.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        else
        {
            b.setBackground(mDefaultButtonColor);
        }
    }

    private void SetBackgroundForAllDirectionButtons()
    {
        Button directionButton = findViewById(R.id.NWID);
        directionButton.setBackground(mDefaultButtonColor);

        directionButton = findViewById(R.id.NID);
        directionButton.setBackground(mDefaultButtonColor);

        directionButton = findViewById(R.id.NEID);
        directionButton.setBackground(mDefaultButtonColor);

        directionButton = findViewById(R.id.WID);
        directionButton.setBackground(mDefaultButtonColor);

        directionButton = findViewById(R.id.EID);
        directionButton.setBackground(mDefaultButtonColor);

        directionButton = findViewById(R.id.SWID);
        directionButton.setBackground(mDefaultButtonColor);

        directionButton = findViewById(R.id.SID);
        directionButton.setBackground(mDefaultButtonColor);

        directionButton = findViewById(R.id.SEID);
        directionButton.setBackground(mDefaultButtonColor);
    }

    public void ReportWind(View v)
    {
        String windString = windSpeedEditText.getText().toString();
        String gustString = gustEditText.getText().toString();
        String location = locationsDropdown.getSelectedItem().toString();
        String direction = currentDirection;
        String comment = commentEditText.getText().toString();

        if (TextUtils.isEmpty(windString+"")) {
            Toast.makeText(getApplicationContext(), "Enter a wind speed", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(gustString+"")) {
            Toast.makeText(getApplicationContext(), "Enter a gust speed", Toast.LENGTH_SHORT).show();
            return;
        }

        int windSpeed = Integer.parseInt(windString);
        int gust = Integer.parseInt(gustString);

        if(gust<windSpeed)
        {
            Toast.makeText(getApplicationContext(), "Gust must be greater or equal than windspeed", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(location+"")) {
            Toast.makeText(getApplicationContext(), "Enter a location", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(direction+"")) {
            Toast.makeText(getApplicationContext(), "Enter a wind direction", Toast.LENGTH_SHORT).show();
            return;
        }



        auth = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        WindReportDTO windReport = new WindReportDTO(windSpeed, direction,null, location, gust, comment,auth);

        UpdateLastWindReport(windReport);
        UpdateAllWindReports(windReport);

        ShowReportStatus();
    }

    private void ShowReportStatus()
    {
        new AlertDialog.Builder(ReportWind.this)
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
        taskMap.put("userReported", windReport.userReported);

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
        taskMap.put("userReported", windReport.userReported);
        
        locationRef.updateChildren(taskMap);
    }
}
