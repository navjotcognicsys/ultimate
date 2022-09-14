package com.todocode.ultimequiz.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.todocode.ultimequiz.Managers.PermissionManager;
import com.todocode.ultimequiz.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditMyProfileActivity extends AppCompatActivity {
    private Button saveBtn;
    private TextInputLayout username, facebook, twitter, instagram;
    private String url;
    private CircleImageView profilePic;
    private ImageView changePRofileImage, btnAllowPermissions;
    private static final int PICK_IMAGE = 1;
    Uri imageurl;
    public static String active = "false";
    private Bitmap bitmap;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    private ProgressBar progressBarImage;
    // AdColony Banner
    public AdColonyAdView adView;
    private LinearLayout linearBannerAdContainer;
    public AdColonyAdOptions adColonyAdOptionsBanner;
    public static String[] AD_UNIT_ZONE_Ids;
    Banner startAppBanner;
    private SharedPreferences admobBannerShared, bannerTypeShared, adcolonyAppIdShared, adcolonyBannerShared, admobAppIdShared, facebookBannerShared,adcolonyInterstitialShared, adcolonyRewardShared;

    private SharedPreferences idShared, usernameShared, emailShared, actualscoreShared, totalscoreShared, imageurlShared,
            referralcodeShared, coinsShared, lastclaimShared, loginmethodShared, facebookShared, twitterShared, instagramShared,
            earningswithdrawedShared, earningsactualShared, earningsActualWithCurrencyShared, membersinceShared;

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
        setContentView(R.layout.activity_edit_my_profile);
        StartAppAd.disableSplash();
        btnAllowPermissions = (ImageView) findViewById(R.id.allow_permission);
        if (new PermissionManager(com.todocode.ultimequiz.Activities.EditMyProfileActivity.this).checkPreference()) {
            btnAllowPermissions.setVisibility(View.GONE);
        }
        active = "true";
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.edit_my_profile));
        url = getResources().getString(R.string.domain_name);
        idShared = getSharedPreferences("idShared", MODE_PRIVATE);
        usernameShared = getSharedPreferences("usernameShared", MODE_PRIVATE);
        emailShared = getSharedPreferences("emailShared", MODE_PRIVATE);
        actualscoreShared = getSharedPreferences("actualscoreShared", MODE_PRIVATE);
        totalscoreShared = getSharedPreferences("totalscoreShared", MODE_PRIVATE);
        imageurlShared = getSharedPreferences("imageurlShared", MODE_PRIVATE);
        referralcodeShared = getSharedPreferences("referralcodeShared", MODE_PRIVATE);
        coinsShared = getSharedPreferences("coinsShared", MODE_PRIVATE);
        lastclaimShared = getSharedPreferences("lastclaimShared", MODE_PRIVATE);
        loginmethodShared = getSharedPreferences("loginmethodShared", MODE_PRIVATE);
        facebookShared = getSharedPreferences("facebookShared", MODE_PRIVATE);
        twitterShared = getSharedPreferences("twitterShared", MODE_PRIVATE);
        instagramShared = getSharedPreferences("instagramShared", MODE_PRIVATE);
        earningswithdrawedShared = getSharedPreferences("earningswithdrawedShared", MODE_PRIVATE);
        earningsactualShared = getSharedPreferences("earningsactualShared", MODE_PRIVATE);
        earningsActualWithCurrencyShared = getSharedPreferences("earningsActualWithCurrencyShared", MODE_PRIVATE);
        membersinceShared = getSharedPreferences("membersinceShared", MODE_PRIVATE);
        saveBtn = (Button) findViewById(R.id.save_btn);
        username = (TextInputLayout) findViewById(R.id.username_edit_text);
        username.getEditText().setText(usernameShared.getString("usernameShared", ""));
        facebook = (TextInputLayout) findViewById(R.id.facebook_edit_text);
        if(facebookShared.getString("facebookShared", "").equals("null")) {
            facebook.getEditText().setText("");
        } else {
            facebook.getEditText().setText(facebookShared.getString("facebookShared", ""));
        }
        twitter = (TextInputLayout) findViewById(R.id.twitter_edit_text);
        if(twitterShared.getString("twitterShared", "").equals("null")) {
            twitter.getEditText().setText("");
        } else {
            twitter.getEditText().setText(twitterShared.getString("twitterShared", ""));
        }
        instagram = (TextInputLayout) findViewById(R.id.instagram_edit_text);
        if(instagramShared.getString("instagramShared", "").equals("null")) {
            instagram.getEditText().setText("");
        } else {
            instagram.getEditText().setText(instagramShared.getString("instagramShared", ""));
        }
        profilePic = (CircleImageView) findViewById(R.id.profile_image);
        if (imageurlShared.getString("imageurlShared", "").equals("")) {
            Picasso.get().load(url+"/img/player.png").fit().centerInside().into(profilePic);
        } else {
            Picasso.get().load(imageurlShared.getString("imageurlShared", "")).fit().centerInside().into(profilePic);
        }
        progressBarImage = (ProgressBar) findViewById(R.id.image_progress_bar_edit);
        changePRofileImage = (ImageView) findViewById(R.id.change_image);
        btnAllowPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
        changePRofileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeInfos();
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
        AdColony.configure(EditMyProfileActivity.this, adcolonyAppIdShared.getString("adcolonyAppIdShared", ""), AD_UNIT_ZONE_Ids);
        // Bottom Banner Ads
        bannerTypeShared = getSharedPreferences("bannerTypeShared", MODE_PRIVATE);
        linearBannerAdContainer = (LinearLayout) findViewById(R.id.bottom_banner_main_activity);
        if (bannerTypeShared.getString("bannerTypeShared", "").equals("admob")) {
            // Show Admob Ads in LinearLayout
            MobileAds.initialize(EditMyProfileActivity.this, admobAppIdShared.getString("admobAppIdShared", ""));
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION : {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED   ) {
                    new PermissionManager(com.todocode.ultimequiz.Activities.EditMyProfileActivity.this).writePreference();
                    // Hide Image
                    btnAllowPermissions.setVisibility(View.GONE);
                }
                return;
            }
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(com.todocode.ultimequiz.Activities.EditMyProfileActivity.this, new String[] {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_WRITE_PERMISSION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            assert data != null;
            imageurl = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageurl);
                bitmap = BitmapFactory.decodeStream(inputStream);
                profilePic.setImageBitmap(bitmap);
                uploadImage();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageToStr(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        String encodedImg = Base64.encodeToString(imageByte, Base64.DEFAULT);
        return encodedImg;
    }

    private void uploadImage() {
        progressBarImage.setVisibility(View.VISIBLE);
        final String imageData = imageToStr(bitmap);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/players/image/upload", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String img = jsonObject.getString("image");
                    if (success.equals("1")) {
                        imageurlShared.edit().putString("imageurlShared", img).apply();
                        progressBarImage.setVisibility(View.GONE);
                        Toast.makeText(EditMyProfileActivity.this, getResources().getString(R.string.image_changed), Toast.LENGTH_SHORT).show();
                        active="false";
                        MyProfileActivity.active="false";
                        Intent splash = new Intent(EditMyProfileActivity.this, com.todocode.ultimequiz.Activities.LoadDataActivity.class);
                        startActivity(splash);
                        splash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                // Check that image is added
                params.put("image", imageData);
                params.put("email", emailShared.getString("emailShared", ""));
                params.put("key", getResources().getString(R.string.api_secret_key));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(com.todocode.ultimequiz.Activities.EditMyProfileActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void changeInfos() {
        final String usernameStr = Objects.requireNonNull(username.getEditText()).getText().toString();
        final String fbStr = Objects.requireNonNull(facebook.getEditText()).getText().toString();
        final String twStr = Objects.requireNonNull(twitter.getEditText()).getText().toString();
        final String insStr = Objects.requireNonNull(instagram.getEditText()).getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/players/complete", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")) {
                        Toast.makeText(com.todocode.ultimequiz.Activities.EditMyProfileActivity.this, getResources().getString(R.string.infos_saved), Toast.LENGTH_SHORT).show();
                        usernameShared.edit().putString("usernameShared", usernameStr).apply();
                        twitterShared.edit().putString("twitterShared", twStr).apply();
                        facebookShared.edit().putString("facebookShared", fbStr).apply();
                        instagramShared.edit().putString("instagramShared", insStr).apply();
                        active="false";
                        MyProfileActivity.active="false";
                        Intent splash = new Intent(com.todocode.ultimequiz.Activities.EditMyProfileActivity.this, com.todocode.ultimequiz.Activities.LoadDataActivity.class);
                        startActivity(splash);
                        splash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                params.put("username", usernameStr);
                params.put("facebook", fbStr);
                params.put("twitter", twStr);
                params.put("instagram", insStr);
                params.put("key", getResources().getString(R.string.api_secret_key));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(com.todocode.ultimequiz.Activities.EditMyProfileActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}