package com.oleg.mir.israwind;
import android.location.Location;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class WindReportDTO {
    private static final String type="WindReportDTO";
    public int windSpeed;
    public int gustSpeed;
    public String comment;
    public String reportTime;
    public String location;
    public String windDirection;

    WindReportDTO(int _windSpeed, String _windDirection,String _reportTime, String _location, int _gustSpeed, String _comment)
    {
        windSpeed = _windSpeed;
        windDirection = _windDirection;

        if(_reportTime == null)
        {
            reportTime = GetCurrentDateTime();
        }
        else
        {
            reportTime=_reportTime;
        }

        comment = _comment;
        location = _location;
        gustSpeed = _gustSpeed;
    }

    private String GetCurrentDateTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Israel"));
        String currentDateandTime = sdf.format(new Date());

        return currentDateandTime;
    }
}


