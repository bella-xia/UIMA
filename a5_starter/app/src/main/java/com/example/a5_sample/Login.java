package com.example.a5_sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.a5_sample.databinding.ActivityLoginBinding;
import com.example.a5_sample.databinding.ActivityMainBinding;

public class Login extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Context context = getApplicationContext();
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String initialName = myPrefs.getString("loginName", "Owner");
        initialName = (initialName.equals("")) ? "Owner" : initialName;
        EditText inputName = (EditText) findViewById(R.id.name_text);
        inputName.setText(initialName);

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor peditor = myPrefs.edit();
                EditText inputName = (EditText) findViewById(R.id.name_text);
                String name = inputName.getText().toString();
                peditor.putString("loginName", name);
                peditor.apply();
                startActivity(new Intent(Login.this, MainActivity.class));
            }
        });
    }
}