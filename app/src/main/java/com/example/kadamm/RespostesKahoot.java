package com.example.kadamm;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RespostesKahoot extends AppCompatActivity {
    private TextView countdown;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private int counter;
    private int lengthAnswers;
    private boolean isFirstQuestion;
    private ArrayList<String> respostes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.respostes_kahoot);

        //     0                 1                        2                  3,4,5,6
        // Pregunta, cuenta atras inicio concurso, cuentra atras respuesta, preguntas
        // Get intent [¿Cual en?, 3, 10, Los romanos, Las pitones, Los egipcios, Monty Python]

        isFirstQuestion = getIntent().getBooleanExtra("isFirstQuestion", true);

        if (isFirstQuestion){
            respostes = getIntent().getStringArrayListExtra("arrayListRespostes");
            counter = Integer.parseInt(respostes.get(2));
            lengthAnswers = respostes.size();

            countdown = findViewById(R.id.tvCountdown);
            counter = Integer.parseInt(countdown.getText().toString());
            btn1 = findViewById(R.id.btnAnswer1);
            btn2 = findViewById(R.id.btnAnswer2);
            btn3 = findViewById(R.id.btnAnswer3);
            btn4 = findViewById(R.id.btnAnswer4);

            // Change the button text
            if (lengthAnswers == 5){
                btn1.setText(respostes.get(3));
                btn2.setText(respostes.get(4));
                btn3.setVisibility(View.GONE);
                btn4.setVisibility(View.GONE);
            }else if(lengthAnswers == 6){
                btn1.setText(respostes.get(3));
                btn2.setText(respostes.get(4));
                btn3.setText(respostes.get(5));
                btn4.setVisibility(View.GONE);
            }
            else if(lengthAnswers == 7){
                btn1.setText(respostes.get(3));
                btn2.setText(respostes.get(4));
                btn3.setText(respostes.get(5));
                btn4.setText(respostes.get(6));
            }
        }




        new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdown.setText(String.valueOf(counter));
                counter--;
            }
            @Override
            public void onFinish() {
                // Disable the buttons
                btn1.setEnabled(false);
                btn2.setEnabled(false);
                btn3.setEnabled(false);
                btn4.setEnabled(false);

                //Grey Color
                @ColorInt int color = Color.parseColor("#A9A9A9");

                // Change the buttons Colors to look disabled
                btn1.setBackgroundTintList(ColorStateList.valueOf(color));
                btn2.setBackgroundTintList(ColorStateList.valueOf(color));
                btn3.setBackgroundTintList(ColorStateList.valueOf(color));
                btn4.setBackgroundTintList(ColorStateList.valueOf(color));
//                btn4.setVisibility(View.GONE);

                countdown.setText("");
            }
        }.start();


    }
}
