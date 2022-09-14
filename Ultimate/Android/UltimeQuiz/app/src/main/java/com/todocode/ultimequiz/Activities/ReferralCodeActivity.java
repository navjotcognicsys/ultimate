package com.todocode.ultimequiz.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.android.material.textfield.TextInputLayout;
import com.startapp.sdk.adsbase.StartAppAd;
import com.todocode.ultimequiz.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReferralCodeActivity extends AppCompatActivity {
    private Button skip, next;
    private TextInputLayout referral_edit_text;
    private String url;
    private int tries = 0;

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
        setContentView(R.layout.activity_referral_code);
        StartAppAd.disableSplash();
        url = getResources().getString(R.string.domain_name);
        skip = (Button) findViewById(R.id.skip);
        next = (Button) findViewById(R.id.next);
        referral_edit_text = (TextInputLayout) findViewById(R.id.referral_edit_text);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent completeProfile = new Intent(com.todocode.ultimequiz.Activities.ReferralCodeActivity.this, com.todocode.ultimequiz.Activities.CompleteProfileActivity.class);
                completeProfile.putExtra("email", getIntent().getStringExtra("email"));
                completeProfile.putExtra("username", getIntent().getStringExtra("username"));
                completeProfile.putExtra("image", getIntent().getStringExtra("image"));
                completeProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(completeProfile);
                finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approveReferral();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        StartAppAd.disableSplash();
    }

    private void approveReferral() {
        String referralCode = referral_edit_text.getEditText().getText().toString();
        String key = getResources().getString(R.string.api_secret_key);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/players/email/referral", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    // No Errors
                    if (success.equals("done")){
                        Toast.makeText(com.todocode.ultimequiz.Activities.ReferralCodeActivity.this, ""+getResources().getString(R.string.referral_code_success), Toast.LENGTH_SHORT).show();
                        Intent completeProfile = new Intent(com.todocode.ultimequiz.Activities.ReferralCodeActivity.this, com.todocode.ultimequiz.Activities.CompleteProfileActivity.class);
                        completeProfile.putExtra("email", getIntent().getStringExtra("email"));
                        completeProfile.putExtra("username", getIntent().getStringExtra("username"));
                        completeProfile.putExtra("image", getIntent().getStringExtra("image"));
                        completeProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(completeProfile);
                        finish();
                    }
                    else if(success.equals("code_not_correct")) {
                        if (tries>3) {
                            Toast.makeText(com.todocode.ultimequiz.Activities.ReferralCodeActivity.this, ""+getResources().getString(R.string.referral_code_not_correct_times), Toast.LENGTH_SHORT).show();
                            Intent completeProfile = new Intent(com.todocode.ultimequiz.Activities.ReferralCodeActivity.this, com.todocode.ultimequiz.Activities.CompleteProfileActivity.class);
                            completeProfile.putExtra("email", getIntent().getStringExtra("email"));
                            completeProfile.putExtra("username", getIntent().getStringExtra("username"));
                            completeProfile.putExtra("image", getIntent().getStringExtra("image"));
                            completeProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(completeProfile);
                            finish();
                        } else {
                            // Give it another try
                            tries=tries+1;
                            Toast.makeText(com.todocode.ultimequiz.Activities.ReferralCodeActivity.this, ""+getResources().getString(R.string.referral_code_not_correct), Toast.LENGTH_SHORT).show();
                        }
                    } else if (success.equals("duplication")) {
                        Toast.makeText(com.todocode.ultimequiz.Activities.ReferralCodeActivity.this, ""+getResources().getString(R.string.referral_code_duplication), Toast.LENGTH_SHORT).show();
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
                params.put("email", getIntent().getStringExtra("email"));
                params.put("referral", referralCode);
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