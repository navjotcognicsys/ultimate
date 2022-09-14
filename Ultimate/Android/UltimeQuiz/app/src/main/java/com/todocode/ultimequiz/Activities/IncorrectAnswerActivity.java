package com.todocode.ultimequiz.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.todocode.ultimequiz.R;

import java.util.Locale;

public class IncorrectAnswerActivity extends AppCompatActivity {
    Button back, retry;
    // AdColony Banner
    public AdColonyAdView adView;
    private LinearLayout linearBannerAdContainer;
    public AdColonyAdOptions adColonyAdOptionsBanner;
    public static String[] AD_UNIT_ZONE_Ids;
    Banner startAppBanner;
    private SharedPreferences facebookInterstitialShared, admobInterstitialShared, interstitialTypeShared, admobBannerShared, bannerTypeShared, adcolonyAppIdShared, adcolonyBannerShared, admobAppIdShared, facebookBannerShared,adcolonyInterstitialShared, adcolonyRewardShared;
    private com.facebook.ads.InterstitialAd facebookInterstitialAd;
    private InterstitialAd mInterstitialAd;
    // AdColony Interstitial
    public AdColonyInterstitial adColonyInterstitiall;
    public AdColonyAdOptions adColonyAdOptions;

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
        setContentView(R.layout.activity_incorrect_answer);
        StartAppAd.disableSplash();
        back = (Button) findViewById(R.id.back_to_list);
        retry = (Button) findViewById(R.id.try_again);
        facebookInterstitialShared = getSharedPreferences("facebookInterstitialShared", MODE_PRIVATE);
        admobInterstitialShared = getSharedPreferences("admobInterstitialShared", MODE_PRIVATE);
        interstitialTypeShared = getSharedPreferences("interstitialTypeShared", MODE_PRIVATE);
        admobAppIdShared = getSharedPreferences("admobAppIdShared", MODE_PRIVATE);
        admobBannerShared = getSharedPreferences("admobBannerShared", MODE_PRIVATE);
        facebookBannerShared = getSharedPreferences("facebookBannerShared", MODE_PRIVATE);
        adcolonyAppIdShared = getSharedPreferences("adcolonyAppIdShared", MODE_PRIVATE);
        adcolonyBannerShared = getSharedPreferences("adcolonyBannerShared", MODE_PRIVATE);
        adcolonyInterstitialShared = getSharedPreferences("adcolonyInterstitialShared", MODE_PRIVATE);
        adcolonyRewardShared = getSharedPreferences("adcolonyRewardShared", MODE_PRIVATE);
        AD_UNIT_ZONE_Ids = new String[] {adcolonyBannerShared.getString("adcolonyBannerShared", ""), adcolonyInterstitialShared.getString("adcolonyInterstitialShared", ""), adcolonyRewardShared.getString("adcolonyRewardShared", "")};
        // Configure AdColony Ads
        AdColony.configure(IncorrectAnswerActivity.this, adcolonyAppIdShared.getString("adcolonyAppIdShared", ""), AD_UNIT_ZONE_Ids);
        if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("facebook")) {
            prepareInterstitialFacebookAd();
        } else if(interstitialTypeShared.getString("interstitialTypeShared", "").equals("admob")) {
            prepareInterstitialAdmobAd();
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextQuestionActivity.active.equals("false")) {
                    // Show Interstitial Ads
                    if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("admob")) {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                            mInterstitialAd.setAdListener(new AdListener() {
                                @Override
                                public void onAdLoaded() {
                                    // Code to be executed when an ad finishes loading.
                                    mInterstitialAd.show();
                                }
                                @Override
                                public void onAdFailedToLoad(int i) {
                                    if (getIntent().getStringExtra("type").equals("text")) {
                                    Intent quiz = new Intent(IncorrectAnswerActivity.this, TextQuestionActivity.class);
                                    quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                    } else if (getIntent().getStringExtra("type").equals("image")) {
                                        Intent quiz = new Intent(IncorrectAnswerActivity.this, ImageQuestionActivity.class);
                                        quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                        quiz.putExtra("question_image_url",getIntent().getStringExtra("question_image_url"));
                                        startActivity(quiz);
                                        finish();
                                    }else if (getIntent().getStringExtra("type").equals("audio")) {
                                        Intent quiz = new Intent(IncorrectAnswerActivity.this, AudioQuestionActivity.class);
                                        quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                        quiz.putExtra("question_audio",getIntent().getStringExtra("question_audio"));
                                        startActivity(quiz);
                                        finish();
                                    }
                                }

                                @Override
                                public void onAdOpened() {
                                    // Code to be executed when the ad is displayed.
                                }

                                @Override
                                public void onAdClicked() {
                                    // Code to be executed when the user clicks on an ad.
                                }

                                @Override
                                public void onAdLeftApplication() {
                                    // Code to be executed when the user has left the app.
                                }

                                @Override
                                public void onAdClosed() {
                                    if (getIntent().getStringExtra("type").equals("text")) {
                                        Intent quiz = new Intent(IncorrectAnswerActivity.this, TextQuestionActivity.class);
                                        quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                    } else if (getIntent().getStringExtra("type").equals("image")) {
                                        Intent quiz = new Intent(IncorrectAnswerActivity.this, ImageQuestionActivity.class);
                                        quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                        quiz.putExtra("question_image_url",getIntent().getStringExtra("question_image_url"));
                                        startActivity(quiz);
                                        finish();
                                    }else if (getIntent().getStringExtra("type").equals("audio")) {
                                        Intent quiz = new Intent(IncorrectAnswerActivity.this, AudioQuestionActivity.class);
                                        quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                        quiz.putExtra("question_audio",getIntent().getStringExtra("question_audio"));
                                        startActivity(quiz);
                                        finish();
                                    }
                                }
                            });
                            prepareInterstitialAdmobAd();
                        } else {
                            if (getIntent().getStringExtra("type").equals("text")) {
                                Intent quiz = new Intent(IncorrectAnswerActivity.this, TextQuestionActivity.class);
                                quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                            } else if (getIntent().getStringExtra("type").equals("image")) {
                                Intent quiz = new Intent(IncorrectAnswerActivity.this, ImageQuestionActivity.class);
                                quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                quiz.putExtra("question_image_url",getIntent().getStringExtra("question_image_url"));
                                startActivity(quiz);
                                finish();
                            }else if (getIntent().getStringExtra("type").equals("audio")) {
                                Intent quiz = new Intent(IncorrectAnswerActivity.this, AudioQuestionActivity.class);
                                quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                quiz.putExtra("question_audio",getIntent().getStringExtra("question_audio"));
                                startActivity(quiz);
                                finish();
                            }
                        }
                    } else if(interstitialTypeShared.getString("interstitialTypeShared", "").equals("facebook")) {
                        if (facebookInterstitialAd.isAdLoaded()) {
                            facebookInterstitialAd.show();
                            InterstitialAdListener listener = new InterstitialAdListener() {
                                @Override
                                public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                                }

                                @Override
                                public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                    if (getIntent().getStringExtra("type").equals("text")) {
                                        Intent quiz = new Intent(IncorrectAnswerActivity.this, TextQuestionActivity.class);
                                        quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                    } else if (getIntent().getStringExtra("type").equals("image")) {
                                        Intent quiz = new Intent(IncorrectAnswerActivity.this, ImageQuestionActivity.class);
                                        quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                        quiz.putExtra("question_image_url",getIntent().getStringExtra("question_image_url"));
                                        startActivity(quiz);
                                        finish();
                                    }else if (getIntent().getStringExtra("type").equals("audio")) {
                                        Intent quiz = new Intent(IncorrectAnswerActivity.this, AudioQuestionActivity.class);
                                        quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                        quiz.putExtra("question_audio",getIntent().getStringExtra("question_audio"));
                                        startActivity(quiz);
                                        finish();
                                    }
                                }

                                @Override
                                public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                    if (getIntent().getStringExtra("type").equals("text")) {
                                        Intent quiz = new Intent(IncorrectAnswerActivity.this, TextQuestionActivity.class);
                                        quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                    } else if (getIntent().getStringExtra("type").equals("image")) {
                                        Intent quiz = new Intent(IncorrectAnswerActivity.this, ImageQuestionActivity.class);
                                        quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                        quiz.putExtra("question_image_url",getIntent().getStringExtra("question_image_url"));
                                        startActivity(quiz);
                                        finish();
                                    }else if (getIntent().getStringExtra("type").equals("audio")) {
                                        Intent quiz = new Intent(IncorrectAnswerActivity.this, AudioQuestionActivity.class);
                                        quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                        quiz.putExtra("question_audio",getIntent().getStringExtra("question_audio"));
                                        startActivity(quiz);
                                        finish();
                                    }
                                }

                                @Override
                                public void onAdLoaded(com.facebook.ads.Ad ad) {
                                    facebookInterstitialAd.show();
                                }

                                @Override
                                public void onAdClicked(com.facebook.ads.Ad ad) {

                                }

                                @Override
                                public void onLoggingImpression(com.facebook.ads.Ad ad) {

                                }
                            };
                            facebookInterstitialAd.loadAd(facebookInterstitialAd.buildLoadAdConfig().withAdListener(listener).build());
                            prepareInterstitialFacebookAd();
                        } else {
                            if (getIntent().getStringExtra("type").equals("text")) {
                                Intent quiz = new Intent(IncorrectAnswerActivity.this, TextQuestionActivity.class);
                                quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                            } else if (getIntent().getStringExtra("type").equals("image")) {
                                Intent quiz = new Intent(IncorrectAnswerActivity.this, ImageQuestionActivity.class);
                                quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                quiz.putExtra("question_image_url",getIntent().getStringExtra("question_image_url"));
                                startActivity(quiz);
                                finish();
                            }else if (getIntent().getStringExtra("type").equals("audio")) {
                                Intent quiz = new Intent(IncorrectAnswerActivity.this, AudioQuestionActivity.class);
                                quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                quiz.putExtra("question_audio",getIntent().getStringExtra("question_audio"));
                                startActivity(quiz);
                                finish();
                            }
                        }
                    } else if(interstitialTypeShared.getString("interstitialTypeShared", "").equals("adcolony")) {
                        AdColonyInterstitialListener listener = new AdColonyInterstitialListener() {
                            @Override
                            public void onRequestFilled(AdColonyInterstitial adColonyInterstitial) {
                                adColonyInterstitiall = adColonyInterstitial;
                                adColonyInterstitiall.show();
                            }

                            @Override
                            public void onRequestNotFilled(AdColonyZone zone) {
                                if (getIntent().getStringExtra("type").equals("text")) {
                                    Intent quiz = new Intent(IncorrectAnswerActivity.this, TextQuestionActivity.class);
                                    quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                } else if (getIntent().getStringExtra("type").equals("image")) {
                                    Intent quiz = new Intent(IncorrectAnswerActivity.this, ImageQuestionActivity.class);
                                    quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                    quiz.putExtra("question_image_url",getIntent().getStringExtra("question_image_url"));
                                    startActivity(quiz);
                                    finish();
                                }else if (getIntent().getStringExtra("type").equals("audio")) {
                                    Intent quiz = new Intent(IncorrectAnswerActivity.this, AudioQuestionActivity.class);
                                    quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                    quiz.putExtra("question_audio",getIntent().getStringExtra("question_audio"));
                                    startActivity(quiz);
                                    finish();
                                }
                            }

                            @Override
                            public void onClosed(AdColonyInterstitial ad) {
                                if (getIntent().getStringExtra("type").equals("text")) {
                                    Intent quiz = new Intent(IncorrectAnswerActivity.this, TextQuestionActivity.class);
                                    quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                } else if (getIntent().getStringExtra("type").equals("image")) {
                                    Intent quiz = new Intent(IncorrectAnswerActivity.this, ImageQuestionActivity.class);
                                    quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                    quiz.putExtra("question_image_url",getIntent().getStringExtra("question_image_url"));
                                    startActivity(quiz);
                                    finish();
                                }else if (getIntent().getStringExtra("type").equals("audio")) {
                                    Intent quiz = new Intent(IncorrectAnswerActivity.this, AudioQuestionActivity.class);
                                    quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                    quiz.putExtra("question_audio",getIntent().getStringExtra("question_audio"));
                                    startActivity(quiz);
                                    finish();
                                }
                            }

                            @Override
                            public void onExpiring(AdColonyInterstitial ad) {
                                if (getIntent().getStringExtra("type").equals("text")) {
                                    Intent quiz = new Intent(IncorrectAnswerActivity.this, TextQuestionActivity.class);
                                    quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                } else if (getIntent().getStringExtra("type").equals("image")) {
                                    Intent quiz = new Intent(IncorrectAnswerActivity.this, ImageQuestionActivity.class);
                                    quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                    quiz.putExtra("question_image_url",getIntent().getStringExtra("question_image_url"));
                                    startActivity(quiz);
                                    finish();
                                }
                                else if (getIntent().getStringExtra("type").equals("audio")) {
                                    Intent quiz = new Intent(IncorrectAnswerActivity.this, AudioQuestionActivity.class);
                                    quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                                    quiz.putExtra("question_audio",getIntent().getStringExtra("question_audio"));
                                    startActivity(quiz);
                                    finish();
                                }
                            }
                        };
                        AdColony.requestInterstitial(adcolonyInterstitialShared.getString("adcolonyInterstitialShared", ""), listener, adColonyAdOptions);
                    }
                    else if(interstitialTypeShared.getString("interstitialTypeShared", "").equals("startapp")) {
                        if (getIntent().getStringExtra("type").equals("text")) {
                            Intent quiz = new Intent(IncorrectAnswerActivity.this, TextQuestionActivity.class);
                            quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                        } else if (getIntent().getStringExtra("type").equals("image")) {
                            Intent quiz = new Intent(IncorrectAnswerActivity.this, ImageQuestionActivity.class);
                            quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                            quiz.putExtra("question_image_url",getIntent().getStringExtra("question_image_url"));
                            startActivity(quiz);
                            finish();
                        } else if (getIntent().getStringExtra("type").equals("audio")) {
                            Intent quiz = new Intent(IncorrectAnswerActivity.this, AudioQuestionActivity.class);
                            quiz.putExtra("id",getIntent().getStringExtra("id"));
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
                            quiz.putExtra("question_audio",getIntent().getStringExtra("question_audio"));
                            startActivity(quiz);
                            finish();
                        }
                        StartAppAd.showAd(IncorrectAnswerActivity.this);
                    }
                }
            }
        });
        // Bottom Banner Ads
        bannerTypeShared = getSharedPreferences("bannerTypeShared", MODE_PRIVATE);
        linearBannerAdContainer = (LinearLayout) findViewById(R.id.bottom_banner_main_activity);
        if (bannerTypeShared.getString("bannerTypeShared", "").equals("admob")) {
            // Show Admob Ads in LinearLayout
            MobileAds.initialize(IncorrectAnswerActivity.this, admobAppIdShared.getString("admobAppIdShared", ""));
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

    private void prepareInterstitialAdmobAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(admobInterstitialShared.getString("admobInterstitialShared", ""));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void prepareInterstitialFacebookAd() {
        AudienceNetworkAds.initialize(this);
        AdSettings.addTestDevice("09a77aa6-326d-45b1-bf3c-55b62bbebc3c");
        facebookInterstitialAd = new com.facebook.ads.InterstitialAd(this, facebookInterstitialShared.getString("facebookInterstitialShared", ""));
        facebookInterstitialAd.loadAd();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        StartAppAd.disableSplash();
    }
}