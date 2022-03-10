package com.mangooa.mymang.mymango.data;

import static com.mangooa.mymang.mymango.activitys.MangoActivity.gd;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mangooa.mymang.mymango.R;
import com.mangooa.mymang.mymango.activitys.MangoActivity;
import com.mangooa.mymang.mymango.activitys.ReadMangaActivity;
import com.mangooa.mymang.mymango.model.ChapterMango;

import java.util.ArrayList;

public class ChapterMangoAdapter extends RecyclerView.Adapter<ChapterMangoAdapter.ChapterMangoViewHoldr> {

    Context context;
    public static MangoActivity mangoActivity;
    public static ArrayList<ChapterMango> arrayListChapter;
    public static String pat;


    public ChapterMangoAdapter(Context context, ArrayList<ChapterMango> arrayListChapter) {
        this.context = context;
        this.arrayListChapter = arrayListChapter;
    }

    @NonNull
    @Override
    public ChapterMangoViewHoldr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.chaptermangoadapter, parent, false);

        return new ChapterMangoViewHoldr(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterMangoViewHoldr holder, int position) {

        ChapterMango chapterMango = arrayListChapter.get(position);
        String urlMngo = chapterMango.getUrlMngo();
        int pos = position;
        pat = chapterMango.getPart();

        holder.buttonChapter.setText(pat);
        holder.buttonChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mangoActivity.getApplicationContext(), ReadMangaActivity.class);
                intent.putExtra("loadd", urlMngo);
                intent.putExtra("position", pos);
                intent.putExtra("id", gd);
                mangoActivity.startActivity(intent);
                Log.d("weq", urlMngo + "wwww");
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayListChapter.size();
    }

    public class ChapterMangoViewHoldr extends RecyclerView.ViewHolder {

        Button buttonChapter;

        public ChapterMangoViewHoldr(@NonNull View itemView) {
            super(itemView);
            ReadMangaActivity.b = 0;
            buttonChapter = itemView.findViewById(R.id.buttonChapter);
        }
    }
}
