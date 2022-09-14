package com.todocode.ultimequiz.Activities;

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
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.todocode.ultimequiz.Adapters.AllCategoriesAdapter;
import com.todocode.ultimequiz.Models.Category;
import com.todocode.ultimequiz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AllCategoriesActivity extends AppCompatActivity {
    private ArrayList<Object> categoriesArrayList;
    private AllCategoriesAdapter categoriesAdapter;
    private String url;
    private RequestQueue queue;
    public static String active = "false";
    public static final int NUMBER_OF_ADS = 5;
    private AdLoader adLoader;
    private final List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    private SharedPreferences admobNativeShared;
    // AdColony Banner
    public AdColonyAdView adView;
    private LinearLayout linearBannerAdContainer;
    public AdColonyAdOptions adColonyAdOptionsBanner;
    public static String[] AD_UNIT_ZONE_Ids;
    Banner startAppBanner;
    private SharedPreferences admobBannerShared, bannerTypeShared, adcolonyAppIdShared, adcolonyBannerShared, admobAppIdShared, facebookBannerShared,adcolonyInterstitialShared, adcolonyRewardShared;
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
        setContentView(R.layout.activity_all_categories);
        StartAppAd.disableSplash();
        active = "true";
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.all_categories));
        admobAppIdShared = getSharedPreferences("admobAppIdShared", MODE_PRIVATE);
        admobNativeShared = getSharedPreferences("admobNativeShared", MODE_PRIVATE);
        url = getResources().getString(R.string.domain_name);
        queue = Volley.newRequestQueue(this);
        // Get Featured Categories
        RecyclerView categoriesRecyclerView = (RecyclerView) findViewById(R.id.all_categories_recycler);
        categoriesArrayList = new ArrayList<>();
        categoriesAdapter = new AllCategoriesAdapter(this, categoriesArrayList);
        categoriesRecyclerView.setAdapter(categoriesAdapter);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        categoriesRecyclerView.setHasFixedSize(true);
        // Admob Native Ads in RecyclerView
        MobileAds.initialize(com.todocode.ultimequiz.Activities.AllCategoriesActivity.this, admobAppIdShared.getString("admobAppIdShared", ""));
        getAllCategories();
        loadAdmobNativeAds();
        admobAppIdShared = getSharedPreferences("admobAppIdShared", MODE_PRIVATE);
        admobBannerShared = getSharedPreferences("admobBannerShared", MODE_PRIVATE);
        facebookBannerShared = getSharedPreferences("facebookBannerShared", MODE_PRIVATE);
        adcolonyAppIdShared = getSharedPreferences("adcolonyAppIdShared", MODE_PRIVATE);
        adcolonyBannerShared = getSharedPreferences("adcolonyBannerShared", MODE_PRIVATE);
        adcolonyInterstitialShared = getSharedPreferences("adcolonyInterstitialShared", MODE_PRIVATE);
        adcolonyRewardShared = getSharedPreferences("adcolonyRewardShared", MODE_PRIVATE);
        AD_UNIT_ZONE_Ids = new String[] {adcolonyBannerShared.getString("adcolonyBannerShared", ""), adcolonyInterstitialShared.getString("adcolonyInterstitialShared", ""), adcolonyRewardShared.getString("adcolonyRewardShared", "")};
        // Configure AdColony Ads
        AdColony.configure(AllCategoriesActivity.this, adcolonyAppIdShared.getString("adcolonyAppIdShared", ""), AD_UNIT_ZONE_Ids);
        // Bottom Banner Ads
        bannerTypeShared = getSharedPreferences("bannerTypeShared", MODE_PRIVATE);
        linearBannerAdContainer = (LinearLayout) findViewById(R.id.bottom_banner_main_activity);
        if (bannerTypeShared.getString("bannerTypeShared", "").equals("admob")) {
            // Show Admob Ads in LinearLayout
            MobileAds.initialize(AllCategoriesActivity.this, admobAppIdShared.getString("admobAppIdShared", ""));
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

    private void insertAdsInMenuItems() {
        if (categoriesArrayList.size() <= 3) {
            return;
        }
        if (categoriesArrayList.size() > 3) {
            int offset = (categoriesArrayList.size() / mNativeAds.size())+1;
            int index = 2;
            for (UnifiedNativeAd ad : mNativeAds) {
                categoriesArrayList.add(index, ad);
                index = index + offset;
            }
            categoriesAdapter.notifyDataSetChanged();
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

    private void getAllCategories() {
        String key = getResources().getString(R.string.api_secret_key);
        String urlApi = url+"/api/categories/all/"+key;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Integer id = jsonObject.getInt("id");
                                String name = jsonObject.getString("category_name");
                                Integer number_of_subcategories = jsonObject.getInt("number_of_subcategories");
                                Integer number_of_quizzes = jsonObject.getInt("number_of_quizzes");
                                String img = url+"/uploads/categories/"+jsonObject.getString("category_img");
                                categoriesArrayList.add(new Category(String.valueOf(id), name, img, String.valueOf(number_of_subcategories), String.valueOf(number_of_quizzes)));
                            }
                            categoriesAdapter.notifyDataSetChanged();
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