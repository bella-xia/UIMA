package com.uima.joanne.lifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // do stuff here
        Log.d("cycle", "onCreate");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // do stuff here
        Log.d("cycle", "onRestoreInstanceState");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        // do stuff here
        Log.d("cycle", "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // do stuff here
        Log.d("cycle","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // do stuff here
        Log.d("cycle","onResume");
    }

    @Override
    protected void onPause() {
        // do stuff here
        Log.d("cycle","onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        // do stuff here
        Log.d("cycle","onStop");
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle myBundle) {
        // do stuff here
        Log.d("cycle","onSaveInstanceState");
        super.onSaveInstanceState(myBundle);
    }


    @Override
    protected void onDestroy() {
        // do stuff here
        Log.d("cycle","onDestroy");
        super.onDestroy();
    }
}
