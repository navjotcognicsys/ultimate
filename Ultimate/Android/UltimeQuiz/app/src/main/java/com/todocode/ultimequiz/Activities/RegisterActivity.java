package com.todocode.ultimequiz.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.startapp.sdk.adsbase.StartAppAd;
import com.todocode.ultimequiz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private Button registerWithEmail, registerFacebook, registerGoogle, registerPhone;
    private LoginButton facebookRealRegisterButton;
    private CallbackManager callbackManager;
    private String url;
    private GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;
    private TextView loginHere;
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
        setContentView(R.layout.activity_register);
        StartAppAd.disableSplash();
        url = getResources().getString(R.string.domain_name);
        loginHere = (TextView) findViewById(R.id.login_here);
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
        loginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                finishAffinity();
            }
        });
        registerWithEmail = (Button) findViewById(R.id.register_with_email);
        registerFacebook = (Button) findViewById(R.id.register_facebook);
        registerGoogle = (Button) findViewById(R.id.register_google);
        registerPhone = (Button) findViewById(R.id.register_phone);
        registerPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerPhone = new Intent(RegisterActivity.this, RegisterWithPhoneNumber.class);
                startActivity(registerPhone);
            }
        });
        registerWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerMail = new Intent(RegisterActivity.this, RegisterWithEmailActivity.class);
                startActivity(registerMail);
            }
        });
        // Facebook Registration
        facebookRealRegisterButton = (LoginButton) findViewById(R.id.facebook_register_btn);
        FacebookSdk.sdkInitialize(getApplicationContext());
        registerFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookRealRegisterButton.performClick();
            }
        });
        facebookRealRegisterButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        callbackManager = CallbackManager.Factory.create();
        facebookRealRegisterButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                facebookLoginInformation(accessToken);
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
            }
        });
        // Google Registration
        registerGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        StartAppAd.disableSplash();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resulrCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resulrCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        super.onActivityResult(requestCode, resulrCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            googleSignIn();
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private void facebookLoginInformation(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            //OnCompleted is invoked once the GraphRequest is successful
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    // GET USER DATA
                    final String name = object.getString("name");
                    final String email = object.getString("email");
                    final String image = object.getJSONObject("picture").getJSONObject("data").getString("url");
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/players/facebook/add", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                // No Errors
                                if (success.equals("done")){
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
                                                Toast.makeText(RegisterActivity.this, ""+getResources().getString(R.string.registered), Toast.LENGTH_SHORT).show();
                                                Intent referral = new Intent(RegisterActivity.this, ReferralCodeActivity.class);
                                                referral.putExtra("email", email);
                                                referral.putExtra("image", image);
                                                referral.putExtra("username", name);
                                                referral.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(referral);
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
                                            params.put("email", email);
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
                                } else if (success.equals("device_error")){
                                    Toast.makeText(RegisterActivity.this, ""+getResources().getString(R.string.one_device), Toast.LENGTH_SHORT).show();
                                }
                                else if (success.equals("email_error")) {
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
                                                Toast.makeText(RegisterActivity.this, ""+getResources().getString(R.string.registered), Toast.LENGTH_SHORT).show();
                                                Toast.makeText(RegisterActivity.this, ""+getResources().getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                                                Intent main = new Intent(RegisterActivity.this, MainActivity.class);
                                                main.putExtra("email", email);
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
                                            params.put("email", email);
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
                            params.put("username", name);
                            params.put("email", email);
                            params.put("image", image);
                            @SuppressLint("HardwareIds") final String device = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                                    Settings.Secure.ANDROID_ID);
                            params.put("device", device);
                            params.put("key", getResources().getString(R.string.api_secret_key));
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // We set parameters to the GraphRequest using a Bundle.
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        // Initiate the GraphRequest
        request.executeAsync();
    }

    private void googleSignIn() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(RegisterActivity.this);
        if (acct != null) {
            final String personName = acct.getDisplayName();
            final String personEmail = acct.getEmail();
            final Uri personPhoto = acct.getPhotoUrl();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/players/google/add", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        // No Errors
                        if (success.equals("done")){
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
                                        Toast.makeText(RegisterActivity.this, ""+getResources().getString(R.string.registered), Toast.LENGTH_SHORT).show();
                                        Intent referral = new Intent(RegisterActivity.this, ReferralCodeActivity.class);
                                        referral.putExtra("email", personEmail);
                                        referral.putExtra("username", personName);
                                        if (personPhoto!=null) {
                                            final String personImageUrl = personPhoto.toString();
                                            referral.putExtra("image", personImageUrl);
                                        } else {
                                            referral.putExtra("image", url+"/img/player.png");
                                        }
                                        referral.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(referral);
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
                                    params.put("email", personEmail);
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
                        } else if (success.equals("device_error")){
                            Toast.makeText(RegisterActivity.this, ""+getResources().getString(R.string.one_device), Toast.LENGTH_SHORT).show();
                        }
                        else if (success.equals("email_error")) {
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
                                        Toast.makeText(RegisterActivity.this, ""+getResources().getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                                        Intent main = new Intent(RegisterActivity.this, MainActivity.class);
                                        main.putExtra("email", personEmail);
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
                                    params.put("email", personEmail);
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
                    params.put("username", personName);
                    params.put("email", personEmail);
                    if (personPhoto!=null) {
                        final String personImageUrl = personPhoto.toString();
                        params.put("image", personImageUrl);
                    } else {
                        params.put("image", url+"/img/player.png");
                    }
                    @SuppressLint("HardwareIds") final String device = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    params.put("device", device);
                    params.put("key", getResources().getString(R.string.api_secret_key));
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }
    }
}