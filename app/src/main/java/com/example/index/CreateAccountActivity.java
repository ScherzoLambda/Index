package com.example.index;

import static java.security.AccessController.getContext;

import static utils.UtilFunctions.gerarSHA256;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import constantes.Constants;
import mongo_api.MongoAPIListener;
import mongo_api.MongoAPITask;
import server_api.ServerAPIListener;
import server_api.ServerAPITask;

public class CreateAccountActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private EditText emailEditText;
    private EditText userEditText;
    private EditText nomeEditText;
    private EditText phoneEditText;
    private EditText passwordEditText;
    private Button createAccountButton;
    String resposta;

    private ImageButton togglePasswordButton;
    private boolean showPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        sharedPreferences = CommomUtils.getSharedPreference(this);

        emailEditText = findViewById(R.id.emailEditText);
        nomeEditText = findViewById(R.id.nomeEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        phoneEditText = findViewById(R.id.telefone);
        userEditText = findViewById(R.id.username_field);
        createAccountButton = findViewById(R.id.createAccountButton);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(CreateAccountActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Lógica para criar a conta do usuário
                    // Você pode adicionar aqui a lógica para salvar o usuário no banco de dados ou autenticar com o Firebase, por exemplo
                    /*if (!callAPIToGetRegisterUser().isEmpty()){
                        Toast.makeText(CreateAccountActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(CreateAccountActivity.this, "Account Not Crated", Toast.LENGTH_SHORT).show();
                    }*/
                    callAPIToGetRegisterUser();


                    // Após criar a conta, redirecionar para a tela de login
                    Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

    public String callAPIToGetRegisterUser(){
        //Toast.makeText(getContext(), "Enviando", Toast.LENGTH_SHORT).show();
        String hashedPass = gerarSHA256(passwordEditText.getText()+"");
        Log.d("RegisterUser | HashPass: ", hashedPass);
        String body = "{\"username\": \""+userEditText.getText()+"\", \"name\": \""+nomeEditText.getText()+"\", \"mail\": \""+emailEditText.getText()+"\", \"password\": \""+hashedPass+"\", \"phone\": \""+phoneEditText.getText()+"\"}";

        new ServerAPITask("create",body, new ServerAPIListener() {
            //Log.d("SernerAPITask", "onCreate() called");
            @Override
            public void onResult(String result) {
                if (result != null) {
                    Log.d("MakeRequest", "onCreate() called");
                    sharedPreferences.edit().putString(Constants.SOCIAL_ID, result).apply();
                    //data_output.setText(result);
                    resposta = result;
                    Toast.makeText(CreateAccountActivity.this, "Account created successfully"+resposta, Toast.LENGTH_LONG).show();


                } else {

                    Toast.makeText(CreateAccountActivity.this, "Erro na requisição", Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();
        return resposta;
    }

}


