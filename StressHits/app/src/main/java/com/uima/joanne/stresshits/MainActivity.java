package com.uima.joanne.stresshits;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Integer numHits;
    private SharedPreferences myPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext(); // app level storage
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor peditor = myPrefs.edit();
        peditor.putInt("hitsValue", -1);
        peditor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();

        numHits = myPrefs.getInt("hitsValue", 0);
        TextView hits = (TextView) findViewById(R.id.hits_value);
        hits.setText(numHits.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {

        SharedPreferences.Editor peditor = myPrefs.edit();
        peditor.putInt("hitsValue", numHits);
        peditor.apply();

        super.onPause();
    }


    @Override
    protected void onStop() {

        SharedPreferences.Editor peditor = myPrefs.edit();
        peditor.putInt("hitsValue", 10);
        peditor.apply();

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // do stuff here
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** Called when the user clicks the HITME button */
    public void hitme(View view) {
        // Do something in response to button

        Intent intent = new Intent(MainActivity.this, HitsActivity.class);
        startActivity(intent);
    }
}

