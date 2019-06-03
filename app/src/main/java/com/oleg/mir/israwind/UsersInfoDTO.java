package com.oleg.mir.israwind;
import java.util.Map;

public class UsersInfoDTO {
    public String UID;
    public String email;
    public String nickname;
    Map<String, String> notificationSettings;
    private int totalReportsCount;
    private String currentAppVersion;

    UsersInfoDTO(String _UID, String _email,String _nickname)
    {
        UID = _UID;
        email = _email;
        nickname = _nickname;
    }
}
