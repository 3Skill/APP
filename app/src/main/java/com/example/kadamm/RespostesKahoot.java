package com.example.kadamm;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kadamm.ui.connexio.InterRMI;

import java.io.IOException;
import java.util.ArrayList;

import lipermi.handler.CallHandler;
import lipermi.net.Client;

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
    private String nickname;
    private String serverIP;
    private boolean isBtnAnswerPressed;
    //private Client client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.respostes_kahoot);

        // Proceed to establish connection to the server
        //connectToServer();

        //     0                 1                        2                  3,4,5,6
        // Pregunta, cuenta atras inicio concurso, cuentra atras respuesta, preguntas
        // Get intent [¿Cual en?, 3, 10, Los romanos, Las pitones, Los egipcios, Monty Python]

        isFirstQuestion = getIntent().getBooleanExtra("isFirstQuestion", true);

        nickname = getIntent().getStringExtra("nickname");

        if (isFirstQuestion){
            respostes = getIntent().getStringArrayListExtra("arrayListRespostes");
            counter = Integer.parseInt(respostes.get(2));
            lengthAnswers = respostes.size();

            countdown = findViewById(R.id.tvCountdown);
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

        new CountDownTimer(counter*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdown.setText(String.valueOf(counter));
                counter--;
            }
            @Override
            public void onFinish() {

                if (!isBtnAnswerPressed) {
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
                }
                countdown.setText("");
            }
        }.start();


    }

    public void chooseAnswer(View v) {
        //Grey Color
        @ColorInt int color = Color.parseColor("#A9A9A9");

        String answer;
        // Add the nickname to the Arraylist
        ArrayList<String> answerArrayList = new ArrayList<String>();
        answerArrayList.add(nickname);
        switch(v.getId())
        {
            case R.id.btnAnswer1:

                //Change variable to true
                isBtnAnswerPressed = true;

                // Save the data of the answer in the ArrayList
                answer = btn1.getText().toString();
                answerArrayList.add(answer);
                Log.d("ArrayButton", String.valueOf(answerArrayList));

                // Disable the buttons
                btn1.setEnabled(false);
                btn2.setEnabled(false);
                btn3.setEnabled(false);
                btn4.setEnabled(false);

                // Change the buttons Colors to look disabled
                btn2.setBackgroundTintList(ColorStateList.valueOf(color));
                btn3.setBackgroundTintList(ColorStateList.valueOf(color));
                btn4.setBackgroundTintList(ColorStateList.valueOf(color));

                // Send data of the answer and nickname
                sendAnswerToServer(answerArrayList);

                break;
            case R.id.btnAnswer2:

                //Change variable to true
                isBtnAnswerPressed = true;

                // Save the data of the answer in the ArrayList
                answer = btn2.getText().toString();
                answerArrayList.add(answer);
                Log.d("ArrayButton", String.valueOf(answerArrayList));

                // Disable the buttons
                btn1.setEnabled(false);
                btn2.setEnabled(false);
                btn3.setEnabled(false);
                btn4.setEnabled(false);

                // Change the buttons Colors to look disabled
                btn1.setBackgroundTintList(ColorStateList.valueOf(color));
                btn3.setBackgroundTintList(ColorStateList.valueOf(color));
                btn4.setBackgroundTintList(ColorStateList.valueOf(color));

                // Send data of the answer and nickname
                sendAnswerToServer(answerArrayList);
                break;

            case R.id.btnAnswer3:

                //Change variable to true
                isBtnAnswerPressed = true;

                // Save the data of the answer in the ArrayList
                answer = btn3.getText().toString();
                answerArrayList.add(answer);
                Log.d("ArrayButton", String.valueOf(answerArrayList));


                // Disable the buttons
                btn1.setEnabled(false);
                btn2.setEnabled(false);
                btn3.setEnabled(false);
                btn4.setEnabled(false);

                // Change the buttons Colors to look disabled
                btn1.setBackgroundTintList(ColorStateList.valueOf(color));
                btn2.setBackgroundTintList(ColorStateList.valueOf(color));
                btn4.setBackgroundTintList(ColorStateList.valueOf(color));

                // Send data of the answer and nickname
                sendAnswerToServer(answerArrayList);

                break;
            case R.id.btnAnswer4:

                //Change variable to true
                isBtnAnswerPressed = true;

                // Save the data of the answer in the ArrayList
                answer = btn4.getText().toString();
                answerArrayList.add(answer);
                Log.d("ArrayButton", String.valueOf(answerArrayList));

                // Disable the buttons
                btn1.setEnabled(false);
                btn2.setEnabled(false);
                btn3.setEnabled(false);
                btn4.setEnabled(false);

                // Change the buttons Colors to look disabled
                btn1.setBackgroundTintList(ColorStateList.valueOf(color));
                btn2.setBackgroundTintList(ColorStateList.valueOf(color));
                btn3.setBackgroundTintList(ColorStateList.valueOf(color));

                // Send data of the answer and nickname
                sendAnswerToServer(answerArrayList);

                break;

            default:
                throw new RuntimeException("Unknow button ID");
        }

    }

    public void sendAnswerToServer(ArrayList<String> nicknameAnswer){
        new Thread(new Runnable() {
            @Override
            public void run() {
                serverIP = getIntent().getStringExtra("serverIP");

                try {
                    CallHandler callHandler = new CallHandler();
                    int port = 1110;
                    Client client = new Client(serverIP, port, callHandler);
                    InterRMI testService = (InterRMI) client.getGlobal(InterRMI.class);
                    testService.setUserAnswer(nicknameAnswer);
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
