package com.oleg.mir.israwind;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    private Button btnChangePassword, btnRemoveUser,
            changePassword, remove, signOut;
    private TextView email;

    private EditText oldEmail, password, newPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    TextView scr0,scr1, scr2,scr3,scr4;
    TableLayout t1,t2;
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
        t2 = (TableLayout)findViewById(R.id.t2);

        int width = getScreenWidth();

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
                    //tableColArray[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.4f));
                }
                else
                {
                    tableColArray[i].setText("|");
                }

                tableColArray[i].setTextSize(15);
                tableColArray[i].setId((j+1)*10+(i+1));


                if(i==0)
                {
                    tableColArray[0].setWidth((int)Math.round(width*0.4));
                }
                else if(i==1)
                {
                    tableColArray[1].setWidth((int)Math.round(width*0.15));
                }
                else if(i==2)
                {
                    tableColArray[2].setWidth((int)Math.round(width*0.2));
                }
                else if(i==3)
                {
                    tableColArray[3].setWidth((int)Math.round(width*0.25));
                }

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

    public int getScreenWidth()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
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


