package com.oleg.mir.israwind;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class IsraWindConsts {
    public static final String[] Location = {"Kineret - Diamond", "Eilat", "Betzet", "Akko", "Kiryat Yam", "Kiryat Haim", "Haifa - Bat Galim", "Haifa - Studentim","Atlit","Sdot Yam","Beit Yanai","Maayan Zvi","Konetiki", "Hertzeliya - Sidni Ali","Hertzeliya - Maaliyot","Hof a Tzuk","Bat Yam", "Ashdod","Ashkelon"};

    public static final Map<String, String> locationMap = createMap();

    private static Map<String, String> createMap() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("Kineret - Diamond", "kineretdiamond");
        result.put("Eilat", "eilat");
        result.put("Betzet", "betzet");
        result.put("Akko", "akko");
        result.put("Kiryat Yam", "kiryatyam");
        result.put("Kiryat Haim", "kiryathaim");
        result.put("Haifa - Bat Galim", "haifabatgalim");
        result.put("Haifa - Studentim", "haifastudentim");
        result.put("Atlit", "atlit");
        result.put("Sdot Yam", "sdotyam");
        result.put("Beit Yanai", "beityanai");
        result.put("Maayan Zvi", "maayanzvi");
        result.put("Konetiki", "konetiki");
        result.put("Hertzeliya - Sidni Ali", "hertzeliyasidniali");
        result.put("Hertzeliya - Maaliyot", "hertzeliyamaaliyot");
        result.put("Hof a Tzuk", "hofatzuk");
        result.put("Bat Yam", "batyam");
        result.put("Ashdod", "ashdod");
        result.put("Ashkelon", "ashkelon");
        return Collections.unmodifiableMap(result);
    }

    public static final String[] WindDirections = {"N ↓", "S ↑", "W →", "E ←", "NW ↘", "NE ↙", "SE ↖", "SW ↗"};

    public static final String LastWindReportReference = "LastWindReport";
    public static final String AllWindReportsReference = "AllWindReports";
    public static final String UsersInfoReference = "UsersInfo";

    public static final String ChooseLocationText="Choose Location...";

    public static int MainReportTextSize = 18;

    public static int reports_time_granularity_in_days = -1;
    public static int NumberOfReportsToShow = 100;
}
