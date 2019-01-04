package com.oleg.mir.israwind;
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
    public enum WindDirection {
        N,
        S,
        W,
        E
    };

    WindDirection windDirection;

    WindReportDTO(int _windSpeed, WindDirection _windDirection )
    {
        windSpeed = _windSpeed;
        windDirection = _windDirection;
        reportTime = GetCurrentDateTime();
        comment = "test comment";
    }

    private String GetCurrentDateTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Israel"));
        String currentDateandTime = sdf.format(new Date());

        return currentDateandTime;
    }
}


