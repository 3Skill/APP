package com.example.kadamm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private ArrayList<String> infoConcurs;
    private int questionTimer;
    private ArrayList<String> answers;
    private ArrayList<String> respostes;
    private String nickname;
    private String serverIP;
    private boolean isBtnAnswerPressed;
    private int questionIterator;
    private @ColorInt int greyColor;
    private @ColorInt int colorBtn1;
    private @ColorInt int colorBtn2;
    private @ColorInt int colorBtn3;
    private @ColorInt int colorBtn4;
    private  long timeElapsed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        timeElapsed = SystemClock.currentThreadTimeMillis();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.respostes_kahoot);

        // Proceed to establish connection to the server
        //connectToServer();

        //     0                 1                        2                  3,4,5,6
        // Pregunta, cuenta atras inicio concurso, cuentra atras respuesta, preguntas
        // Get intent [¿Cual en?, 3, 10, Los romanos, Las pitones, Los egipcios, Monty Python]

        nickname = getIntent().getStringExtra("nickname");
        serverIP = getIntent().getStringExtra("serverIP");
        questionIterator = getIntent().getIntExtra("questionIterator", 0);
        respostes = getIntent().getStringArrayListExtra("arrayListRespostes");
        counter = Integer.parseInt(respostes.get(2));
        lengthAnswers = respostes.size();

        countdown = findViewById(R.id.tvCountdown);
        btn1 = findViewById(R.id.btnAnswer1);
        btn2 = findViewById(R.id.btnAnswer2);
        btn3 = findViewById(R.id.btnAnswer3);
        btn4 = findViewById(R.id.btnAnswer4);

        // Generate Colors
        //Grey Color
        greyColor = Color.parseColor("#A9A9A9");
        colorBtn1 = Color.parseColor("#03A9F4");
        colorBtn2 = Color.parseColor("#F44336");
        colorBtn3 = Color.parseColor("#FFEB3B");
        colorBtn4 = Color.parseColor("#4CAF50");

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

        counter = counter - Integer.parseInt(String.valueOf(String.valueOf((timeElapsed)).charAt(0)));

        System.out.println("------------------------------------------++++"+counter);
        new CountDownTimer((counter*1000),1000) {
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


                    // Change the buttons Colors to look disabled
                    btn1.setBackgroundTintList(ColorStateList.valueOf(greyColor));
                    btn2.setBackgroundTintList(ColorStateList.valueOf(greyColor));
                    btn3.setBackgroundTintList(ColorStateList.valueOf(greyColor));
                    btn4.setBackgroundTintList(ColorStateList.valueOf(greyColor));
//                btn4.setVisibility(View.GONE);
                    sendAnswerToServer(new ArrayList<String>());
                }
                countdown.setText("");
                questionIterator++;
                getServerQuestion();
            }
        }.start();


    }

    public void chooseAnswer(View v) {


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
                btn2.setBackgroundTintList(ColorStateList.valueOf(greyColor));
                btn3.setBackgroundTintList(ColorStateList.valueOf(greyColor));
                btn4.setBackgroundTintList(ColorStateList.valueOf(greyColor));

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
                btn1.setBackgroundTintList(ColorStateList.valueOf(greyColor));
                btn3.setBackgroundTintList(ColorStateList.valueOf(greyColor));
                btn4.setBackgroundTintList(ColorStateList.valueOf(greyColor));

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
                btn1.setBackgroundTintList(ColorStateList.valueOf(greyColor));
                btn2.setBackgroundTintList(ColorStateList.valueOf(greyColor));
                btn4.setBackgroundTintList(ColorStateList.valueOf(greyColor));

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
                btn1.setBackgroundTintList(ColorStateList.valueOf(greyColor));
                btn2.setBackgroundTintList(ColorStateList.valueOf(greyColor));
                btn3.setBackgroundTintList(ColorStateList.valueOf(greyColor));

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

                try {
                    CallHandler callHandler = new CallHandler();
                    int port = 1110;
                    Client client = new Client(serverIP, port, callHandler);
                    InterRMI testService = (InterRMI) client.getGlobal(InterRMI.class);
                    if (nicknameAnswer != null){
                        if(testService.setUserAnswer(nicknameAnswer)){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(RespostesKahoot.this, "Resposta enviada", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(RespostesKahoot.this, "Resposta no enviada", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }
                    testService.setWaitingRoom2Status(false);
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    public void getServerQuestion(){
        new Thread(new Runnable() {


            @Override
            public void run() {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    CallHandler callHandler = new CallHandler();
                    int port = 1110;
                    Client client = new Client(serverIP, port, callHandler);
                    InterRMI testService = (InterRMI) client.getGlobal(InterRMI.class);

                    if (testService.getWaitingRoom2Status()){
                        Log.d("Get Server question", "question iterator: "+ questionIterator);
                        infoConcurs = testService.getConcurs(questionIterator);
                        Log.d("Get server question", "infoConcurs: "+infoConcurs.toString());
                        answers = new ArrayList<String>();
                        try {
                            questionTimer = Integer.parseInt(infoConcurs.get(2));
                            for (int i = 3;i< infoConcurs.size();i++){
                                answers.add(infoConcurs.get(i));}
                        }catch (Exception e){
                            // The infoConcurs ArrayList is empty, Return to the MainActivity Spec 33
                            e.printStackTrace();
                            client.close();
                            Intent i = new Intent(RespostesKahoot.this, MainActivity.class);
                            startActivity(i);
                        }



                        Log.d("Get server question", "answers: "+answers.toString());
                        updateActivity();

                    }else {
                        client.close();
                        getServerQuestion();
                    }
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();


                }

            }
        }).start();
    }



    public void updateActivity() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timeElapsed = SystemClock.currentThreadTimeMillis();
                // Restart the button colors
                // Change the buttons Colors to look disabled
                btn1.setBackgroundTintList(ColorStateList.valueOf(colorBtn1));
                btn2.setBackgroundTintList(ColorStateList.valueOf(colorBtn2));
                btn3.setBackgroundTintList(ColorStateList.valueOf(colorBtn3));
                btn4.setBackgroundTintList(ColorStateList.valueOf(colorBtn4));

                // Enable the buttons
                btn1.setEnabled(true);
                btn2.setEnabled(true);
                btn3.setEnabled(true);
                btn4.setEnabled(true);

                isBtnAnswerPressed = false;
                lengthAnswers = answers.size();

                // Change the button text
                if (lengthAnswers == 2) {
                    btn1.setText(answers.get(0));
                    btn2.setText(answers.get(1));
                    btn3.setVisibility(View.GONE);
                    btn4.setVisibility(View.GONE);
                } else if (lengthAnswers == 3) {
                    btn1.setText(answers.get(0));
                    btn2.setText(answers.get(1));
                    btn3.setText(answers.get(2));
                    btn4.setVisibility(View.GONE);
                } else if (lengthAnswers == 4) {
                    btn1.setText(answers.get(0));
                    btn2.setText(answers.get(1));
                    btn3.setText(answers.get(2));
                    btn4.setText(answers.get(3));
                }

                questionTimer = questionTimer - Integer.parseInt(String.valueOf(String.valueOf((timeElapsed)).charAt(0)));
                new CountDownTimer(questionTimer*1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        countdown.setText(String.valueOf(questionTimer));
                        questionTimer--;
                    }

                    @Override
                    public void onFinish() {

                        if (!isBtnAnswerPressed) {
                            // Disable the buttons
                            btn1.setEnabled(false);
                            btn2.setEnabled(false);
                            btn3.setEnabled(false);
                            btn4.setEnabled(false);



                            // Change the buttons Colors to look disabled
                            btn1.setBackgroundTintList(ColorStateList.valueOf(greyColor));
                            btn2.setBackgroundTintList(ColorStateList.valueOf(greyColor));
                            btn3.setBackgroundTintList(ColorStateList.valueOf(greyColor));
                            btn4.setBackgroundTintList(ColorStateList.valueOf(greyColor));
                            sendAnswerToServer(new ArrayList<String>());
//                btn4.setVisibility(View.GONE);
                        }
                        countdown.setText("");
                        questionIterator++;
                        getServerQuestion();
                    }
                }.start();

            }

        });
    }
}
