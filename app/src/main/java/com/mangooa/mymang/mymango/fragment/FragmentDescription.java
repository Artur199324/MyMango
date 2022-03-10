package com.mangooa.mymang.mymango.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mangooa.mymang.mymango.R;
import com.mangooa.mymang.mymango.activitys.MangoActivity;


public class FragmentDescription extends Fragment {
    public static TextView textBigText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_description, container, false);
        textBigText = view.findViewById(R.id.textBigText);
        textBigText.setText(MangoActivity.description);
        return view;
    }
}