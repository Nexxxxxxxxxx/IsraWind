package com.oleg.mir.israwind;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oleg.mir.israwind.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.oleg.mir.israwind.AccountActivity.LoginActivity;
import com.oleg.mir.israwind.AccountActivity.SignupActivity;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private Button btnChangePassword, btnRemoveUser,
            changePassword, remove, signOut;
    private TextView email;

    private EditText oldEmail, password, newPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;


    TextView scr0,scr1, scr2,scr3,scr4;
    TableLayout t1;
    TableRow tr;

    public AllWindReports allWindReports;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference windReportDatabase = database.getReference(IsraWindConsts.LastWindReportReference);
    Object allReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t1 = (TableLayout)findViewById(R.id.t1);
        t1.setColumnStretchable(0,true);
        t1.setColumnStretchable(1,true);
        t1.setColumnStretchable(2,true);
        t1.setColumnStretchable(3,true);

        TableRow [] tableRowArray = new TableRow[IsraWindConsts.Location.length];
        TextView[] tableColArray = new TextView[4];

        for(int j=0; j < IsraWindConsts.Location.length ; j++)
        {
            tableRowArray[j] = new TableRow(this);

            for(int i = 0; i < 4; i++) {
                tableColArray[i] = new TextView(this);

                if(i==0)
                {
                    tableColArray[i].setText(IsraWindConsts.Location[j]);
                }
                else
                {
                    tableColArray[i].setText("|");
                }

                tableColArray[i].setTextSize(15);
                tableColArray[i].setId((j+1)*10+(i+1));

                tableRowArray[j].addView(tableColArray[i]);

            }

            tableRowArray[j].setId(j);

            tableRowArray[j].setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick( View v ) {
                    int id = v.getId();
                    ShowReportsPerLocation(IsraWindConsts.Location[id]);
                }
            } );

            t1.addView(tableRowArray[j]);
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

                    Integer locationID =  IsraWindUtils.GetLocationID(location);

                    if(locationID != -1)
                    {
                        locationID=locationID+1;
                        locationID = locationID*10;
                        i=locationID;

                        textView = (TextView) (TextView)findViewById(getResources().getIdentifier(Integer.toString(i+1), "id", getPackageName()));
                        textView.setText(location);

                        textView = (TextView) (TextView)findViewById(getResources().getIdentifier(Integer.toString(i+2) , "id", getPackageName()));
                        textView.setText("| " +windSpeed + "-" + gustSpeed);

                        textView = (TextView) (TextView)findViewById(getResources().getIdentifier(Integer.toString(i+3) , "id", getPackageName()));
                        textView.setText("| " +windDirection);

                        textView = (TextView) (TextView)findViewById(getResources().getIdentifier(Integer.toString(i+4) , "id", getPackageName()));
                        textView.setText("| " +reportTime.substring(8));

                        i++;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void AddWindReport(View v)
    {
        Intent intent = new Intent(getApplicationContext(), report_wind.class);
        startActivity(intent);
    }

    public void ShowReportsPerLocation(String location)
    {
        Intent intent = new Intent(getApplicationContext(), ReportsPerLaction.class);

        intent.putExtra("location", location);
        startActivity(intent);
    }
}


