package com.example.fitbrov2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Objects;

public class registration extends AppCompatActivity {

    private TextView loginButton;
    private FirebaseAuth mAuth;
    private EditText email, username, password;
    private CheckBox checkbox;
    private Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        loginButton = (TextView) findViewById(R.id.textView8);
        email = (EditText)findViewById(R.id.editText10);
        username = (EditText)findViewById(R.id.editText11);
        password = (EditText)findViewById(R.id.editText12);
        checkbox = (CheckBox)findViewById(R.id.checkBox2);
        signUp = (Button)findViewById(R.id.button6);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSignUp();
            }
        });
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    public void login()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void setSignUp()
    {
        String emailCheck = email.getText().toString().trim();
        String nameCheck = username.getText().toString().trim();
        String passwordCheck = password.getText().toString().trim();
        Double ociValue = 1200.00;
        Double calorieConsumed = 0.00;
        int dailyResetCheck = 0;
        String genderCheck = "None";
        Double weightCheck = 0.00;
        Double heightCheck = 0.00;
        int ageCheck = 0;
        String statusCheck = "Not Available";

        if(emailCheck.isEmpty()){
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }

        if(nameCheck.isEmpty()){
            username.setError("Username is required!");
            username.requestFocus();
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
        if(checkbox.isChecked() == false){
            Toast.makeText(getApplicationContext(), "Please check the checkbox before proceed!", Toast.LENGTH_SHORT).show();
        }

        if(!isEmpty(email) && !isEmpty(username) && !isEmpty(password) && checkbox.isChecked()){
            mAuth.createUserWithEmailAndPassword(emailCheck,passwordCheck).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        User user = new User(nameCheck, emailCheck, passwordCheck, ociValue, calorieConsumed, dailyResetCheck, genderCheck, weightCheck, heightCheck, ageCheck, statusCheck);

                        FirebaseDatabase.getInstance("https://fitbro-4fe4a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("UserInfo")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Registration Succeed! Go back to the login screen to login", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Registration Failed, please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(), "Registration Failed, please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "Registration Failed, please try again.", Toast.LENGTH_SHORT).show();
        }
    }


}