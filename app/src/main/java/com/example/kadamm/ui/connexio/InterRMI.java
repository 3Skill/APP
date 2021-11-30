package com.example.kadamm.ui.connexio;

import java.io.Serializable;
import java.util.ArrayList;

public interface InterRMI {

    public void setNickName(String nickname);
    public boolean getWaitingRoomStatus();
    public boolean getWaitingRoom2Status();
    public boolean isUserAvailable(String nickname);
    public ArrayList<String> getConcurs(int i);
    public void setUserAnswer(ArrayList<String> nicknameAnswer);
}
