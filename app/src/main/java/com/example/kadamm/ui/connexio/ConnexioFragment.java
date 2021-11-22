package com.example.kadamm.ui.connexio;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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

import androidx.fragment.app.Fragment;


import com.example.kadamm.MainActivity;
import com.example.kadamm.R;
import com.example.kadamm.databinding.FragmentConnexioBinding;


import java.io.IOException;

import lipermi.handler.CallHandler;
import lipermi.net.Client;


public class ConnexioFragment extends Fragment {


    private ConnexioViewModel connexioViewModel;
    private FragmentConnexioBinding binding;
    private String serverIP = "";
    private Handler handler;
    private String nickname = "";
    private TextView tvNickName;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        handler = new Handler(Looper.getMainLooper());
        View v = inflater.inflate(R.layout.fragment_connexio, container, false);
        tvNickName  = (TextView)v.findViewById(R.id.tvEmptyNickname);
        Button button = (Button) v.findViewById(R.id.btnConnecta);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Load the nickname from SharedPreferences
                loadData();
                // Check if the username is empty
                if (nickname.isEmpty()) {
                    tvNickName.setText("Introdueix un nickname");
                }
                else{ // Test the connection with the server
                    EditText et1 = (EditText) v.findViewById(R.id.ipUsuari);
                    String texto = et1.getText().toString();

                    serverIP = texto;
                    new Conn().execute();
                }

            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Load the Shared Prefereneces with the Nickname created in the UsuariFragment class
    public void loadData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("spNickname", Context.MODE_PRIVATE);
        nickname = sharedPreferences.getString("nickname", "");

    }
    class Conn extends AsyncTask<Void, Void, MainActivity> {


        protected MainActivity doInBackground(Void... params) {
            Looper.prepare();
            try {

                CallHandler callHandler = new CallHandler();
                Client client = new Client(serverIP, 1110, callHandler);
                InterRMI testService = (InterRMI) client.getGlobal(InterRMI.class);
                Log.d("lksdjf", nickname);
                testService.setNickName(nickname);
                Toast.makeText(getContext(), "Servidor disponible", Toast.LENGTH_LONG).show();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ImageView tuImageView = (ImageView) getView().findViewById(R.id.imgViewSemafor);
                        tuImageView.setImageResource(R.drawable.traffic_light_orange);
                    }
                });

                System.out.println("Se ha conectado correctamente");
                client.close();
            } catch (IOException e) {
                System.out.println("No se ha conectado correctamente");
            }
            Looper.loop();
            return null;
        }

    }


}




