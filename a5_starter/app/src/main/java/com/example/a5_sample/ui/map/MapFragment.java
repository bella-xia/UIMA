package com.example.a5_sample.ui.map;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a5_sample.R;
import com.example.a5_sample.databinding.FragmentChatBinding;


public class MapFragment extends Fragment {

    private FragmentChatBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // COMMENTED OUT TO SIMPLIFY
        //       ChatViewModel notificationsViewModel =
        //               new ViewModelProvider(this).get(ChatViewModel.class);

        binding = FragmentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // ADDED THIS LINE TO AVOID USING THE ChatViewModel class
        binding.textChat.setText(R.string.map_tab_line);

        // COMMENTED OUT TO SIMPLIFY
        //       final TextView textView = binding.textNotifications;
        //       notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}