package com.example.a5_sample;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.a5_sample.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    public static ArrayList<Park> parks;
    public static ParkAdapter pa;
    public Park current;
    public Random randy = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parks = new ArrayList<Park>();
        parks.add(new Park("Oriole Park" , randy.nextInt(10)));
        parks.add(new Park("BlueJay Park", randy.nextInt(10)));
        parks.add(new Park("Park Robin", randy.nextInt(10)));
        pa = new ParkAdapter(this, R.layout.parklayout, parks);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Context context = getApplicationContext();
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        current = null;

        // BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_map, R.id.navigation_home, R.id.navigation_chat, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        String name = myPrefs.getString("loginName", "Owner");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Context context = getApplicationContext();
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String newPark= myPrefs.getString("newPark", "");
        if (! newPark.equals("")) {
            parks.add(new Park(newPark, 0));
            SharedPreferences.Editor peditor = myPrefs.edit();
            peditor.putString("newPark", "");
            peditor.apply();
            }
        pa = new ParkAdapter(this, R.layout.parklayout, parks);
    }

    // TODO: uncomment these sections to activate the Options menu after creating it in layout/menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            // TODO: add code to close this activity
            startActivity(new Intent(MainActivity.this, Login.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}