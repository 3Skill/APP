package com.example.kadamm.ui.connexio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.os.Parcelable;
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

import com.example.kadamm.MainActivity;
import com.example.kadamm.R;
import com.example.kadamm.RespostesKahoot;
import com.example.kadamm.databinding.FragmentConnexioBinding;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ConnexioFragment extends Fragment {

    private FragmentConnexioBinding binding;
    private EditText serverIP;
    private String nickname = "";
    private TextView tvNickName;
    private ImageView trafficLight;
    private boolean isServerAvailable;
    private boolean isNickName;

    private int questionIterator = 0;
    private View v;
    Button btnConnection;

    // Server Attributes
    //private InterRMI testService;
    //Atributos recibidos
    private String tempsResposta;
    private int tempsIniciConcurs;
    private String pregunta;
    private ArrayList<String> respostes;
    private ArrayList<String> infoConcurs;


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
                                getAttributes(serverIP);
                                checkStatusRoom(serverIP);
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
    // Method for get all atributes of the actual position of the concurs
    public void getAttributes(String serverIP){

        new Thread(new Runnable() {
            @Override
            public void run() {
                //POLL de 2 segundos
                try {
                    Thread.sleep(2000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    CallHandler callHandler = new CallHandler();
                    int port = 1110;
                    Client client = new Client(serverIP, port, callHandler);
                    InterRMI testService = (InterRMI) client.getGlobal(InterRMI.class);

                    infoConcurs = testService.getConcurs(questionIterator);
                    respostes = new ArrayList<String>();
                    pregunta = infoConcurs.get(0);
                    tempsIniciConcurs = Integer.parseInt(infoConcurs.get(1));
                    tempsResposta = infoConcurs.get(2);
                    for (int i = 3;i< infoConcurs.size();i++){
                        respostes.add(infoConcurs.get(i));
                    }

                    System.out.println("++++++++++++Datos recogido++++++++++++\n"+infoConcurs.toString());
                    client.close();
                } catch (Exception e) {
                    System.out.println("---------------------------------------------");
                    e.printStackTrace();
                }

            }
        }).start();

    }
    private Thread hilo;
    public void checkStatusRoom(String serverIP){

        new Thread(new Runnable() {
            @Override
            public void run() {
                //POLL de 2 segundos
                try {
                    Thread.sleep(500);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    CallHandler callHandler = new CallHandler();
                    int port = 1110;
                    Client client = new Client(serverIP, port, callHandler);
                    InterRMI testService = (InterRMI) client.getGlobal(InterRMI.class);
                    if (testService.getWaitingRoom2Status()){

//                        try {
//                            //Aqui hay que editar el timer
//                            Thread.sleep(tempsIniciConcurs);
//                            System.out.println("Dentro del concurso");
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                        System.out.println("CUENTA ATRAS:");
                        System.out.println("Hilo acabado");

                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ProgressDialog progressDialog = new ProgressDialog(requireActivity());
                                progressDialog.setTitle("Concurs");
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.setMessage("Iniciant...");
                                progressDialog.show();
                                progressDialog.setCancelable(false);

                                new CountDownTimer(tempsIniciConcurs*1000,1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        //countdown.setText(String.valueOf(counter));
                                        System.out.println(tempsIniciConcurs);
                                        tempsIniciConcurs--;
                                    }
                                    @Override
                                    public void onFinish() {
                                        System.out.println("DENTRO");
                                        progressDialog.dismiss();
                                        // Create Intent
                                        Intent intent = new Intent(requireActivity(), RespostesKahoot.class);
                                        intent.putExtra("arrayListRespostes", infoConcurs);
                                        intent.putExtra("nickname", nickname);
                                        intent.putExtra("serverIP", serverIP);
                                        intent.putExtra("questionIterator", questionIterator);
                                        startActivity(intent);

                                        //countdown.setText("Finished");
                                    }
                                }.start();
                            }
                        });


                    }else{
                        //System.out.println("No ha comenzado el concurso");

                        checkStatusRoom(serverIP);
                    }
                    testService.setWaitingRoom2Status(false);
                    client.close();
                } catch (Exception e) {
                    System.out.println("---------------------------------------------");
                    e.printStackTrace();
                }

            }
        }).start();


    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}