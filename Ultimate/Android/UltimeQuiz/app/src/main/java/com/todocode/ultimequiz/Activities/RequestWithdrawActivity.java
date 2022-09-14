package com.todocode.ultimequiz.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.google.android.material.textfield.TextInputLayout;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.todocode.ultimequiz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class RequestWithdrawActivity extends AppCompatActivity {
    private TextView amount, score_points;
    private TextInputLayout amountEntered, paymentInfo;
    private Spinner selectMethod;
    private SharedPreferences userSituation, idShared;
    private String url;
    private RequestQueue queue;
    ArrayList<String> methods;
    private Button requestWithdraw;
    // AdColony Banner
    public AdColonyAdView adView;
    private LinearLayout linearBannerAdContainer;
    public AdColonyAdOptions adColonyAdOptionsBanner;
    public static String[] AD_UNIT_ZONE_Ids;
    Banner startAppBanner;
    public static String active = "false";
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
        setContentView(R.layout.activity_request_withdraw);
        active = "true";
        StartAppAd.disableSplash();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.request_withdraw));
        url = getResources().getString(R.string.domain_name);
        queue = Volley.newRequestQueue(this);
        idShared = getSharedPreferences("idShared", MODE_PRIVATE);
        amount = (TextView) findViewById(R.id.amount);
        userSituation = getSharedPreferences("userEmail", MODE_PRIVATE);
        score_points = (TextView) findViewById(R.id.actual_score_points);
        amountEntered = (TextInputLayout) findViewById(R.id.select_amount);
        paymentInfo = (TextInputLayout) findViewById(R.id.payment_info);
        selectMethod = (Spinner) findViewById(R.id.spinner_payment_methods);
        requestWithdraw = (Button) findViewById(R.id.send_request);
        getPaymentMethods();
        getPlayerEarningsAndScoreAndMinimumToWithdraw();
        admobAppIdShared = getSharedPreferences("admobAppIdShared", MODE_PRIVATE);
        admobBannerShared = getSharedPreferences("admobBannerShared", MODE_PRIVATE);
        facebookBannerShared = getSharedPreferences("facebookBannerShared", MODE_PRIVATE);
        adcolonyAppIdShared = getSharedPreferences("adcolonyAppIdShared", MODE_PRIVATE);
        adcolonyBannerShared = getSharedPreferences("adcolonyBannerShared", MODE_PRIVATE);
        adcolonyInterstitialShared = getSharedPreferences("adcolonyInterstitialShared", MODE_PRIVATE);
        adcolonyRewardShared = getSharedPreferences("adcolonyRewardShared", MODE_PRIVATE);
        AD_UNIT_ZONE_Ids = new String[] {adcolonyBannerShared.getString("adcolonyBannerShared", ""), adcolonyInterstitialShared.getString("adcolonyInterstitialShared", ""), adcolonyRewardShared.getString("adcolonyRewardShared", "")};
        // Configure AdColony Ads
        AdColony.configure(RequestWithdrawActivity.this, adcolonyAppIdShared.getString("adcolonyAppIdShared", ""), AD_UNIT_ZONE_Ids);
        // Bottom Banner Ads
        bannerTypeShared = getSharedPreferences("bannerTypeShared", MODE_PRIVATE);
        linearBannerAdContainer = (LinearLayout) findViewById(R.id.bottom_banner_main_activity);
        if (bannerTypeShared.getString("bannerTypeShared", "").equals("admob")) {
            // Show Admob Ads in LinearLayout
            MobileAds.initialize(RequestWithdrawActivity.this, admobAppIdShared.getString("admobAppIdShared", ""));
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

    private void getPlayerEarningsAndScoreAndMinimumToWithdraw() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/players/getplayerdata", new Response.Listener<String>() {
            @SuppressLint({"CommitPrefEdits", "SetTextI18n"})
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String minToWithdraw = jsonObject.getString("min_to_withdraw");
                    Double earnings = jsonObject.getDouble("earnings_actual");
                    Double min = jsonObject.getDouble("min_to_withdraw_double");
                    String earningsCur = jsonObject.getString("earnings_actual_with_currency");
                    amount.setText(earningsCur);
                    Integer actual_score = jsonObject.getInt("actual_score");
                    score_points.setText(getResources().getString(R.string.min_to_widthraw)+" "+ minToWithdraw);
                    requestWithdraw.setVisibility(View.VISIBLE);
                    requestWithdraw.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Verify Available Amount
                            if (earnings >= min) {
                                // Verify Entered Amount
                                if (!amountEntered.getEditText().getText().toString().isEmpty()) {
                                    if (Double.parseDouble(amountEntered.getEditText().getText().toString()) <= earnings) {
                                        // Check if payment info is not empty
                                        if (!paymentInfo.getEditText().getText().toString().isEmpty()) {
                                            // Send Withdraw Request And Deactivate Request Button
                                            sendWithdrawRequest();
                                        } else {
                                            Toast.makeText(com.todocode.ultimequiz.Activities.RequestWithdrawActivity.this, getResources().getString(R.string.payment_info_required), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(com.todocode.ultimequiz.Activities.RequestWithdrawActivity.this, getResources().getString(R.string.more_than_available), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(com.todocode.ultimequiz.Activities.RequestWithdrawActivity.this, getResources().getString(R.string.enter_valid_amount), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(com.todocode.ultimequiz.Activities.RequestWithdrawActivity.this, getResources().getString(R.string.less_than_min), Toast.LENGTH_LONG).show();
                                //requestWithdraw.setVisibility(View.GONE);
                            }
                        }
                    });
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

    private void getPaymentMethods() {
        String key = getResources().getString(R.string.api_secret_key);
        String url = getResources().getString(R.string.domain_name)+"/api/paymentmethods/"+key;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            methods = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject method = jsonArray.getJSONObject(i);
                                String payment_method = method.getString("method");
                                methods.add(payment_method);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                    android.R.layout.simple_spinner_item, methods);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            selectMethod.setAdapter(adapter);
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

    private void sendWithdrawRequest() {
        final String amountToSend = amountEntered.getEditText().getText().toString();
        final String paymentInfosDetails = paymentInfo.getEditText().getText().toString();
        String url = getResources().getString(R.string.domain_name);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/withdraws/request/send", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")) {
                        Toast.makeText(com.todocode.ultimequiz.Activities.RequestWithdrawActivity.this, getResources().getString(R.string.withdrawal_success), Toast.LENGTH_LONG).show();
                        Intent splash = new Intent(com.todocode.ultimequiz.Activities.RequestWithdrawActivity.this, com.todocode.ultimequiz.Activities.LoadDataActivity.class);
                        startActivity(splash);
                        splash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                    }
                    if (success.equals("0")) {
                        Toast.makeText(com.todocode.ultimequiz.Activities.RequestWithdrawActivity.this, "0", Toast.LENGTH_LONG).show();
                    }
                    if (success.equals("2")) {
                        Toast.makeText(com.todocode.ultimequiz.Activities.RequestWithdrawActivity.this, "2", Toast.LENGTH_LONG).show();
                    }
                    if (success.equals("3")) {
                        Toast.makeText(com.todocode.ultimequiz.Activities.RequestWithdrawActivity.this, "3", Toast.LENGTH_LONG).show();
                    }
                } catch(JSONException e) {
                    e.getStackTrace();
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
                params.put("player_email", userSituation.getString("userEmail", ""));
                params.put("player_id", idShared.getString("idShared", ""));
                params.put("amount", amountToSend);
                params.put("infos", paymentInfosDetails);
                params.put("key", getResources().getString(R.string.api_secret_key));
                params.put("method", selectMethod.getSelectedItem().toString());
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
    public void onBackPressed() {
        super.onBackPressed();
        active = "false";
    }
}