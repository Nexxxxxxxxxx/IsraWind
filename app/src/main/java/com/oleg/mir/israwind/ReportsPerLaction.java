package com.oleg.mir.israwind;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ReportsPerLaction extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference windReportDatabase = database.getReference(IsraWindConsts.AllWindReportsReference);

    TableLayout t1;
    TableRow tr;
    TextView[] tableColArray;
    public String location;
    int reportsCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_per_laction);

        IsraWindUtils.SetAdMob(this);

        Bundle b = getIntent().getExtras();
        location = b.getString("location");
        Query searchQuery = windReportDatabase.child(location).limitToLast(IsraWindConsts.NumberOfReportsToShow);

        DatabaseReference a = searchQuery.getRef();

        setTitle(location+" | "+"Last "+IsraWindConsts.NumberOfReportsToShow+" Reports");

        t1 = (TableLayout)findViewById(R.id.reportsPerLocationTableIdData);
        t1.setColumnStretchable(0,true);
        t1.setColumnStretchable(1,true);
        t1.setColumnStretchable(2,true);
        t1.setColumnStretchable(3,true);

        //Query last =  windReportDatabase.orderByKey().limitToFirst(5);

        a.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> allReports = dataSnapshot.getChildren();

                for(DataSnapshot reportItem : allReports) {
                    reportsCounter = reportsCounter +1;
                    Integer windSpeed = reportItem.child("windSpeed").getValue(Integer.class);
                    Integer gustSpeed = reportItem.child("gustSpeed").getValue(Integer.class);
                    String windDirection = reportItem.child("windDirection").getValue(String.class);
                    String reportTime = reportItem.child("reportTime").getValue(String.class);
                    String comment = reportItem.child("comment").getValue(String.class);
                    String userReported = reportItem.child("userReported").getValue(String.class);

                    WindReportDTO windReport = new WindReportDTO(windSpeed, windDirection,reportTime, location, gustSpeed, comment,userReported);

                    SetReportsTable(windReport);

                    if(reportsCounter>IsraWindConsts.NumberOfReportsToShow)
                    {
                        return;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
            });
         }

    private void SetReportsTable(WindReportDTO windReportDTO)
    {
        tr = new TableRow(this);
        tableColArray = new TextView[4];

        tableColArray[0] = new TextView(this);
        tableColArray[1] = new TextView(this);
        tableColArray[2] = new TextView(this);
        tableColArray[3] = new TextView(this);


        tableColArray[0].setText(String.valueOf(windReportDTO.windSpeed)+"-"+String.valueOf(windReportDTO.gustSpeed));
        tableColArray[1].setText("| "+windReportDTO.windDirection);
        tableColArray[2].setText("| "+windReportDTO.reportTime);
        tableColArray[3].setText("| "+windReportDTO.userReported);

        tr.addView(tableColArray[0]);
        tr.addView(tableColArray[1]);
        tr.addView(tableColArray[2]);
        tr.addView(tableColArray[3]);

        t1.addView(tr,1);
    }
}
