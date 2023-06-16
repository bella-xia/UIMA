package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myapplication.MESSAGE";
    private static Random randy = new Random();
    private int secret;
    private int guess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        secret = randy.nextInt(100) + 1;
        Log.d("secret", secret + "");
    }

    /**
     * Called when the user taps the Send button
     */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        guess = Integer.parseInt(message);
        if (guess < secret) {
            message = "go higher";
        } else if (guess > secret) {
            message = "go lower";
        } else {
            message = "correct";
        }
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}