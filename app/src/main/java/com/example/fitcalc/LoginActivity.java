package com.example.fitcalc;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLogin;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextLogin = findViewById(R.id.inputlogin);
        editTextPassword = findViewById(R.id.inputpassword);
        buttonLogin = findViewById(R.id.loginbutton);
        textViewRegister = findViewById(R.id.textregister);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = editTextLogin.getText().toString().trim();
                final String password = editTextPassword.getText().toString().trim();
                if (!username.isEmpty() && !password.isEmpty()) {
                    new LoginTask().execute(username, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Podaj login i hasło.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://10.0.2.2:3000/api/login");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                JSONObject jsonParams = new JSONObject();
                jsonParams.put("username", username);
                jsonParams.put("password", password);

                Log.d("LoginTask", "Request JSON: " + jsonParams.toString());

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(jsonParams.toString().getBytes());
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    return jsonResponse.getBoolean("success");
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                // Logowanie udane, wykonaj odpowiednie akcje (np. przejście do kolejnej aktywności)
                Intent intent = new Intent(LoginActivity.this, DietActivity.class);
                startActivity(intent);
            } else {
                // Logowanie nieudane, wyświetl komunikat o błędzie
                Toast.makeText(LoginActivity.this, "Nieprawidłowy login lub hasło.", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
