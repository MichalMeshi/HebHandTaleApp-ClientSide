package com.example.test1;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.test1.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hide the ProgressBar initially
        binding.progressBar.setVisibility(View.VISIBLE);

        // Simulate a loading process
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // After the loading time is done, navigate to the FirstFragment
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        }, 8000); // 2000 milliseconds (2 seconds) - Change this value as per your requirement
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
