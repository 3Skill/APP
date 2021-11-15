package com.example.kadamm.ui.usuari;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.kadamm.R;
import com.example.kadamm.databinding.FragmentUsuariBinding;

public class UsuariFragment extends Fragment {

    private UsuariViewModel usuariViewModel;
    private FragmentUsuariBinding binding;
    private TextView nickNameTitle;
    private EditText nickName;
    private Button saveNickName;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";

    private String text;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        usuariViewModel =
                new ViewModelProvider(this).get(UsuariViewModel.class);

        binding = FragmentUsuariBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textDashboard;
//        usuariViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        nickNameTitle = (TextView) root.findViewById(R.id.tvNickname);
        nickName = (EditText) root.findViewById(R.id.etNickname);
        saveNickName = (Button) root.findViewById(R.id.btnSaveNickname);

        saveNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickNameTitle.setText(nickName.getText());
                saveData();
            }
        });

        loadData();
        updateViews();

        return root;
    }

    public void saveData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT, nickNameTitle.getText().toString());

        editor.apply();

        Toast.makeText(this.getActivity(), "Usuari Guardat", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "");

    }

    public void updateViews() {
        nickNameTitle.setText(text);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}