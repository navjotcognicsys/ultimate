package com.todocode.ultimequiz.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.todocode.ultimequiz.Adapters.AllPlayersAdapter;
import com.todocode.ultimequiz.Models.Player;
import com.todocode.ultimequiz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderboardsActivity extends AppCompatActivity {
    public static String active = "false";
    private CircleImageView imageFirst, imageSecond, imageTirth, image1, image2, image3;
    private TextView username1, username2, username3, points1, points2, points3;
    private String url;
    private RequestQueue queue;
    private AllPlayersAdapter playersAdapter;
    private RecyclerView playersRecyclerView;
    private ArrayList<Player> playersArrayList;
    private LinearLayout linear1, linear2, linear3;
    // AdColony Banner
    public AdColonyAdView adView;
    private LinearLayout linearBannerAdContainer;
    public AdColonyAdOptions adColonyAdOptionsBanner;
    public static String[] AD_UNIT_ZONE_Ids;
    Banner startAppBanner;
    private SharedPreferences userSituation, admobBannerShared, bannerTypeShared, adcolonyAppIdShared, adcolonyBannerShared, admobAppIdShared, facebookBannerShared,adcolonyInterstitialShared, adcolonyRewardShared;

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
        setContentView(R.layout.activity_leaderboards);
        StartAppAd.disableSplash();
        active = "true";
        Toolbar toolbar = findViewById(R.id.leaderboards_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.drawer_menu_leaderboards));
        linear1 = (LinearLayout) findViewById(R.id.linear1);
        linear2 = (LinearLayout) findViewById(R.id.linear2);
        linear3 = (LinearLayout) findViewById(R.id.linear3);
        userSituation = getSharedPreferences("userEmail", MODE_PRIVATE);
        imageFirst = (CircleImageView) findViewById(R.id.image_first);
        imageSecond = (CircleImageView) findViewById(R.id.image_second);
        imageTirth = (CircleImageView) findViewById(R.id.image_tirth);
        image1 = (CircleImageView) findViewById(R.id.image_1);
        image2 = (CircleImageView) findViewById(R.id.image_2);
        image3 = (CircleImageView) findViewById(R.id.image_3);
        username1 = (TextView) findViewById(R.id.username_1);
        username2 = (TextView) findViewById(R.id.username_2);
        username3 = (TextView) findViewById(R.id.username_3);
        points1 = (TextView) findViewById(R.id.points_1);
        points2 = (TextView) findViewById(R.id.points_2);
        points3 = (TextView) findViewById(R.id.points_3);
        url = getString(R.string.domain_name);
        queue = Volley.newRequestQueue(this);
        getFirstsPlayer();
        playersRecyclerView = (RecyclerView) findViewById(R.id.all_players_recycler);
        playersArrayList = new ArrayList<>();
        playersAdapter = new AllPlayersAdapter(this, playersArrayList);
        playersRecyclerView.setAdapter(playersAdapter);
        playersRecyclerView.setHasFixedSize(true);
        playersRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        getPlayersFrom4To50();
        playersAdapter.setOnItemClickListener(new AllPlayersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (playersArrayList.get(position).getEmail_or_phone().equals(userSituation.getString("userEmail", ""))) {
                    if (MyProfileActivity.active.equals("false")) {
                        Intent myprofile = new Intent(LeaderboardsActivity.this, MyProfileActivity.class);
                        startActivity(myprofile);
                    }
                } else {
                    if (PlayerProfileActivity.active.equals("false")) {
                        Intent player = new Intent(LeaderboardsActivity.this, PlayerProfileActivity.class);
                        player.putExtra("username",playersArrayList.get(position).getUsername());
                        player.putExtra("since",playersArrayList.get(position).getMember_since());
                        player.putExtra("image",playersArrayList.get(position).getImage_url());
                        player.putExtra("points",playersArrayList.get(position).getActual_score());
                        player.putExtra("coins",playersArrayList.get(position).getCoins());
                        player.putExtra("earnings",playersArrayList.get(position).getEarnings_actual());
                        player.putExtra("facebook",playersArrayList.get(position).getFacebook());
                        player.putExtra("twitter",playersArrayList.get(position).getTwitter());
                        player.putExtra("instagram",playersArrayList.get(position).getInstagram());
                        player.putExtra("referrals",playersArrayList.get(position).getReferrals());
                        startActivity(player);
                    }
                }
            }
        });
        admobAppIdShared = getSharedPreferences("admobAppIdShared", MODE_PRIVATE);
        admobBannerShared = getSharedPreferences("admobBannerShared", MODE_PRIVATE);
        facebookBannerShared = getSharedPreferences("facebookBannerShared", MODE_PRIVATE);
        adcolonyAppIdShared = getSharedPreferences("adcolonyAppIdShared", MODE_PRIVATE);
        adcolonyBannerShared = getSharedPreferences("adcolonyBannerShared", MODE_PRIVATE);
        adcolonyInterstitialShared = getSharedPreferences("adcolonyInterstitialShared", MODE_PRIVATE);
        adcolonyRewardShared = getSharedPreferences("adcolonyRewardShared", MODE_PRIVATE);
        AD_UNIT_ZONE_Ids = new String[] {adcolonyBannerShared.getString("adcolonyBannerShared", ""), adcolonyInterstitialShared.getString("adcolonyInterstitialShared", ""), adcolonyRewardShared.getString("adcolonyRewardShared", "")};
        // Configure AdColony Ads
        AdColony.configure(LeaderboardsActivity.this, adcolonyAppIdShared.getString("adcolonyAppIdShared", ""), AD_UNIT_ZONE_Ids);
        // Bottom Banner Ads
        bannerTypeShared = getSharedPreferences("bannerTypeShared", MODE_PRIVATE);
        linearBannerAdContainer = (LinearLayout) findViewById(R.id.bottom_banner_main_activity);
        if (bannerTypeShared.getString("bannerTypeShared", "").equals("admob")) {
            // Show Admob Ads in LinearLayout
            MobileAds.initialize(LeaderboardsActivity.this, admobAppIdShared.getString("admobAppIdShared", ""));
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
    public void onBackPressed() {
        super.onBackPressed();
        active = "false";
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        StartAppAd.disableSplash();
    }

    private void getFirstsPlayer() {
        String key = getResources().getString(R.string.api_secret_key);
        String secondsUrl = url+"/api/players/firsts/"+key;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, secondsUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            // First Player Data
                            JSONObject firstPlayer = jsonArray.getJSONObject(0);
                            String name1 = firstPlayer.getString("username");
                            String imageUrl1 = firstPlayer.getString("image_url");
                            int points = firstPlayer.getInt("total_score");
                            Picasso.get().load(imageUrl1).fit().centerInside().into(imageFirst);
                            Picasso.get().load(imageUrl1).fit().centerInside().into(image1);
                            username1.setText(name1);
                            points1.setText(String.valueOf(points) + " pts.");
                            String email_or_phone = firstPlayer.getString("email_or_phone");
                            String since = firstPlayer.getString("member_since");
                            Integer pointss = firstPlayer.getInt("actual_score");
                            Integer coins = firstPlayer.getInt("coins");
                            String earnings = firstPlayer.getString("earnings_actual_with_currency");
                            String facebook = firstPlayer.getString("facebook");
                            String twitter = firstPlayer.getString("twitter");
                            String instagram = firstPlayer.getString("instagram");
                            String referral = firstPlayer.getString("referral_code");
                            linear1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (email_or_phone.equals(userSituation.getString("userEmail", ""))) {
                                        if (MyProfileActivity.active.equals("false")) {
                                            Intent myprofile = new Intent(LeaderboardsActivity.this, MyProfileActivity.class);
                                            startActivity(myprofile);
                                        }
                                    } else {
                                        if (PlayerProfileActivity.active.equals("false")) {
                                            Intent player = new Intent(LeaderboardsActivity.this, PlayerProfileActivity.class);
                                            player.putExtra("username",name1);
                                            player.putExtra("since",since);
                                            player.putExtra("image",imageUrl1);
                                            player.putExtra("points",pointss);
                                            player.putExtra("coins",coins);
                                            player.putExtra("earnings",earnings);
                                            player.putExtra("facebook",facebook);
                                            player.putExtra("twitter",twitter);
                                            player.putExtra("instagram",instagram);
                                            player.putExtra("referrals",referral);
                                            startActivity(player);
                                        }
                                    }
                                }
                            });
                            // Second Player Data
                            JSONObject secondPlayer = jsonArray.getJSONObject(1);
                            String name2 = secondPlayer.getString("username");
                            String imageUrl2 = secondPlayer.getString("image_url");
                            int point2 = secondPlayer.getInt("total_score");
                            Picasso.get().load(imageUrl2).fit().centerInside().into(imageSecond);
                            Picasso.get().load(imageUrl2).fit().centerInside().into(image2);
                            username2.setText(name2);
                            points2.setText(String.valueOf(point2) + " pts.");
                            String email_or_phone2 = firstPlayer.getString("member_since");
                            String since2 = firstPlayer.getString("member_since");
                            Integer pointss2 = firstPlayer.getInt("actual_score");
                            Integer coins2 = firstPlayer.getInt("coins");
                            String earnings2 = firstPlayer.getString("earnings_actual_with_currency");
                            String facebook2 = firstPlayer.getString("facebook");
                            String twitter2 = firstPlayer.getString("twitter");
                            String instagram2 = firstPlayer.getString("instagram");
                            String referral2 = firstPlayer.getString("referral_code");
                            linear2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (email_or_phone2.equals(userSituation.getString("userEmail", ""))) {
                                        if (MyProfileActivity.active.equals("false")) {
                                            Intent myprofile = new Intent(LeaderboardsActivity.this, MyProfileActivity.class);
                                            startActivity(myprofile);
                                        }
                                    } else {
                                        if (PlayerProfileActivity.active.equals("false")) {
                                            Intent player = new Intent(LeaderboardsActivity.this, PlayerProfileActivity.class);
                                            player.putExtra("username",name2);
                                            player.putExtra("since",since2);
                                            player.putExtra("image",imageUrl2);
                                            player.putExtra("points",pointss2);
                                            player.putExtra("coins",coins2);
                                            player.putExtra("earnings",earnings2);
                                            player.putExtra("facebook",facebook2);
                                            player.putExtra("twitter",twitter2);
                                            player.putExtra("instagram",instagram2);
                                            player.putExtra("referrals",referral2);
                                            startActivity(player);
                                        }
                                    }
                                }
                            });
                            // Tirth Player Data
                            JSONObject tirthPlayer = jsonArray.getJSONObject(2);
                            String name3 = tirthPlayer.getString("username");
                            String imageUrl3 = tirthPlayer.getString("image_url");
                            int point3 = tirthPlayer.getInt("total_score");
                            Picasso.get().load(imageUrl3).fit().centerInside().into(imageTirth);
                            Picasso.get().load(imageUrl3).fit().centerInside().into(image3);
                            username3.setText(name3);
                            points3.setText(String.valueOf(point3) + " pts.");
                            String email_or_phone3 = firstPlayer.getString("member_since");
                            String since3 = firstPlayer.getString("member_since");
                            Integer pointss3 = firstPlayer.getInt("actual_score");
                            Integer coins3 = firstPlayer.getInt("coins");
                            String earnings3 = firstPlayer.getString("earnings_actual_with_currency");
                            String facebook3 = firstPlayer.getString("facebook");
                            String twitter3 = firstPlayer.getString("twitter");
                            String instagram3 = firstPlayer.getString("instagram");
                            String referral3 = firstPlayer.getString("referral_code");
                            linear3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (email_or_phone3.equals(userSituation.getString("userEmail", ""))) {
                                        if (MyProfileActivity.active.equals("false")) {
                                            Intent myprofile = new Intent(LeaderboardsActivity.this, MyProfileActivity.class);
                                            startActivity(myprofile);
                                        }
                                    } else {
                                        if (PlayerProfileActivity.active.equals("false")) {
                                            Intent player = new Intent(LeaderboardsActivity.this, PlayerProfileActivity.class);
                                            player.putExtra("username",name3);
                                            player.putExtra("since",since3);
                                            player.putExtra("image",imageUrl3);
                                            player.putExtra("points",pointss3);
                                            player.putExtra("coins",coins3);
                                            player.putExtra("earnings",earnings3);
                                            player.putExtra("facebook",facebook3);
                                            player.putExtra("twitter",twitter3);
                                            player.putExtra("instagram",instagram3);
                                            player.putExtra("referrals",referral3);
                                            startActivity(player);
                                        }
                                    }
                                }
                            });
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

    private void getPlayersFrom4To50() {
        String key = getResources().getString(R.string.api_secret_key);
        String allUrl = url+"/api/players/leaderboards/"+key;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, allUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject player = jsonArray.getJSONObject(i);
                                int id = player.getInt("id");
                                String idStr = String.valueOf(id);
                                String username = player.getString("username");
                                String email_or_phone = player.getString("email_or_phone");
                                String image_url = player.getString("image_url");
                                String referral_code = player.getString("referral_code");
                                String last_claim = player.getString("last_claim");
                                String login_method = player.getString("login_method");
                                String device_id = player.getString("device_id");
                                String facebook = player.getString("facebook");
                                String twitter = player.getString("twitter");
                                String instagram = player.getString("instagram");
                                String blocked = player.getString("blocked");
                                String member_since = player.getString("member_since");
                                int actual_score = player.getInt("actual_score");
                                int total_score = player.getInt("total_score");
                                int coins = player.getInt("coins");
                                int earnings_withdrawed = player.getInt("earnings_withdrawed");
                                int earnings_actual = player.getInt("earnings_actual");
                                int referrals = player.getInt("referrals");
                                playersArrayList.add(new Player(idStr,username, email_or_phone, image_url, referral_code, last_claim, login_method, device_id, facebook, twitter, instagram, blocked, member_since, actual_score, total_score, coins, earnings_withdrawed, earnings_actual, referrals));
                            }
                            playersAdapter.notifyDataSetChanged();
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
}