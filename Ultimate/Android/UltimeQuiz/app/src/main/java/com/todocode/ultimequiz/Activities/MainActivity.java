package com.todocode.ultimequiz.Activities;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.todocode.ultimequiz.Adapters.BestPlayersAdapter;
import com.todocode.ultimequiz.Adapters.ContinueQuizzesAdapter;
import com.todocode.ultimequiz.Adapters.PopularCategoriesAdapter;
import com.todocode.ultimequiz.Adapters.RecentQuizAdapter;
import com.todocode.ultimequiz.Models.Category;
import com.todocode.ultimequiz.Models.ContinueQuiz;
import com.todocode.ultimequiz.Models.Player;
import com.todocode.ultimequiz.Models.Quiz;
import com.todocode.ultimequiz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private String url;
    private LinearLayout adViewFb;
    private List<ContinueQuiz> quizzesPagerList;
    private ContinueQuizzesAdapter quizzesMainViewPagerAdapter;
    private List<Category> categoriesArrayList;
    private PopularCategoriesAdapter categoriesAdapter;
    private ViewPager2 quizzesViewPager;
    private List<Object> recentquizzesArrayList;
    private RecentQuizAdapter recentQuizAdapter;
    public static String active = "false";
    public static final int NUMBER_OF_ADS = 2;
    private AdLoader adLoader;
    private final List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    private SharedPreferences admobAppIdShared, admobNativeShared;
    private RequestQueue queue;
    Timer timer1;
    private BestPlayersAdapter playersAdapter;
    private ArrayList<Player> playersArrayList;
    private TextView currentUserNameHeader, currentEmailHeader, mainUsrname, mainEarnings;
    private CircleImageView currentProfileImageHeader, mainProfileImage;
    GoogleSignInClient mGoogleSignInClient;
    private SharedPreferences userSituation, userSituationId;
    FirebaseAuth mAuth;
    private SharedPreferences dailyRewardSharedPrefs, videoAdCoinsShared, idShared, usernameShared, emailShared, imageurlShared,
    referralcodeShared, coinsShared, loginmethodShared, facebookShared, twitterShared, instagramShared,
    earningswithdrawedShared, earningsactualShared, earningsActualWithCurrencyShared, membersinceShared, currencyShared;
    private LinearLayout dailyQuizLinear;
    private TextView dailyNumberOfQuestions;
    private String dailyQuizId;
    private Integer text_questions, images_questions, audio_questions;
    private Integer started_text_questions, started_images_questions, started_audio_questions;
    private TextView allPlayers, allCategories;
    // AdColony Interstitial
    public AdColonyInterstitial adColonyInterstitiall;
    public AdColonyAdOptions adColonyAdOptions;
    // AdColony Banner
    public AdColonyAdView adView;
    private LinearLayout linearBannerAdContainer;
    public AdColonyAdOptions adColonyAdOptionsBanner;
    public static String[] AD_UNIT_ZONE_Ids;
    Banner startAppBanner;
    private SharedPreferences admobBannerShared, bannerTypeShared, adcolonyAppIdShared, adcolonyBannerShared, adcolonyInterstitialShared, adcolonyRewardShared, facebookBannerShared, admobInterstitialShared, facebookInterstitialShared, interstitialTypeShared;
    private com.facebook.ads.InterstitialAd facebookInterstitialAd;
    private InterstitialAd mInterstitialAd;
    private NativeAdLayout nativeAdLayout;
    private NativeBannerAd nativeBannerAd;
    private SharedPreferences facebookNativeShared, langShared, lastclaimShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        langShared = getSharedPreferences("langShared", MODE_PRIVATE);
        Locale locale = new Locale(langShared.getString("langShared", ""));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        StartAppAd.disableSplash();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        dailyRewardSharedPrefs = getSharedPreferences("dailyRewardSharedPrefs", MODE_PRIVATE);
        facebookNativeShared = getSharedPreferences("facebookNativeShared", MODE_PRIVATE);
        facebookInterstitialShared = getSharedPreferences("facebookInterstitialShared", MODE_PRIVATE);
        admobInterstitialShared = getSharedPreferences("admobInterstitialShared", MODE_PRIVATE);
        interstitialTypeShared = getSharedPreferences("interstitialTypeShared", MODE_PRIVATE);
        if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("facebook")) {
            prepareInterstitialFacebookAd();
        } else if(interstitialTypeShared.getString("interstitialTypeShared", "").equals("admob")) {
            prepareInterstitialAdmobAd();
        }
        currencyShared = getSharedPreferences("currencyShared", MODE_PRIVATE);
        idShared = getSharedPreferences("idShared", MODE_PRIVATE);
        usernameShared = getSharedPreferences("usernameShared", MODE_PRIVATE);
        emailShared = getSharedPreferences("emailShared", MODE_PRIVATE);
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
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // get menu from navigationView
        Menu menu = navigationView.getMenu();
        // find MenuItem you want to change
        langShared = getSharedPreferences("langShared", MODE_PRIVATE);
        Locale localee = new Locale(langShared.getString("langShared", ""));
        Locale.setDefault(locale);
        Configuration configurationn = new Configuration();
        configurationn.locale = localee;
        getBaseContext().getResources().updateConfiguration(configurationn, getBaseContext().getResources().getDisplayMetrics());
        MenuItem home = menu.findItem(R.id.home);
        home.setTitle(getResources().getString(R.string.drawer_menu_home));
        MenuItem profile = menu.findItem(R.id.profile);
        profile.setTitle(getResources().getString(R.string.drawer_menu_profile));
        MenuItem categories = menu.findItem(R.id.categories);
        categories.setTitle(getResources().getString(R.string.drawer_menu_categories));
        MenuItem leader = menu.findItem(R.id.leaderboards);
        leader.setTitle(getResources().getString(R.string.drawer_menu_leaderboards));
        MenuItem stat = menu.findItem(R.id.statistics);
        stat.setTitle(getResources().getString(R.string.drawer_menu_statistics));
        MenuItem invite = menu.findItem(R.id.invite_friends);
        invite.setTitle(getResources().getString(R.string.drawer_menu_invite));
        MenuItem instru = menu.findItem(R.id.instructions);
        instru.setTitle(getResources().getString(R.string.drawer_menu_instructions));
        MenuItem privacy = menu.findItem(R.id.privacy_policy);
        privacy.setTitle(getResources().getString(R.string.drawer_menu_privacy));
        MenuItem temrs = menu.findItem(R.id.terms_of_use);
        temrs.setTitle(getResources().getString(R.string.drawer_menu_terms));
        MenuItem rate = menu.findItem(R.id.rate);
        rate.setTitle(getResources().getString(R.string.drawer_menu_rate));
        MenuItem share = menu.findItem(R.id.share);
        share.setTitle(getResources().getString(R.string.drawer_menu_share));
        MenuItem report = menu.findItem(R.id.report);
        report.setTitle(getResources().getString(R.string.drawer_menu_report));
        MenuItem contact = menu.findItem(R.id.contact_us);
        contact.setTitle(getResources().getString(R.string.drawer_menu_contact));
        MenuItem exit = menu.findItem(R.id.exit);
        exit.setTitle(getResources().getString(R.string.drawer_menu_exit));
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        url = getResources().getString(R.string.domain_name);
        queue = Volley.newRequestQueue(this);
        admobAppIdShared = getSharedPreferences("admobAppIdShared", MODE_PRIVATE);
        admobNativeShared = getSharedPreferences("admobNativeShared", MODE_PRIVATE);
        admobBannerShared = getSharedPreferences("admobBannerShared", MODE_PRIVATE);
        facebookBannerShared = getSharedPreferences("facebookBannerShared", MODE_PRIVATE);
        adcolonyAppIdShared = getSharedPreferences("adcolonyAppIdShared", MODE_PRIVATE);
        adcolonyBannerShared = getSharedPreferences("adcolonyBannerShared", MODE_PRIVATE);
        adcolonyInterstitialShared = getSharedPreferences("adcolonyInterstitialShared", MODE_PRIVATE);
        adcolonyRewardShared = getSharedPreferences("adcolonyRewardShared", MODE_PRIVATE);
        AD_UNIT_ZONE_Ids = new String[] {adcolonyBannerShared.getString("adcolonyBannerShared", ""), adcolonyInterstitialShared.getString("adcolonyInterstitialShared", ""), adcolonyRewardShared.getString("adcolonyRewardShared", "")};
        // Configure AdColony Ads
        AdColony.configure(MainActivity.this, adcolonyAppIdShared.getString("adcolonyAppIdShared", ""), AD_UNIT_ZONE_Ids);
        // Bottom Banner Ads
        bannerTypeShared = getSharedPreferences("bannerTypeShared", MODE_PRIVATE);
        linearBannerAdContainer = (LinearLayout) findViewById(R.id.bottom_banner_main_activity);
        if (bannerTypeShared.getString("bannerTypeShared", "").equals("admob")) {
            // Show Admob Ads in LinearLayout
            MobileAds.initialize(MainActivity.this, admobAppIdShared.getString("admobAppIdShared", ""));
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
            AdSettings.addTestDevice("567936cd-17d4-4243-b2b6-3342dca11966");
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
        // Ger Connectd User Info
        View header = navigationView.getHeaderView(0);
        Button logout = (Button) header.findViewById(R.id.logout);
        currentUserNameHeader = (TextView) header.findViewById(R.id.current_user_name);
        currentEmailHeader = (TextView) header.findViewById(R.id.current_user_email);
        currentProfileImageHeader = (CircleImageView) header.findViewById(R.id.profile_image_header);
        mainUsrname = (TextView) findViewById(R.id.main_username);
        mainEarnings = (TextView) findViewById(R.id.main_earnings);
        mainProfileImage = (CircleImageView) findViewById(R.id.main_profile_image);
        mAuth=FirebaseAuth.getInstance();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change Shared Preferences
                userSituation.edit().putString("userEmail", "").apply();
                userSituationId.edit().putString("userId", "").apply();
                // Logout Google
                if (mGoogleSignInClient != null) {
                    mGoogleSignInClient.signOut();
                }
                // Logout Facebook
                if (LoginManager.getInstance() != null) {
                    LoginManager.getInstance().logOut();
                }
                if (mAuth != null) {
                    FirebaseAuth.getInstance().signOut();
                }
                Intent registerPage = new Intent(MainActivity.this, LoginActivity.class);
                registerPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(registerPage);
                finish();
            }
        });
        // Set Header User Infos
        currentUserNameHeader.setText(usernameShared.getString("usernameShared", ""));
        currentEmailHeader.setText(emailShared.getString("emailShared", ""));
        mainUsrname.setText(usernameShared.getString("usernameShared", ""));
        mainEarnings.setText(earningsActualWithCurrencyShared.getString("earningsActualWithCurrencyShared", ""));
        if(!imageurlShared.getString("imageurlShared","").isEmpty()) {
            Picasso.get().load(imageurlShared.getString("imageurlShared", "")).fit().centerInside().into(currentProfileImageHeader);
            Picasso.get().load(imageurlShared.getString("imageurlShared", "")).fit().centerInside().into(mainProfileImage);
        } else {
            Picasso.get().load(getResources().getString(R.string.domain_name)+"/img/player.png").fit().centerInside().into(currentProfileImageHeader);
            Picasso.get().load(getResources().getString(R.string.domain_name)+"/img/player.png").fit().centerInside().into(mainProfileImage);
        }
        // Continue Quizzes View Pager
        quizzesViewPager = findViewById(R.id.continue_quiz_viewpager);
        quizzesPagerList = new ArrayList<>();
        quizzesMainViewPagerAdapter = new ContinueQuizzesAdapter(quizzesPagerList);
        quizzesViewPager.setAdapter(quizzesMainViewPagerAdapter);
        getContinueQuizzes();
        quizzesMainViewPagerAdapter.setOnItemClickListener(new ContinueQuizzesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (QuizQuestionsTypeActivity.active.equals("false")) {
                    Intent quiz = new Intent(MainActivity.this, QuizQuestionsTypeActivity.class);
                    quiz.putExtra("quiz_id",quizzesPagerList.get(position).getQuizId());
                    quiz.putExtra("text",String.valueOf(quizzesPagerList.get(position).getTextQuestions()));
                    quiz.putExtra("image", String.valueOf(quizzesPagerList.get(position).getImageQuestions()));
                    quiz.putExtra("audio", String.valueOf(quizzesPagerList.get(position).getAudioQuestions()));
                    startActivity(quiz);
                }
            }
        });
        TimerTask timerTask1 = new TimerTask() {
            @Override
            public void run() {
                quizzesViewPager.post(new Runnable(){
                    @Override
                    public void run() {
                        if (quizzesPagerList.size()!=0) {
                            quizzesViewPager.setCurrentItem((quizzesViewPager.getCurrentItem()+1)%quizzesPagerList.size());
                        }
                    }
                });
            }
        };
        timer1 = new Timer();
        timer1.schedule(timerTask1, 3000, 3000);
        // Get Top Players
        RecyclerView playersRecyclerView = (RecyclerView) findViewById(R.id.best_player_recycler);
        playersArrayList = new ArrayList<>();
        playersAdapter = new BestPlayersAdapter(this, playersArrayList);
        playersRecyclerView.setAdapter(playersAdapter);
        playersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        getBestPlayers();
        playersAdapter.setOnItemClickListener(new BestPlayersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (playersArrayList.get(position).getEmail_or_phone().equals(userSituation.getString("userEmail", ""))) {
                    if (MyProfileActivity.active.equals("false")) {
                        Intent myprofile = new Intent(MainActivity.this, MyProfileActivity.class);
                        startActivity(myprofile);
                    }
                } else {
                    if (PlayerProfileActivity.active.equals("false")) {
                        Intent player = new Intent(MainActivity.this, PlayerProfileActivity.class);
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
        // Get Featured Categories
        RecyclerView categoriesRecyclerView = (RecyclerView) findViewById(R.id.popular_categories_recycler);
        categoriesArrayList = new ArrayList<>();
        categoriesAdapter = new PopularCategoriesAdapter(categoriesArrayList);
        categoriesRecyclerView.setAdapter(categoriesAdapter);
        categoriesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        getTrendingCategories();
        categoriesAdapter.setOnItemClickListener(new PopularCategoriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (SingleCategoryActivity.active.equals("false")) {
                    Intent singleCategory = new Intent(MainActivity.this, SingleCategoryActivity.class);
                    singleCategory.putExtra("category_name", categoriesArrayList.get(position).getName());
                    singleCategory.putExtra("category_id", categoriesArrayList.get(position).getId());
                    startActivity(singleCategory);
                }
            }
        });
        // Get Recent Quizzes
        RecyclerView quizzesRecyclerView = (RecyclerView) findViewById(R.id.latest_quiz_recycler);
        recentquizzesArrayList = new ArrayList<>();
        recentQuizAdapter = new RecentQuizAdapter(this, recentquizzesArrayList);
        quizzesRecyclerView.setAdapter(recentQuizAdapter);
        quizzesRecyclerView.setHasFixedSize(true);
        quizzesRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        MobileAds.initialize(MainActivity.this, admobAppIdShared.getString("admobAppIdShared", ""));
        getLatestQuizzes();
        loadAdmobNativeAds();
        // DailyQuiz Linear
        dailyQuizLinear = (LinearLayout) findViewById(R.id.daily_quiz_linear);
        dailyNumberOfQuestions = (TextView) findViewById(R.id.daily_quiz_number_of_questions);
        getDailyQuiz();
        dailyQuizLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (QuizQuestionsTypeActivity.active.equals("false")) {
                    // Show Interstitial Ads
                    if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("admob")) {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                            mInterstitialAd.setAdListener(new AdListener() {
                                @Override
                                public void onAdLoaded() {
                                    // Code to be executed when an ad finishes loading.
                                    mInterstitialAd.show();
                                }

                                @Override
                                public void onAdFailedToLoad(int i) {
                                    Intent daily = new Intent(MainActivity.this, QuizQuestionsTypeActivity.class);
                                    daily.putExtra("quiz_id", dailyQuizId);
                                    daily.putExtra("text", String.valueOf(text_questions));
                                    daily.putExtra("image", String.valueOf(images_questions));
                                    daily.putExtra("audio", String.valueOf(audio_questions));
                                    startActivity(daily);
                                }

                                @Override
                                public void onAdOpened() {
                                    // Code to be executed when the ad is displayed.
                                }

                                @Override
                                public void onAdClicked() {
                                    // Code to be executed when the user clicks on an ad.
                                }

                                @Override
                                public void onAdLeftApplication() {
                                    // Code to be executed when the user has left the app.
                                }

                                @Override
                                public void onAdClosed() {
                                    Intent daily = new Intent(MainActivity.this, QuizQuestionsTypeActivity.class);
                                    daily.putExtra("quiz_id", dailyQuizId);
                                    daily.putExtra("text", String.valueOf(text_questions));
                                    daily.putExtra("image", String.valueOf(images_questions));
                                    daily.putExtra("audio", String.valueOf(audio_questions));
                                    startActivity(daily);
                                }
                            });
                            prepareInterstitialAdmobAd();
                        } else {
                            Intent daily = new Intent(MainActivity.this, QuizQuestionsTypeActivity.class);
                            daily.putExtra("quiz_id", dailyQuizId);
                            daily.putExtra("text", String.valueOf(text_questions));
                            daily.putExtra("image", String.valueOf(images_questions));
                            daily.putExtra("audio", String.valueOf(audio_questions));
                            startActivity(daily);
                        }
                    } else if(interstitialTypeShared.getString("interstitialTypeShared", "").equals("facebook")) {
                        if (facebookInterstitialAd.isAdLoaded()) {
                            facebookInterstitialAd.show();
                            InterstitialAdListener listener = new InterstitialAdListener() {
                                @Override
                                public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                                }

                                @Override
                                public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                    Intent daily = new Intent(MainActivity.this, QuizQuestionsTypeActivity.class);
                                    daily.putExtra("quiz_id", dailyQuizId);
                                    daily.putExtra("text", String.valueOf(text_questions));
                                    daily.putExtra("image", String.valueOf(images_questions));
                                    daily.putExtra("audio", String.valueOf(audio_questions));
                                    startActivity(daily);
                                }

                                @Override
                                public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                    Intent daily = new Intent(MainActivity.this, QuizQuestionsTypeActivity.class);
                                    daily.putExtra("quiz_id", dailyQuizId);
                                    daily.putExtra("text", String.valueOf(text_questions));
                                    daily.putExtra("image", String.valueOf(images_questions));
                                    daily.putExtra("audio", String.valueOf(audio_questions));
                                    startActivity(daily);
                                }

                                @Override
                                public void onAdLoaded(com.facebook.ads.Ad ad) {
                                    facebookInterstitialAd.show();
                                }

                                @Override
                                public void onAdClicked(com.facebook.ads.Ad ad) {

                                }

                                @Override
                                public void onLoggingImpression(com.facebook.ads.Ad ad) {

                                }
                            };
                            facebookInterstitialAd.loadAd(facebookInterstitialAd.buildLoadAdConfig().withAdListener(listener).build());
                            prepareInterstitialFacebookAd();
                        } else {
                            Intent daily = new Intent(MainActivity.this, QuizQuestionsTypeActivity.class);
                            daily.putExtra("quiz_id", dailyQuizId);
                            daily.putExtra("text", String.valueOf(text_questions));
                            daily.putExtra("image", String.valueOf(images_questions));
                            daily.putExtra("audio", String.valueOf(audio_questions));
                            startActivity(daily);
                        }
                    } else if(interstitialTypeShared.getString("interstitialTypeShared", "").equals("adcolony")) {
                        AdColonyInterstitialListener listener = new AdColonyInterstitialListener() {
                            @Override
                            public void onRequestFilled(AdColonyInterstitial adColonyInterstitial) {
                                adColonyInterstitiall = adColonyInterstitial;
                                adColonyInterstitiall.show();
                            }

                            @Override
                            public void onRequestNotFilled(AdColonyZone zone) {
                                Intent daily = new Intent(MainActivity.this, QuizQuestionsTypeActivity.class);
                                daily.putExtra("quiz_id", dailyQuizId);
                                daily.putExtra("text", String.valueOf(text_questions));
                                daily.putExtra("image", String.valueOf(images_questions));
                                daily.putExtra("audio", String.valueOf(audio_questions));
                                startActivity(daily);
                            }

                            @Override
                            public void onClosed(AdColonyInterstitial ad) {
                                Intent daily = new Intent(MainActivity.this, QuizQuestionsTypeActivity.class);
                                daily.putExtra("quiz_id", dailyQuizId);
                                daily.putExtra("text", String.valueOf(text_questions));
                                daily.putExtra("image", String.valueOf(images_questions));
                                daily.putExtra("audio", String.valueOf(audio_questions));
                                startActivity(daily);
                            }

                            @Override
                            public void onExpiring(AdColonyInterstitial ad) {
                                Intent daily = new Intent(MainActivity.this, QuizQuestionsTypeActivity.class);
                                daily.putExtra("quiz_id", dailyQuizId);
                                daily.putExtra("text", String.valueOf(text_questions));
                                daily.putExtra("image", String.valueOf(images_questions));
                                daily.putExtra("audio", String.valueOf(audio_questions));
                                startActivity(daily);
                            }
                        };
                        AdColony.requestInterstitial(adcolonyInterstitialShared.getString("adcolonyInterstitialShared", ""), listener, adColonyAdOptions);
                    }
                    else if(interstitialTypeShared.getString("interstitialTypeShared", "").equals("startapp")) {
                        Intent daily = new Intent(MainActivity.this, QuizQuestionsTypeActivity.class);
                        daily.putExtra("quiz_id", dailyQuizId);
                        daily.putExtra("text", String.valueOf(text_questions));
                        daily.putExtra("image", String.valueOf(images_questions));
                        daily.putExtra("audio", String.valueOf(audio_questions));
                        startActivity(daily);
                        StartAppAd.showAd(MainActivity.this);
                    }
                }
            }
        });
        mainProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyProfileActivity.active.equals("false")) {
                    Intent profile = new Intent(MainActivity.this, MyProfileActivity.class);
                    startActivity(profile);
                }
            }
        });
        allPlayers = (TextView) findViewById(R.id.all_players);
        allPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LeaderboardsActivity.active.equals("false")) {
                Intent leader = new Intent(MainActivity.this, LeaderboardsActivity.class);
                startActivity(leader);
            }
            }
        });
        allCategories = (TextView) findViewById(R.id.all_categories);
        allCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AllCategoriesActivity.active.equals("false")) {
                    Intent categories = new Intent(MainActivity.this, AllCategoriesActivity.class);
                    startActivity(categories);
                }
            }
        });

        nativeAdLayout = findViewById(R.id.question_native_banner_ad_container);
        // Facebook Ads
        AudienceNetworkAds.initialize(this);
        AdSettings.addTestDevice("567936cd-17d4-4243-b2b6-3342dca11966");
        loadNativeAd();
        // Reward Daily
        wachDazt24hOlaLa();
    }

    private void inflateAd(NativeBannerAd nativeBannerAd) {
        // Unregister last ad
        nativeBannerAd.unregisterView();

        // Add the Ad view into the ad container.
        // nativeAdLayout = getView().findViewById(R.id.statistics_native_banner_ad_container);
        LayoutInflater inflater = LayoutInflater.from(this);
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        adViewFb = (LinearLayout) inflater.inflate(R.layout.facebook_native_banner_layout, nativeAdLayout, false);
        nativeAdLayout.addView(adViewFb);

        // Add the AdChoices icon
        RelativeLayout adChoicesContainer = adViewFb.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(this, nativeBannerAd, nativeAdLayout);
        adOptionsView.setIconSizeDp(23);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        TextView nativeAdTitle = adViewFb.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = adViewFb.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = adViewFb.findViewById(R.id.native_ad_sponsored_label);
        MediaView nativeAdIconView = adViewFb.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction = adViewFb.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(adViewFb, nativeAdIconView, clickableViews);
    }

    private void loadNativeAd() {
        // Instantiate a NativeAd object.
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
        nativeBannerAd = new NativeBannerAd(this, facebookNativeShared.getString("facebookNativeShared", ""));
        NativeAdListener listener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Race condition, load() called again before last ad was displayed
                if (nativeBannerAd == null || nativeBannerAd != ad) {
                    return;
                }
                // Inflate Native Banner Ad into Container
                inflateAd(nativeBannerAd);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };
        nativeBannerAd.loadAd(nativeBannerAd.buildLoadAdConfig().withAdListener(listener).build());
    }

    private void prepareInterstitialAdmobAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(admobInterstitialShared.getString("admobInterstitialShared", ""));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void prepareInterstitialFacebookAd() {
        AudienceNetworkAds.initialize(this);
        AdSettings.addTestDevice("567936cd-17d4-4243-b2b6-3342dca11966");
        facebookInterstitialAd = new com.facebook.ads.InterstitialAd(this, facebookInterstitialShared.getString("facebookInterstitialShared", ""));
        facebookInterstitialAd.loadAd();
    }

    private void getDailyQuiz() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/quiz/daily/get", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    text_questions = jsonObject.getInt("text_questions");
                    images_questions = jsonObject.getInt("image_questions");
                    Integer quiz_id = jsonObject.getInt("quiz_id");
                    audio_questions = jsonObject.getInt("audio_questions");
                    Integer numberOfQuestions = text_questions + images_questions + audio_questions;
                    dailyNumberOfQuestions.setText(String.valueOf(numberOfQuestions) + " " +getResources().getString(R.string.mixed_questions));
                    dailyQuizId = String.valueOf(quiz_id);
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

    private void getTrendingCategories() {
        String key = getResources().getString(R.string.api_secret_key);
        String urlApi = url+"/api/categories/popular/"+key;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Integer id = jsonObject.getInt("id");
                                String name = jsonObject.getString("category_name");
                                Integer number_of_subcategories = jsonObject.getInt("number_of_subcategories");
                                Integer number_of_quizzes = jsonObject.getInt("number_of_quizzes");
                                String img = url+"/uploads/categories/"+jsonObject.getString("category_img");
                                categoriesArrayList.add(new Category(String.valueOf(id), name, img, String.valueOf(number_of_subcategories), String.valueOf(number_of_quizzes)));
                            }
                            categoriesAdapter.notifyDataSetChanged();
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
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void getContinueQuizzes() {
        String key = getResources().getString(R.string.api_secret_key);
        String urlApi = url+"/api/"+ userSituationId.getString("userId", "") +"/quizzes/continue/"+key;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Integer text = jsonObject.getInt("text_questions");
                                Integer image = jsonObject.getInt("image_questions");
                                Integer audio = jsonObject.getInt("audio_questions");
                                Integer id = jsonObject.getInt("id");
                                Integer quizId = jsonObject.getInt("quiz_id");
                                String quizName = jsonObject.getString("quiz_name");
                                String quizImageUrl = url+"/uploads/quizzes/"+jsonObject.getString("quiz_image_url");
                                Integer subcategoryId = jsonObject.getInt("subcategory_id");
                                String subcategoryName = jsonObject.getString("subcategory_name");
                                Integer categoryId = jsonObject.getInt("category_id");
                                String categoryName = jsonObject.getString("category_name");
                                Integer playerId = jsonObject.getInt("player_id");
                                Integer quiz_number_of_questions = jsonObject.getInt("quiz_number_of_questions");
                                Integer  quiz_number_of_remaining_questions = jsonObject.getInt("quiz_number_of_remaining_questions");
                                quizzesPagerList.add(new ContinueQuiz(String.valueOf(id),String.valueOf(quizId), quizName, quizImageUrl, String.valueOf(subcategoryId), subcategoryName, String.valueOf(categoryId), categoryName, String.valueOf(playerId), quiz_number_of_questions, quiz_number_of_remaining_questions, text, image, audio));
                            }
                            quizzesMainViewPagerAdapter.notifyDataSetChanged();
                            if (quizzesPagerList.size()==0) {
                                LinearLayout continue_quiz_linear = (LinearLayout) findViewById(R.id.continue_quiz_linear);
                                continue_quiz_linear.setVisibility(View.GONE);
                                quizzesViewPager.setVisibility(View.GONE);
                            } else {
                                LinearLayout continue_quiz_linear = (LinearLayout) findViewById(R.id.continue_quiz_linear);
                                continue_quiz_linear.setVisibility(View.VISIBLE);
                                quizzesViewPager.setVisibility(View.VISIBLE);
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
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void getBestPlayers() {
        String key = getResources().getString(R.string.api_secret_key);
        String urlApi = url+"/api/players/best/15/"+key;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlApi, null,
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
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void getLatestQuizzes() {
        String key = getResources().getString(R.string.api_secret_key);
        String urlApi = url+"/api/quizzes/recent/"+key;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Integer id = jsonObject.getInt("id");
                                String quizName = jsonObject.getString("quiz_name");
                                String img = url+"/uploads/quizzes/"+jsonObject.getString("quiz_image_url");
                                Integer subcategoryId = jsonObject.getInt("subcategory_id");
                                String subcategoryName = jsonObject.getString("subcategory_name");
                                Integer categoryId = jsonObject.getInt("category_id");
                                String categoryName = jsonObject.getString("category_name");
                                Integer quizNumberOfQuestions = jsonObject.getInt("quiz_number_of_questions");
                                Integer textquizN = jsonObject.getInt("quiz_number_of_text_questions");
                                Integer imagequizN = jsonObject.getInt("quiz_number_of_image_questions");
                                Integer audioQuizN = jsonObject.getInt("quiz_number_of_audio_questions");
                                recentquizzesArrayList.add(new Quiz(String.valueOf(id), quizName, img, String.valueOf(subcategoryId), subcategoryName, String.valueOf(categoryId), categoryName, quizNumberOfQuestions, textquizN, imagequizN, audioQuizN));
                            }
                            recentQuizAdapter.notifyDataSetChanged();
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
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void insertAdsInMenuItems() {
        if (recentquizzesArrayList.size() <= 3) {
            return;
        }
        if (recentquizzesArrayList.size() > 3) {
            int offset = (recentquizzesArrayList.size() / mNativeAds.size())+1;
            int index = 2;
            for (UnifiedNativeAd ad : mNativeAds) {
                recentquizzesArrayList.add(index, ad);
                index = index + offset;
            }
            recentQuizAdapter.notifyDataSetChanged();
        }
    }

    private void loadAdmobNativeAds() {
        AdLoader.Builder builder = new AdLoader.Builder(this, admobNativeShared.getString("admobNativeShared", ""));

        adLoader = builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        mNativeAds.add(unifiedNativeAd);
                        if (!adLoader.isLoading()) {
                            insertAdsInMenuItems();
                        }
                    }
                }).withAdListener(
                new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        if (!adLoader.isLoading()) {
                            insertAdsInMenuItems();
                        }
                    }
                }).build();
        // Load the Native ads.
        adLoader.loadAds(new AdRequest.Builder().build(), NUMBER_OF_ADS);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home :
                break;
            case R.id.profile :
                if (MyProfileActivity.active.equals("false")) {
                    Intent profile = new Intent(MainActivity.this, MyProfileActivity.class);
                    startActivity(profile);
                    break;
                }
            case R.id.categories :
                if (AllCategoriesActivity.active.equals("false")) {
                Intent categories = new Intent(MainActivity.this, AllCategoriesActivity.class);
                startActivity(categories);
                break;
                }
            case R.id.leaderboards :
                if (LeaderboardsActivity.active.equals("false")) {
                    Intent leaderboards = new Intent(MainActivity.this, LeaderboardsActivity.class);
                    startActivity(leaderboards);
                    break;
                }
            case R.id.statistics :
                if (MyStatisticsActivity.active.equals("false")) {
                    Intent statistics = new Intent(MainActivity.this, MyStatisticsActivity.class);
                    startActivity(statistics);
                    break;
                }
            case R.id.invite_friends :
                if (InviteFriendsActivity.active.equals("false")) {
                    Intent invite_friends = new Intent(MainActivity.this, InviteFriendsActivity.class);
                    startActivity(invite_friends);
                    break;
                }
            case R.id.instructions :
                if (InstructionsActivity.active.equals("false")) {
                    Intent instructions = new Intent(MainActivity.this, InstructionsActivity.class);
                    startActivity(instructions);
                    break;
                }
            case R.id.privacy_policy :
                if (PrivacyPolicyActivity.active.equals("false")) {
                    Intent privacy_policy = new Intent(MainActivity.this, PrivacyPolicyActivity.class);
                    startActivity(privacy_policy);
                    break;
                }
            case R.id.terms_of_use :
                if (TermsOfUseActivity.active.equals("false")) {
                    Intent terms_of_use = new Intent(MainActivity.this, TermsOfUseActivity.class);
                    startActivity(terms_of_use);
                    break;
                }
            case R.id.report :
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.menu_report_a_bug));
                builder.setMessage(getResources().getString(R.string.menu_report_a_bug_message));
                builder.setPositiveButton("Send Email",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent emailSelectorIntent = new Intent(Intent.ACTION_SENDTO);
                                emailSelectorIntent.setData(Uri.parse("mailto:"));
                                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email)});
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.menu_report_a_bug));
                                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                emailIntent.setSelector( emailSelectorIntent );
                                if( emailIntent.resolveActivity(getPackageManager()) != null )
                                    startActivity(emailIntent);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.share :
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                intent.putExtra(Intent.EXTRA_TEXT, "Download this APP From : http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                startActivity(Intent.createChooser(intent, "Share Now"));
                break;
            case R.id.contact_us :
                Intent emailSelectorIntent = new Intent(Intent.ACTION_SENDTO);
                emailSelectorIntent.setData(Uri.parse("mailto:"));
                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email)});
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                emailIntent.setSelector( emailSelectorIntent );
                if( emailIntent.resolveActivity(getPackageManager()) != null )
                    startActivity(emailIntent);
                break;
            case R.id.rate :
                try{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));
                }
                catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                }
                break;
            case R.id.exit :
                finishAndRemoveTask();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(getResources().getDrawable(R.drawable.ic_exit))
                    .setTitle(getResources().getString(R.string.drawer_menu_exit))
                    .setMessage(getResources().getString(R.string.really_exit))
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Stop the activity
                            finishAndRemoveTask();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options_rate:
                try{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));
                }
                catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                }
                return true;
            case R.id.options_profile:
                if (MyProfileActivity.active.equals("false")) {
                Intent profile = new Intent(MainActivity.this, MyProfileActivity.class);
                startActivity(profile);
                }
                return true;
            case R.id.options_statistics:
                if (MyStatisticsActivity.active.equals("false")) {
                Intent stat = new Intent(MainActivity.this, MyStatisticsActivity.class);
                startActivity(stat);
                }
                return true;
        }
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        StartAppAd.disableSplash();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StartAppAd.disableSplash();
    }

    @Override
    protected void onStop() {
        super.onStop();
        StartAppAd.disableSplash();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void showDailyRewardDialog(int daily_reward) throws ParseException {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.AlertDialogCustom));
        builder.setView(R.layout.claim_daily_reward_dialog);
        builder.setPositiveButton(getString(R.string.claim)+ " " +String.valueOf(dailyRewardSharedPrefs.getInt("dailyRewardSharedPrefs",0)) + " coins", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addDailyRewardCoins();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        if(positiveButton != null) {
            LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
            positiveButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
            positiveButton.setTextSize(16);
            positiveButton.setLayoutParams(positiveButtonLL);
            positiveButton.setBackgroundColor(getResources().getColor(R.color.true_green));
        }
    }

    public void showRewardedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        builder.setView(R.layout.reward_collected_layout);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        // Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        };
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });
        handler.postDelayed(runnable, 1500);
    }

    private void addDailyRewardCoins() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/players/coins/daily", new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    coinsShared.edit().putString("coinsShared", success).apply();
                    Log.e("coins", success);
                    Toast.makeText(MainActivity.this, "You have earned "+success+ " additional coins!", Toast.LENGTH_LONG).show();
                    showRewardedDialog();
                } catch(JSONException e) {
                    e.getStackTrace();
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
                params.put("key", getResources().getString(R.string.api_secret_key));
                params.put("email", userSituation.getString("userEmail", ""));
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

    public void wachDazt24hOlaLa(){
        String userIdStr = userSituationId.getString("userId","");
        final int dailyReward = dailyRewardSharedPrefs.getInt("dailyRewardSharedPrefs", 0);
        Log.e("dailyMain", String.valueOf(dailyReward));
        final String userToCheck = userIdStr;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/players/reward/last/claim/check", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    // No Errors
                    if (success.equals("dazt")){
                        showDailyRewardDialog(dailyReward);
                    }
                } catch(JSONException | ParseException e){
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
                params.put("key", getResources().getString(R.string.api_secret_key));
                params.put("user_id", userToCheck);
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