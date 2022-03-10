package com.mangooa.mymang.mymango.data;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mangooa.mymang.mymango.R;
import com.mangooa.mymang.mymango.model.Mango;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MangoAdapter extends RecyclerView.Adapter<MangoAdapter.MangoViewHolder> {

    Context context;
    ArrayList<Mango> mangos;
    private OnItemClickListener listener;
    public static int position;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MangoAdapter(Context context, ArrayList<Mango> mangos) {
        this.context = context;
        this.mangos = mangos;
    }

    @NonNull
    @Override
    public MangoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.mango_item, parent, false);

        return new MangoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MangoViewHolder holder, int position) {

        Mango carountMango = mangos.get(position);
        String title = carountMango.getTitle();
        String imgUrl = carountMango.getImgUrl();
        String year = carountMango.getYear();
        holder.textTitel.setText(title);
        holder.textYear.setText(year);
        Picasso.get().load(imgUrl).fit().centerInside().into(holder.imagePoster);
    }

    @Override
    public int getItemCount() {
        return mangos.size();
    }

    public class MangoViewHolder extends RecyclerView.ViewHolder {

        ImageView imagePoster;
        TextView textTitel, textYear;

        public MangoViewHolder(@NonNull View itemView) {
            super(itemView);

            imagePoster = itemView.findViewById(R.id.imageView6);
            textTitel = itemView.findViewById(R.id.textTitel);
            textYear = itemView.findViewById(R.id.textYear);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
