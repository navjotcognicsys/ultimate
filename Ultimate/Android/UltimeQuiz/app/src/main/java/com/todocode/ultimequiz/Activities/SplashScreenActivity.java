package com.todocode.ultimequiz.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.startapp.sdk.adsbase.StartAppAd;
import com.todocode.ultimequiz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {
    private RequestQueue queue;
    GoogleSignInClient mGoogleSignInClient;
    private String url;
    private SharedPreferences userSituation, userSituationId;
    FirebaseAuth mAuth;
    private SharedPreferences videoAdCoinsShared, fiftyFiftyShared, hintCoinsShared, currencyShared, idShared, usernameShared, emailShared, actualscoreShared, totalscoreShared, imageurlShared,
            referralcodeShared, coinsShared, lastclaimShared, loginmethodShared, facebookShared, twitterShared, instagramShared,
            earningswithdrawedShared, earningsactualShared, earningsActualWithCurrencyShared, membersinceShared, minToWithdrawShared;
    private SharedPreferences dailyRewardSharedPrefs, admobAppIdShared, admobBannerShared, admobInterstitialShared,
            admobNativeShared, admobRewardShared, facebookBannerShared, facebookInterstitialShared,
            facebookNativeShared, facebookRewardShared, adcolonyAppIdShared, adcolonyBannerShared,
            adcolonyInterstitialShared, adcolonyRewardShared, startappAppIdShared, startappBannerShared,
            startappInterstitialShared, startappRewardShared, bannerTypeShared, interstitialTypeShared, videoTypeShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.dark));
        setContentView(R.layout.activity_splash_screen);
        StartAppAd.disableSplash();
        dailyRewardSharedPrefs = getSharedPreferences("dailyRewardSharedPrefs", MODE_PRIVATE);
        videoAdCoinsShared = getSharedPreferences("videoAdCoinsShared", MODE_PRIVATE);
        fiftyFiftyShared = getSharedPreferences("fiftyFiftyShared", MODE_PRIVATE);
        hintCoinsShared = getSharedPreferences("hintCoinsShared", MODE_PRIVATE);
        admobAppIdShared = getSharedPreferences("admobAppIdShared", MODE_PRIVATE);
        admobBannerShared = getSharedPreferences("admobBannerShared", MODE_PRIVATE);
        admobInterstitialShared = getSharedPreferences("admobInterstitialShared", MODE_PRIVATE);
        admobNativeShared = getSharedPreferences("admobNativeShared", MODE_PRIVATE);
        admobRewardShared = getSharedPreferences("admobRewardShared", MODE_PRIVATE);
        facebookBannerShared = getSharedPreferences("facebookBannerShared", MODE_PRIVATE);
        facebookInterstitialShared = getSharedPreferences("facebookInterstitialShared", MODE_PRIVATE);
        facebookNativeShared = getSharedPreferences("facebookNativeShared", MODE_PRIVATE);
        facebookRewardShared = getSharedPreferences("facebookRewardShared", MODE_PRIVATE);
        adcolonyAppIdShared = getSharedPreferences("adcolonyAppIdShared", MODE_PRIVATE);
        adcolonyBannerShared = getSharedPreferences("adcolonyBannerShared", MODE_PRIVATE);
        adcolonyInterstitialShared = getSharedPreferences("adcolonyInterstitialShared", MODE_PRIVATE);
        adcolonyRewardShared = getSharedPreferences("adcolonyRewardShared", MODE_PRIVATE);
        startappAppIdShared = getSharedPreferences("startappAppIdShared", MODE_PRIVATE);
        startappBannerShared = getSharedPreferences("startappBannerShared", MODE_PRIVATE);
        startappInterstitialShared = getSharedPreferences("startappInterstitialShared", MODE_PRIVATE);
        startappRewardShared = getSharedPreferences("startappRewardShared", MODE_PRIVATE);
        bannerTypeShared = getSharedPreferences("bannerTypeShared", MODE_PRIVATE);
        interstitialTypeShared = getSharedPreferences("interstitialTypeShared", MODE_PRIVATE);
        videoTypeShared = getSharedPreferences("videoTypeShared", MODE_PRIVATE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.blink);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setDuration(750);
        final ImageView splash = (ImageView) findViewById(R.id.splash_image);
        final LinearLayout linear = (LinearLayout) findViewById(R.id.linearr);
        linear.startAnimation(animation);
        int SPLASH_DISPLAY_LENGTH = 3000;
        currencyShared = getSharedPreferences("currencyShared", MODE_PRIVATE);
        userSituation = getSharedPreferences("userEmail", MODE_PRIVATE);
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
        userSituation = getSharedPreferences("userEmail", MODE_PRIVATE);
        userSituationId = getSharedPreferences("userId", MODE_PRIVATE);
        minToWithdrawShared = getSharedPreferences("minToWithdrawShared", MODE_PRIVATE);
        url = getResources().getString(R.string.domain_name);
        queue = Volley.newRequestQueue(this);
        mAuth=FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isNetworkConnected()) {
                    getAds();
                    //getRateAndCurrency();
                    getUserSituation();
                } else {
                    showAlertDialogConnectionProblem();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        StartAppAd.disableSplash();
    }

    private void getAds() {
        String adsUrl = url+"/api/ads/ids/"+getResources().getString(R.string.api_secret_key);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, adsUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject ads = jsonArray.getJSONObject(i);
                                final String admob_app_id = ads.getString("admob_app_id");
                                admobAppIdShared.edit().putString("admobAppIdShared", admob_app_id).apply();
                                //Log.e("admob_app_id",admob_app_id);
                                final String admob_banner = ads.getString("admob_banner");
                                admobBannerShared.edit().putString("admobBannerShared", admob_banner).apply();
                                //Log.e("admob_banner",admob_banner);
                                final String admob_interstitial = ads.getString("admob_interstitial");
                                admobInterstitialShared.edit().putString("admobInterstitialShared", admob_interstitial).apply();
                                //Log.e("admob_interstitial",admob_interstitial);
                                final String admob_native = ads.getString("admob_native");
                                admobNativeShared.edit().putString("admobNativeShared", admob_native).apply();
                                //Log.e("admob_native",admob_native);
                                final String admob_reward = ads.getString("admob_reward");
                                admobRewardShared.edit().putString("admobRewardShared", admob_reward).apply();
                                //Log.e("admob_reward",admob_reward);
                                final String facebook_banner = ads.getString("facebook_banner");
                                facebookBannerShared.edit().putString("facebookBannerShared", facebook_banner).apply();
                                //Log.e("facebook_banner",facebook_banner);
                                final String facebook_interstitial = ads.getString("facebook_interstitial");
                                facebookInterstitialShared.edit().putString("facebookInterstitialShared", facebook_interstitial).apply();
                                //Log.e("facebook_interstitial",facebook_interstitial);
                                final String facebook_native = ads.getString("facebook_native");
                                facebookNativeShared.edit().putString("facebookNativeShared", facebook_native).apply();
                                //Log.e("facebook_native",facebook_native);
                                final String facebook_reward = ads.getString("facebook_reward");
                                facebookRewardShared.edit().putString("facebookRewardShared", facebook_reward).apply();
                                //Log.e("facebook_reward",facebook_reward);
                                final String adcolony_app_id = ads.getString("adcolony_app_id");
                                adcolonyAppIdShared.edit().putString("adcolonyAppIdShared", adcolony_app_id).apply();
                                //Log.e("adcolony_app_id",adcolony_app_id);
                                final String adcolony_banner = ads.getString("adcolony_banner");
                                adcolonyBannerShared.edit().putString("adcolonyBannerShared", adcolony_banner).apply();
                                //Log.e("adcolony_banner",adcolony_banner);
                                final String adcolony_interstitial = ads.getString("adcolony_interstitial");
                                adcolonyInterstitialShared.edit().putString("adcolonyInterstitialShared", adcolony_interstitial).apply();
                                //Log.e("adcolony_interstitial",adcolony_interstitial);
                                final String adcolony_reward = ads.getString("adcolony_reward");
                                adcolonyRewardShared.edit().putString("adcolonyRewardShared", adcolony_reward).apply();
                                //Log.e("adcolony_reward",adcolony_reward);
                                final String startapp_app_id = ads.getString("startapp_app_id");
                                startappAppIdShared.edit().putString("startappAppIdShared", startapp_app_id).apply();
                                //Log.e("startapp_app_id",startapp_app_id);
                                final String startapp_banner = ads.getString("startapp_banner");
                                startappBannerShared.edit().putString("startappBannerShared", startapp_banner).apply();
                                //Log.e("startapp_banner",startapp_banner);
                                final String startapp_interstitial = ads.getString("startapp_interstitial");
                                startappInterstitialShared.edit().putString("startappInterstitialShared", startapp_interstitial).apply();
                                //Log.e("startapp_interstitial",startapp_interstitial);
                                final String startapp_reward = ads.getString("startapp_reward");
                                startappRewardShared.edit().putString("startappRewardShared", startapp_reward).apply();
                                //Log.e("startapp_reward",startapp_reward);
                                final String banner_type = ads.getString("banner_type");
                                bannerTypeShared.edit().putString("bannerTypeShared", banner_type).apply();
                                //Log.e("banner_type",banner_type);
                                final String interstitial_type = ads.getString("interstitial_type");
                                interstitialTypeShared.edit().putString("interstitialTypeShared", interstitial_type).apply();
                                //Log.e("interstitial_type",interstitial_type);
                                final String video_type = ads.getString("video_type");
                                videoTypeShared.edit().putString("videoTypeShared", video_type).apply();
                                //Log.e("video_type",video_type);
                                final int daily = ads.getInt("daily_reward");
                                dailyRewardSharedPrefs.edit().putInt("dailyRewardSharedPrefs", daily).apply();
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
        });
        queue.add(request);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void getConnectedUserData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/players/getplayerdata", new Response.Listener<String>() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String minToWithdraw = jsonObject.getString("min_to_withdraw");
                    minToWithdrawShared.edit().putString("minToWithdrawShared", String.valueOf(minToWithdraw)).apply();
                    int hintCoins = jsonObject.getInt("hint_coins");
                    hintCoinsShared.edit().putString("hintCoinsShared", String.valueOf(hintCoins)).apply();
                    int videoAdCoins = jsonObject.getInt("video_ad_coins");
                    videoAdCoinsShared.edit().putString("videoAdCoinsShared", String.valueOf(videoAdCoins)).apply();
                    String userName = jsonObject.getString("username");
                    usernameShared.edit().putString("usernameShared", userName).apply();
                    String fifty_fifty = jsonObject.getString("fifty_fifty");
                    fiftyFiftyShared.edit().putString("fiftyFiftyShared", fifty_fifty).apply();
                    String userEmail = jsonObject.getString("email_or_phone");
                    emailShared.edit().putString("emailShared", userEmail).apply();
                    String userId = String.valueOf(jsonObject.getInt("id"));
                    idShared.edit().putString("idShared", String.valueOf(userId)).apply();
                    String userImageUrl = jsonObject.getString("image_url");
                    imageurlShared.edit().putString("imageurlShared", userImageUrl).apply();
                    Double earnings = jsonObject.getDouble("earnings_actual");
                    earningsactualShared.edit().putString("earningsactualShared", String.valueOf(earnings)).apply();
                    String earningsCur = jsonObject.getString("earnings_actual_with_currency");
                    earningsActualWithCurrencyShared.edit().putString("earningsActualWithCurrencyShared", earningsCur).apply();
                    Integer actual_score = jsonObject.getInt("actual_score");
                    actualscoreShared.edit().putString("actualscoreShared", String.valueOf(actual_score)).apply();
                    Integer total_score = jsonObject.getInt("total_score");
                    totalscoreShared.edit().putString("totalscoreShared", String.valueOf(total_score)).apply();
                    Double earnings_withdrawed = jsonObject.getDouble("earnings_withdrawed");
                    earningswithdrawedShared.edit().putString("earningswithdrawedShared", String.valueOf(earnings_withdrawed)).apply();
                    Integer coins = jsonObject.getInt("coins");
                    coinsShared.edit().putString("coinsShared", String.valueOf(coins)).apply();
                    String referral_code = jsonObject.getString("referral_code");
                    referralcodeShared.edit().putString("referralcodeShared", referral_code).apply();
                    String currency = jsonObject.getString("currency");
                    currencyShared.edit().putString("currencyShared", currency).apply();
                    String last_claim = jsonObject.getString("last_claim");
                    lastclaimShared.edit().putString("lastclaimShared", last_claim).apply();
                    String member_since = jsonObject.getString("member_since");
                    membersinceShared.edit().putString("membersinceShared", member_since).apply();
                    String login_method = jsonObject.getString("login_method");
                    loginmethodShared.edit().putString("loginmethodShared", login_method).apply();
                    String facebook = jsonObject.getString("facebook");
                    facebookShared.edit().putString("facebookShared", facebook).apply();
                    String twitter = jsonObject.getString("twitter");
                    twitterShared.edit().putString("twitterShared", twitter).apply();
                    String instagram = jsonObject.getString("instagram");
                    instagramShared.edit().putString("instagramShared", instagram).apply();
                    // Register User Situation In Shared Prefs
                    userSituationId.edit().putString("userId", userId).apply();
                    userSituation.edit().putString("userEmail", userEmail).apply();
                    Intent homePage = new Intent(com.todocode.ultimequiz.Activities.SplashScreenActivity.this, MainActivity.class);
                    homePage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homePage);
                    finish();
                } catch(JSONException e){
                    Log.e("Error ", e.getMessage());
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
                params.put("email", userSituation.getString("userEmail", ""));
                params.put("key", getResources().getString(R.string.api_secret_key));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void getUserSituation() {
        String userEmailStr = userSituation.getString("userEmail","");
        if (!userEmailStr.equals("")) {
            final String userToCheck = userEmailStr;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/players/situation", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        // No Errors
                        if (success.equals("loggedSuccess")){
                            getConnectedUserData();
                        } else {
                            userSituation.edit().putString("userEmail", "").apply();
                            if (LoginManager.getInstance() != null) {
                                LoginManager.getInstance().logOut();
                            }
                            // Logout Google
                            if (mGoogleSignInClient != null) {
                                mGoogleSignInClient.signOut();
                            }
                            if (mAuth != null) {
                                FirebaseAuth.getInstance().signOut();
                            }
                            Intent registerPage = new Intent(com.todocode.ultimequiz.Activities.SplashScreenActivity.this, WelcomeActivity.class);
                            registerPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(registerPage);
                            finish();
                        }
                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", userToCheck);
                    params.put("key", getResources().getString(R.string.api_secret_key));
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            Intent loginPage = new Intent(com.todocode.ultimequiz.Activities.SplashScreenActivity.this, WelcomeActivity.class);
            loginPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginPage);
            finish();
        }
    }

    public void showAlertDialogConnectionProblem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.splash_connection_problem_title));
        builder.setIcon(R.drawable.ic_wifi);
        builder.setMessage(getString(R.string.splash_connection_problem_message));
        builder.setPositiveButton(getString(R.string.splash_connection_problem_retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                com.todocode.ultimequiz.Activities.SplashScreenActivity.this.recreate();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}