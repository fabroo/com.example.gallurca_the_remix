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


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String MESSAGE = "USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void navigateRegister(View view) {
        Intent intent = new Intent(MainActivity.this, Register.class);
        startActivity(intent);
    }

    public void login(View view) {
        mAuth = FirebaseAuth.getInstance();

        EditText mailET = findViewById(R.id.loginMail);
        EditText pswET = findViewById(R.id.loginPsw);

        String mail = mailET.getText().toString();
        String psw = pswET.getText().toString();

        Log.d("mail", mail);
        Log.d("password", psw);

        if (mail.isEmpty() || psw.isEmpty()){
            Toast.makeText(MainActivity.this, "parametros vacios",
                    Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(mail, psw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //System.out.println(user);
                            System.out.println("Success!");
                            String email = user.getEmail();
                            Intent intent = new Intent(MainActivity.this, Casa.class);
                            intent.putExtra(MESSAGE, email);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "El email o contraseña son incorrectos",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}