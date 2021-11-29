package com.example.kadamm.ui.connexio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import lipermi.handler.CallHandler;
import lipermi.net.Client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.example.kadamm.R;
import com.example.kadamm.databinding.FragmentConnexioBinding;

import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnexioFragment extends Fragment {

    private FragmentConnexioBinding binding;
    private EditText serverIP;
    private String nickname = "";
    private TextView tvNickName;
    private ImageView trafficLight;
    private boolean isServerAvailable;
    private boolean isNickName;

//    private InterRMI testService;
    private View v;
    Button btnConnection;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_connexio, container, false);
        tvNickName  = (TextView) v.findViewById(R.id.tvEmptyNickname);
        btnConnection = (Button) v.findViewById(R.id.btnConnecta);
        serverIP = (EditText) v.findViewById(R.id.ipUsuari);
        trafficLight = (ImageView) v.findViewById(R.id.imgViewSemafor);

        btnConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Load the nickname from SharedPreferences
                loadData();

                // Test the IP connection
                testServerConnection(serverIP.getText().toString());

                // Check if the user entered a nickName
                if (nickname.isEmpty()) {
                    tvNickName.setText("Introdueix un nickname");
                    isNickName = false;
                }else{
                    isNickName = true;
                }

                // Test if there's some Waiting Room available
                searchWaitingRoom(serverIP.getText().toString());

            }
        });

        return v;
    }

    // Test the Connection to the server
    public void testServerConnection(String serverIP){

        new Thread(new Runnable() {

            @Override
            public void run() {

                Socket socket;
                final int port = 1110;
                final int timeout = 2000;   // 2 seconds

                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(serverIP, port), timeout);
                    isServerAvailable = true;
                }
                catch (Exception e){
                    e.printStackTrace();
                    isServerAvailable = false;
                }

                // Change the UI, traffic lights
                displayServerStatus(isServerAvailable);

            }
        }).start();

    }

    // Displays toast with Connection Status to the server and change the traffic light image
    public void displayServerStatus(boolean bool){

        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bool){
                    Toast.makeText(getContext(), "Servidor disponible", Toast.LENGTH_SHORT).show();
                    trafficLight.setImageResource(R.drawable.traffic_light_orange);
                }else {
                    Toast.makeText(v.getContext(), "Error en la connexió", Toast.LENGTH_SHORT).show();
                    trafficLight.setImageResource(R.drawable.traffic_light_red);
                }
            }
        });
    }

    // Search if there's any Waiting Room available
    public void searchWaitingRoom(String serverIP){

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    CallHandler callHandler = new CallHandler();
                    int port = 1110;
                    Client client = new Client(serverIP, port, callHandler);
                    InterRMI testService = (InterRMI) client.getGlobal(InterRMI.class);

                    if (testService.getWaitingRoomStatus()){
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                trafficLight.setImageResource(R.drawable.traffic_light_green);
                                Toast.makeText(requireActivity(), "Sala Disponible", Toast.LENGTH_SHORT).show();
                                if (isNickName){
                                    registerNickName(serverIP);
                                }
                            }
                        });
                    }
                    else{ // Loop 3 seconds trying to look for a Waiting Room
                        searchWaitingRoom(serverIP);
                    }

                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        }).start();

    }

    // Tries to register the user in the Waiting Room
    public void registerNickName(String serverIP){

        new Thread(new Runnable() {

            @Override
            public void run() {

                // Registration attempts every 3 seconds
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    CallHandler callHandler = new CallHandler();
                    int port = 1110;
                    Client client = new Client(serverIP, port, callHandler);
                    InterRMI testService = (InterRMI) client.getGlobal(InterRMI.class);

                    // Check if nickname not already in the waiting room
                    if (testService.isUserAvailable(nickname)){
                        // Register the user to the waiting room with the nickname
                        testService.setNickName(nickname);
                        // The nickname is correctly added
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(requireActivity(), "Usuari registrat", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }else { // There's a conflict with duplicated nickname
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showDialog();
                            }
                        });

                    }

                    client.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }

    // Load the Shared Prefereneces with the Nickname created in the UsuariFragment class
    public void loadData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("spNickname", Context.MODE_PRIVATE);
        nickname = sharedPreferences.getString("nickname", "");

    }

    // Dialog if there's conflict with duplicated nicknames in the same waiting room
    public void showDialog(){
        EditText taskEditText = new EditText(requireActivity());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireActivity());
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("COL·LISIO");
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("El nickname "+ nickname + " ja s'esta utilitzant en la sala.");
        // Non closing alert
        alertDialogBuilder.setCancelable(false);
        // Add editText to the Dialog
        alertDialogBuilder.setView(taskEditText);
        alertDialogBuilder.setPositiveButton("D'acord", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                String newNickname = taskEditText.getText().toString();
                if (!newNickname.isEmpty() & !newNickname.equals(nickname)){
                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("spNickname", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("nickname", newNickname);
                    editor.apply();
                    loadData();
                    try {
                        finalize();
                        registerNickName(serverIP.getText().toString());
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
                else{
                    showDialog();
                }

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}