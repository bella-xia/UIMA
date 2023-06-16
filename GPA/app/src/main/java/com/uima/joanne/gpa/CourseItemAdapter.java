package com.uima.joanne.gpa;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class CourseItemAdapter extends ArrayAdapter<CourseItem> {
	
	int resource;
	
	public CourseItemAdapter(Context ctx, int res, List<CourseItem> items)
	{
		super(ctx, res, items);
		resource = res;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout courseView;
		CourseItem course = getItem(position);
		
		if (convertView == null) {
			courseView = new RelativeLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
			vi.inflate(resource, courseView, true);
		} else {
			courseView = (RelativeLayout) convertView;
		}
		
		TextView nameView = (TextView) courseView.findViewById(R.id.crsetext);
		TextView gradeView = (TextView) courseView.findViewById(R.id.gradetext);
		TextView creditsView = (TextView) courseView.findViewById(R.id.creditstext);
		
		nameView.setText(course.getName());
		gradeView.setText(course.getGrade());
		creditsView.setText(course.getCredits()+"");
		
		return courseView;
	}

}
