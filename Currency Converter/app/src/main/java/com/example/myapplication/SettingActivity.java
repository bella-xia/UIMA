package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingActivity extends AppCompatActivity {

    private SharedPreferences myPrefs;
    private final String[] CURRENCIES = {"US Dollar (USD)", "Euro (EUR)",
            "Chinese Yuan (CNY)", "Sterling Pound (GBP)", "Swiss Franc (CHF)"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Intent intent = getIntent();

        setTitle("Currency Settings");

        Context context = getApplicationContext();  // app level storage
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        int homeCurr = myPrefs.getInt("homeCurr", -1);
        float feeRate = myPrefs.getFloat("feeRate", 0);

        if (homeCurr != -1) {
            Spinner spinner2 = findViewById(R.id.spinner2);
            ArrayAdapter myAdapter = (ArrayAdapter) spinner2.getAdapter();
            spinner2.setSelection(myAdapter.getPosition(CURRENCIES[homeCurr]));
        }

        if (feeRate != 0) {
            EditText feeRateNum = findViewById(R.id.home_number);
            feeRateNum.setText(Float.toString(feeRate));
        }

        Button saveSetting = findViewById(R.id.save_setting);
        saveSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor peditor = myPrefs.edit();
                Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
                int homeCurrInt = convertCurrToIndex(spinner2.getSelectedItem().toString());
                peditor.putInt("homeCurr", homeCurrInt);

                EditText feeRateNum = findViewById(R.id.home_number);
                float feeRate = Float.parseFloat(feeRateNum.getText().toString());
                peditor.putFloat("feeRate", (float) feeRate);
                peditor.apply();

                startActivity(new Intent(SettingActivity.this, MainActivity.class));
            }
        });
    }

    private int convertCurrToIndex(String curr) {
        for (int i = 0; i < CURRENCIES.length; ++i) {
            if (curr.equals(CURRENCIES[i])) {
                return i;
            }
        }
        return -1;
    }
}