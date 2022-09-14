package com.todocode.ultimequiz.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.todocode.ultimequiz.R;

import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity {
    private CircleImageView profileImage;
    private TextView since, myUsername;
    private SharedPreferences idShared, usernameShared, imageurlShared, membersinceShared, minToWithdrawShared, earningsactualShared;
    private LinearLayout editProfile, myEarnings, withdraws, requestWithdraw, referrals, invite;
    public static String active = "false";
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
        getWindow().setStatusBarColor(getResources().getColor(R.color.secondary));
        setContentView(R.layout.activity_my_profile);
        StartAppAd.disableSplash();
        active = "true";
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.my_profile));
        idShared = getSharedPreferences("idShared", MODE_PRIVATE);
        usernameShared = getSharedPreferences("usernameShared", MODE_PRIVATE);
        imageurlShared = getSharedPreferences("imageurlShared", MODE_PRIVATE);
        membersinceShared = getSharedPreferences("membersinceShared", MODE_PRIVATE);
        earningsactualShared = getSharedPreferences("earningsactualShared", MODE_PRIVATE);
        membersinceShared = getSharedPreferences("membersinceShared", MODE_PRIVATE);
        profileImage = (CircleImageView) findViewById(R.id.profile_image);
        since = (TextView) findViewById(R.id.since);
        myUsername = (TextView) findViewById(R.id.my_username);
        since.setText(getResources().getString(R.string.member_since) + " " + membersinceShared.getString("membersinceShared", ""));
        myUsername.setText(usernameShared.getString("usernameShared", ""));
        Picasso.get().load(imageurlShared.getString("imageurlShared", "")).fit().centerInside().into(profileImage);
        editProfile = (LinearLayout) findViewById(R.id.edit_profile_linear);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EditMyProfileActivity.active.equals("false")) {
                    Intent edit = new Intent(MyProfileActivity.this, EditMyProfileActivity.class);
                    startActivity(edit);
                }
            }
        });
        myEarnings = (LinearLayout) findViewById(R.id.my_earnings_linear);
        myEarnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyEarningsActivity.active.equals("false")) {
                    Intent earn = new Intent(MyProfileActivity.this, MyEarningsActivity.class);
                    startActivity(earn);
                }
            }
        });
        withdraws = (LinearLayout) findViewById(R.id.withdraws_linear);
        withdraws.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyWithdrawsActivity.active.equals("false")) {
                    Intent withd = new Intent(MyProfileActivity.this, MyWithdrawsActivity.class);
                    startActivity(withd);
                }
            }
        });
        requestWithdraw = (LinearLayout) findViewById(R.id.request_withdraw_linear);
        requestWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RequestWithdrawActivity.active.equals("false")) {
                    Intent req = new Intent(MyProfileActivity.this, RequestWithdrawActivity.class);
                    startActivity(req);
                }
            }
        });
        referrals = (LinearLayout) findViewById(R.id.referrals_linear);
        referrals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyReferralsActivity.active.equals("false")) {
                    Intent ref = new Intent(MyProfileActivity.this, MyReferralsActivity.class);
                    startActivity(ref);
                }
            }
        });
        invite = (LinearLayout) findViewById(R.id.invite_friends_linear);
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InviteFriendsActivity.active.equals("false")) {
                    Intent inv = new Intent(MyProfileActivity.this, InviteFriendsActivity.class);
                    startActivity(inv);
                }
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
        AdColony.configure(MyProfileActivity.this, adcolonyAppIdShared.getString("adcolonyAppIdShared", ""), AD_UNIT_ZONE_Ids);
        // Bottom Banner Ads
        bannerTypeShared = getSharedPreferences("bannerTypeShared", MODE_PRIVATE);
        linearBannerAdContainer = (LinearLayout) findViewById(R.id.bottom_banner_main_activity);
        if (bannerTypeShared.getString("bannerTypeShared", "").equals("admob")) {
            // Show Admob Ads in LinearLayout
            MobileAds.initialize(MyProfileActivity.this, admobAppIdShared.getString("admobAppIdShared", ""));
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