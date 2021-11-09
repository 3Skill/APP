package com.example.kadamm.ui.connexio;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.kadamm.MainActivity;
import com.example.kadamm.databinding.FragmentConnexioBinding;

import java.io.IOException;

import lipermi.handler.CallHandler;
import lipermi.net.Client;


public class ConnexioFragment extends Fragment {

    private ConnexioViewModel connexioViewModel;
    private FragmentConnexioBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        connexioViewModel =
                new ViewModelProvider(this).get(ConnexioViewModel.class);

        binding = FragmentConnexioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textHome;
//        connexioViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    class Conn extends AsyncTask<Void, Void, MainActivity> {


        protected MainActivity doInBackground(String ip) {
            Looper.prepare();
            try {
                CallHandler callHandler = new CallHandler();
                String serverIP;
                Client client = new Client(ip, 7777, callHandler);
               // TestService testService = (TestService) client.getGlobal(TestService.class);


                System.out.println("Se ha conectado correctamente");
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Looper.loop();
            return null;
        }

    }
}
