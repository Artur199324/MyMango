package com.mangooa.mymang.mymango.activitys;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.mangooa.mymang.mymango.R;
import com.mangooa.mymang.mymango.data.MangoAdapter;
import com.mangooa.mymang.mymango.model.Mango;
import com.mangooa.mymang.mymango.room.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BookmarksActivity extends AppCompatActivity implements MangoAdapter.OnItemClickListener {

    private ImageView imageHome;
    private RecyclerView recyclerView;
    private ArrayList<Entity> urlSave;
    private TextView textViewNosave;
    private MangoAdapter mangoAdapter;
    private RequestQueue requestQueue;
    private String str;
    private String title;
    private String urlImg;
    private String year;
    private String id;
    private int top;
    private Mango mango;
    public static boolean fl = false;
    private ArrayList<Mango> mangoss;
    final int DIALOG_EXIT = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmarks);
        getWindow().addFlags(1024);
        imageHome = findViewById(R.id.imageHome);
        recyclerView = findViewById(R.id.recyclerViewChapter);
        recyclerView.hasFixedSize();
        mangoss = new ArrayList<>();
        textViewNosave = findViewById(R.id.textViewNosave);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestQueue = Volley.newRequestQueue(this);



        getBookmarks();

        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AllMangaActivity.class));
                finishAffinity();
            }
        });
    }

    private void getBookmarks() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    int size = AllMangaActivity.mangoDataBase.daoTab().getEntity().size();

                    if (size == 0) {

                        textViewNosave.setVisibility(View.VISIBLE);

                    } else {
                        for (int i = 0; i < size; i++) {
                            urlSave = (ArrayList<Entity>) AllMangaActivity.mangoDataBase.daoTab().getEntity();

                            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(urlSave.get(i).mangoSave).openConnection();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                            str = bufferedReader.readLine();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonObject = new JSONObject(str);
                                        title = jsonObject.getString("ru_name");
                                        urlImg = jsonObject.getString("image_url");
                                        id = jsonObject.getString("id");
                                        year = jsonObject.getString("year");
                                        ReadMangaActivity.idmg = id;
                                        mango = new Mango();
                                        mango.setTitle(title);
                                        mango.setYear(year);
                                        mango.setImgUrl(urlImg);
                                        mango.setId(id);
                                        mangoss.add(mango);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mangoAdapter = new MangoAdapter(BookmarksActivity.this, mangoss);
                                mangoAdapter.setOnItemClickListener((MangoAdapter.OnItemClickListener) BookmarksActivity.this);
                                if (top != 0) {
                                    recyclerView.smoothScrollToPosition(top); // for top
                                    Log.d("www", top + "");
                                }
                                recyclerView.setAdapter(mangoAdapter);
                            }
                        });
                    }

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


    @Override
    public void onItemClick(int position) {
        top = position;
        Mango mango = null;
        for (int i = 0; i < mangoss.size(); i++) {
            mango = mangoss.get(position);
        }
        Intent intent = new Intent(this, MangoActivity.class);
        intent.putExtra("id", mango.getId());
        startActivity(intent);
        fl = true;

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
