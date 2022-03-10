package com.mangooa.mymang.mymango.fragment;

import static com.mangooa.mymang.mymango.activitys.MangoActivity.tt;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mangooa.mymang.mymango.R;
import com.mangooa.mymang.mymango.activitys.MangoActivity;
import com.mangooa.mymang.mymango.data.ChapterMangoAdapter;
import com.mangooa.mymang.mymango.model.ChapterMango;


public class FragmentChapter extends Fragment {

    public static RecyclerView recyclerViewChapter;
    public static MangoActivity mangoActivity;
    public static ChapterMango chapterMango;
    public static ChapterMangoAdapter chapterMangoAdapter;
    private TextView textViewNoChapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chapter, container, false);

        recyclerViewChapter = view.findViewById(R.id.recyclerViewChapter);
        textViewNoChapter = view.findViewById(R.id.textViewNoChapter);

        if (tt == true) {
            recyclerViewChapter.setVisibility(View.INVISIBLE);
            textViewNoChapter.setVisibility(View.VISIBLE);
            tt = false;


        } else {
            recyclerViewChapter.hasFixedSize();
            recyclerViewChapter.setLayoutManager(new LinearLayoutManager(mangoActivity));
            chapterMangoAdapter = new ChapterMangoAdapter(mangoActivity, MangoActivity.arrayListChapter);
            recyclerViewChapter.setAdapter(chapterMangoAdapter);
        }


        return view;
    }
}