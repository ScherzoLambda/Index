package com.example.index;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = CommomUtils.getSharedPreference(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //sharedPreferences.edit().putBoolean("isLoggedIn", false).apply();
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        createAccountButton = findViewById(R.id.createAccountButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Suponha que a lógica de login é bem-sucedida
                boolean loginSuccessful = true; // Substitua pela lógica real

                if (loginSuccessful) {
                    sharedPreferences.edit().putBoolean("isLoggedIn", true).apply();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ir para a tela de criação de conta
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }
}


class CommomUtils {
    public static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
    }
}