package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences myPrefs;
    private String fromCurr;
    private int homeCurrInt;
    private double toAmount;
    private double toFee;
    private double feeRate;
    private double fromAmount;
    private boolean visibility;

    private final double[][] CONVERTION = {{1.00, 0.94, 6.81, 0.83, 0.92},
            {1.07, 1.00, 7.27, 0.89, 0.99}, {0.15, 0.14, 1.00, 0.12, 0.14},
            {1.21, 1.13, 8.21, 1.00, 1.11},{1.08, 1.01, 7.37, 0.90, 1.00}};
    private final String[] CURRENCIES = {"US Dollar (USD)", "Euro (EUR)",
            "Chinese Yuan (CNY)", "Sterling Pound (GBP)", "Swiss Franc (CHF)"};
    private final String[] CURRENCIES_ABBREV = {"USD", "EUR", "CNY", "GBP", "CHF"};

    private final CharSequence SAME_CURRENCY = "Must have different currencies in from and to.";
    private final CharSequence SELECT_CURRENCY_FROM = "Must select a currency in from.";
    private final CharSequence POSITIVE_VALUE = "Must have positive value in from amount.";
    private final CharSequence SELECCT_HOME_COUNTRY = "Please select a home country";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Currency Converter");

        Context context = getApplicationContext();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        homeCurrInt = myPrefs.getInt("homeCurr", -1);
        feeRate = myPrefs.getFloat("feeRate", 0);
        fromAmount = 0;
        visibility = false;
        toAmount = 0;
        toFee = 0;

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter myAdapter = (ArrayAdapter) spinner.getAdapter();

        EditText from_number =  (EditText) findViewById(R.id.from_number);
        from_number.setText(Double.toString(fromAmount));

        if (homeCurrInt != -1) {
            TextView home_currency = findViewById(R.id.home_currency);
            home_currency.setText(CURRENCIES[homeCurrInt]);

            Button convert_button = (Button) findViewById(R.id.convert_button);
            convert_button.setText("Convert to " + CURRENCIES_ABBREV[homeCurrInt]);
        }

        changeVisibility(true);

        Button convert = (Button) findViewById(R.id.convert_button);
        convert.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText from_number = (EditText) findViewById(R.id.from_number);
                fromAmount = Double.parseDouble(from_number.getText().toString());
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                fromCurr = spinner.getSelectedItem().toString();
                int fromCurrInt = convertCurrToIndex(fromCurr);
                homeCurrInt = myPrefs.getInt("homeCurr", -1);
                if (fromCurr.equals("Select Currency")) {
                    changeVisibility(true);
                    makeToast(SELECT_CURRENCY_FROM);
                } else if (homeCurrInt == -1) {
                    changeVisibility(true);
                    makeToast(SELECCT_HOME_COUNTRY);
                } else if(fromCurrInt == homeCurrInt) {
                    changeVisibility(true);
                    makeToast(SAME_CURRENCY);
                } else if (fromAmount <= 0) {
                    changeVisibility(true);
                    makeToast(POSITIVE_VALUE);
                } else {
                    visibility = true;
                    changeVisibility(false);
                    toAmount = fromAmount *
                            CONVERTION[fromCurrInt][homeCurrInt];
                    feeRate = myPrefs.getFloat("feeRate", 0);
                    toFee = toAmount * feeRate / 100;
                    TextView convertAmount = (TextView) findViewById(R.id.convert_amount);
                    String convertAmountString = Math.round(toAmount * 100) / 100.0
                            + " " + CURRENCIES_ABBREV[homeCurrInt];
                    String convertFeeString = "+ " + Math.round(toFee * 100) / 100.0
                            + " Fee";
                    convertAmount.setText(convertAmountString);
                    TextView convertFee = (TextView) findViewById(R.id.fee_amount);
                    convertFee.setText(convertFeeString);
                }
            }
        });

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                myPrefs = PreferenceManager.getDefaultSharedPreferences(context);

                SharedPreferences.Editor peditor = myPrefs.edit();
                peditor.putInt("homeCurr", homeCurrInt);
                peditor.putFloat("feeRate", (float) feeRate);
                peditor.apply();

                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Context context = getApplicationContext();  // app level storage
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        homeCurrInt = myPrefs.getInt("homeCurr", -1);
        feeRate = myPrefs.getFloat("feeRate", 0);
        changeVisibility(true);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter myAdapter = (ArrayAdapter) spinner.getAdapter();
        spinner.setSelection(myAdapter.getPosition(fromCurr));

        if (homeCurrInt != -1) {
            TextView home_currency = findViewById(R.id.home_currency);
            home_currency.setText(CURRENCIES[homeCurrInt]);

            Button convert_button = (Button) findViewById(R.id.convert_button);
            convert_button.setText("Convert to " + CURRENCIES_ABBREV[homeCurrInt]);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
            outState.putDouble("fromAmount", fromAmount);
            outState.putString("fromCurr", fromCurr);
            outState.putBoolean("visibility", true);
            outState.putBoolean("visibility", visibility);
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            fromAmount = savedInstanceState.getDouble("fromAmount");
            fromCurr = savedInstanceState.getString("fromCurr");
            Context context = getApplicationContext();  // app level storage
            myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            homeCurrInt = myPrefs.getInt("homeCurr", -1);
            feeRate = myPrefs.getFloat("feeRate", 0);
            visibility = savedInstanceState.getBoolean("visibility");
            changeVisibility(! (visibility));

            Spinner spinner = (Spinner) findViewById(R.id.spinner);
            ArrayAdapter myAdapter = (ArrayAdapter) spinner.getAdapter();
            spinner.setSelection(myAdapter.getPosition(fromCurr));

            EditText from_number =  (EditText) findViewById(R.id.from_number);
            from_number.setText(Double.toString(fromAmount));

            if (homeCurrInt != -1) {

                TextView home_currency = findViewById(R.id.home_currency);
                home_currency.setText(CURRENCIES[homeCurrInt]);

                Button convert_button = (Button) findViewById(R.id.convert_button);
                convert_button.setText("Convert to " + CURRENCIES_ABBREV[homeCurrInt]);
            }

        }
    }




    private void makeToast(CharSequence msg) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.main_activity), msg,
                Snackbar.LENGTH_LONG);
        snackbar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.white));
        TextView textView = (TextView)
                snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(R.color.theme));
        snackbar.show();
    }


    private void changeVisibility(boolean toInvisible) {
        View pic = findViewById(R.id.suitcase_img);
        View convert = findViewById(R.id.convert_amount);
        View fee = findViewById(R.id.fee_amount);
        if (toInvisible) {
            pic.setVisibility(View.INVISIBLE);
            convert.setVisibility(View.INVISIBLE);
            fee.setVisibility(View.INVISIBLE);
        } else {
            pic.setVisibility(View.VISIBLE);
            convert.setVisibility(View.VISIBLE);
            fee.setVisibility(View.VISIBLE);
        }
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