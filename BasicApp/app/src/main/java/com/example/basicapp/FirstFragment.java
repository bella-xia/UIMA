package com.example.basicapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.basicapp.databinding.FragmentFirstBinding;

/* THIS CLASS IS AN ADAPTATION FROM THE BASIC ACTIVITY NEW PROJECT TEMPLATE,
   BASED ON THE "BUILD YOUR FIRST APP" TUTORIAL (a1 homework)
 */
public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;  // Note relationship between name and layout file
    private TextView showCount;  // added to project template for tutorial step 8

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    /*  THIS IS HOW WE INITIALIZE AND INFLATE LAYOUTS WITHOUT USING BINDINGS:
        View fragmentFirstLayout = inflater.inflate(R.layout.fragment_first, container, false);
        return fragmentFirstLayout;
    */
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // added to project template for tutorial but in onViewCreated instead of onCreateView
        // uses parameter instead of finding View like the above code would do (step 8)
        showCount = (TextView) view.getRootView().findViewById(R.id.textview_first);

        binding.randomButton.setOnClickListener(new View.OnClickListener() {
 /* OR: view.findViewById(R.id.random_button).setOnClickListener(new View.OnClickListener() { */
            @Override
            public void onClick(View view) {
                // BELOW IS AN ALTERNATE WAY TO SAVE & PASS DATA TO THE SECOND FRAGMENT (TUTORIAL, step 9)
                SharedPreferences prefs = getActivity().getPreferences(Activity.MODE_PRIVATE);
                SharedPreferences.Editor peditor = prefs.edit();
                Integer count = Integer.parseInt(binding.textviewFirst.getText().toString());
                peditor.putInt("MYCOUNT", count);
                peditor.apply();


                // THE BELOW ACTION IS DEFINED IN nav_graph.xml AND APPEARS IN THE PROJECT TEMPLATE
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);


            }
        });

        /* USING findViewById approach, added for tutorial step 8 */
        view.findViewById(R.id.toast_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast myToast = Toast.makeText(getActivity(), "Hello toast!", Toast.LENGTH_SHORT);
                myToast.show();
            }
        });

        /* USING binding approach - note id/field name relationship, added for tutorial step 8 */
        binding.countButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countMe(view);
            }
        });
    }

    /* ADDED TO PROJECT TEMPLATE FOR TUTORIAL, step 8 */
    private void countMe(View view) {
        String countString = showCount.getText().toString();
        Integer count = Integer.parseInt(countString);
        count++;
        showCount.setText(count.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}