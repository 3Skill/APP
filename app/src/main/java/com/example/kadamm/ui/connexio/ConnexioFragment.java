package com.example.kadamm.ui.connexio;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;

import com.example.kadamm.MainActivity;
import com.example.kadamm.R;
import com.example.kadamm.databinding.FragmentConnexioBinding;


import java.io.IOException;

import lipermi.handler.CallHandler;
import lipermi.net.Client;


public class ConnexioFragment extends Fragment {


    private ConnexioViewModel connexioViewModel;
    private FragmentConnexioBinding binding;
    private String serverIP = null;
    //private String serverIP = "192.168.0.176";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_connexio, container, false);
        Button button = (Button) v.findViewById(R.id.btnConnecta);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText et1 = (EditText) v.findViewById(R.id.ipUsuari);
                String texto = et1.getText().toString();
                //System.out.println(texto.isEmpty());
                serverIP = texto;
                new Conn().execute();
            }
        });


//        final TextView textView = binding.textHome;
//        connexioViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    class Conn extends AsyncTask<Void, Void, MainActivity> {


        protected MainActivity doInBackground(Void... params) {
            Looper.prepare();
            try {
                CallHandler callHandler = new CallHandler();

                Client client = new Client(serverIP, 1110, callHandler);
               // TestService testService = (TestService) client.getGlobal(TestService.class);
                Toast.makeText(getContext(), "Servidor disponible", Toast.LENGTH_SHORT).show();

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
