package com.mycode.goran.flags;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;


import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.ads.InterstitialAd;
public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    final static long INTERVAL = 1000; // 1 second
    final static long TIMEOUT = 7000; // 7 sconds

     int show_ad=0;
    int progressValue = 0;
    int wronganswer = 0;



    public static CountDownTimer mCountDown; // for progressbar
    List<Question> questionPlay = new ArrayList<>(); //total Question
    DbHelper db;
    int index = 0, score = 0, thisQuestion = 0, totalQuestion, correctAnswer;
    String mode = "";


    ProgressBar progressBar;
    ImageView imageView;
    Button btnA, btnB, btnC, btnD;
    TextView txtScore, txtQuestion, wronganswercount;

    int counter = 0;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MobileAds.initialize(this, getString(R.string.ad_id));

        // MobileAds.initialize(this,getString(R.string.banner_test));

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


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
        //        save instance for rotation
        if (savedInstanceState != null) {
            savedInstanceState.getInt("KEY_QUESTION_COUNT");
            savedInstanceState.getInt("KEY_TOTAL_QUESTION_COUNT");
            savedInstanceState.getInt("KEY_SCORE_COUNT");

        }


        //Get Data from MainActivity
        Bundle extra = getIntent().getExtras();
        if (extra != null)
            mode = extra.getString("MODE");

        db = new DbHelper(this);

        txtScore = findViewById(R.id.txtScore);
        txtQuestion = findViewById(R.id.txtQuestion);
        progressBar = findViewById(R.id.progressBar);
        wronganswercount = findViewById(R.id.wronganswercount);


        imageView = findViewById(R.id.question_flag);

        btnA = findViewById(R.id.btnAnswerA);
        btnB = findViewById(R.id.btnAnswerB);
        btnC = findViewById(R.id.btnAnswerC);
//        btnD = findViewById(R.id.btnAnswerD);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
//        btnD.setOnClickListener(this);

    }


    @Override
    protected void onResume() {
//        final MediaPlayer timeover = MediaPlayer.create(this, R.raw.timeover);
        super.onResume();

        questionPlay = db.Question_Mode(mode);
        totalQuestion = questionPlay.size();

        mCountDown = new CountDownTimer(TIMEOUT + 30000, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish() {

                mCountDown.cancel();
                showQuestion(++index);
//                timeover.start();
                wronganswer++;
            }
        };
        showQuestion(index);
    }
    @Override
    public void onBackPressed() {
        askToClose();
    }

    private void askToClose() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setMessage(R.string.exit);
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent);
                mCountDown.cancel();
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
    }

    private void showQuestion(int index) {
        if (index < totalQuestion) {
            thisQuestion++;
            txtQuestion.setText(String.format("%d/%d", thisQuestion, totalQuestion));
            wronganswercount.setText("Wrong: " + wronganswer + "/" + 10);
            progressBar.setProgress(0);
            progressValue = 0;

            int ImageId = this.getResources().getIdentifier(questionPlay.get(index).getImage().toLowerCase(), "drawable", getPackageName());
            imageView.setBackgroundResource(ImageId);
            btnA.setText(questionPlay.get(index).getAnswerA());
            btnB.setText(questionPlay.get(index).getAnswerB());
            btnC.setText(questionPlay.get(index).getAnswerC());
//            btnD.setText(questionPlay.get(index).getAnswerD());

            mCountDown.start();
        } else {
            Intent intent = new Intent(this, End_Activity.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("SCORE", score);
            dataSend.putInt("TOTAL", totalQuestion);
            dataSend.putInt("CORRECT", correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        final MediaPlayer right = MediaPlayer.create(this, R.raw.right);
        final MediaPlayer wrong = MediaPlayer.create(this, R.raw.wrong);
        //  ads();
        mCountDown.cancel();
        if (index < totalQuestion) {
            Button clickedButton = (Button) v;
            if (clickedButton.getText().equals(questionPlay.get(index).getCorrectAnswer())) {
                Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
                score++;
                counter++;
                right.start();

            } else {
                Toast.makeText(this, "InCorrect", Toast.LENGTH_SHORT).show();
                wronganswer++;
                wrong.start();
                if (wronganswer == 10)
                {

                    Intent intent = new Intent(getApplicationContext(), End_Activity.class);
                    startActivity(intent);
                    mCountDown.cancel();
//                    finish();
                    Toast.makeText(this, "You have failed 10 times", Toast.LENGTH_SHORT).show();
                    if (mInterstitialAd.isLoaded()){
                        mInterstitialAd.show();
                    }
                }
            }
//////////////////////////////////////////////////////////////
            if (counter==10) {              //ish nkt
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    counter=0;
                    mCountDown.cancel();        //ama masre.
                }
            }
            if (counter==11) {
                counter=0;

                }

            else
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    showQuestion(++index); // If choose right , just go to next question
                }
            }, 3 * 1000); //your delay time


            counter++;

            //      txtScore.setText(String.format("%d",score));
            txtScore.setText("Correct:" + score);
        }

    }

}


