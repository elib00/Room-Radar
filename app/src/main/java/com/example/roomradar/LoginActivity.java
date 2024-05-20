package com.example.roomradar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.roomradar.Database.DatabaseManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText password;
    private TextInputEditText email;
    private TextView forgotPassword;
    private Button login;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        setFirebaseAuth();
        initializeView();
    }

    private void initializeView(){
        email = (TextInputEditText) findViewById(R.id.emailLoginInput);
        password = (TextInputEditText) findViewById(R.id.passwordLoginInput);

        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        login = (Button) findViewById(R.id.loginButton);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseManager.registerUser(LoginActivity.this, auth, email.getText().toString(), password.getText().toString());
            }
        });
    }

    private void setFirebaseAuth(){
        auth = FirebaseAuth.getInstance();
    }
}