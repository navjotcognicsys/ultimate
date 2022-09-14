package com.todocode.ultimequiz.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class RegisterWithEmailActivity extends AppCompatActivity {
    private Button registerBtn;
    private TextInputLayout usernameInput, emailInput, passwordInput;
    private String url;
    private TextView alreadyMember;
    private AwesomeValidation validator;

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
        setContentView(R.layout.activity_register_with_email);
        StartAppAd.disableSplash();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        validator = new AwesomeValidation(ValidationStyle.BASIC);
        alreadyMember = (TextView) findViewById(R.id.already_member);
        alreadyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(com.todocode.ultimequiz.Activities.RegisterWithEmailActivity.this, com.todocode.ultimequiz.Activities.LoginActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                finish();
            }
        });
        registerBtn = (Button) findViewById(R.id.register_btn);
        usernameInput = (TextInputLayout) findViewById(R.id.register_username_edit_text);
        emailInput = (TextInputLayout) findViewById(R.id.register_email_edit_text);
        passwordInput = (TextInputLayout) findViewById(R.id.register_password_edit_text);
        url = getResources().getString(R.string.domain_name);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupRules();
                if (validator.validate()) {
                    verifyIfEmailDoesNotExistAndNotBlocked();
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

    public void setupRules() {
        validator.addValidation(this, R.id.register_username_edit_text, RegexTemplate.NOT_EMPTY, R.string.not_empty);
        validator.addValidation(this, R.id.register_password_edit_text, RegexTemplate.NOT_EMPTY, R.string.valid_password);
        validator.addValidation(this, R.id.register_email_edit_text, Patterns.EMAIL_ADDRESS, R.string.valid_email);
    }

    private void verifyIfEmailDoesNotExistAndNotBlocked() {
        String userEmailStr = emailInput.getEditText().getText().toString();
        if (!userEmailStr.equals("")) {
            final String userToCheck = userEmailStr;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/players/before/verify/email", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        // No Errors
                        if (success.equals("blocked")){
                            Toast.makeText(com.todocode.ultimequiz.Activities.RegisterWithEmailActivity.this, ""+getResources().getString(R.string.email_blocked), Toast.LENGTH_SHORT).show();
                        }
                        else if(success.equals("already")) {
                            Toast.makeText(com.todocode.ultimequiz.Activities.RegisterWithEmailActivity.this, ""+getResources().getString(R.string.email_exists), Toast.LENGTH_SHORT).show();
                        } else if(success.equals("ready")) {
                            goToValidateRegistration();
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
        }
    }

    private void goToValidateRegistration() {
        Intent goToValidationPage = new Intent(com.todocode.ultimequiz.Activities.RegisterWithEmailActivity.this, RandomCodeRegistrationActivity.class);
        goToValidationPage.putExtra("username", usernameInput.getEditText().getText().toString());
        goToValidationPage.putExtra("email", emailInput.getEditText().getText().toString());
        goToValidationPage.putExtra("image", url+"/img/player.png");
        goToValidationPage.putExtra("password", passwordInput.getEditText().getText().toString());
        goToValidationPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(goToValidationPage);
        finish();
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