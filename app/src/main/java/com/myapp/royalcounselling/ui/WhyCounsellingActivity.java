package com.myapp.royalcounselling.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.myapp.royalcounselling.R;

public class WhyCounsellingActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_why_counselling, container, false);
        Button whyCounselling;
        whyCounselling = rootView.findViewById(R.id.btn_why_counselling_register);
        whyCounselling.setOnClickListener(v -> {
            Fragment fragment = new DisplaySeminarFragment();
            FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
        });
        return rootView;
    }
}