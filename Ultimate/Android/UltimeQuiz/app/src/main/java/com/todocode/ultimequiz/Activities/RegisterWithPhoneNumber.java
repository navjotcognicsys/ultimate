package com.todocode.ultimequiz.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.startapp.sdk.adsbase.StartAppAd;
import com.todocode.ultimequiz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RegisterWithPhoneNumber extends AppCompatActivity {
    private TextInputLayout phoneNumberEditText, verificationEditText;
    private Button sendOtpBtn, verifyOtp;
    private CountryCodePicker ccp;
    private LinearLayout linear1, linear2;
    String phonenumber;
    String otpid;
    String url;
    FirebaseAuth mAuth;
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
        setContentView(R.layout.activity_register_with_phone_number);
        StartAppAd.disableSplash();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        phoneNumberEditText = (TextInputLayout) findViewById(R.id.register_phone_edit_text);
        sendOtpBtn = (Button) findViewById(R.id.send_otp);
        verificationEditText = (TextInputLayout) findViewById(R.id.verification_code);
        verifyOtp = (Button) findViewById(R.id.verify_otp);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        linear1 = (LinearLayout) findViewById(R.id.linear1);
        linear2 = (LinearLayout) findViewById(R.id.linear2);
        mAuth=FirebaseAuth.getInstance();
        sendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear1.setVisibility(View.GONE);
                linear2.setVisibility(View.VISIBLE);
                phonenumber = ccp.getSelectedCountryCodeWithPlus()+phoneNumberEditText.getEditText().getText().toString();
                sendOtpCode();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        StartAppAd.disableSplash();
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

    private void sendOtpCode() {
        Toast.makeText(RegisterWithPhoneNumber.this, "OTP code will be sent!", Toast.LENGTH_SHORT).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phonenumber,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
                {
                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken)
                    {
                        otpid=s;
                        Log.e("code", "sent");
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                        Toast.makeText(RegisterWithPhoneNumber.this, "TimeOut!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
                    {
                        verifyOtp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.e("verify", "clicked");
                                if(Objects.requireNonNull(verificationEditText.getEditText()).getText().toString().isEmpty())
                                    Toast.makeText(getApplicationContext(),"Blank Field can not be processed",Toast.LENGTH_LONG).show();
                                else if(verificationEditText.getEditText().getText().toString().length()!=6)
                                    Toast.makeText(getApplicationContext(),"Invalid OTP Format",Toast.LENGTH_LONG).show();
                                else
                                { if (Objects.equals(phoneAuthCredential.getSmsCode(), verificationEditText.getEditText().getText().toString())) {
                                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(otpid,verificationEditText.getEditText().getText().toString());
                                    signInWithPhoneAuthCredential(credential);
                                } else {
                                    Toast.makeText(RegisterWithPhoneNumber.this, "Code Incorrect!", Toast.LENGTH_SHORT).show();
                                }
                                }
                            }
                        });
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
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

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(RegisterWithPhoneNumber.this, "Code Correct", Toast.LENGTH_SHORT).show();
                            verifyIfThisPlayerExists();

                        } else {
                            Toast.makeText(getApplicationContext(),"Sign in with phone auth credential error!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void registerPlayerviaOtp() {
        String userPhone = phonenumber;
        String key = getResources().getString(R.string.api_secret_key);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/players/otp/add", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    // No Errors
                    if (success.equals("done")){
                        getConnectedUserData(userPhone);
                        Toast.makeText(RegisterWithPhoneNumber.this, ""+getResources().getString(R.string.registered), Toast.LENGTH_SHORT).show();
                        Intent gotoReferral = new Intent(RegisterWithPhoneNumber.this, ReferralCodeActivity.class);
                        gotoReferral.putExtra("email", userPhone);
                        gotoReferral.putExtra("username", userPhone);
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
                params.put("phone", userPhone);
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

    private void verifyIfThisPlayerExists() {
        String phoneNumberStr = ccp.getSelectedCountryCodeWithPlus()+phoneNumberEditText.getEditText().getText().toString();
        String key = getResources().getString(R.string.api_secret_key);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/players/otp/verify", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    int message = jsonObject.getInt("message");
                    // No Errors
                    if (success.equals("done")){
                        registerPlayerviaOtp();
                    }
                    else if (success.equals("already")){
                        // Here
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/players/getplayerdata", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.e("email", phoneNumberStr);
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
                                    userSituationId.edit().putString("userId", userId).apply();
                                    userSituation.edit().putString("userEmail", userEmail).apply();
                                    userSituationId.edit().putString("userId", String.valueOf(message)).apply();
                                    Toast.makeText(RegisterWithPhoneNumber.this, ""+getResources().getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                                    Intent main = new Intent(RegisterWithPhoneNumber.this, MainActivity.class);
                                    main.putExtra("email", phoneNumberStr);
                                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(main);
                                    finishAffinity();
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
                                params.put("email", phoneNumberStr);
                                params.put("key", getResources().getString(R.string.api_secret_key));
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                10000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        requestQueue.add(stringRequest);
                    }

                    else if (success.equals("one_device")){
                        Toast.makeText(RegisterWithPhoneNumber.this, "A Player with this device already exists!", Toast.LENGTH_SHORT).show();
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
                params.put("phone", phoneNumberStr);
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
}