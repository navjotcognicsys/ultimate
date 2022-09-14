package com.todocode.ultimequiz.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.todocode.ultimequiz.Adapters.QuizAudioQuestionsAdapter;
import com.todocode.ultimequiz.Adapters.QuizTextQuestionsAdapter;
import com.todocode.ultimequiz.Models.AudioQuestion;
import com.todocode.ultimequiz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AudioQuestionsSetActivity extends AppCompatActivity {
    private String url;
    private RequestQueue queue;
    private List<AudioQuestion> textquestionsArrayList;
    private QuizAudioQuestionsAdapter quizTextQuestionsAdapter;
    // AdColony Banner
    public AdColonyAdView adView;
    private LinearLayout linearBannerAdContainer;
    public AdColonyAdOptions adColonyAdOptionsBanner;
    public static String[] AD_UNIT_ZONE_Ids;
    Banner startAppBanner;
    private SharedPreferences admobBannerShared, bannerTypeShared, adcolonyAppIdShared, adcolonyBannerShared, admobAppIdShared, facebookBannerShared,adcolonyInterstitialShared, adcolonyRewardShared;
    public static String active = "false";
    SharedPreferences langShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        langShared = getSharedPreferences("langShared", MODE_PRIVATE);
        Locale locale = new Locale(langShared.getString("langShared", ""));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_questions_set);
        StartAppAd.disableSplash();
        active = "true";
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.audio_questions));
        url = getResources().getString(R.string.domain_name);
        queue = Volley.newRequestQueue(this);
        // Get Image Questions
        RecyclerView quizzesRecyclerView = (RecyclerView) findViewById(R.id.quiz_audioquestions_recycler);
        textquestionsArrayList = new ArrayList<>();
        quizTextQuestionsAdapter = new QuizAudioQuestionsAdapter(textquestionsArrayList, this);
        quizzesRecyclerView.setAdapter(quizTextQuestionsAdapter);
        quizzesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        getQuizAudioQuestions();
        quizTextQuestionsAdapter.setOnItemClickListener(new QuizAudioQuestionsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/questions/audio/completed/check", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                if (AudioQuestionActivity.active.equals("false")) {
                                    Intent quiz = new Intent(AudioQuestionsSetActivity.this, AudioQuestionActivity.class);
                                    quiz.putExtra("id",String.valueOf(textquestionsArrayList.get(position).getId()));
                                    quiz.putExtra("rank",String.valueOf(textquestionsArrayList.get(position).getRank()));
                                    quiz.putExtra("category_id",String.valueOf(textquestionsArrayList.get(position).getCategory_id()));
                                    quiz.putExtra("subcategory_id",String.valueOf(textquestionsArrayList.get(position).getSubcategory_id()));
                                    quiz.putExtra("quiz_id",String.valueOf(textquestionsArrayList.get(position).getQuiz_id()));
                                    quiz.putExtra("points",String.valueOf(textquestionsArrayList.get(position).getPoints()));
                                    quiz.putExtra("seconds",String.valueOf(textquestionsArrayList.get(position).getSeconds()));
                                    quiz.putExtra("hint",textquestionsArrayList.get(position).getHint());
                                    quiz.putExtra("true_answer",textquestionsArrayList.get(position).getTrue_answer());
                                    quiz.putExtra("false1",textquestionsArrayList.get(position).getFalse1());
                                    quiz.putExtra("false2",textquestionsArrayList.get(position).getFalse2());
                                    quiz.putExtra("false3",textquestionsArrayList.get(position).getFalse3());
                                    quiz.putExtra("question_audio",textquestionsArrayList.get(position).getQuestion_audio_url());
                                    startActivity(quiz);
                                }
                            }
                            else if(success.equals("0")) {
                                Toast.makeText(AudioQuestionsSetActivity.this, getResources().getString(R.string.question_already_completed), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        SharedPreferences userSituationId;
                        userSituationId = getSharedPreferences("userId", MODE_PRIVATE);
                        params.put("player_id", userSituationId.getString("userId", ""));
                        params.put("question_id", String.valueOf(textquestionsArrayList.get(position).getId()));
                        params.put("key", getResources().getString(R.string.api_secret_key));
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(AudioQuestionsSetActivity.this);
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            }
        });
        admobAppIdShared = getSharedPreferences("admobAppIdShared", MODE_PRIVATE);
        admobBannerShared = getSharedPreferences("admobBannerShared", MODE_PRIVATE);
        facebookBannerShared = getSharedPreferences("facebookBannerShared", MODE_PRIVATE);
        adcolonyAppIdShared = getSharedPreferences("adcolonyAppIdShared", MODE_PRIVATE);
        adcolonyBannerShared = getSharedPreferences("adcolonyBannerShared", MODE_PRIVATE);
        adcolonyInterstitialShared = getSharedPreferences("adcolonyInterstitialShared", MODE_PRIVATE);
        adcolonyRewardShared = getSharedPreferences("adcolonyRewardShared", MODE_PRIVATE);
        AD_UNIT_ZONE_Ids = new String[] {adcolonyBannerShared.getString("adcolonyBannerShared", ""), adcolonyInterstitialShared.getString("adcolonyInterstitialShared", ""), adcolonyRewardShared.getString("adcolonyRewardShared", "")};
        // Configure AdColony Ads
        AdColony.configure(AudioQuestionsSetActivity.this, adcolonyAppIdShared.getString("adcolonyAppIdShared", ""), AD_UNIT_ZONE_Ids);
        // Bottom Banner Ads
        bannerTypeShared = getSharedPreferences("bannerTypeShared", MODE_PRIVATE);
        linearBannerAdContainer = (LinearLayout) findViewById(R.id.bottom_banner_main_activity);
        if (bannerTypeShared.getString("bannerTypeShared", "").equals("admob")) {
            // Show Admob Ads in LinearLayout
            MobileAds.initialize(AudioQuestionsSetActivity.this, admobAppIdShared.getString("admobAppIdShared", ""));
            AdView bannerAdmobAdView = new AdView(this);
            bannerAdmobAdView.setAdUnitId(admobBannerShared.getString("admobBannerShared", ""));
            bannerAdmobAdView.setAdSize(AdSize.FULL_BANNER);
            linearBannerAdContainer.setVisibility(View.VISIBLE);
            linearBannerAdContainer.addView(bannerAdmobAdView);
            linearBannerAdContainer.setGravity(Gravity.CENTER_HORIZONTAL);
            AdRequest adRequest = new AdRequest.Builder().build();
            bannerAdmobAdView.loadAd(adRequest);
            bannerAdmobAdView.setAdListener(new AdListener(){
                @Override
                public void onAdLoaded() {
                    linearBannerAdContainer.setVisibility(View.VISIBLE);
                }
            });
        } else if (bannerTypeShared.getString("bannerTypeShared", "").equals("facebook")) {
            // Show Facebook Ads in LinearLayout
            linearBannerAdContainer.setVisibility(View.VISIBLE);
            AudienceNetworkAds.initialize(this);
            AdSettings.addTestDevice("09a77aa6-326d-45b1-bf3c-55b62bbebc3c");
            com.facebook.ads.AdView facebookAdView = new com.facebook.ads.AdView(this, facebookBannerShared.getString("facebookBannerShared", ""), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
            linearBannerAdContainer.addView(facebookAdView);
            facebookAdView.loadAd();
        } else if (bannerTypeShared.getString("bannerTypeShared", "").equals("adcolony")) {
            // Show Adcolony Ads in LinearLayout
            linearBannerAdContainer.setVisibility(View.VISIBLE);
            AdColonyAdViewListener listener1 = new AdColonyAdViewListener() {
                @Override
                public void onRequestFilled(AdColonyAdView adColonyAdView) {
                    linearBannerAdContainer.addView(adColonyAdView);
                    adView = adColonyAdView;
                }

            };
            AdColony.requestAdView(adcolonyBannerShared.getString("adcolonyBannerShared", ""), listener1, AdColonyAdSize.BANNER, adColonyAdOptionsBanner);

        } else if (bannerTypeShared.getString("bannerTypeShared", "").equals("startapp")) {
            // Show StartApp Ads
            startAppBanner = (Banner) findViewById(R.id.startapp_banner);
            startAppBanner.setVisibility(View.VISIBLE);
        }
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

    private void getQuizAudioQuestions() {
        String key = getResources().getString(R.string.api_secret_key);
        String urlApi = url+"/api/quiz/daily/questions/audio/"+getIntent().getStringExtra("quiz_id")+"/"+key;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Integer id = jsonObject.getInt("id");
                                String hint = jsonObject.getString("hint");
                                String question_audio_url = jsonObject.getString("question_audio_url");
                                String true_answer = jsonObject.getString("true_answer");
                                String false1 = jsonObject.getString("false1");
                                String false2 = jsonObject.getString("false2");
                                String false3 = jsonObject.getString("false3");
                                String premium_or_not = jsonObject.getString("premium_or_not");
                                Integer category_id = jsonObject.getInt("category_id");
                                Integer subcategory_id = jsonObject.getInt("subcategory_id");
                                Integer quiz_id = jsonObject.getInt("quiz_id");
                                Integer points = jsonObject.getInt("points");
                                Integer seconds = jsonObject.getInt("seconds");
                                Integer rank = i+1;
                                textquestionsArrayList.add(new AudioQuestion("image", hint, question_audio_url, true_answer, false1, false2, false3, premium_or_not, id,category_id,subcategory_id, quiz_id, points, seconds,rank));
                            }
                            quizTextQuestionsAdapter.notifyDataSetChanged();
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
}