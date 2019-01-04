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

public class report_wind extends AppCompatActivity {
    EditText windSpeedEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_wind);

        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.spinner);
        //create a list of items for the spinner.
        String[] items = new String[]{"1", "2", "three2"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        windSpeedEditText = findViewById(R.id.windSpeedID);

    }

    public void ReportWind(View v)
    {
        int windSpeed = Integer.parseInt(windSpeedEditText.getText().toString());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference windReportDatabase = database.getReference("WindReportDto");
        String id = windReportDatabase.push().getKey();

        WindReportDTO windReport = new WindReportDTO(windSpeed,WindReportDTO.WindDirection.N);
        windReportDatabase.child(id).setValue(windReport);

    }
}
