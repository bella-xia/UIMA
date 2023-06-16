package com.uima.joanne.gpa;


public class CourseItem {
	
    private String name;
    private String grade;
    private float credits;
    private String semester;
    
    public CourseItem(String n, String g, float c, String s)
    {
    	name = n;
    	grade = g;
    	credits = c;
    	semester = s;
    }
    
    public String toString()
    {
    	return name + "  " + grade + "  " + credits;
    }
    
    public String getName()
    {
    	return name;
    }
    
    public String  getGrade()
    {
    	return grade;
    }
    
    public double getCredits()
    {
    	return credits;
    }
    
    public String getSemester()
    {
    	return semester;
    }
}