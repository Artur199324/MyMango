package com.mangooa.mymang.mymango.activitys;

import static com.mangooa.mymang.mymango.fragment.AllMangoFragment.recyclerView;
import static com.mangooa.mymang.mymango.fragment.SearchFragment.recyclerViewSearch;
import static com.mangooa.mymang.mymango.utils.Host.mangoFirstQuiru;
import static com.mangooa.mymang.mymango.utils.Host.mangoFirstQuiruPart2;
import static com.mangooa.mymang.mymango.utils.Host.mangoSearchstr;
import static com.mangooa.mymang.mymango.utils.Host.mangoSearchstrPart2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.mangooa.mymang.mymango.R;
import com.mangooa.mymang.mymango.data.MangoAdapter;
import com.mangooa.mymang.mymango.fragment.AllMangoFragment;
import com.mangooa.mymang.mymango.fragment.SearchFragment;
import com.mangooa.mymang.mymango.model.Mango;
import com.mangooa.mymang.mymango.room.MangoDataBase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class AllMangaActivity extends AppCompatActivity implements MangoAdapter.OnItemClickListener {



    public static MangoAdapter mangoAdapter;
    private ArrayList<Mango> mangos;
    private ArrayList<Mango> mangosSearch;
    private RequestQueue requestQueue;
    public static MangoDataBase mangoDataBase;
    private String s;
    private ImageView imageBookmarks, imageHome, imageViewSearch;
    private int count = 1;
    private Mango mango;
    final int DIALOG_EXIT = 1;
    public ConstraintLayout constLay;
    private EditText editTextSearch;
    private boolean searchmang = false;
    NavController navControllerAllMan;
    String urll;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_manga);
        getWindow().addFlags(1024);
        ReadMangaActivity.ooo = false;
        AllMangoFragment.allMangaActivity = this;
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SearchFragment.allMangaActivity = this;
        imageBookmarks = findViewById(R.id.bookmarks);
        imageHome = findViewById(R.id.imageHome);
        constLay = findViewById(R.id.constLay);
        editTextSearch = findViewById(R.id.editTextSearch);
        imageViewSearch = findViewById(R.id.imageViewSearch);
        mangos = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);
        navControllerAllMan = Navigation.findNavController(this, R.id.allMan);
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

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchmang = true;
                String str = charSequence.toString();
                try {

                     urll = mangoSearchstr + URLEncoder.encode(str, String.valueOf(StandardCharsets.UTF_8)) + mangoSearchstrPart2;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

              if (i2 == 0){
                 startActivity(new Intent(getApplicationContext(),AllMangaActivity.class));
                 finishAffinity();

              }else {
                  navControllerAllMan.navigate(R.id.searchFragment);
                  mangosSearch = new ArrayList<>();
                  new Thread(new Runnable() {
                      @Override
                      public void run() {

                          try {
                              HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(urll).openConnection();
                              BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                              String sss = bufferedReader.readLine();
                              Log.d("ooo",sss);
                              Log.d("oooo","---------------------------------------");

                              runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      try {
                                          JSONArray jsonArray = new JSONArray(sss);
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
                                              mangosSearch.add(mango);
                                          }

                                          mangoAdapter = new MangoAdapter(AllMangaActivity.this, mangosSearch);
                                          mangoAdapter.setOnItemClickListener((MangoAdapter.OnItemClickListener) AllMangaActivity.this);
                                          recyclerViewSearch.setAdapter(mangoAdapter);
                                      } catch (Exception e) {
                                      }

                                  }
                              });
                          }catch (Exception e){

                              Log.d("ooo" , e.getMessage().toString());
                          }
                      }
                  }).start();
              }

            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                                    recyclerView.smoothScrollToPosition(MangoAdapter.position);
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

        if (searchmang == false){
            for (int i = 0; i < mangos.size(); i++) {
                mango = mangos.get(position);
            }
        }else {
            for (int i = 0; i < mangosSearch.size(); i++) {
                mango = mangosSearch.get(position);
            }
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

