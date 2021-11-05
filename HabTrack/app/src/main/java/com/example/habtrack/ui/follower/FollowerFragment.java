package com.example.habtrack.ui.follower;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.habtrack.databinding.FragmentFollowerBinding;

public class FollowerFragment extends Fragment {

    private FollowerViewModel followerViewModel;
    private FragmentFollowerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        followerViewModel =
                new ViewModelProvider(this).get(FollowerViewModel.class);

        binding = FragmentFollowerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textFollower;
        followerViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}