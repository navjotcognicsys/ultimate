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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
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

public class CompleteProfileActivity extends AppCompatActivity {
    private Button saveBtn;
    private TextInputLayout username, facebook, twitter, instagram;
    private String url;
    private CircleImageView profilePic;
    private ImageView changePRofileImage, btnAllowPermissions;
    private static final int PICK_IMAGE = 1;
    Uri imageurl;
    private Bitmap bitmap;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    private ProgressBar progressBarImage;
    private SharedPreferences langShared, imageurlShared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        langShared = getSharedPreferences("langShared", MODE_PRIVATE);
        Locale locale = new Locale(langShared.getString("langShared", ""));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        StartAppAd.disableSplash();
        imageurlShared = getSharedPreferences("imageurlShared", MODE_PRIVATE);
        btnAllowPermissions = (ImageView) findViewById(R.id.allow_permission);
        if (new PermissionManager(com.todocode.ultimequiz.Activities.CompleteProfileActivity.this).checkPreference()) {
            btnAllowPermissions.setVisibility(View.GONE);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.complete_profile));
        setSupportActionBar(toolbar);
        saveBtn = (Button) findViewById(R.id.save_btn);
        username = (TextInputLayout) findViewById(R.id.username_edit_text);
        facebook = (TextInputLayout) findViewById(R.id.facebook_edit_text);
        twitter = (TextInputLayout) findViewById(R.id.twitter_edit_text);
        instagram = (TextInputLayout) findViewById(R.id.instagram_edit_text);
        url = getResources().getString(R.string.domain_name);
        profilePic = (CircleImageView) findViewById(R.id.profile_image);
        username.getEditText().setText(getIntent().getStringExtra("username"));
        if (getIntent().getStringExtra("image").equals("")) {
            Picasso.get().load(url+"/img/player.png").fit().centerInside().into(profilePic);
        } else {
            Picasso.get().load(getIntent().getStringExtra("image")).fit().centerInside().into(profilePic);
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
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        StartAppAd.disableSplash();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    new PermissionManager(CompleteProfileActivity.this).writePreference();
                    // Hide Image
                    btnAllowPermissions.setVisibility(View.GONE);
                }
                return;
            }
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(com.todocode.ultimequiz.Activities.CompleteProfileActivity.this, new String[] {
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
                String imgUrl = jsonObject.getString("image");
                imageurlShared.edit().putString("imageurlShared", imgUrl).apply();
                progressBarImage.setVisibility(View.GONE);
                Toast.makeText(com.todocode.ultimequiz.Activities.CompleteProfileActivity.this, getResources().getString(R.string.image_changed), Toast.LENGTH_SHORT).show();
            }
                catch(JSONException e){
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
                params.put("email", getIntent().getStringExtra("email"));
                params.put("key", getResources().getString(R.string.api_secret_key));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(com.todocode.ultimequiz.Activities.CompleteProfileActivity.this);
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
                        Toast.makeText(com.todocode.ultimequiz.Activities.CompleteProfileActivity.this, getResources().getString(R.string.infos_saved), Toast.LENGTH_SHORT).show();
                        Intent social = new Intent(com.todocode.ultimequiz.Activities.CompleteProfileActivity.this, MainActivity.class);
                        social.putExtra("email", getIntent().getStringExtra("email"));
                        social.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(social);
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
                params.put("email", getIntent().getStringExtra("email"));
                params.put("username", usernameStr);
                params.put("facebook", fbStr);
                params.put("twitter", twStr);
                params.put("instagram", insStr);
                params.put("key", getResources().getString(R.string.api_secret_key));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(com.todocode.ultimequiz.Activities.CompleteProfileActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}