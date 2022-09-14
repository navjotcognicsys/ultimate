package com.todocode.ultimequiz.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Random;

public class RandomCodeRegistrationActivity extends AppCompatActivity {
    private TextView code;
    private TextInputLayout verificationEditText;
    private Button validateBtn;
    private AwesomeValidation validator;
    private String url;
    private int tries = 0;
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
        setContentView(R.layout.activity_random_code_registration);
        StartAppAd.disableSplash();
        code = (TextView) findViewById(R.id.code);
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
        verificationEditText = (TextInputLayout) findViewById(R.id.verification_code_edit_text);
        validateBtn = (Button) findViewById(R.id.validate);
        String rand = generateRandomChars("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890", 8);
        code.setText(rand);
        url = getResources().getString(R.string.domain_name);
        validator = new AwesomeValidation(ValidationStyle.BASIC);
        validateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupRules();
                if (validator.validate()) {
                    checkIfValidated();
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

    private void checkIfValidated() {
        String codeStr = code.getText().toString();
        String codeEnteredByUser = verificationEditText.getEditText().getText().toString();
        if (codeStr.equals(codeEnteredByUser)) {
            accountValidatedAndRegister();
        } else {
            if (tries>3) {
                blockEmail();
            } else {
                // Give it another try
                tries=tries+1;
                Toast.makeText(this, ""+getResources().getString(R.string.verification_not_correct), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void blockEmail() {
        String emailToBlock = getIntent().getStringExtra("email");
        String key = getResources().getString(R.string.api_secret_key);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/players/email/block", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    // No Errors
                    if (success.equals("done")){
                        Toast.makeText(com.todocode.ultimequiz.Activities.RandomCodeRegistrationActivity.this, ""+getResources().getString(R.string.blocked), Toast.LENGTH_SHORT).show();
                        Intent goToRegisterPage = new Intent(com.todocode.ultimequiz.Activities.RandomCodeRegistrationActivity.this, com.todocode.ultimequiz.Activities.RegisterActivity.class);
                        goToRegisterPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(goToRegisterPage);
                        finishAffinity();
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
                params.put("email", emailToBlock);
                params.put("key", key);
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

    private void accountValidatedAndRegister() {
        String userEmail = getIntent().getStringExtra("email");
        String userName = getIntent().getStringExtra("username");
        String userPassword = getIntent().getStringExtra("password");
        String key = getResources().getString(R.string.api_secret_key);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/players/email/add", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    // No Errors
                    if (success.equals("done")){
                        getConnectedUserData(userEmail);
                        Toast.makeText(com.todocode.ultimequiz.Activities.RandomCodeRegistrationActivity.this, ""+getResources().getString(R.string.registered), Toast.LENGTH_SHORT).show();
                        Intent gotoReferral = new Intent(com.todocode.ultimequiz.Activities.RandomCodeRegistrationActivity.this, com.todocode.ultimequiz.Activities.ReferralCodeActivity.class);
                        gotoReferral.putExtra("email", getIntent().getStringExtra("email"));
                        gotoReferral.putExtra("username", getIntent().getStringExtra("username"));
                        gotoReferral.putExtra("image", url+"/img/player.png");
                        gotoReferral.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(gotoReferral);
                        finishAffinity();
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
                params.put("email", userEmail);
                params.put("username", userName);
                params.put("password", userPassword);
                @SuppressLint("HardwareIds") final String device = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                params.put("device", device);
                params.put("key", key);
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

    public static String generateRandomChars(String candidateChars, int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(candidateChars.charAt(random.nextInt(candidateChars
                    .length())));
        }
        return sb.toString();
    }

    public void setupRules() {
        validator.addValidation(this, R.id.validate, RegexTemplate.NOT_EMPTY, R.string.not_empty);
    }

}