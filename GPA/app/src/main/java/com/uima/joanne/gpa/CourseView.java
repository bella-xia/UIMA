package com.uima.joanne.gpa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class CourseView extends Activity {

	private EditText editName;
	private EditText editCreds;
	private Spinner spinGrade;
	private CourseItem myCourse;

	Intent launcher;
	private boolean changed;
	private boolean editMode;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_view);
		
		changed = false;
		
		editName = (EditText) findViewById(R.id.editName);
		editCreds = (EditText) findViewById(R.id.editCredits);
		spinGrade = (Spinner) findViewById(R.id.grade_list);
		ArrayAdapter<CharSequence> gadapter = ArrayAdapter.createFromResource(
				this, R.array.letterGrades,
				android.R.layout.simple_spinner_item);
		gadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinGrade.setAdapter(gadapter);

		/*
		 * Spinner s2 = (Spinner) findViewById(R.id.semester);
		 * ArrayAdapter<CharSequence> sadapter =
		 * ArrayAdapter.createFromResource(this, R.array.semesterNames,
		 * android.R.layout.simple_spinner_item);
		 * adapter.setDropDownViewResource
		 * (android.R.layout.simple_spinner_dropdown_item);
		 * s2.setAdapter(adapter);
		 */

		launcher = getIntent();
		editMode = launcher.getBooleanExtra("GPAcur.EDIT", false);
		
		if (!editMode) {
			editName.setText("");
			editCreds.setText("3");
			spinGrade.setSelection(2);
		}
		else {  // get course data to edit
			editName.setText(launcher.getStringExtra("GPAcur.CNAME"));
			editCreds.setText(launcher.getFloatExtra("GPAcur.CCREDITS", 0)+"");
			String letter = launcher.getStringExtra("GPAcur.CGRADE");
			spinGrade.setSelection(gadapter.getPosition(letter));
		}
	
		Button saveBtn = (Button) findViewById(R.id.save_button);
		saveBtn.setOnClickListener(saveListener);

		Button cancelBtn = (Button) findViewById(R.id.cancel_button);
		cancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (changed) {   // done
					changed = false;
					setResult(RESULT_OK);
				} else
					setResult(RESULT_CANCELED);
				finish();
			}
		});

	}

	@Override
	public void onPause() {
		// TODO: if the user hits back instead of done/cancel, the courselist doesn't update (but the course is saved) - should it?
		super.onPause();
	}

	private OnClickListener saveListener = new OnClickListener() {
		public void onClick(View v) {
			changed = true;
			String currName = editName.getText().toString();
			float currCredits = Float.parseFloat(editCreds.getText().toString());
			String currGrade = (String) spinGrade.getSelectedItem();
			
			if (!editMode) {   // new course being added
			myCourse = new CourseItem(currName, currGrade, currCredits, null);
			CourseList.dbAdapt.insertCourse(myCourse);
			editName.setText("");
			}
			else { // editing existing course!!
				// easy way out: delete course then re-add, but this changes the CRSE_ID field, poor choice
				// harder way: update individual fields
				long currID = launcher.getLongExtra("GPAcur.CID", 0);
				CourseList.dbAdapt.updateGrade(currID, currGrade);
				CourseList.dbAdapt.updateName(currID, currName);
				CourseList.dbAdapt.updateCredits(currID, currCredits);
				setResult(RESULT_OK);
				finish();
			}
		}
	};
}