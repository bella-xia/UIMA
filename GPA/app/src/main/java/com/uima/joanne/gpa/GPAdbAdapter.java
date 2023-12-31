package com.uima.joanne.gpa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class GPAdbAdapter {

	private SQLiteDatabase db;
	private GPAdbHelper dbHelper;
	private final Context context;

	private static final String DB_NAME = "gpa.db";
	private static int dbVersion = 2;  // added semester

	private static final String COURSE_TABLE = "courses";
	public static final String CRSE_ID = "crse_id";   // column 0
	public static final String CRSE_WHAT = "crse_what";
	public static final String CRSE_GRADE = "crse_grade";
	public static final String CRSE_CREDS = "crse_creds";
	public static final String CRSE_SEM = "crse_semester";
	public static final String[] CRSE_COLS = {CRSE_ID, CRSE_WHAT, CRSE_GRADE, CRSE_CREDS, CRSE_SEM};

	private static final String SEMESTER_TABLE = "semesters";
	public static final String SEM_ID = "sem_id";
	public static final String SEM_WHEN = "sem_when";
	public static final String SEM_YEAR = "sem_year";

	public GPAdbAdapter(Context ctx) {
		context = ctx;
		dbHelper = new GPAdbHelper(context, DB_NAME, null, dbVersion);
	}

	public void open() throws SQLiteException {
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = dbHelper.getReadableDatabase();
		}
	}

	public void close() {
		db.close();
	}
	
	public void clear() {
		dbHelper.onUpgrade(db, dbVersion, dbVersion+1);  // change version to dump old data
		dbVersion++;
	}
	
	// database update methods
	
	public long insertCourse(CourseItem crse) {
		// create a new row of values to insert
		ContentValues cvalues = new ContentValues();
		// assign values for each col
		cvalues.put(CRSE_WHAT, crse.getName());
		cvalues.put(CRSE_GRADE, crse.getGrade());
		cvalues.put(CRSE_CREDS, crse.getCredits());
		cvalues.put(CRSE_SEM, crse.getSemester());
		// add to course table in database
		return db.insert(COURSE_TABLE, null, cvalues);
	}
	
	public boolean removeCourse(long ri) {   // ri is the course id
		return db.delete(COURSE_TABLE, "CRSE_ID="+ri, null) > 0;
	}
	
	public boolean updateGrade(long ri, String gr) {
		ContentValues cvalue = new ContentValues();
		cvalue.put(CRSE_GRADE, gr);
		return db.update(COURSE_TABLE, cvalue, CRSE_ID+"="+ri, null) > 0;
	}
	
	public boolean updateName(long ri, String nm) {
		ContentValues cvalue = new ContentValues();
		cvalue.put(CRSE_WHAT, nm);
		return db.update(COURSE_TABLE, cvalue, CRSE_ID+"="+ri, null) > 0;
	}	
	
	public boolean updateCredits(long ri, float cr) {
		ContentValues cvalue = new ContentValues();
		cvalue.put(CRSE_CREDS, cr);
		return db.update(COURSE_TABLE, cvalue, CRSE_ID+"="+ri, null) > 0;
	}
	
	// database query methods
	public Cursor getAllCourses() {
		return db.query(COURSE_TABLE, CRSE_COLS, null, null, null, null, null);
	}
	
	public Cursor getCourseCursor(long ri) throws SQLException {
		Cursor result = db.query(true, COURSE_TABLE, CRSE_COLS, CRSE_ID+"="+ri, null, null, null, null, null);
		if ((result.getCount() == 0) || !result.moveToFirst()) {
			throw new SQLException("No course items found for row: " + ri);
		}
		return result;
	}
	
	public CourseItem getCourseItem(long ri) throws SQLException {
		Cursor cursor = db.query(true, COURSE_TABLE, CRSE_COLS, CRSE_ID+"="+ri, null, null, null, null, null);
		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			throw new SQLException("No course items found for row: " + ri);
		}
		// must use column indices to get column values
		int whatIndex = cursor.getColumnIndex(CRSE_WHAT);
		CourseItem result = new CourseItem(cursor.getString(whatIndex), cursor.getString(2), cursor.getFloat(3), cursor.getString(4));
		return result;
	}


	private static class GPAdbHelper extends SQLiteOpenHelper {

		// SQL statement to create a new database
		private static final String DB_CREATE = "CREATE TABLE " + COURSE_TABLE
				+ " (" + CRSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CRSE_WHAT + " TEXT,"
				+ CRSE_GRADE + " TEXT, " + CRSE_CREDS + " REAL," + CRSE_SEM + " TEXT);";

		public GPAdbHelper(Context context, String name, CursorFactory fct, int version) {
			super(context, name, fct, version);
		}

		@Override
		public void onCreate(SQLiteDatabase adb) {
			// TODO Auto-generated method stub
			adb.execSQL(DB_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase adb, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.w("GPAdb", "upgrading from version " + oldVersion + " to "
					+ newVersion + ", destroying old data");
			// drop old table if it exists, create new one
			// better to migrate existing data into new table
			adb.execSQL("DROP TABLE IF EXISTS " + COURSE_TABLE);
			onCreate(adb);
		}
	} // GPAdbHelper class

} // GPAdbAdapter class
