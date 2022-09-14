package com.todocode.ultimequiz.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyReward;
import com.adcolony.sdk.AdColonyRewardListener;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.VideoListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.todocode.ultimequiz.Models.TextQuestion;
import com.todocode.ultimequiz.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class TextQuestionActivity extends AppCompatActivity {
    private SharedPreferences coinsShared, hintCoinsShared, emailShared, fiftyFiftyShared, actualscoreShared, totalscoreShared;
    private CountDownTimer countDown;
    String url;
    public static String active = "false";
    Button submit;
    RequestQueue queue;
    private int selectedAnswer = 0;
    private TextView hint, seconds, points, coins, question_text, answer1, answer2, answer3, answer4, showHint, cinq_cinq, free_coins;
    // AdColony Banner
    public AdColonyAdView adView;
    private LinearLayout linearBannerAdContainer;
    public AdColonyAdOptions adColonyAdOptionsBanner;
    public static String[] AD_UNIT_ZONE_Ids;
    Banner startAppBanner;
    private SharedPreferences videoTypeShared, interstitialTypeShared;
    Button clickReward;
    public AdColonyInterstitial rewardAdColony;
    public AdColonyInterstitialListener rewardListener;
    public AdColonyAdOptions adColonyAdOptionsReward;
    private static boolean isRewardLoaded;
    private com.facebook.ads.RewardedVideoAd fbrewardedVideoAd;
    private String TAG = "video";
    private RewardedVideoAd rewardedVideoAd;
    public AdColonyInterstitial adColonyInterstitiall;
    public AdColonyAdOptions adColonyAdOptions;
    private SharedPreferences videoAdCoinsShared, adcolonyRewardShared, admobRewardShared, facebookRewardShared, admobBannerShared, bannerTypeShared, adcolonyAppIdShared, adcolonyBannerShared, admobAppIdShared, facebookBannerShared,adcolonyInterstitialShared;

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
        getWindow().setStatusBarColor(getResources().getColor(R.color.secondary));
        setContentView(R.layout.activity_text_question);
        StartAppAd.disableSplash();
        active = "true";
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Question " + getIntent().getStringExtra("rank"));
        videoAdCoinsShared = getSharedPreferences("videoAdCoinsShared", MODE_PRIVATE);
        adcolonyRewardShared = getSharedPreferences("adcolonyRewardShared", MODE_PRIVATE);
        videoTypeShared = getSharedPreferences("videoTypeShared", MODE_PRIVATE);
        facebookRewardShared = getSharedPreferences("facebookRewardShared", MODE_PRIVATE);
        admobRewardShared = getSharedPreferences("admobRewardShared", MODE_PRIVATE);
        actualscoreShared = getSharedPreferences("actualscoreShared", MODE_PRIVATE);
        totalscoreShared = getSharedPreferences("totalscoreShared", MODE_PRIVATE);
        adcolonyAppIdShared = getSharedPreferences("adcolonyAppIdShared", MODE_PRIVATE);
        admobAppIdShared = getSharedPreferences("admobAppIdShared", MODE_PRIVATE);
        admobBannerShared = getSharedPreferences("admobBannerShared", MODE_PRIVATE);
        facebookBannerShared = getSharedPreferences("facebookBannerShared", MODE_PRIVATE);
        adcolonyAppIdShared = getSharedPreferences("adcolonyAppIdShared", MODE_PRIVATE);
        adcolonyBannerShared = getSharedPreferences("adcolonyBannerShared", MODE_PRIVATE);
        adcolonyInterstitialShared = getSharedPreferences("adcolonyInterstitialShared", MODE_PRIVATE);
        adcolonyRewardShared = getSharedPreferences("adcolonyRewardShared", MODE_PRIVATE);
        AD_UNIT_ZONE_Ids = new String[] {adcolonyBannerShared.getString("adcolonyBannerShared", ""), adcolonyInterstitialShared.getString("adcolonyInterstitialShared", ""), adcolonyRewardShared.getString("adcolonyRewardShared", "")};
        // Configure AdColony Ads
        AdColony.configure(TextQuestionActivity.this, adcolonyAppIdShared.getString("adcolonyAppIdShared", ""), AD_UNIT_ZONE_Ids);
        url = getResources().getString(R.string.domain_name);
        queue = Volley.newRequestQueue(this);
        fiftyFiftyShared = getSharedPreferences("fiftyFiftyShared", MODE_PRIVATE);
        hintCoinsShared = getSharedPreferences("hintCoinsShared", MODE_PRIVATE);
        emailShared = getSharedPreferences("emailShared", MODE_PRIVATE);
        coinsShared = getSharedPreferences("coinsShared", MODE_PRIVATE);
        seconds = (TextView) findViewById(R.id.timer);
        seconds.setText(getIntent().getStringExtra("seconds"));
        points = (TextView) findViewById(R.id.question_points);
        points.setText(getIntent().getStringExtra("points"));
        coins = (TextView) findViewById(R.id.playr_coins);
        coins.setText(coinsShared.getString("coinsShared", "")+" coins");
        question_text = (TextView) findViewById(R.id.question_text);
        question_text.setText(getIntent().getStringExtra("question_text"));
        hint = (TextView) findViewById(R.id.question_hint);
        hint.setText(getIntent().getStringExtra("hint"));
        answer1 = (TextView) findViewById(R.id.answer1);
        answer2 = (TextView) findViewById(R.id.answer2);
        answer3 = (TextView) findViewById(R.id.answer3);
        answer4 = (TextView) findViewById(R.id.answer4);
        showHint = (TextView) findViewById(R.id.show_hint);
        // Countdown
        countDown = new CountDownTimer(Integer.parseInt(getIntent().getStringExtra("seconds"))*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                seconds.setText(String.valueOf(millisUntilFinished / 1000) + " seconds");
            }
            public void onFinish() {
                // If Time is Finished
                if (selectedAnswer == 0) {
                    goToIncorrectActivity();
                } else {
                    submit.performClick();
                }
            }
        }.start();
        showHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verify if he has enough coins to show hint !
                if (Integer.parseInt(coinsShared.getString("coinsShared", ""))>=Integer.parseInt(hintCoinsShared.getString("hintCoinsShared", ""))) {
                    minusCoins();
                } else {
                    Toast.makeText(TextQuestionActivity.this, getResources().getString(R.string.enough_coins), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cinq_cinq = (TextView) findViewById(R.id.cinq_cinq);
        if (fiftyFiftyShared.getString("fiftyFiftyShared", "").equals("yes")) {
            cinq_cinq.setVisibility(View.VISIBLE);
            cinq_cinq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cinq_cinq.setVisibility(View.GONE);
                    int fiftyPoints = Integer.parseInt(getIntent().getStringExtra("points")) / 2;
                    points.setText(String.valueOf(fiftyPoints));
                    if (answer1.getText().toString().equalsIgnoreCase(getIntent().getStringExtra("false1"))) {
                        answer1.setVisibility(View.GONE);
                    }
                    if (answer1.getText().toString().equalsIgnoreCase(getIntent().getStringExtra("false2"))) {
                        answer1.setVisibility(View.GONE);
                    }
                    if (answer2.getText().toString().equalsIgnoreCase(getIntent().getStringExtra("false1"))) {
                        answer2.setVisibility(View.GONE);
                    }
                    if (answer2.getText().toString().equalsIgnoreCase(getIntent().getStringExtra("false2"))) {
                        answer2.setVisibility(View.GONE);
                    }
                    if (answer3.getText().toString().equalsIgnoreCase(getIntent().getStringExtra("false1"))) {
                        answer3.setVisibility(View.GONE);
                    }
                    if (answer3.getText().toString().equalsIgnoreCase(getIntent().getStringExtra("false2"))) {
                        answer3.setVisibility(View.GONE);
                    }
                    if (answer4.getText().toString().equalsIgnoreCase(getIntent().getStringExtra("false1"))) {
                        answer4.setVisibility(View.GONE);
                    }
                    if (answer4.getText().toString().equalsIgnoreCase(getIntent().getStringExtra("false2"))) {
                        answer4.setVisibility(View.GONE);
                    }
                    Toast.makeText(TextQuestionActivity.this, getResources().getString(R.string.cinq_cinq_mode), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            cinq_cinq.setVisibility(View.GONE);
        }
        free_coins = (TextView) findViewById(R.id.free_coins);
        // Rewarded Video Ads
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        loadRewardedVideoAd();
        // Facebook Video Ad Loading
        AudienceNetworkAds.initialize(TextQuestionActivity.this);
        AdSettings.addTestDevice("c377b922-aa02-4425-a50b-af7a1f1ee1b6");
        fbrewardedVideoAd = new com.facebook.ads.RewardedVideoAd(TextQuestionActivity.this, facebookRewardShared.getString("facebookRewardShared", ""));
        final com.facebook.ads.RewardedVideoAdListener rewardedVideoAdListener = new com.facebook.ads.RewardedVideoAdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
                // Rewarded video ad failed to load
                Log.e(TAG, "Rewarded video ad failed to load: " + error.getErrorMessage());
            }
            @Override
            public void onAdLoaded(Ad ad) {
                fbrewardedVideoAd.show();
                // Rewarded video ad is loaded and ready to be displayed
                Log.d(TAG, "Rewarded video ad is loaded and ready to be displayed!");
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Rewarded video ad clicked
                Log.d(TAG, "Rewarded video ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Rewarded Video ad impression - the event will fire when the
                // video starts playing
                Log.d(TAG, "Rewarded video ad impression logged!");
            }

            @Override
            public void onRewardedVideoCompleted() {
                // Rewarded Video View Complete - the video has been played to the end.
                // You can use this event to initialize your reward
                Log.d(TAG, "Rewarded video completed!");
            }

            @Override
            public void onRewardedVideoClosed() {
                int newCoins = Integer.parseInt(coinsShared.getString("coinsShared", "")) + Integer.parseInt(videoAdCoinsShared.getString("videoAdCoinsShared", ""));
                coins.setText(String.valueOf(newCoins)+" coins");
                coinsShared.edit().putString("coinsShared", String.valueOf(newCoins)).apply();
                changeCoinsInDatabase();
            }
        };
        // Reward Video Clicked
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {
                countDown.cancel();
            }

            @Override
            public void onRewardedVideoStarted() {
                countDown.cancel();
            }

            @Override
            public void onRewardedVideoAdClosed() {
                // Preload the next video ad.
                loadRewardedVideoAd();
                countDown.cancel();
                countDown.start();
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                int newCoins = Integer.parseInt(coinsShared.getString("coinsShared", "")) + Integer.parseInt(videoAdCoinsShared.getString("videoAdCoinsShared", ""));
                coins.setText(String.valueOf(newCoins)+" coins");
                coinsShared.edit().putString("coinsShared", String.valueOf(newCoins)).apply();
                changeCoinsInDatabase();
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });
        free_coins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoTypeShared.getString("videoTypeShared", "").equals("admob")) {
                    if (rewardedVideoAd.isLoaded()) {
                        rewardedVideoAd.show();
                    } else {
                        loadRewardedVideoAd();
                        rewardedVideoAd.show();
                    }
                }
                else if(videoTypeShared.getString("videoTypeShared", "").equals("facebook")) {
                    fbrewardedVideoAd.loadAd(
                            fbrewardedVideoAd.buildLoadAdConfig()
                                    .withAdListener(rewardedVideoAdListener)
                                    .build());
                }
                else if(videoTypeShared.getString("videoTypeShared", "").equals("adcolony")) {
                    // AdColony Reward
                    AdColony.setRewardListener(new AdColonyRewardListener() {
                        @Override
                        public void onReward(AdColonyReward adColonyReward) {
                            int newCoins = Integer.parseInt(coinsShared.getString("coinsShared", "")) + Integer.parseInt(videoAdCoinsShared.getString("videoAdCoinsShared", ""));
                            coins.setText(String.valueOf(newCoins)+" coins");
                            coinsShared.edit().putString("coinsShared", String.valueOf(newCoins)).apply();
                            changeCoinsInDatabase();
                        }

                    });
                    rewardListener = new AdColonyInterstitialListener() {
                        @Override
                        public void onRequestFilled(AdColonyInterstitial adColonyInterstitial) {
                            rewardAdColony = adColonyInterstitial;
                            isRewardLoaded = true;
                            if (rewardAdColony!=null && isRewardLoaded) {
                                rewardAdColony.show();
                                isRewardLoaded = false;
                            }
                        }
                        @Override
                        public void onClosed(AdColonyInterstitial ad) {
                            super.onClosed(ad);
                            AdColony.requestInterstitial(adcolonyAppIdShared.getString("adcolonyAppIdShared",""), rewardListener, adColonyAdOptionsReward);
                        }
                    };
                    adColonyAdOptionsReward = new AdColonyAdOptions().enableConfirmationDialog(false)
                            .enableResultsDialog(false);
                    AdColony.requestInterstitial(adcolonyRewardShared.getString("adcolonyRewardShared", ""), rewardListener, adColonyAdOptionsReward);
                }
                else if(videoTypeShared.getString("videoTypeShared", "").equals("startapp")) {
                    final StartAppAd offerwallAd = new StartAppAd(TextQuestionActivity.this);
                    offerwallAd.setVideoListener(new VideoListener() {
                        @Override
                        public void onVideoCompleted() {
                            int newCoins = Integer.parseInt(coinsShared.getString("coinsShared", "")) + Integer.parseInt(videoAdCoinsShared.getString("videoAdCoinsShared", ""));
                            coins.setText(String.valueOf(newCoins)+" coins");
                            coinsShared.edit().putString("coinsShared", String.valueOf(newCoins)).apply();
                            changeCoinsInDatabase();
                        }
                    });
                    offerwallAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
                        @Override
                        public void onReceiveAd(com.startapp.sdk.adsbase.Ad ad) {
                            offerwallAd.showAd();
                        }

                        @Override
                        public void onFailedToReceiveAd(com.startapp.sdk.adsbase.Ad ad) {
                            ad.getErrorMessage();
                        }
                    });
                }
            }
        });
        // Shuffle Answers
        List<String> listOfAnswers = new ArrayList<>();
        listOfAnswers.add(getIntent().getStringExtra("true_answer"));
        listOfAnswers.add(getIntent().getStringExtra("false1"));
        listOfAnswers.add(getIntent().getStringExtra("false2"));
        listOfAnswers.add(getIntent().getStringExtra("false3"));
        Collections.shuffle(listOfAnswers);
        answer1.setText(listOfAnswers.get(0));
        answer2.setText(listOfAnswers.get(1));
        answer3.setText(listOfAnswers.get(2));
        answer4.setText(listOfAnswers.get(3));
        // Change Background of selected answer
        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer1.setBackgroundTintList(ColorStateList.valueOf( getResources().getColor(R.color.accent) ));
                answer2.setBackgroundTintList(ColorStateList.valueOf( getResources().getColor(R.color.secondary) ));
                answer3.setBackgroundTintList(ColorStateList.valueOf( getResources().getColor(R.color.secondary) ));
                answer4.setBackgroundTintList(ColorStateList.valueOf( getResources().getColor(R.color.secondary) ));
                selectedAnswer = 1;
            }
        });
        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer2.setBackgroundTintList(ColorStateList.valueOf( getResources().getColor(R.color.accent) ));
                answer1.setBackgroundTintList(ColorStateList.valueOf( getResources().getColor(R.color.secondary) ));
                answer3.setBackgroundTintList(ColorStateList.valueOf( getResources().getColor(R.color.secondary) ));
                answer4.setBackgroundTintList(ColorStateList.valueOf( getResources().getColor(R.color.secondary) ));
                selectedAnswer = 2;
            }
        });
        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer3.setBackgroundTintList(ColorStateList.valueOf( getResources().getColor(R.color.accent) ));
                answer1.setBackgroundTintList(ColorStateList.valueOf( getResources().getColor(R.color.secondary) ));
                answer2.setBackgroundTintList(ColorStateList.valueOf( getResources().getColor(R.color.secondary) ));
                answer4.setBackgroundTintList(ColorStateList.valueOf( getResources().getColor(R.color.secondary) ));
                selectedAnswer = 3;
            }
        });
        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer4.setBackgroundTintList(ColorStateList.valueOf( getResources().getColor(R.color.accent) ));
                answer1.setBackgroundTintList(ColorStateList.valueOf( getResources().getColor(R.color.secondary) ));
                answer3.setBackgroundTintList(ColorStateList.valueOf( getResources().getColor(R.color.secondary) ));
                answer2.setBackgroundTintList(ColorStateList.valueOf( getResources().getColor(R.color.secondary) ));
                selectedAnswer = 4;
            }
        });
        submit = (Button) findViewById(R.id.submit_answer);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedAnswer == 0) {
                    Toast.makeText(TextQuestionActivity.this, "Nothing Selected!", Toast.LENGTH_SHORT).show();
                }
                if (selectedAnswer == 1) {
                    countDown.cancel();
                    submit.setVisibility(View.GONE);
                    if (answer1.getText().toString().equals(getIntent().getStringExtra("true_answer"))) {
                        addPointsAndMakeItCompleted();
                    } else {
                        goToIncorrectActivity();
                    }
                }
                if (selectedAnswer == 2) {
                    countDown.cancel();
                    submit.setVisibility(View.GONE);
                    if (answer2.getText().toString().equals(getIntent().getStringExtra("true_answer"))) {
                        addPointsAndMakeItCompleted();
                    } else {
                        goToIncorrectActivity();
                    }
                }
                if (selectedAnswer == 3) {
                    countDown.cancel();
                    submit.setVisibility(View.GONE);
                    if (answer3.getText().toString().equals(getIntent().getStringExtra("true_answer"))) {
                        addPointsAndMakeItCompleted();
                    } else {
                        goToIncorrectActivity();
                    }
                }
                if (selectedAnswer == 4) {
                    countDown.cancel();
                    submit.setVisibility(View.GONE);
                    if (answer4.getText().toString().equals(getIntent().getStringExtra("true_answer"))) {
                        addPointsAndMakeItCompleted();
                    } else {
                        goToIncorrectActivity();
                    }
                }
            }
        });// Bottom Banner Ads
        bannerTypeShared = getSharedPreferences("bannerTypeShared", MODE_PRIVATE);
        linearBannerAdContainer = (LinearLayout) findViewById(R.id.bottom_banner_main_activity);
        if (bannerTypeShared.getString("bannerTypeShared", "").equals("admob")) {
            MobileAds.initialize(TextQuestionActivity.this, admobAppIdShared.getString("admobAppIdShared", ""));
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

    private void changeCoinsInDatabase() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/players/coins/add", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")) {
                        free_coins.setVisibility(View.GONE);
                        Toast.makeText(TextQuestionActivity.this, "You got " + videoAdCoinsShared.getString("videoAdCoinsShared", "") + " coins", Toast.LENGTH_SHORT).show();
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
                params.put("email", emailShared.getString("emailShared", ""));
                params.put("key", getResources().getString(R.string.api_secret_key));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(TextQuestionActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void loadRewardedVideoAd() {
        if (!rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.loadAd(admobRewardShared.getString("admobRewardShared", ""), new AdRequest.Builder().build());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        StartAppAd.disableSplash();
    }

    @Override
    protected void onStart() {
        if (!rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.loadAd(admobRewardShared.getString("admobRewardShared", ""), new AdRequest.Builder().build());
        }
        super.onStart();
    }

    @Override
    public void onResume() {
        rewardedVideoAd.resume(TextQuestionActivity.this);
        super.onResume();
    }

    @Override
    public void onPause() {
        rewardedVideoAd.pause(TextQuestionActivity.this);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        countDown.cancel();
        active = "false";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDown.cancel();
    }

    private void goToIncorrectActivity() {
        active = "false";
        countDown.cancel();
        Intent quiz = new Intent(TextQuestionActivity.this, IncorrectAnswerActivity.class);
        quiz.putExtra("id",getIntent().getStringExtra("id"));
        quiz.putExtra("type","text");
        quiz.putExtra("rank",getIntent().getStringExtra("rank"));
        quiz.putExtra("category_id",getIntent().getStringExtra("category_id"));
        quiz.putExtra("subcategory_id",getIntent().getStringExtra("subcategory_id"));
        quiz.putExtra("quiz_id",getIntent().getStringExtra("quiz_id"));
        quiz.putExtra("points",getIntent().getStringExtra("points"));
        quiz.putExtra("seconds",getIntent().getStringExtra("seconds"));
        quiz.putExtra("hint",getIntent().getStringExtra("hint"));
        quiz.putExtra("true_answer",getIntent().getStringExtra("true_answer"));
        quiz.putExtra("false1",getIntent().getStringExtra("false1"));
        quiz.putExtra("false2",getIntent().getStringExtra("false2"));
        quiz.putExtra("false3",getIntent().getStringExtra("false3"));
        quiz.putExtra("question_text",getIntent().getStringExtra("question_text"));
        startActivity(quiz);
        finish();
    }

    private void addPointsAndMakeItCompleted() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/players/points", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("try", "try");
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")) {
                        countDown.cancel();
                        int actualScore = jsonObject.getInt("actual_score");
                        active = "false";
                        Intent score = new Intent(TextQuestionActivity.this, CorrectAnswerActivity.class);
                        score.putExtra("points", points.getText().toString());
                        score.putExtra("actual_score", String.valueOf(actualScore));
                        startActivity(score);
                        finish();
                    } else if(success.equals("finish")) {
                        active = "false";
                        finish();
                        Toast.makeText(TextQuestionActivity.this, "This Question is already Completed!", Toast.LENGTH_SHORT).show();
                    }else if(success.equals("2")) {
                        active = "false";
                        countDown.cancel();
                        int actualScore2 = jsonObject.getInt("actual_score");
                        Intent score = new Intent(TextQuestionActivity.this, CorrectAnswerActivity.class);
                        score.putExtra("points", points.getText().toString());
                        score.putExtra("actual_score", String.valueOf(actualScore2));
                        startActivity(score);
                        finish();
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
                params.put("email", emailShared.getString("emailShared", ""));
                params.put("points", points.getText().toString());
                params.put("question_id", getIntent().getStringExtra("id"));
                params.put("quiz_id", getIntent().getStringExtra("quiz_id"));
                params.put("subcategory_id", getIntent().getStringExtra("subcategory_id"));
                params.put("category_id", getIntent().getStringExtra("category_id"));
                params.put("key", getResources().getString(R.string.api_secret_key));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(TextQuestionActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void minusCoins() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/players/coins/change", new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")) {
                        showHint.setVisibility(View.GONE);
                        hint.setVisibility(View.VISIBLE);
                        int coinSharedd = Integer.parseInt(coinsShared.getString("coinsShared", ""));
                        int coinHintSharedd = Integer.parseInt(hintCoinsShared.getString("hintCoinsShared", ""));
                        int newCoins = coinSharedd - coinHintSharedd;
                        coinsShared.edit().putString("coinsShared", String.valueOf(newCoins)).apply();
                        coins.setText(coinsShared.getString("coinsShared", "")+ " coins");
                    } else if (success.equals("0")) {
                        Toast.makeText(TextQuestionActivity.this, "After Verification, You don't have enough coins!", Toast.LENGTH_SHORT).show();
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
                params.put("email", emailShared.getString("emailShared", ""));
                params.put("key", getResources().getString(R.string.api_secret_key));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(TextQuestionActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                active = "false";
                countDown.cancel();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}