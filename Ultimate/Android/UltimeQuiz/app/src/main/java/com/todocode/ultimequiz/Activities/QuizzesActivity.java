package com.todocode.ultimequiz.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.startapp.sdk.adsbase.StartAppAd;
import com.todocode.ultimequiz.Adapters.QuizzesAdapter;
import com.todocode.ultimequiz.Adapters.SubcategoriesAdapter;
import com.todocode.ultimequiz.Models.Quiz;
import com.todocode.ultimequiz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class QuizzesActivity extends AppCompatActivity {
    private ArrayList<Object> quizzesArrayList;
    private QuizzesAdapter quizzesAdapter;
    private String url;
    private RequestQueue queue;
    public static String active = "false";
    public static final int NUMBER_OF_ADS = 4;
    private AdLoader adLoader;
    private final List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    private SharedPreferences admobAppIdShared, admobNativeShared;

    private SharedPreferences langShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        langShared = getSharedPreferences("langShared", MODE_PRIVATE);
        Locale locale = new Locale(langShared.getString("langShared", ""));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizzes);
        StartAppAd.disableSplash();
        active = "true";
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("subcategory_name"));
        admobAppIdShared = getSharedPreferences("admobAppIdShared", MODE_PRIVATE);
        admobNativeShared = getSharedPreferences("admobNativeShared", MODE_PRIVATE);
        url = getResources().getString(R.string.domain_name);
        queue = Volley.newRequestQueue(this);
        // Get Featured Categories
        RecyclerView quizzesRecyclerView = (RecyclerView) findViewById(R.id.quizzes_recycler);
        quizzesArrayList = new ArrayList<>();
        quizzesAdapter = new QuizzesAdapter(this, quizzesArrayList);
        quizzesRecyclerView.setAdapter(quizzesAdapter);
        quizzesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        quizzesRecyclerView.setHasFixedSize(true);
        // Admob Native Ads in RecyclerView
        MobileAds.initialize(com.todocode.ultimequiz.Activities.QuizzesActivity.this, admobAppIdShared.getString("admobAppIdShared", ""));
        getQuizzes();
        loadAdmobNativeAds();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        active = "false";
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        StartAppAd.disableSplash();
    }

    private void getQuizzes() {
        String key = getResources().getString(R.string.api_secret_key);
        String subcategoryId = getIntent().getStringExtra("id");
        String urlApi = url+"/api/quizzes/"+subcategoryId+"/"+key;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Integer id = jsonObject.getInt("id");
                                String quizName = jsonObject.getString("quiz_name");
                                String img = url+"/uploads/quizzes/"+jsonObject.getString("quiz_image_url");
                                Integer subcategoryId = jsonObject.getInt("subcategory_id");
                                String subcategoryName = jsonObject.getString("subcategory_name");
                                Integer categoryId = jsonObject.getInt("category_id");
                                String categoryName = jsonObject.getString("category_name");
                                Integer quizNumberOfQuestions = jsonObject.getInt("quiz_number_of_questions");
                                Integer textquizN = jsonObject.getInt("quiz_number_of_text_questions");
                                Integer imagequizN = jsonObject.getInt("quiz_number_of_image_questions");
                                Integer audioQuizN = jsonObject.getInt("quiz_number_of_audio_questions");
                                quizzesArrayList.add(new Quiz(String.valueOf(id), quizName, img, String.valueOf(subcategoryId), subcategoryName, String.valueOf(categoryId), categoryName, quizNumberOfQuestions, textquizN, imagequizN, audioQuizN));
                            }
                            quizzesAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void insertAdsInMenuItems() {
        if (quizzesArrayList.size() <= 3) {
            return;
        }
        if (quizzesArrayList.size() > 3) {
            int offset = (quizzesArrayList.size() / mNativeAds.size())+1;
            int index = 2;
            for (UnifiedNativeAd ad : mNativeAds) {
                quizzesArrayList.add(index, ad);
                index = index + offset;
            }
            quizzesAdapter.notifyDataSetChanged();
        }
    }

    private void loadAdmobNativeAds() {
        AdLoader.Builder builder = new AdLoader.Builder(this, admobNativeShared.getString("admobNativeShared", ""));

        adLoader = builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        mNativeAds.add(unifiedNativeAd);
                        if (!adLoader.isLoading()) {
                            insertAdsInMenuItems();
                            Log.e("adloader", "done");
                        }
                    }
                }).withAdListener(
                new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        Log.e("MainActivity", "The previous native ad failed to load. Attempting to"
                                + " load another.");
                        if (!adLoader.isLoading()) {
                            insertAdsInMenuItems();
                            Log.e("adloader", "done");
                        }
                    }
                }).build();
        // Load the Native ads.
        adLoader.loadAds(new AdRequest.Builder().build(), NUMBER_OF_ADS);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                active = "false";
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}