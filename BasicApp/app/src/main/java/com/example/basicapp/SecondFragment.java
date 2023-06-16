package com.example.basicapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.basicapp.databinding.FragmentSecondBinding;

import java.util.Random;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;   // Note relationship between name and layout file

    Random random = new java.util.Random();  // moved here instead of initializing each time view created

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // added for tutorial step 9, but uses different approach for saving/passing data
        SharedPreferences prefs = getActivity().getPreferences(Activity.MODE_PRIVATE);
        int count = prefs.getInt("MYCOUNT", 0);
        String countText = getString(R.string.random_heading, count);  // composes like printf
        TextView headerView = view.getRootView().findViewById(R.id.textview_header); // old way
        headerView.setText(countText);

        Integer randomNumber = 0;
        if (count > 0) {
            randomNumber = random.nextInt(count + 1);
        }
        binding.textviewRandom.setText(randomNumber.toString()); // new way to access view

        // BELOW USES THE NAV GRAPH TO GO FROM 2nd FRAG TO 1st
        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}