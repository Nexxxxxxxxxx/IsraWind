package com.oleg.mir.israwind;

public class WindDTO {
    public int windSpeed;
    public int gustSpeed;
    public String comment;
    public enum WindDirection {
        N,
        S,
        W,
        E
    };
}


