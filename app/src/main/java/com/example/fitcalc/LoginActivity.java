package com.example.fitcalc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLogin;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

//        editTextLogin = findViewById(R.id.inputlogin);
//        editTextPassword = findViewById(R.id.inputpassword);
//        buttonLogin = findViewById(R.id.loginbutton);
//        //textViewRegister = findViewById(R.id.textregister);
//
//        buttonLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Logika logowania
//            }
//        });
//
//        textViewRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Przejdź do aktywności rejestracji
//            }
//        });
    }
}
