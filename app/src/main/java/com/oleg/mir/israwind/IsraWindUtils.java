package com.oleg.mir.israwind;

import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

public final class IsraWindUtils {
    public static int GetLocationID(String locationName)
    {
        return Arrays.asList(IsraWindConsts.Location).indexOf(locationName);
    }

    public static String GetCurrentDateTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Israel"));
        String currentDateandTime = sdf.format(new Date());

        return currentDateandTime;
    }

    public static <T> T[] add2BeginningOfArray(T element,T[] elements)
    {
        T[] newArray = Arrays.copyOf(elements, elements.length + 1);
        newArray[0] = element;
        System.arraycopy(elements, 0, newArray, 1, elements.length);

        return newArray;
    }

    public static void SetAdMob(AppCompatActivity a)
    {
        AdView mAdView;
        mAdView = a.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
