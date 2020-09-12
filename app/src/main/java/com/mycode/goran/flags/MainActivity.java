package com.mycode.goran.flags;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.google.android.gms.ads.*;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button btn;
    SeekBar seekBar;
    TextView txtMode;
    Button btnPlay,btnScore;
    DbHelper db;
    private FrameLayout adContainerView;
    private AdView adView;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btn = findViewById(R.id.btn_setting);
        final MediaPlayer start_tone = MediaPlayer.create(this, R.raw.right);

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });
        adContainerView = findViewById(R.id.ad_view_container);
        // Step 1 - Create an AdView and set the ad unit ID on it.
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.banner_test));
        adContainerView.addView(adView);
        loadBanner();
        /////////////////////////////////////////////////////////////////////////////////////////////////////


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.Interstatial_test));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu pm = new PopupMenu(MainActivity.this, btn);
                pm.getMenuInflater().inflate(R.menu.popupmenu, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {


                        switch (item.getItemId()) {

                            case R.id.Share:

                                Toast.makeText(MainActivity.this,R.string.Share_App, Toast.LENGTH_SHORT).show();

                                Intent share = new Intent(Intent.ACTION_SEND);
                                share.setType("text/plain");
                                share.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name) + "\n" + "https://play.google.com/store/apps/details?id=com.mycode.goran.flags");
                                if (share.resolveActivity(getPackageManager()) != null) {
                                    startActivity(Intent.createChooser(share, getString(R.string.Share_App)));
                                }
                                return true;


                            case R.id.MoreApps:



                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage(R.string.open_google_play);
                                builder.setCancelable(true);
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {

                                        Toast.makeText(MainActivity.this, "R.string.MoreApps", Toast.LENGTH_SHORT).show();
                                        Intent MoreApps = new Intent(Intent.ACTION_VIEW);
                                        MoreApps.setData(Uri.parse("https://play.google.com/store/apps/developer?id=GORAN+FARAJ"));
                                        if (MoreApps.resolveActivity(getPackageManager()) != null) {
                                            startActivity(MoreApps);

                                        }
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            return true;
                            case R.id.RateApp:

                                Toast.makeText(MainActivity.this, "R.string.RateApp", Toast.LENGTH_SHORT).show();

                                Intent RateApp = new Intent(Intent.ACTION_VIEW);
                                RateApp.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.mycode.goran.flags"));
                                if (RateApp.resolveActivity(getPackageManager()) != null) {
                                    startActivity(RateApp);
                                }
                                return true;
                        }

                        return true;
                    }
                });
                pm.show();
            }

        });

 seekBar = findViewById(R.id.seekBar);
        txtMode = findViewById(R.id.txtMode);
        btnPlay = findViewById(R.id.btnPlay);
        btnScore = findViewById(R.id.btnScore);


        db = new DbHelper(this);
        try{
            db.createDataBase();
        }
        catch (IOException e){
            e.printStackTrace();
        }




        //Event
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == 0)
                    txtMode.setText(style.MODE.Easy.toString());
                else if(progress == 1)
                    txtMode.setText(style.MODE.Medium.toString());
                else if(progress == 2)
                    txtMode.setText(style.MODE.Hard.toString());
                else if(progress == 3)
                    txtMode.setText(style.MODE.Hardest.toString());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_tone.start();
                Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                intent.putExtra("MODE",getPlayMode()); // Send Mode to GameActivity page
                startActivity(intent);
                finish();
            }
        });

        btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),Score.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private String getPlayMode() {
        if(seekBar.getProgress()==0)

            return style.MODE.Easy.toString();
        else if(seekBar.getProgress()==1)
            return style.MODE.Medium.toString();
        else if(seekBar.getProgress()==2)
            return style.MODE.Hard.toString();
        else
            return style.MODE.Hardest.toString();
    }
    private void loadBanner() {
        // Create an ad request. Check your logcat output for the hashed device ID
        // to get test ads on a physical device, e.g.,
        // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this
        // device."
        AdRequest adRequest =
                new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();

        AdSize adSize = getAdSize();
        // Step 4 - Set the adaptive ad size on the ad view.
        adView.setAdSize(adSize);

        // Step 5 - Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }
}
