package com.uima.joanne.navuiclasswork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        TextView msg = (TextView) findViewById(R.id.my_display);

        // TODO: change this code to get name and reps from the Intent launcher
        Intent launcher = getIntent();
        String name = launcher.getStringExtra("MYNAME");
        int reps = launcher.getIntExtra("MYNUM", 0);
        // end of this to do

        StringBuilder toDisplay = new StringBuilder("");
        for (int i = 0; i < reps; i++) {
            toDisplay.append(name);
            toDisplay.append("\n");
        }
        msg.setText(toDisplay.toString());
    }

    public void quitThis(View v) {
        setResult(RESULT_OK);
        finish();
    }
}