package com.example.fitbrov2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
{
    private Button loginButton;
    private TextView registration, forgotPassword;
    private EditText email, password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        email = (EditText)findViewById(R.id.editText8);
        password = (EditText)findViewById(R.id.editText9);
        loginButton = (Button)findViewById(R.id.btnReset);
        registration = (TextView) findViewById(R.id.textView5);
        forgotPassword = (TextView) findViewById(R.id.textView7);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Registration();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotPassword();
            }
        });

    }

    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        System.exit(0);
    }

    public void login()
    {
        String emailCheck = email.getText().toString().trim();
        String passwordCheck = password.getText().toString().trim();

        if(emailCheck.isEmpty()){
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }

        if(passwordCheck.isEmpty()){
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailCheck).matches()){
            email.setError("Please provide a valid email!");
            email.requestFocus();
            return;
        }
        if(passwordCheck.length() < 6){
            password.setError("The minimum password length is 6 characters!");
            password.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(emailCheck,passwordCheck).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    //redirect to the home page
                    RedirectHomePage();

                }else{
                    Toast.makeText(getApplicationContext(), "Something wrong with the email or password, please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void Registration()
    {
        Intent intent = new Intent(this, registration.class);
        startActivity(intent);
    }

    public void RedirectHomePage(){
        Intent intent = new Intent(this, BottomNavigation.class);
        startActivity(intent);
    }

    public void ForgotPassword(){
        Intent intent = new Intent(this, ResetPassword.class);
        startActivity(intent);
    }
}

