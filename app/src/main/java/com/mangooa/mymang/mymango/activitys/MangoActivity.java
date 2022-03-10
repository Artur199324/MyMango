package com.mangooa.mymang.mymango.activitys;

import static com.mangooa.mymang.mymango.activitys.BookmarksActivity.fl;
import static com.mangooa.mymang.mymango.fragment.FragmentChapter.chapterMango;
import static com.mangooa.mymang.mymango.fragment.FragmentChapter.mangoActivity;
import static com.mangooa.mymang.mymango.fragment.FragmentDescription.textBigText;
import static com.mangooa.mymang.mymango.utils.Host.mangoFulQuiry;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.mangooa.mymang.mymango.R;
import com.mangooa.mymang.mymango.data.ChapterMangoAdapter;
import com.mangooa.mymang.mymango.model.ChapterMango;
import com.mangooa.mymang.mymango.room.Entity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MangoActivity extends AppCompatActivity {

    public static String gd;
    private String s;
    private String part = "-";
    private ImageView imagePosterBig, imageViewСhapter;
    private TextView textViewTitle;
    private String urlMango;
    private String url;
    private String urlFull = null;
    private ArrayList<Entity> saveUrl;
    public static String description;
    public static Entity entity;
    public static int idEntity;
    public static boolean tt = false;


    public static ArrayList<ChapterMango> arrayListChapter;
    NavController navController;
    Button buttonDiscript, buttonChapter, mangoSaveButton, mangoDellButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(1024);
        setContentView(R.layout.mango_activity);
        imagePosterBig = findViewById(R.id.imagePosterBig);
        textViewTitle = findViewById(R.id.textViewTitle);
        mangoActivity = this;
        ChapterMangoAdapter.mangoActivity = this;
        arrayListChapter = new ArrayList<>();
        buttonDiscript = findViewById(R.id.buttonDiscript);
        buttonChapter = findViewById(R.id.buttonChapter);
        mangoSaveButton = findViewById(R.id.mangoSaveButton);
        mangoDellButton = findViewById(R.id.mangoDellButton);
        imageViewСhapter = findViewById(R.id.imageViewСhapter);
        gd = getIntent().getStringExtra("id");
        navController = Navigation.findNavController(this, R.id.dydsb);
        ReadMangaActivity.idmg = gd;


        try {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    saveUrl = (ArrayList<Entity>) AllMangaActivity.mangoDataBase.daoTab().getEntity();

                    for (int i = 0; i < saveUrl.size(); i++) {
                        if (saveUrl.get(i).mangoSave.contains(gd)) {
                            idEntity = saveUrl.get(i).id;
                            mangoSaveButton.setVisibility(View.INVISIBLE);
                            mangoDellButton.setVisibility(View.VISIBLE);
                            Entity ent = AllMangaActivity.mangoDataBase.daoTab().getEntityId(idEntity);
                            if (ent.getChapterMangoSave() != null) {
                                imageViewСhapter.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                }
            }).start();
        } catch (Exception e) {

        }


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(mangoFulQuiry + gd).openConnection();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    s = bufferedReader.readLine();


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String title = jsonObject.getString("ru_name");
                                description = jsonObject.getString("description");
                                String urlImg = jsonObject.getString("image_url");
                                urlMango = jsonObject.getString("url");
                                JSONArray jsonArrayChapters = jsonObject.getJSONArray("chapters");
                                for (int i = 0; i < jsonArrayChapters.length(); i++) {
                                    JSONObject jsonObjectChapters = jsonArrayChapters.getJSONObject(i);
                                    url = jsonObjectChapters.getString("url");
                                    urlFull = urlMango + url + "/";
                                    part = jsonObjectChapters.getString("name");
                                    chapterMango = new ChapterMango();
                                    chapterMango.setUrlMngo(urlFull);
                                    chapterMango.setPart(part);
                                    arrayListChapter.add(chapterMango);
                                }

                                textViewTitle.setText(title);
                                textBigText.setText(description);
                                Picasso.get().load(urlImg).fit().centerInside().into(imagePosterBig);
                                if (part == "-") {
                                    tt = true;
                                }


                            } catch (Exception e) {
                                Log.d("weq", "2");
                            }
                        }

                    });

                } catch (Exception e) {

                    Toast toast = Toast.makeText(getApplicationContext(), R.string.nointernetconnection, Toast.LENGTH_LONG);
                    toast.show();

                }


            }
        }).start();


        buttonDiscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                navController.navigate(R.id.fragmentDescription);
            }
        });

        buttonChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                navController.navigate(R.id.fragmentChapter);
            }
        });

        mangoSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        entity = new Entity(mangoFulQuiry + gd);
                        AllMangaActivity.mangoDataBase.daoTab().entity(entity);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mangoSaveButton.setVisibility(View.INVISIBLE);
                                mangoDellButton.setVisibility(View.VISIBLE);
                                Toast toast = Toast.makeText(getApplicationContext(), R.string.toastStringSave, Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });

                    }
                }).start();
            }
        });


        mangoDellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mangoSaveButton.setVisibility(View.VISIBLE);
                mangoDellButton.setVisibility(View.INVISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AllMangaActivity.mangoDataBase.daoTab().deleteById(idEntity);
                    }
                }).start();
                imageViewСhapter.setVisibility(View.INVISIBLE);
                Toast toast = Toast.makeText(getApplicationContext(), R.string.toastStringDell, Toast.LENGTH_LONG);
                toast.show();
            }
        });

        imageViewСhapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Entity entity = AllMangaActivity.mangoDataBase.daoTab().getEntityId(idEntity);
                        Log.d("eeee", entity.getChapterMangoSave());
                        Log.d("eeee", entity.getPageNumber() + "");
                        String load = entity.getChapterMangoSave();
                        ReadMangaActivity.b = entity.getPageNumber();
                        ReadMangaActivity.position = entity.getPosition();
                        Intent intent = new Intent(mangoActivity.getApplicationContext(), ReadMangaActivity.class);
                        intent.putExtra("loadd", load);
                        startActivity(intent);
                        finishAffinity();


                    }
                }).start();

            }
        });

    }


    @Override
    public void onBackPressed() {
        if (fl == true) {
            startActivity(new Intent(getApplicationContext(), BookmarksActivity.class));
            finishAffinity();
            fl = false;
        } else {
            if (ReadMangaActivity.ff == true) {
                startActivity(new Intent(getApplicationContext(), AllMangaActivity.class));
                finishAffinity();
                ReadMangaActivity.ff = false;
            } else {
                super.onBackPressed();
            }

        }
    }
}
