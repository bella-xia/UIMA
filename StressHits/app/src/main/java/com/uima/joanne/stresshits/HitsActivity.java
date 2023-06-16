package com.uima.joanne.stresshits;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HitsActivity extends AppCompatActivity {

    private Integer hits;
    private TextView msg;
    private SharedPreferences myPrefs;

    private final View.OnClickListener bigListener = new View.OnClickListener()  {
        public void onClick(View v)  {
            // do stuff
            hits += 10;
            msg.setText(hits.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hits);
        Intent intent = getIntent();

        Context context = getApplicationContext();  // app level storage
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        hits = myPrefs.getInt("hitsValue", -1);

        msg = (TextView) findViewById(R.id.msg);
        msg.setText(hits.toString());

        Button little = (Button) findViewById(R.id.btn_sm);
        little.setOnClickListener(new View.OnClickListener() {
                                      public void onClick(View v) {// Do something in response to button click 
                                          hits += 1;
                                          msg.setText(hits.toString());
                                      }
                                  }
        );

        Button big = (Button) findViewById(R.id.btn_lg);
        big.setOnClickListener(bigListener);
    }

    @Override
    protected void onPause() {
        SharedPreferences.Editor peditor = myPrefs.edit();
        peditor.putInt("hitsValue", hits);
        peditor.apply();   // TO SAVE CHANGES

        super.onPause();
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

}
