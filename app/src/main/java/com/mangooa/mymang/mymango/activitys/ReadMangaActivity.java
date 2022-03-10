package com.mangooa.mymang.mymango.activitys;


import static com.mangooa.mymang.mymango.activitys.MangoActivity.entity;
import static com.mangooa.mymang.mymango.activitys.MangoActivity.gd;
import static com.mangooa.mymang.mymango.activitys.MangoActivity.idEntity;
import static com.mangooa.mymang.mymango.fragment.FragmentChapter.chapterMangoAdapter;
import static com.mangooa.mymang.mymango.fragment.FragmentChapter.mangoActivity;
import static com.mangooa.mymang.mymango.fragment.FragmentChapter.recyclerViewChapter;
import static com.mangooa.mymang.mymango.utils.Host.mangoFulQuiry;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mangooa.mymang.mymango.R;
import com.mangooa.mymang.mymango.data.ChapterMangoAdapter;
import com.mangooa.mymang.mymango.model.ChapterMango;
import com.mangooa.mymang.mymango.room.Entity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadMangaActivity extends AppCompatActivity {
    static String loadd;
    static String urlImg[];
    static ArrayList<String> arrayListUrl;
    WebView webViewmango;
    public static String idmg;
    boolean add = false;
    boolean aloo = false;
    public static boolean ooo = false;
    int uuuu = 0;
    public static int position;
    final int DIALOG = 1;
    boolean pp = false;
    ArrayList<ChapterMango> arrayChapter;
    Button buttonNext, buttonPrevious, buttonnextchapter;
    ConstraintLayout constraiMango;
    int a;
    public static int b = 0;
    int bbbb;
    int rrr;
    ImageView homeImageView, imageViewbookmark, imageViewSetings, imageViewСhapter;
    public static boolean ff = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(1024);
        setContentView(R.layout.readmango);
        loadd = getIntent().getStringExtra("loadd");
        position = getIntent().getIntExtra("position", position + 1);

        arrayChapter = ChapterMangoAdapter.arrayListChapter;
        Log.d("yyyyy", loadd);
        Log.d("yyyyy", position + "");
        homeImageView = findViewById(R.id.homeImageView);
        buttonNext = findViewById(R.id.buttonNext);
        buttonPrevious = findViewById(R.id.buttonPrevious);
        constraiMango = findViewById(R.id.constraiMango);
        imageViewbookmark = findViewById(R.id.imageViewbookmark);
        imageViewSetings = findViewById(R.id.imageViewSetings);
        imageViewСhapter = findViewById(R.id.imageViewСhapter);
        buttonnextchapter = findViewById(R.id.buttonnextchapter);
        arrayListUrl = new ArrayList<String>();
        webViewmango = findViewById(R.id.webViewmango);
        webViewmango.getSettings().setLoadWithOverviewMode(true);
        webViewmango.getSettings().setUseWideViewPort(true);

        webViewmango.setWebViewClient(new WebViewClient() {

                                          public void onPageFinished(WebView view, String url) {
                                              bbbb = (int) (webViewmango.getContentHeight() * webViewmango.getScale());
                                              rrr = webViewmango.getHeight();

                                              if (bbbb == 0) {
                                                  loadMango();
                                              } else {
                                                  webViewmango.setVisibility(View.VISIBLE);
                                                  if (bbbb <= rrr + 100) {
                                                      touchMango();
                                                  } else {
                                                      scrollMango();
                                                  }
                                              }
                                          }

                                      }

        );

        if (arrayListUrl.size() == 1) {
            aloo = true;
        }
        pars(loadd);

        updButtonSave();


        buttonnextchapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Log.d("yyyyy", position + 1 + "");
                    Intent intent = new Intent(getApplicationContext(), ReadMangaActivity.class);
                    intent.putExtra("loadd", ChapterMangoAdapter.arrayListChapter.get(position + 1).getUrlMngo());
                    intent.putExtra("position", position + 1);
                    startActivity(intent);
                    finishAffinity();
                    b = 0;
                } catch (Exception e) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.ower, Toast.LENGTH_LONG);
                    toast.show();
                    Intent intent = new Intent(getApplicationContext(), MangoActivity.class);
                    intent.putExtra("id", gd);
                    startActivity(intent);
                    finishAffinity();
                    ooo = true;
                }


            }
        });

        homeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ff = true;
                onBackPressed();
            }
        });

        imageViewСhapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG);
            }
        });


        imageViewbookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            List<Entity> list = AllMangaActivity.mangoDataBase.daoTab().getEntity();
                            Log.d("uuuu", idmg);
                            Log.d("uuuu", list.size() + "");
                            boolean a = false;
                            if (list.size() != 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).mangoSave.contains(idmg)) {
                                        Log.d("uuuu", "yas");
                                        a = true;
                                        break;
                                    } else {
                                        a = false;
                                        Log.d("uuuu", "no");

                                    }

                                }
                            } else {
                                entity = new Entity(mangoFulQuiry + gd);
                                entity.setChapterMangoSave(loadd);
                                entity.setPageNumber(b);
                                entity.setPosition(position);
                                AllMangaActivity.mangoDataBase.daoTab().entity(entity);
                                imageViewbookmark.setImageResource(R.drawable.ic_launcher_in_foreground);
                                a = true;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ReadMangaActivity.this, R.string.toastStringSaveInRead, Toast.LENGTH_LONG).show();
                                    }
                                });

                                Log.d("uuuu", "3");
                            }


                            if (a) {
                                AllMangaActivity.mangoDataBase.daoTab().update(loadd, b, position, idEntity);
                                imageViewbookmark.setImageResource(R.drawable.ic_launcher_in_foreground);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ReadMangaActivity.this, R.string.toastStringSaveInRead2, Toast.LENGTH_LONG).show();
                                    }
                                });

                            } else {
                                Entity entity = new Entity(mangoFulQuiry + gd);
                                entity.setChapterMangoSave(loadd);
                                entity.setPageNumber(b);
                                entity.setPosition(position);
                                AllMangaActivity.mangoDataBase.daoTab().entity(entity);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ReadMangaActivity.this, R.string.toastStringSaveInRead, Toast.LENGTH_LONG).show();
                                    }
                                });

                                imageViewbookmark.setImageResource(R.drawable.ic_launcher_in_foreground);
                            }


                        } catch (Exception e) {

                            Log.d("uuuu", (e).getMessage() + "222");
                        }


                    }
                }).start();
            }
        });
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(R.string.chapternumberstr);
        try {
            String chapter = MangoActivity.arrayListChapter.get(position).getPart();
            adb.setMessage(chapter);
        } catch (Exception e) {

            try {
                String chapter = MangoActivity.arrayListChapter.get(position - 1).getPart();
                adb.setMessage(chapter);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }


        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);
        recyclerViewChapter = view.findViewById(R.id.recyclerViewChapter);
        recyclerViewChapter.hasFixedSize();
        recyclerViewChapter.setLayoutManager(new LinearLayoutManager(mangoActivity.getApplicationContext()));
        chapterMangoAdapter = new ChapterMangoAdapter(mangoActivity.getApplicationContext(), MangoActivity.arrayListChapter);
        recyclerViewChapter.setAdapter(chapterMangoAdapter);
        recyclerViewChapter.hasFixedSize();
        recyclerViewChapter.setLayoutManager(new LinearLayoutManager(mangoActivity.getApplicationContext()));
        chapterMangoAdapter = new ChapterMangoAdapter(mangoActivity.getApplicationContext(), MangoActivity.arrayListChapter);
        recyclerViewChapter.setAdapter(chapterMangoAdapter);
        adb.setView(view);
        return adb.create();
    }


    @Override
    public void onBackPressed() {
        if (ff) {
            Intent intent = new Intent(this, MangoActivity.class);
            intent.putExtra("id", gd);
            startActivity(intent);
            finishAffinity();
            ooo = true;
        }
    }

    public void loadMango() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                webViewmango.loadUrl(arrayListUrl.get(b));
                a = arrayListUrl.size();
            }
        }, 1800);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void scrollMango() {
        webViewmango.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {

                add = true;

                int aa = (int) (webViewmango.getContentHeight() * webViewmango.getScale());
                int cc = (int) (webViewmango.getHeight() + webViewmango.getScrollY());
                if (aa <= cc + 100) {

                    homeImageView.setVisibility(View.INVISIBLE);
                    imageViewbookmark.setVisibility(View.INVISIBLE);
                    imageViewSetings.setVisibility(View.INVISIBLE);
                    imageViewСhapter.setVisibility(View.INVISIBLE);

                    if (aloo) {
                        buttonNext.setVisibility(View.INVISIBLE);
                        buttonPrevious.setVisibility(View.INVISIBLE);
                        buttonnextchapter.setVisibility(View.VISIBLE);
                        homeImageView.setVisibility(View.VISIBLE);
                    } else {
                        buttonNext.setVisibility(View.VISIBLE);
                        buttonPrevious.setVisibility(View.VISIBLE);
                    }

                    buttonNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                if (b != a - 1) {
                                    b++;
                                    updButtonSave();
                                    webViewmango.loadUrl(arrayListUrl.get(b));
                                    int ddd = arrayListUrl.size();
                                    int aaa = b + 1;
                                    Log.d("qqqq", ddd + "");
                                    Log.d("qqqq", aaa + "");
                                    if (ddd == aaa) {
                                        aloo = true;
                                    }

                                }

                            } catch (Exception e) {


                            }

                        }
                    });

                    buttonPrevious.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                if (b != a) {
                                    b--;
                                    webViewmango.loadUrl(arrayListUrl.get(b));
                                    updButtonSave();
                                    aloo = false;

                                }
                            } catch (Exception e) {

                                b = 0;
                            }

                        }
                    });
                } else {

                    buttonNext.setVisibility(View.INVISIBLE);
                    buttonPrevious.setVisibility(View.INVISIBLE);
                    buttonnextchapter.setVisibility(View.INVISIBLE);
                    homeImageView.setVisibility(View.VISIBLE);
                    imageViewbookmark.setVisibility(View.VISIBLE);
                    imageViewSetings.setVisibility(View.VISIBLE);
                    imageViewСhapter.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    private void touchMango() {
        webViewmango.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int ddd = arrayListUrl.size();
                int aaa = b + 1;
                if (ddd == aaa) {
                    aloo = true;
                }
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        switch (uuuu) {
                            case 0:
                                imageViewbookmark.setVisibility(View.INVISIBLE);
                                imageViewSetings.setVisibility(View.INVISIBLE);
                                imageViewСhapter.setVisibility(View.INVISIBLE);
                                homeImageView.setVisibility(View.INVISIBLE);
                                if (aloo == true) {
                                    buttonNext.setVisibility(View.INVISIBLE);
                                    buttonPrevious.setVisibility(View.INVISIBLE);
                                    buttonnextchapter.setVisibility(View.VISIBLE);
                                    homeImageView.setVisibility(View.VISIBLE);
                                } else {
                                    buttonNext.setVisibility(View.VISIBLE);
                                    buttonPrevious.setVisibility(View.VISIBLE);
                                }
                                uuuu = 1;
                                break;
                            case 1:
                                imageViewbookmark.setVisibility(View.VISIBLE);
                                imageViewSetings.setVisibility(View.VISIBLE);
                                imageViewСhapter.setVisibility(View.VISIBLE);
                                homeImageView.setVisibility(View.VISIBLE);
                                buttonNext.setVisibility(View.INVISIBLE);
                                buttonPrevious.setVisibility(View.INVISIBLE);
                                buttonnextchapter.setVisibility(View.INVISIBLE);
                                uuuu = 0;
                                break;
                        }

                        break;
                }

                buttonNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (b != a - 1) {
                            b++;
                            bbbb = 0;
                            rrr = 0;
                            imageViewbookmark.setVisibility(View.VISIBLE);
                            imageViewSetings.setVisibility(View.VISIBLE);
                            imageViewСhapter.setVisibility(View.VISIBLE);
                            Intent intent = new Intent(getApplicationContext(), ReadMangaActivity.class);
                            intent.putExtra("loadd", loadd);
                            intent.putExtra("position", position);
                            intent.putExtra("bul", pp);
                            startActivity(intent);
                            finishAffinity();
                            homeImageView.setVisibility(View.VISIBLE);
                            buttonNext.setVisibility(View.INVISIBLE);
                            buttonPrevious.setVisibility(View.INVISIBLE);
                            uuuu = 0;
                            int ddd = arrayListUrl.size();
                            Log.d("dddd", ddd + "");
                            int aaa = b + 1;
                            Log.d("dddd", b + 1 + "");
                            if (ddd == aaa) {
                                aloo = true;

                            }

                        }
                    }
                });

                buttonPrevious.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            if (b != a - 1) {
                                b--;
                                bbbb = 0;
                                rrr = 0;
                                webViewmango.loadUrl(arrayListUrl.get(b));
                                homeImageView.setVisibility(View.VISIBLE);
                                imageViewbookmark.setVisibility(View.VISIBLE);
                                imageViewSetings.setVisibility(View.VISIBLE);
                                imageViewСhapter.setVisibility(View.VISIBLE);
                                buttonNext.setVisibility(View.INVISIBLE);
                                buttonPrevious.setVisibility(View.INVISIBLE);
                                updButtonSave();
                                uuuu = 0;
                            }
                        } catch (Exception e) {

                            b = 0;
                        }

                    }
                });
                return true;
            }
        });
    }


    public void pars(String loaa) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    Document doc = Jsoup.connect(loaa).get();
                    String ds = String.valueOf(doc.select("script"));
                    String ofdk[] = ds.split("initReader");
                    String ufdj[] = ofdk[1].split("\\u0028");
                    String ooo[] = ufdj[1].split("\\u005B");
                    for (int i = 0; i < ooo.length; i++) {
                        if (ooo[i].contains("'https:")) {
                            String dsds = ooo[i];
                            String ifkd[] = dsds.split(",");
                            String ut = ifkd[0] + ifkd[2];
                            String igf = ut.replace("'", "").replace("\"", "");
                            Log.d("weq", igf);
                            arrayListUrl.add(igf);

                        }
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.nointernetconnection, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }).start();

        loadMango();
    }


    private void updButtonSave() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Entity> list = AllMangaActivity.mangoDataBase.daoTab().getEntity();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).mangoSave.contains(idmg)) {
                        int aa = 0;
                        aa = list.get(i).id;
                        if (aa != 0) {
                            Entity entity = AllMangaActivity.mangoDataBase.daoTab().getEntityId(aa);
                            try {
                                if (entity.getChapterMangoSave().equals(loadd) && entity.getPageNumber() == b) {
                                    imageViewbookmark.setImageResource(R.drawable.ic_launcher_in_foreground);
                                } else {
                                    imageViewbookmark.setImageResource(R.drawable.ic_launcher_not_foreground);
                                }
                            } catch (Exception e) {

                            }
                        }

                    }
                }
            }
        }).start();
    }

}
