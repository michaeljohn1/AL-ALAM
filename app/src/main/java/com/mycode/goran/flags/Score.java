package com.mycode.goran.flags;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

public class Score extends AppCompatActivity {
    ListView lstView;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MobileAds.initialize(this,getString(R.string.banner_test));

        // banner add

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        lstView = findViewById(R.id.lstRanking);
        DbHelper db = new DbHelper(this);
        List<Ranking> lstRanking = db.Ranking();
        if(lstRanking.size() > 0)
        {
            CustomAdapter customAdapter = new CustomAdapter(this,lstRanking);
            lstView.setAdapter(customAdapter);
        }
    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Score.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
