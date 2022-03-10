package com.mangooa.mymang.mymango.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mangooa.mymang.mymango.R;
import com.mangooa.mymang.mymango.activitys.AllMangaActivity;


public class SearchFragment extends Fragment {

    public static RecyclerView recyclerViewSearch;
    public static AllMangaActivity allMangaActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.fragment_search, container, false);
       recyclerViewSearch = view.findViewById(R.id.recyclerViewSearch);
        recyclerViewSearch.hasFixedSize();
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(allMangaActivity.getApplicationContext()));
       return view;
    }
}