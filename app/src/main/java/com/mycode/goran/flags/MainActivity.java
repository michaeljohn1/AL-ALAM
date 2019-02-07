package com.mycode.goran.flags;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.google.android.gms.ads.*;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button btn;
    private AdView mAdView;
    SeekBar seekBar;
    TextView txtMode;
    Button btnPlay,btnScore;
    DbHelper db;

    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btn = findViewById(R.id.btn_setting);
        final MediaPlayer start_tone = MediaPlayer.create(this, R.raw.right);

//        MobileAds.initialize(this, getString(R.string.APP_ID));

        MobileAds.initialize(this,
                "ca-app-pub-3940256099942544~3347511713");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
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

                                Toast.makeText(MainActivity.this, "R.string.MoreApps", Toast.LENGTH_SHORT).show();

                            {
                                Intent MoreApps = new Intent(Intent.ACTION_VIEW);
                                MoreApps.setData(Uri.parse("https://play.google.com/store/apps/developer?id=GORAN+FARAJ"));
                                if (MoreApps.resolveActivity(getPackageManager()) != null) {
                                    startActivity(MoreApps);

                                }
                            }


                            return true;
                            case R.id.RateApp:

                                Toast.makeText(MainActivity.this, "R.string.RateApp", Toast.LENGTH_SHORT).show();

                                Intent RateApp = new Intent(Intent.ACTION_VIEW);
                                RateApp.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.mycode.goran.alam"));
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


//        MobileAds.initialize(this,getString(R.string.banner_test));
//
//        // banner add
//
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        //



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
}