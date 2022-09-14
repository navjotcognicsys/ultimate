package com.todocode.ultimequiz.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.todocode.ultimequiz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CheckLanguageActivity extends AppCompatActivity {
    private String url;
    private SharedPreferences langShared;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_check_language);
        moveTaskToBack(true);*/
        queue = Volley.newRequestQueue(this);
        url = getResources().getString(R.string.domain_name);
        langShared = getSharedPreferences("langShared", MODE_PRIVATE);
        getLang();
    }

    private void getLang() {
        Log.e("get", "get");
        String adsUrl = url+"/api/lang/"+getResources().getString(R.string.api_secret_key);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, adsUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("done", "done");
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject settings = jsonArray.getJSONObject(i);
                                final String lang = settings.getString("lang");
                                langShared.edit().putString("langShared", lang).apply();
                                Intent intro = new Intent(CheckLanguageActivity.this, IntroPagesActivity.class);
                                startActivity(intro);
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
        });
        queue.add(request);
    }
}