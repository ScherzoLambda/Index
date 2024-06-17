package com.example.index;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static utils.UtilFunctions.gerarSHA256;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

import constantes.Constants;
import server_api.ServerAPIListener;
import server_api.ServerAPITask;

public class LoginActivity extends AppCompatActivity {

    int resposta;
    private SharedPreferences sharedPreferences;
    private EditText loginEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = CommomUtils.getSharedPreference(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //sharedPreferences.edit().putBoolean("isLoggedIn", false).apply();
        loginEditText = findViewById(R.id.loginEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        createAccountButton = findViewById(R.id.createAccountButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String hashedPass = gerarSHA256(password);
                // Suponha que a lógica de login é bem-sucedida
                boolean loginSuccessful = true; // Substitua pela lógica real
                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                }else{
                    callAPIToLogUser();
                    /*if (resposta == 1) {
                        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply();
                        //sharedPreferences.edit().putString("loggedToken", loggedToken).apply();


                    }else {
                        Toast.makeText(LoginActivity.this, "Login ou senha invalidos", Toast.LENGTH_SHORT).show();
                    }*/
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

    public int callAPIToLogUser(){
        //Toast.makeText(getContext(), "Enviando", Toast.LENGTH_SHORT).show();
        String hashedPass = gerarSHA256(passwordEditText.getText()+"");

        Log.d("RegisterUser | HashPass: ", hashedPass);
        String body = "{\"username\": \""+loginEditText.getText()+"\", \"password\": \""+hashedPass+"\"}";

        new ServerAPITask("login", body, new ServerAPIListener() {
            //Log.d("SernerAPITask", "onCreate() called");
            @Override
            public void onResult(String result) {
                if (result != null) {
                    Toast.makeText(LoginActivity.this, "Login successfully"+resposta, Toast.LENGTH_LONG).show();
                    Log.d("Resquest Finalizada", "Dentro do onResult()");
                    sharedPreferences.edit().putString(Constants.SOCIAL_ID, result).apply();
                    sharedPreferences.edit().putString(Constants.USERNAME, getUserName(result)).apply();
                    Log.d("NOME USUARIO lOGADO:", sharedPreferences.getString(Constants.USERNAME, getUserName(result)));
                    sharedPreferences.edit().putBoolean(Constants.LOGGEDIN, true).apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    resposta = 0;
                    Toast.makeText(LoginActivity.this, "Erro na requisição", Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();
        return resposta;
    }

    public String getUserName(String value){
        String jsonString = value;

        // Converter JSON em Map
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> userData = null;
        try {
            userData = mapper.readValue(jsonString, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Acessar valores dos campos
        String username = userData.get("name");
        String mail = userData.get("mail");
        sharedPreferences.edit().putString(Constants.MAIL, mail).apply();
        Log.d("NOME USUARIO lOGADO:", username);
        return username;

    }
    public void redirectMain(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}


class CommomUtils {
    public static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
    }
}