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
        TextView[] tableColArray = new TextView[5];

        for(int j=0; j < IsraWindConsts.Location.length ; j++)
        {
            tableRowArray[j] = new TableRow(this);

            for(int i = 0; i < 4; i++) {
                tableColArray[i] = new TextView(this);

                tableColArray[i].setText("");
                tableColArray[i].setTextSize(15);
                tableColArray[i].setId((j+1)*10+(i+1));

                tableRowArray[j].addView(tableColArray[i]);

            }

            t1.addView(tableRowArray[j]);
        }



        //windReportDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
        windReportDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allReports = dataSnapshot.getValue();
                int i =1;

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    TextView textView;

                    String location = ds.child("location").getValue(String.class);
                    Integer windSpeed = ds.child("windSpeed").getValue(Integer.class);
                    Integer gustSpeed = ds.child("gustSpeed").getValue(Integer.class);
                    String windDirection = ds.child("windDirection").getValue(String.class);
                    String reportTime = ds.child("reportTime").getValue(String.class);

                    textView = (TextView) (TextView)findViewById(getResources().getIdentifier(Integer.toString(i) + "1", "id", getPackageName()));
                    textView.setText(location);

                    textView = (TextView) (TextView)findViewById(getResources().getIdentifier(Integer.toString(i) + "2", "id", getPackageName()));
                    textView.setText("| " +windSpeed + "-" + gustSpeed);

                    textView = (TextView) (TextView)findViewById(getResources().getIdentifier(Integer.toString(i) + "3", "id", getPackageName()));
                    textView.setText("| " +windDirection);

                    textView = (TextView) (TextView)findViewById(getResources().getIdentifier(Integer.toString(i) + "4", "id", getPackageName()));
                    textView.setText("| " +reportTime.substring(8));

                    i++;
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
        /*
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        email = (TextView) findViewById(R.id.useremail);

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setDataToView(user);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
    }


        btnChangePassword = (Button) findViewById(R.id.change_password_button);

        btnRemoveUser = (Button) findViewById(R.id.remove_user_button);

        changePassword = (Button) findViewById(R.id.changePass);

        remove = (Button) findViewById(R.id.remove);
        signOut = (Button) findViewById(R.id.sign_out);

        oldEmail = (EditText) findViewById(R.id.old_email);

        password = (EditText) findViewById(R.id.password);
        newPassword = (EditText) findViewById(R.id.newPassword);

        oldEmail.setVisibility(View.GONE);

        password.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);

        changePassword.setVisibility(View.GONE);

        remove.setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }


        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldEmail.setVisibility(View.GONE);

                password.setVisibility(View.GONE);
                newPassword.setVisibility(View.VISIBLE);

                changePassword.setVisibility(View.VISIBLE);

                remove.setVisibility(View.GONE);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null && !newPassword.getText().toString().trim().equals("")) {
                    if (newPassword.getText().toString().trim().length() < 6) {
                        newPassword.setError("Password too short, enter minimum 6 characters");
                        progressBar.setVisibility(View.GONE);
                    } else {
                        user.updatePassword(newPassword.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(MainActivity.this, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT).show();
                                            signOut();
                                            progressBar.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(MainActivity.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                } else if (newPassword.getText().toString().trim().equals("")) {
                    newPassword.setError("Enter password");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });


        btnRemoveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null) {
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this, SignupActivity.class));
                                        finish();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(MainActivity.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void setDataToView(FirebaseUser user) {

        email.setText("User Email: " + user.getEmail());


    }
   // this listener will be called when there is change in firebase user session
    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            } else {
                setDataToView(user);

            }
        }


    };

    //sign out method
    public void signOut() {
        auth.signOut();


// this listener will be called when there is change in firebase user session
        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
  */
}

// and yesss we have made it. WE HAVE MADE AN APP WITH LOGIN AND REGISTRATION
// PLEASE DO LIKE AND SUBSCRIBE FOR MORE.
//THANK YOU


