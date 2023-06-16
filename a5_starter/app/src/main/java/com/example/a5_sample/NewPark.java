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

public class NewPark extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_park);

        Button sbtn = (Button) findViewById(R.id.save_btn);
        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add code here to program the SAVE action
                EditText newParkName = (EditText) findViewById(R.id.new_park);
                String StrNewParkName = newParkName.getText().toString();
                if (! StrNewParkName.equals("")) {
                    Context context = getApplicationContext();
                    SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor peditor = myPrefs.edit();
                    peditor.putString("newPark", StrNewParkName);
                    peditor.apply();
                }
                finish();
            }
        });

        Button qbtn = (Button) findViewById(R.id.quit_btn);
        qbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}