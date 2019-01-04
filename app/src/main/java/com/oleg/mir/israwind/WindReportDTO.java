package com.oleg.mir.israwind;

public class WindReportDTO {
    private static final String type="WindReportDTO";
    public int windSpeed;
    public int gustSpeed;
    public String comment;
    public enum WindDirection {
        N,
        S,
        W,
        E
    };

    WindDirection windDirection;

    WindReportDTO(int _windSpeed, WindDirection _windDirection)
    {
        windSpeed = _windSpeed;
        windDirection = _windDirection;
    }
}


