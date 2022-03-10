package com.mangooa.mymang.mymango.activitys;

import static com.mangooa.mymang.mymango.utils.Host.mangoFirstQuiru;
import static com.mangooa.mymang.mymango.utils.Host.mangoFirstQuiruPart2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.mangooa.mymang.mymango.R;
import com.mangooa.mymang.mymango.data.MangoAdapter;
import com.mangooa.mymang.mymango.model.Mango;
import com.mangooa.mymang.mymango.room.MangoDataBase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class AllMangaActivity extends AppCompatActivity implements MangoAdapter.OnItemClickListener {


    private RecyclerView recyclerView;
    private MangoAdapter mangoAdapter;
    private ArrayList<Mango> mangos;
    private RequestQueue requestQueue;
    public static MangoDataBase mangoDataBase;
    private String s;
    private ImageView imageBookmarks, imageHome ,imageViewSearch;
    private int count = 1;
    private Mango mango;
    final int DIALOG_EXIT = 1;
    public ConstraintLayout constLay;
    private EditText editTextSearch;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_manga);
        getWindow().addFlags(1024);
        ReadMangaActivity.ooo = false;
        recyclerView = findViewById(R.id.recyclerViewChapter);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        imageBookmarks = findViewById(R.id.bookmarks);
        imageHome = findViewById(R.id.imageHome);
        constLay = findViewById(R.id.constLay);
        editTextSearch = findViewById(R.id.editTextSearch);
        imageViewSearch = findViewById(R.id.imageViewSearch);
        mangos = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        getMango();

        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if (isLastItemDisplaying(recyclerView)) {
                    count++;
                    String page = String.valueOf(count);
                    String url = mangoFirstQuiru + page + mangoFirstQuiruPart2;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                                s = bufferedReader.readLine();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        try {
                                            JSONArray jsonArray = new JSONArray(s);
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String title = jsonObject.getString("ru_name");
                                                String year = jsonObject.getString("year");
                                                String imgUrl = jsonObject.getString("image_url");
                                                String id = jsonObject.getString("id");
                                                mango = new Mango();
                                                mango.setTitle(title);
                                                mango.setYear(year);
                                                mango.setImgUrl(imgUrl);
                                                mango.setId(id);
                                                mangos.add(mango);
                                            }

                                            mangoAdapter.setOnItemClickListener((MangoAdapter.OnItemClickListener) AllMangaActivity.this);

                                            if (MangoAdapter.position != 0) {
                                                recyclerView.smoothScrollToPosition(MangoAdapter.position);// for top
                                            }


                                            mangoAdapter.notifyDataSetChanged();

                                        } catch (Exception e) {
                                        }
                                    }
                                });


                            } catch (Exception e) {
                            }

                        }
                    }).start();

                }
            }

        });
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        MangoAdapter.position = 0;

                }
                return false;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                mangoDataBase = Room.databaseBuilder(getApplicationContext(),
                        MangoDataBase.class, "mango").build();
            }
        }).start();


        imageBookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BookmarksActivity.class));
                finishAffinity();
            }
        });

        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AllMangaActivity.class));
                MangoAdapter.position = 0;
                mangoAdapter.notifyDataSetChanged();
                finishAffinity();

            }
        });


        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });

    }


    private void getMango() {

        String page = String.valueOf(count);
        String url = mangoFirstQuiru + page + mangoFirstQuiruPart2;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    s = bufferedReader.readLine();
                    runOnUiThread(new Runnable() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void run() {

                            try {
                                JSONArray jsonArray = new JSONArray(s);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String title = jsonObject.getString("ru_name");
                                    String year = jsonObject.getString("year");
                                    String imgUrl = jsonObject.getString("image_url");
                                    String id = jsonObject.getString("id");
                                    mango = new Mango();
                                    mango.setTitle(title);
                                    mango.setYear(year);
                                    mango.setImgUrl(imgUrl);
                                    mango.setId(id);
                                    mangos.add(mango);
                                }

                                mangoAdapter = new MangoAdapter(AllMangaActivity.this, mangos);
                                mangoAdapter.setOnItemClickListener((MangoAdapter.OnItemClickListener) AllMangaActivity.this);

                                if (MangoAdapter.position != 0) {
                                    recyclerView.smoothScrollToPosition(MangoAdapter.position); // for top
                                    mangoAdapter.notifyDataSetChanged();

                                }
                                recyclerView.setAdapter(mangoAdapter);
                            } catch (Exception e) {
                            }

                        }
                    });


                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getApplicationContext(), R.string.nointernetconnection, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });

                }

            }
        }).start();

    }

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    @Override
    public void onItemClick(int position) {

        Mango mango = null;
        for (int i = 0; i < mangos.size(); i++) {
            mango = mangos.get(position);
        }

        Intent intent = new Intent(this, MangoActivity.class);
        intent.putExtra("id", mango.getId());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        showDialog(DIALOG_EXIT);
    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_EXIT) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle(R.string.exit);
            adb.setPositiveButton(R.string.yes, myClickListener);
            adb.setNegativeButton(R.string.no, myClickListener);
            adb.setCancelable(false);
            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            switch (i) {
                case Dialog.BUTTON_POSITIVE:
                    finishAffinity();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    break;

            }
        }
    };
}

