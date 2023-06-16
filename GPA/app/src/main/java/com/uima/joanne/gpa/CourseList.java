package com.uima.joanne.gpa;

// GPAcur

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CourseList extends Activity implements OnSharedPreferenceChangeListener {

    public static final int MENU_ITEM_EDITVIEW = Menu.FIRST;
    public static final int MENU_ITEM_DELETE = Menu.FIRST + 1;

    private TextView creditText;
    private TextView gpaText;
    private ListView courseList;
    private float totalCredits;
    private float totalPoints;

    protected static ArrayList<CourseItem> courseItems;
    protected static CourseItemAdapter aa;
    private SharedPreferences prefs;
    private Context context;

    protected static GPAdbAdapter dbAdapt; // made static to access in
    // CourseView
    private static Cursor cCursor; // course table cursor

    public final static DecimalFormat fmt = new DecimalFormat("0.00");
    public static float pointValues[] = { 4.0f, 4.0f, 3.7f, 3.3f, 3.0f, 2.7f,
            2.3f, 2.0f, 1.7f, 1.3f, 1.0f, 0f };
    public static String[] gradesArray;

    static final int EDIT_COURSE = 0;
    static final int NEW_COURSE = 1;

    AlertDialog clearWarning;
    private static final String TAG="debugging";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.registerOnSharedPreferenceChangeListener(this);

        setContentView(R.layout.course_list);

        Resources res = getResources();
        gradesArray = res.getStringArray(R.array.letterGrades);

        // set-up database
        dbAdapt = new GPAdbAdapter(this);
        dbAdapt.open();

        // for testing
        // dbAdapt.insertCourse(new CourseItem("intro java", "A+", 3.5f, null));

        gpaText = (TextView) findViewById(R.id.gpa_value);
        creditText = (TextView) findViewById(R.id.tot_credits);
        courseList = (ListView) findViewById(R.id.crselist);

        // create ArrayList of courses from database
        courseItems = new ArrayList<CourseItem>();
        // make array adapter to bind arraylist to listview with new custom item layout
        aa = new CourseItemAdapter(this, R.layout.course_item, courseItems);
        courseList.setAdapter(aa);

        // create the display
        updateArray();

        registerForContextMenu(courseList);

        Button addb = (Button) findViewById(R.id.addBtn);
        addb.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CourseList.this, CourseView.class);
                startActivityForResult(intent, NEW_COURSE);
            }
        });

        Button clearb = (Button) findViewById(R.id.clearBtn);
        clearb.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // launch dialog check
                clearWarning.show();
            }
        });

        // initialize clear dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(
                "Are you sure you want to permanently clear all data?")
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.clear)
                .setPositiveButton("Yes", clearListener)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        clearWarning = builder.create();
    }


    /**
     * Update the course listing in the view, and the stored GPA data
     *
     */
    public void updateArray() {
        cCursor = dbAdapt.getAllCourses();
        courseItems.clear();
        totalCredits = 0;
        totalPoints = 0;
        boolean aplus43 = prefs.getBoolean("PREF_APLUS_43", false);
        pointValues[0] = (float) (aplus43 ? 4.3 : 4.0);
        String grade;
        if (cCursor.moveToFirst())
            do {
                CourseItem result = getCourse(cCursor);
                courseItems.add(0, result);
                totalCredits += result.getCredits();
                grade = result.getGrade();
                totalPoints += result.getCredits() * getPoints(grade);
            } while (cCursor.moveToNext());

        aa.notifyDataSetChanged();

        // update stored values and display for GPA and credits
        updateGPA();
    }

    /**
     * Get the course object from the current database cursor object
     *
     */
    public CourseItem getCourse(Cursor curse) {
        return new CourseItem(curse.getString(1), curse.getString(2),
                curse.getFloat(3), curse.getString(4));
    }

    /**
     * Look-up the point value for a particular grade
     *
     * @param g the letter grade to look up
     * @return the point value
     */
    public static float getPoints(String g) {
        int i, where = -1;
        for (i = 0; i < gradesArray.length && where < 0; i++)
            if (gradesArray[i].equalsIgnoreCase(g))
                where = i;
        if (where < 0 || where > pointValues.length)
            return 0;
        else
            return pointValues[where];
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_COURSE) {
            updateArray();
        }
        if (resultCode == RESULT_OK && requestCode == NEW_COURSE) {
            updateArray();
        }
    }

    private void updateGPA() {
        Log.v(TAG, "totalPoints="+totalPoints);

        gpaText.setText(fmt.format(totalPoints / totalCredits));
        creditText.setText(totalCredits + "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mm_add_course:
                Intent intentA = new Intent(CourseList.this, CourseView.class);
                intentA.putExtra("GPAcur.EDIT", false);
                startActivityForResult(intentA, NEW_COURSE);
                return true;
            case R.id.mm_quickie:
                Intent intentQ = new Intent(CourseList.this, QuickCalc.class);
                startActivity(intentQ);
                return true;
            case R.id.mm_settings:
                Intent intentS = new Intent(CourseList.this, SettingsActivity.class);
                startActivity(intentS);
                return true;
            default:
                return super.onOptionsItemSelected(item); // should return false for
            // unprocessed items
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // create menu in code instead of in xml file (xml approach preferred)
        menu.setHeaderTitle("Select Course Item");

        // Add menu items
        menu.add(0, MENU_ITEM_EDITVIEW, 0, R.string.menu_editview);
        menu.add(0, MENU_ITEM_DELETE, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        AdapterView.AdapterContextMenuInfo menuInfo;
        menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = menuInfo.position; // position in array adapter
        // index in array adapter is reverse order of database row
        cCursor = dbAdapt.getAllCourses();
        // Toast.makeText(context, "dbase count is " +
        // cCursor.getCount(),Toast.LENGTH_SHORT).show();
        cCursor.move(cCursor.getCount() - index);
        long crseID = cCursor.getLong(0);

        switch (item.getItemId()) {
            case MENU_ITEM_EDITVIEW: {
                CourseItem theCourse = getCourse(cCursor);
                Toast.makeText(context, "edit request: " + theCourse,
                        Toast.LENGTH_SHORT).show();
                Intent intentE = new Intent(CourseList.this, CourseView.class);
                intentE.putExtra("GPAcur.EDIT", true);
                intentE.putExtra("GPAcur.CID", crseID);
                intentE.putExtra("GPAcur.CNAME", cCursor.getString(1));
                intentE.putExtra("GPAcur.CGRADE", cCursor.getString(2));
                intentE.putExtra("GPAcur.CCREDITS", cCursor.getFloat(3));
                intentE.putExtra("GPAcur.CSEM", cCursor.getString(4));
                startActivityForResult(intentE, EDIT_COURSE);
                return false;
            }
            case MENU_ITEM_DELETE: {
                if (dbAdapt.removeCourse(crseID)) {
                    updateArray();
                    Toast.makeText(context, "course " + index + " deleted",
                            Toast.LENGTH_SHORT).show();
                    return true;
                } else
                    return false;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbAdapt.close();
    }

    private DialogInterface.OnClickListener clearListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            Toast.makeText(context, "Clearing all course data",
                    Toast.LENGTH_LONG).show();

            gpaText.setText(fmt.format(0));
            creditText.setText("0.0");
            courseItems.clear();
            aa.notifyDataSetChanged();
            dbAdapt.clear(); // wipe out database!
            updateArray();
        }
    };

    public void onSharedPreferenceChanged(SharedPreferences pfs, String key) {
        // check prefs values for key and update this as relevant
        Log.d(TAG, "key is " + (key));
        if (key.equals("PREF_APLUS_43")) {  // Must use correct name!!!
            updateArray();  // recalculate GPA
        }
    }


}