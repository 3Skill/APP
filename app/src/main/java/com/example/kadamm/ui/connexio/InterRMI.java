package com.example.kadamm.ui.connexio;

public interface InterRMI{

    public void setNickName(String nickname);
    public boolean getWaitingRoomStatus();
    public boolean isUserAvailable(String nickname);
}
