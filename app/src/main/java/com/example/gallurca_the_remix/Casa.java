package com.example.gallurca_the_remix;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import android.support.v7.app.AppCompatActivity;

public class Casa extends AppCompatActivity {
    TextView timerTxt;
    private long startTime;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTxt.setText(String.format("%d:%02d:%02d", minutes, seconds,millis));

            timerHandler.postDelayed(this, 100);
        }
    };

    public void guardar(View view){
        String mensaje = "Tiempo: " + timerTxt.getText().toString();
        Toast.makeText(Casa.this, mensaje, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casa);
        timerTxt =  (TextView) findViewById(R.id.timerTxt);
        timerTxt.setText("Press start");
        Button b = (Button) findViewById(R.id.startBtn);
        b.setText("START");
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals("STOP")) {
                    timerHandler.removeCallbacks(timerRunnable);
                    b.setText("REINICIAR");
                } else {
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                    b.setText("STOP");
                }
            }

        });

    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        Button b = (Button)findViewById(R.id.startBtn);
        b.setText("START");
    }
    public void logout(View view) {

        Intent intent = new Intent(Casa.this, MainActivity.class);
        startActivity(intent);

    }
}