package com.uima.joanne.gpa;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class QuickCalc extends Activity {
	
	private TextView creditText;
	private TextView gpaText;
	private EditText editCreds;
	private Spinner spinGrade;
	private Button saveb;
	private Button clearb;
	private float totalCredits = 0f;
	private float totalPoints = 0f;
			
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quick_calc);
        
        gpaText = (TextView) findViewById(R.id.qgpa_value);
        creditText = (TextView) findViewById(R.id.qtot_credits);
        clear();
 
        editCreds = (EditText) findViewById(R.id.qeditCredits);
        editCreds.setText("3");
       
        spinGrade = (Spinner) findViewById(R.id.qgrade_list);
        ArrayAdapter<CharSequence> gadapter = ArrayAdapter.createFromResource(
                this, R.array.letterGrades, android.R.layout.simple_spinner_item);
        gadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinGrade.setAdapter(gadapter);
        spinGrade.setSelection(1);

        saveb = (Button) findViewById(R.id.qsave_button);
        saveb.setOnClickListener(saveListener);
        
        clearb = (Button) findViewById(R.id.qclear_button);
        clearb.setOnClickListener(clearListener);
    	
    }
    
    private void clear()
    {
       	totalCredits = 0f;
    	totalPoints = 0f;
        gpaText.setText(CourseList.fmt.format(0));
        creditText.setText("0.0");         
    }
    
    
    private OnClickListener clearListener = new OnClickListener()
    {
        public void onClick(View v)
        {
       		clear();
        }
    };

    private OnClickListener saveListener = new OnClickListener()
    {
        public void onClick(View v)
        {
              float currCredits = Float.parseFloat(editCreds.getText().toString());
              int gradeIndex = spinGrade.getSelectedItemPosition();
              totalCredits += currCredits;
              totalPoints += currCredits * CourseList.pointValues[gradeIndex];
       	  
              float gpaValue = totalPoints/totalCredits;
              if (gpaValue < 2.0f) {
            	  Toast toast = Toast.makeText(getBaseContext(), "Uh-oh, Ac Pro!", Toast.LENGTH_SHORT);
            	  toast.show();
              }
              gpaText.setText(CourseList.fmt.format(gpaValue));
              creditText.setText(totalCredits+"");     
        }
    };


}