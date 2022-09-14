package com.todocode.ultimequiz.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.todocode.ultimequiz.Adapters.ContinueQuizzesAdapter;
import com.todocode.ultimequiz.Adapters.MyReferralsAdapter;
import com.todocode.ultimequiz.Adapters.MyWithdrawsAdapter;
import com.todocode.ultimequiz.Adapters.StartedQuizStatisticsAdapter;
import com.todocode.ultimequiz.Models.CompletedQuestion;
import com.todocode.ultimequiz.Models.ContinueQuiz;
import com.todocode.ultimequiz.Models.Referral;
import com.todocode.ultimequiz.Models.Withdraw;
import com.todocode.ultimequiz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MyStatisticsActivity extends AppCompatActivity {
    public static String active = "false";
    private List<ContinueQuiz> quizzesPagerList;
    private StartedQuizStatisticsAdapter quizzesMainViewPagerAdapter;
    private RecyclerView quizzesViewPager;
    private SharedPreferences userSituationId;
    // AdColony Banner
    public AdColonyAdView adView;
    private LinearLayout linearBannerAdContainer;
    public AdColonyAdOptions adColonyAdOptionsBanner;
    public static String[] AD_UNIT_ZONE_Ids;
    Banner startAppBanner;
    private SharedPreferences admobBannerShared, bannerTypeShared, adcolonyAppIdShared, adcolonyBannerShared, admobAppIdShared, facebookBannerShared,adcolonyInterstitialShared, adcolonyRewardShared;
    private String url;
    private RequestQueue queue;
    private List<Referral> referralList;
    private MyReferralsAdapter referralsAdapter;
    private List<Withdraw> withdrawsArrayList;
    private MyWithdrawsAdapter withdrawsAdapter;

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
        setContentView(R.layout.activity_my_statistics);
        StartAppAd.disableSplash();
        active = "true";
        url = getResources().getString(R.string.domain_name);
        queue = Volley.newRequestQueue(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.drawer_menu_statistics));
        userSituationId = getSharedPreferences("userId", MODE_PRIVATE);
        admobAppIdShared = getSharedPreferences("admobAppIdShared", MODE_PRIVATE);
        admobBannerShared = getSharedPreferences("admobBannerShared", MODE_PRIVATE);
        facebookBannerShared = getSharedPreferences("facebookBannerShared", MODE_PRIVATE);
        adcolonyAppIdShared = getSharedPreferences("adcolonyAppIdShared", MODE_PRIVATE);
        adcolonyBannerShared = getSharedPreferences("adcolonyBannerShared", MODE_PRIVATE);
        adcolonyInterstitialShared = getSharedPreferences("adcolonyInterstitialShared", MODE_PRIVATE);
        adcolonyRewardShared = getSharedPreferences("adcolonyRewardShared", MODE_PRIVATE);
        AD_UNIT_ZONE_Ids = new String[] {adcolonyBannerShared.getString("adcolonyBannerShared", ""), adcolonyInterstitialShared.getString("adcolonyInterstitialShared", ""), adcolonyRewardShared.getString("adcolonyRewardShared", "")};
        // Configure AdColony Ads
        AdColony.configure(MyStatisticsActivity.this, adcolonyAppIdShared.getString("adcolonyAppIdShared", ""), AD_UNIT_ZONE_Ids);
        // Bottom Banner Ads
        bannerTypeShared = getSharedPreferences("bannerTypeShared", MODE_PRIVATE);
        linearBannerAdContainer = (LinearLayout) findViewById(R.id.bottom_banner_main_activity);
        if (bannerTypeShared.getString("bannerTypeShared", "").equals("admob")) {
            // Show Admob Ads in LinearLayout
            MobileAds.initialize(MyStatisticsActivity.this, admobAppIdShared.getString("admobAppIdShared", ""));
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
        quizzesViewPager = findViewById(R.id.started_quiz_recycler);
        quizzesPagerList = new ArrayList<>();
        quizzesMainViewPagerAdapter = new StartedQuizStatisticsAdapter(quizzesPagerList);
        quizzesViewPager.setAdapter(quizzesMainViewPagerAdapter);
        quizzesViewPager.setHasFixedSize(true);
        quizzesViewPager.setLayoutManager(new GridLayoutManager(this, 1));
        getStartedQuiz();
        quizzesMainViewPagerAdapter.setOnItemClickListener(new StartedQuizStatisticsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Go to Activity List Of Completed Questions in this quiz (check first if false)
                if (CompletedQuestionsActivity.active.equals("false")) {
                    Intent completed = new Intent(MyStatisticsActivity.this, CompletedQuestionsActivity.class);
                    completed.putExtra("quiz_id", quizzesPagerList.get(position).getQuizId());
                    startActivity(completed);
                }
            }
        });
        // Get Text Questions
        RecyclerView referralsRecyclerView = (RecyclerView) findViewById(R.id.my_referrals_recycler);
        referralList = new ArrayList<>();
        referralsAdapter = new MyReferralsAdapter(referralList);
        referralsRecyclerView.setAdapter(referralsAdapter);
        referralsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        getReferrals();
        // Get Text Questions
        RecyclerView withdrawsRecyclerView = (RecyclerView) findViewById(R.id.my_withdraws_recycler);
        withdrawsArrayList = new ArrayList<>();
        withdrawsAdapter = new MyWithdrawsAdapter(withdrawsArrayList);
        withdrawsRecyclerView.setAdapter(withdrawsAdapter);
        withdrawsRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        getWithdraws();
    }

    private void getReferrals() {
        String key = getResources().getString(R.string.api_secret_key);
        String player_id = userSituationId.getString("userId", "");
        String urlApi = url+"/api/refers/"+ player_id +"/"+key;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Integer id = jsonObject.getInt("id");
                                Integer playerId = jsonObject.getInt("player_id");
                                String playeremail = jsonObject.getString("player_email");
                                String player_username = jsonObject.getString("player_username");
                                String player_image = jsonObject.getString("player_image");
                                Integer referred_source_id = jsonObject.getInt("referred_source_id");
                                referralList.add(new Referral(String.valueOf(id), String.valueOf(playerId),
                                        playeremail, player_username, player_image, String.valueOf(referred_source_id)));
                            }
                            referralsAdapter.notifyDataSetChanged();
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

    private void getWithdraws() {
        String key = getResources().getString(R.string.api_secret_key);
        String player_id = userSituationId.getString("userId", "");
        String urlApi = url+"/api/withdraws/"+ player_id +"/"+key;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Integer playerId = jsonObject.getInt("player_id");
                                String playeremail = jsonObject.getString("player_email");
                                String amount = jsonObject.getString("amount");
                                Integer points = jsonObject.getInt("points");
                                String status = jsonObject.getString("status");
                                String payment_method = jsonObject.getString("payment_method");
                                String payment_info = jsonObject.getString("payment_info");
                                String date = jsonObject.getString("date");
                                String formedDate = getResources().getString(R.string.sent)+ " " + date;
                                withdrawsArrayList.add(new Withdraw(String.valueOf(playerId), playeremail,amount,
                                        String.valueOf(points), status, payment_method, payment_info, formedDate));
                            }
                            withdrawsAdapter.notifyDataSetChanged();
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

    private void getStartedQuiz() {
        String key = getResources().getString(R.string.api_secret_key);
        String player_id = userSituationId.getString("userId", "");
        String urlApi = url+"/api/"+ player_id +"/quizzes/continue/"+key;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("try", "try");
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Integer text = jsonObject.getInt("text_questions");
                                Integer image = jsonObject.getInt("image_questions");
                                Integer audio = jsonObject.getInt("audio_questions");
                                Integer id = jsonObject.getInt("id");
                                Integer quizId = jsonObject.getInt("quiz_id");
                                String quizName = jsonObject.getString("quiz_name");
                                String quizImageUrl = url+"/uploads/quizzes/"+jsonObject.getString("quiz_image_url");
                                Integer subcategoryId = jsonObject.getInt("subcategory_id");
                                String subcategoryName = jsonObject.getString("subcategory_name");
                                Integer categoryId = jsonObject.getInt("category_id");
                                String categoryName = jsonObject.getString("category_name");
                                Integer playerId = jsonObject.getInt("player_id");
                                Integer quiz_number_of_questions = jsonObject.getInt("quiz_number_of_questions");
                                Integer  quiz_number_of_remaining_questions = jsonObject.getInt("quiz_number_of_remaining_questions");
                                quizzesPagerList.add(new ContinueQuiz(String.valueOf(id),String.valueOf(quizId), quizName, quizImageUrl, String.valueOf(subcategoryId), subcategoryName, String.valueOf(categoryId), categoryName, String.valueOf(playerId), quiz_number_of_questions, quiz_number_of_remaining_questions, text, image, audio));
                            }
                            quizzesMainViewPagerAdapter.notifyDataSetChanged();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        StartAppAd.disableSplash();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        active = "false";
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