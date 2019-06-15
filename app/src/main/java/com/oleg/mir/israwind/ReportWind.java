package com.oleg.mir.israwind;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
        commentEditText = findViewById(R.id.commentID);

        Button nw = findViewById(R.id.NWID);
        mDefaultButtonColor = ((Drawable) nw.getBackground());

    }

    public void NWSelection(View v)
    {
        Button nw = findViewById(R.id.NWID);
        currentDirection = "NW ↘";
        SetButtonsCollor(nw);
    }

    public void NSelection(View v)
    {
        Button n = findViewById(R.id.NID);
        currentDirection = "N ⬇";
        SetButtonsCollor(n);
    }

    public void NESelection(View v)
    {
        Button n = findViewById(R.id.NEID);
        currentDirection = "NE ↙";
        SetButtonsCollor(n);
    }

    public void WSelection(View v)
    {
        Button n = findViewById(R.id.WID);
        currentDirection = "W ➡";
        SetButtonsCollor(n);
    }

    public void ESelection(View v)
    {
        Button n = findViewById(R.id.EID);
        currentDirection = "E ⬅";
        SetButtonsCollor(n);
    }

    public void SWSelection(View v)
    {
        Button n = findViewById(R.id.SWID);
        currentDirection = "SW ↗";
        SetButtonsCollor(n);
    }

    public void SSelection(View v)
    {
        Button n = findViewById(R.id.SID);
        currentDirection = "S ⬆";
        SetButtonsCollor(n);
    }

    public void SESelection(View v)
    {
        Button n = findViewById(R.id.SEID);
        currentDirection = "SE ↖";
        SetButtonsCollor(n);
    }

    private void SetButtonsCollor(Button b)
    {
        SetBackgroundForAllDirectionButtons();
        b.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
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

        if(gust <= 0 || windSpeed <= 0)
        {
            Toast.makeText(getApplicationContext(), "Wind speed and gust must me greater that 0", Toast.LENGTH_SHORT).show();
            return;
        }
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


        if(!isNetworkAvailable())
        {
            NoInternetAlert();
            return;

        }

        auth = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        WindReportDTO windReport = new WindReportDTO(windSpeed, direction,null, location, gust, comment,auth);

        UpdateLastWindReport(windReport);
        UpdateAllWindReports(windReport);

        ShowReportStatus();
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void ShowReportStatus()
    {
        new AlertDialog.Builder(ReportWind.this)
                .setTitle("Report Status")
                .setMessage("Your Report was added successfully!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void NoInternetAlert()
    {
        new AlertDialog.Builder(ReportWind.this)
                .setTitle("No Internet connection!")
                .setMessage("Please check your internet connection.")
                .setPositiveButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.stat_notify_error)
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
