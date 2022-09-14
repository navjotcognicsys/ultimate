package com.todocode.ultimequiz.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.startapp.sdk.adsbase.StartAppAd;
import com.todocode.ultimequiz.R;

import java.util.Locale;

public class WelcomeActivity extends AppCompatActivity {
    private Button signIn, signUp;
    private TextView privacy, terms;
    private CheckBox agreeBox;
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
        setContentView(R.layout.activity_welcome);
        StartAppAd.disableSplash();
        signIn = (Button) findViewById(R.id.welcome_login);
        signUp = (Button) findViewById(R.id.welcome_register);
        privacy = (TextView) findViewById(R.id.privacy_policy);
        terms = (TextView) findViewById(R.id.terms_of_use);
        agreeBox = (CheckBox) findViewById(R.id.agree_with_terms);
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent privacy = new Intent(com.todocode.ultimequiz.Activities.WelcomeActivity.this, com.todocode.ultimequiz.Activities.PrivacyPolicyActivity.class);
                startActivity(privacy);
            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent terms = new Intent(com.todocode.ultimequiz.Activities.WelcomeActivity.this, com.todocode.ultimequiz.Activities.TermsOfUseActivity.class);
                startActivity(terms);
            }
        });
        if (agreeBox.isChecked()) {
            // Go To Register Activity
            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent register = new Intent(com.todocode.ultimequiz.Activities.WelcomeActivity.this, RegisterActivity.class);
                    startActivity(register);
                }
            });
            // Go To Login Activity
            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent login = new Intent(com.todocode.ultimequiz.Activities.WelcomeActivity.this, com.todocode.ultimequiz.Activities.LoginActivity.class);
                    startActivity(login);
                }
            });
        } else {
            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(com.todocode.ultimequiz.Activities.WelcomeActivity.this, getResources().getString(R.string.agree_our_privacy), Toast.LENGTH_SHORT).show();
                }
            });
            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(com.todocode.ultimequiz.Activities.WelcomeActivity.this, getResources().getString(R.string.agree_our_privacy), Toast.LENGTH_SHORT).show();
                }
            });
        }
        agreeBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (agreeBox.isChecked()) {
                    // Go To Register Activity
                    signUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent register = new Intent(com.todocode.ultimequiz.Activities.WelcomeActivity.this, RegisterActivity.class);
                            startActivity(register);
                        }
                    });
                    // Go To Login Activity
                    signIn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent login = new Intent(com.todocode.ultimequiz.Activities.WelcomeActivity.this, com.todocode.ultimequiz.Activities.LoginActivity.class);
                            startActivity(login);
                        }
                    });
                } else {
                    signUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(com.todocode.ultimequiz.Activities.WelcomeActivity.this, getResources().getString(R.string.agree_our_privacy), Toast.LENGTH_SHORT).show();
                        }
                    });
                    signIn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(com.todocode.ultimequiz.Activities.WelcomeActivity.this, getResources().getString(R.string.agree_our_privacy), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        StartAppAd.disableSplash();
    }
}