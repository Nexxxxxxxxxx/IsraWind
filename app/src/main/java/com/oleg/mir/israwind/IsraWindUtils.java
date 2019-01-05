package com.oleg.mir.israwind;

import java.util.Arrays;

public final class IsraWindUtils {
    public static int GetLocationID(String locationName)
    {
        return Arrays.asList(IsraWindConsts.Location).indexOf(locationName);
    }
}
