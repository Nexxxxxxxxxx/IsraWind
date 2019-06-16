package com.oleg.mir.israwind;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.Map;

public class NotificationSettings extends AppCompatActivity {

    String locationNotification;
    String locationId;

    private String subFolder = "/userdata";
    private String file = "user_notifications.ser";
    Map<String, Object> userSettings = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        setTitle("Notifications");

        IsraWindUtils.SetAdMob(this);

        if(!isNetworkAvailable())
        {
            NoInternetAlert();
            return;
        }

        readSetttings();
        ShowNotificationSettings();
    }

    public void writeSettings() {
        File cacheDir = null;
        File appDirectory = null;

        if (android.os.Environment.getExternalStorageState().
                equals(android.os.Environment.MEDIA_MOUNTED)) {
            cacheDir = getApplicationContext().getExternalCacheDir();
            appDirectory = new File(cacheDir + subFolder);

        } else {
            cacheDir = getApplicationContext().getCacheDir();
            String BaseFolder = cacheDir.getAbsolutePath();
            appDirectory = new File(BaseFolder + subFolder);

        }

        if (appDirectory != null && !appDirectory.exists()) {
            appDirectory.mkdirs();
        }

        File fileName = new File(appDirectory, file);

        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(fileName);
            out = new ObjectOutputStream(fos);
            out.writeObject(userSettings);
        } catch (IOException ex) {
            ex.printStackTrace();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.flush();
                fos.close();
                if (out != null)
                    out.flush();
                out.close();
            } catch (Exception e) {

            }
        }
    }


    public void readSetttings() {
        File cacheDir = null;
        File appDirectory = null;
        if (android.os.Environment.getExternalStorageState().
                equals(android.os.Environment.MEDIA_MOUNTED)) {
            cacheDir = getApplicationContext().getExternalCacheDir();
            appDirectory = new File(cacheDir + subFolder);
        } else {
            cacheDir = getApplicationContext().getCacheDir();
            String BaseFolder = cacheDir.getAbsolutePath();
            appDirectory = new File(BaseFolder + subFolder);
        }

        if (appDirectory != null && !appDirectory.exists()) return; // File does not exist

        File fileName = new File(appDirectory, file);

        FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
            fis = new FileInputStream(fileName);
            in = new ObjectInputStream(fis);
            Map<String, Object> myHashMap = (Map<String, Object> ) in.readObject();
            userSettings = myHashMap;
            System.out.println("count of hash map::"+userSettings.size() + " " + userSettings);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            try {
                if(fis != null) {
                    fis.close();
                }
                if(in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean StringToBoolean(String s)
    {
        if(s != null)
        {
            if(s == "true")
            {
                return true;
            }
        }

        return false;
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void NoInternetAlert()
    {
        new AlertDialog.Builder(NotificationSettings.this)
                .setTitle("No Internet connection!")
                .setMessage("Please check your internet connection.")
                .setPositiveButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.stat_notify_error)
                .show();
    }

    private void ShowNotificationSettings()
    {
        for(int j=0; j < IsraWindConsts.Location.length ; j++) {
            String locationText = IsraWindConsts.Location[j];
            final Switch sw = new Switch(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(30, 30, 30, 30);
            layoutParams.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            sw.setLayoutParams(layoutParams);
            sw.setText(locationText);
            sw.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

            if(userSettings == null)
            {
                sw.setChecked(false);
            }
            else
            {
                Log.d("myTag", "location: "+ locationText);
                Object b=userSettings.get(locationText);
                if(b == null)
                {
                    sw.setChecked(false);
                }
                else
                {
                    sw.setChecked( StringToBoolean(b.toString()));
                }

            }

            NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.ScrollViewID);

            LinearLayout linearLayout = findViewById(R.id.NotificationslayoutID);

            if (linearLayout != null) {
                linearLayout.addView(sw);
            }

            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    locationNotification = buttonView.getText().toString();

                    userSettings.put(locationNotification, isChecked);

                    locationId = IsraWindConsts.locationMap.get(locationNotification).toString();

                    writeSettings();
                    if(isChecked)
                    {
                        FirebaseMessaging.getInstance().subscribeToTopic(locationId)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        String msg;
                                        if(task.isSuccessful())
                                        {
                                            msg = "Subscribed to: "+locationNotification;
                                        }
                                        else
                                        {
                                            msg = "Failed to Subscribe";
                                        }
                                        Log.d("LocationNotification", msg);
                                        Toast.makeText(NotificationSettings.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    else
                    {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(locationId)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        String msg;
                                        if(task.isSuccessful())
                                        {
                                            msg = "Unsubscribed from: "+locationNotification;
                                        }

                                        else{
                                            msg = "Failed to Unsubscribe";
                                        }
                                        Log.d("LocationNotification", msg);
                                        Toast.makeText(NotificationSettings.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });
        }
    }
}