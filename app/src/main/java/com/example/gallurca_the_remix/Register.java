package com.example.gallurca_the_remix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

//import android.support.v7.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String MESSAGE = "user";
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void navigateLogin(View view){
        Intent intent = new Intent(Register.this, MainActivity.class);
        startActivity(intent);
    }

    public void registrar(View view){

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        EditText mailET = findViewById(R.id.registerMail);
        EditText pswET = findViewById(R.id.registerPsw);
        EditText apodoET = findViewById(R.id.registerApodo);


        String mail = mailET.getText().toString();
        String psw = pswET.getText().toString();
        String apodo = apodoET.getText().toString();

        if (mail.isEmpty() || psw.isEmpty() || apodo.isEmpty()){
            Toast.makeText(Register.this, "parametros vacios",
                    Toast.LENGTH_LONG).show();
            return;
        }

        Map<String, Object> docData = new HashMap<>();
        docData.put("apodo", apodo);

        mAuth.createUserWithEmailAndPassword(mail, psw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();

                            db.collection("users").document(uid).set(docData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent intent = new Intent(Register.this, Casa.class);
                                            startActivity(intent);
                                        }
                                    });


                        } else {
                            Toast.makeText(Register.this, "Fallo al crear cuenta.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}