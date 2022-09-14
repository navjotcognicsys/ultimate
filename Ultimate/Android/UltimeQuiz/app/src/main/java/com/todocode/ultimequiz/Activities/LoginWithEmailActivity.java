package com.todocode.ultimequiz.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.textfield.TextInputLayout;
import com.startapp.sdk.adsbase.StartAppAd;
import com.todocode.ultimequiz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class LoginWithEmailActivity extends AppCompatActivity {
    private TextInputLayout loginEmail, loginPassword;
    private Button loginBtn;
    private AwesomeValidation validator;
    private String url;
    SharedPreferences userSituation, userSituationId;
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
        setContentView(R.layout.activity_login_with_email);
        StartAppAd.disableSplash();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        validator = new AwesomeValidation(ValidationStyle.BASIC);
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
        userSituation = getSharedPreferences("userEmail", MODE_PRIVATE);
        url = getResources().getString(R.string.domain_name);
        loginEmail = (TextInputLayout) findViewById(R.id.login_email_edit_text);
        loginPassword = (TextInputLayout) findViewById(R.id.login_password_edit_text);
        loginBtn = (Button) findViewById(R.id.login_with_email);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupRules();
                if (validator.validate()) {
                    loginWithEmail();
                    validator.clear();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        StartAppAd.disableSplash();
    }

    private void getConnectedUserData(String emailStr) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/players/getplayerdata", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String userName = jsonObject.getString("username");
                    usernameShared.edit().putString("usernameShared", userName).apply();
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
                params.put("email", emailStr);
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

    private void loginWithEmail() {
        final String enteredEmail = Objects.requireNonNull(this.loginEmail.getEditText()).getText().toString().trim();
        final String enteredPassword = Objects.requireNonNull(this.loginPassword.getEditText()).getText().toString().trim();
        Log.e("email", enteredEmail);
        Log.e("password", enteredPassword);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/players/login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    // No Errors
                    if (success.equals("1")){
                        getConnectedUserData(enteredEmail);
                        Toast.makeText(com.todocode.ultimequiz.Activities.LoginWithEmailActivity.this, getString(R.string.login_success), Toast.LENGTH_LONG).show();
                        Intent homePage = new Intent(com.todocode.ultimequiz.Activities.LoginWithEmailActivity.this, MainActivity.class);
                        homePage.putExtra("email", enteredEmail);
                        homePage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homePage);
                        finishAffinity();
                        finish();
                    } else {
                        if(success.equals("emailError")) {
                            Toast.makeText(com.todocode.ultimequiz.Activities.LoginWithEmailActivity.this, "Email Error "+getResources().getString(R.string.no_records), Toast.LENGTH_SHORT).show();
                        }
                        if(success.equals("passwordError")) {
                            Toast.makeText(com.todocode.ultimequiz.Activities.LoginWithEmailActivity.this, "Password Error "+getResources().getString(R.string.no_records), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch(JSONException e){
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
                params.put("email", enteredEmail);
                params.put("password", enteredPassword);
                params.put("key", getResources().getString(R.string.api_secret_key));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(com.todocode.ultimequiz.Activities.LoginWithEmailActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void setupRules() {
        validator.addValidation(this, R.id.login_password_edit_text, RegexTemplate.NOT_EMPTY, R.string.valid_password);
        validator.addValidation(this, R.id.login_email_edit_text, Patterns.EMAIL_ADDRESS, R.string.valid_email);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}