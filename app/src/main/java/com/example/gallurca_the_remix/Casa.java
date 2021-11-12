package com.example.gallurca_the_remix;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import android.support.v7.app.AppCompatActivity;

public class Casa extends AppCompatActivity {
    private TextView timerTxt;
    private long startTime;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseUser;
    private FirebaseUser user;
    private TextView mainText;

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
        String uid = user.getUid();
        Log.d("UID", uid);
        ArrayList<String> time = new ArrayList<String>();
        time.add(timerTxt.getText().toString());

        Map<String, Object> map = new HashMap<>();
        map.put("lastTimes", FieldValue.arrayUnion(time));

        Log.d("mapa", map.toString());

        db.collection("users").document(uid).update("lastTimes", FieldValue.arrayUnion(timerTxt.getText().toString()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Casa.this, "SAVED", Toast.LENGTH_LONG).show();

                    }
                });



        }
    public void guardados(View view){
        String uid = user.getUid();

        db.collection("users").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshots) {
                        Toast.makeText(Casa.this, documentSnapshots.get("lastTimes").toString(), Toast.LENGTH_LONG).show();
                    }});
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance();
        user = firebaseUser.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casa);
        timerTxt =  (TextView) findViewById(R.id.timerTxt);
        mainText = (TextView) findViewById(R.id.mainTxt);

        db.collection("users").document(user.getUid()).get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshots) {
                    mainText.setText("El timer de "+documentSnapshots.get("apodo").toString());
                }});

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