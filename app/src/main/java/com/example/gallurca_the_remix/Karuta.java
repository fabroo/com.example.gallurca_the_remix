package com.example.gallurca_the_remix;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Karuta extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ArrayList<ImageView> views;
    private boolean caught;

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    public void flipBack() {
        int flip = R.drawable.img004;
        for (ImageView v : views){
            v.setImageResource(flip);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        caught = true;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karuta);
        Button roll = (Button) findViewById(R.id.roll_button);
        int[] images = {R.drawable.boca_de_dama, R.drawable.frutigran_violeta, R.drawable.oreos, R.drawable.pepitos, R.drawable.pitusas_chocolate};

        views = new ArrayList<>();
        views.add(findViewById(R.id.foto_1));
        views.add(findViewById(R.id.foto_2));

        roll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(caught){
                    caught = false;
                    Log.d("View", Integer.toString(views.size()));
                    for(ImageView v : views){
                        Log.d("View", Integer.toString(v.getId()));
                        int random = getRandomNumber(0,images.length);
                        v.setImageResource(images[random]);
                        v.setOnClickListener( new View.OnClickListener(){
                            @Override
                            public void onClick(View view){

                                if(!caught) {
                                    caught = true;

                                    String picname = getResources().getResourceName(images[random]).split("/")[1];
                                    picname = picname.substring(0, 1).toUpperCase() + picname.substring(1);

                                    int quality = getRandomNumber(1,4);

                                    Map<String, Object> data = new HashMap<>();
                                    data.put("nombre", picname);
                                    data.put("imageid", images[random]);
                                    data.put("quality", quality);

                                    db.collection("users").document(user.getUid()).collection("cookies").add(data);
                                    Toast.makeText(Karuta.this, "Agarraste " + picname + "! Calidad: " + quality +"★", Toast.LENGTH_LONG).show();
                                    flipBack();
                                }
                            }
                        });
                    }
                }
            }
        });
    }


}
