package com.mangooa.mymang.mymango.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mangooa.mymang.mymango.R;
import com.mangooa.mymang.mymango.activitys.AllMangaActivity;


public class AllMangoFragment extends Fragment {
    public static RecyclerView recyclerView;
    public static AllMangaActivity allMangaActivity;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_mango, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewChapter);




     return view;
    }
}