package com.example.gallurca_the_remix;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//import android.support.v7.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String MESSAGE = "user";

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
        Log.d("msg","inside");
        System.out.println("aaa");
        mAuth = FirebaseAuth.getInstance();
        EditText mailET = findViewById(R.id.registerMail);
        EditText pswET = findViewById(R.id.registerPsw);
        EditText pswET2 = findViewById(R.id.registerPsw2);

        String mail = mailET.getText().toString();
        String psw = pswET.getText().toString();
        String psw2 = pswET.getText().toString();

        if (mail.isEmpty() || psw.isEmpty() || psw2.isEmpty()){
            Toast.makeText(Register.this, "parametros vacios",
                    Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(mail, psw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            System.out.println("Success!");
                            String email = user.getEmail();
                            Intent intent = new Intent(Register.this, Casa.class);
                            intent.putExtra(MESSAGE, email);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Register.this, "Fallo al crear cuenta.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}