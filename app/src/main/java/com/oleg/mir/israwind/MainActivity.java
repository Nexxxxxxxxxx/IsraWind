package com.oleg.mir.israwind;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TableLayout t1,t2;
    TableRow tr;

    public AllWindReports allWindReports;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference windReportDatabase = database.getReference(IsraWindConsts.LastWindReportReference);

    Object allReports;
    Date currentTimeMinusGranularity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        checkPlayServices();

        FirebaseMessaging.getInstance().subscribeToTopic("Akko")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "subscribed";
                        if (!task.isSuccessful()) {
                            msg = "failed";
                        }
                        Log.d("123", msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


        setTitle("IsraWind - Last 24h Reports");

        t1 = (TableLayout)findViewById(R.id.t1);
        t2 = (TableLayout)findViewById(R.id.t2);

        int width = getScreenWidth();

        TableRow [] tableRowArray = new TableRow[IsraWindConsts.Location.length];
        TextView[] tableColArray = new TextView[4];

        SetCurrentTimeMinusGranularity();

        for(int j=0; j < IsraWindConsts.Location.length ; j++)
        {
            tableRowArray[j] = new TableRow(this);

            for(int i = 0; i < 4; i++) {
                tableColArray[i] = new TextView(this);

                if(i==0)
                {
                    tableColArray[i].setText(IsraWindConsts.Location[j]);
                    tableColArray[i].setTextSize(IsraWindConsts.MainReportTextSize);
                }
                else
                {
                    tableColArray[i].setText("|");
                }

                tableColArray[i].setId((j+1)*10+(i+1));


                if(i==0)
                {
                    tableColArray[0].setWidth((int)Math.round(width*0.35));
                }
                else if(i==1)
                {
                    tableColArray[1].setWidth((int)Math.round(width*0.15));
                }
                else if(i==2)
                {
                    tableColArray[2].setWidth((int)Math.round(width*0.25));
                }
                else if(i==3)
                {
                    tableColArray[3].setWidth((int)Math.round(width*0.25));
                }

                tableRowArray[j].addView(tableColArray[i]);


            }

            tableRowArray[j].setId(j);

            if(j%2==0)
            {
                tableRowArray[j].setBackgroundColor(0xFFF0F8FF);
            }
            else
            {
                tableRowArray[j].setBackgroundColor(0xFFF8F8FF);
            }


            tableRowArray[j].setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick( View v ) {
                    int id = v.getId();
                    SetClickAnimation(v);
                    ShowReportsPerLocation(IsraWindConsts.Location[id]);
                }
            } );

            t2.addView(tableRowArray[j]);
        }



        //windReportDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
        windReportDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allReports = dataSnapshot.getValue();
                int i = 1;

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    TextView textView;

                    String location = ds.child("location").getValue(String.class);
                    Integer windSpeed = ds.child("windSpeed").getValue(Integer.class);
                    Integer gustSpeed = ds.child("gustSpeed").getValue(Integer.class);
                    String windDirection = ds.child("windDirection").getValue(String.class);
                    String reportTime = ds.child("reportTime").getValue(String.class);

                    Date reportTimeDateFormat = currentTimeMinusGranularity;
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                        reportTimeDateFormat = dateFormat.parse(reportTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(reportTimeDateFormat.after(currentTimeMinusGranularity))
                    {
                        Integer locationID =  IsraWindUtils.GetLocationID(location);

                        if(locationID != -1)
                        {
                            locationID=locationID+1;
                            locationID = locationID*10;
                            i=locationID;

                            textView = (TextView) (TextView)findViewById(getResources().getIdentifier(Integer.toString(i+2) , "id", getPackageName()));
                            textView.setText("| " +windSpeed + "-" + gustSpeed);
                            textView.setTextSize(IsraWindConsts.MainReportTextSize);

                            textView = (TextView) (TextView)findViewById(getResources().getIdentifier(Integer.toString(i+3) , "id", getPackageName()));
                            textView.setText("| " +windDirection);
                            textView.setTextSize(IsraWindConsts.MainReportTextSize);

                            textView = (TextView) (TextView)findViewById(getResources().getIdentifier(Integer.toString(i+4) , "id", getPackageName()));
                            textView.setText("| " +reportTime.substring(8));
                            textView.setTextSize(IsraWindConsts.MainReportTextSize);

                            i++;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
            }

            return false;
        }

        return true;
    }

    private void SetCurrentTimeMinusGranularity()
    {
        try {
            Calendar cal = Calendar.getInstance();
            String  currentTime = IsraWindUtils.GetCurrentDateTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            cal.setTime(dateFormat.parse(currentTime));
            cal.add(Calendar.DATE, IsraWindConsts.reports_time_granularity_in_days);
            currentTimeMinusGranularity = cal.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void SetClickAnimation(View v)
    {
        v.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
        v.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
    }
    public int getScreenWidth()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        return width;
    }

    public void AddWindReport(View v)
    {
        Intent intent = new Intent(getApplicationContext(), ReportWind.class);
        startActivity(intent);
    }

    public void ModifyNotifications(View v)
    {
        Intent intent = new Intent(getApplicationContext(), ScrollingActivityModifyNotifications.class);
        startActivity(intent);
    }
    public void ShowReportsPerLocation(String location)
    {
        Intent intent = new Intent(getApplicationContext(), ReportsPerLaction.class);

        intent.putExtra("location", location);
        startActivity(intent);
    }
}


