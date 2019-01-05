package com.oleg.mir.israwind;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AllWindReports {
    public WindReportDTO [] allWindReports;
    public int numOfLocations = IsraWindConsts.Location.length;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference windReportDatabase = database.getReference("WindReportDto");

    public AllWindReports()
    {
        windReportDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GetReportsFromFirebase(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void GetReportsFromFirebase(DataSnapshot dataSnapshot)
    {
        Object data = dataSnapshot.getValue();
    }
}
