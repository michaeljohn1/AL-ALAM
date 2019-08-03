package com.mycode.goran.flags;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import static com.mycode.goran.flags.GameActivity.mCountDown;


public class End_Activity extends AppCompatActivity {

    Button btnTryAgain;
    TextView txtResultScore, txtResultQuestion;
    ProgressBar progressBarResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        DbHelper db = new DbHelper(this);


        txtResultScore =  findViewById(R.id.txtTotalScore);
        txtResultQuestion =  findViewById(R.id.txtTotalQuestion);
        progressBarResult =  findViewById(R.id.doneProgressBar);
        btnTryAgain = findViewById(R.id.btnTryAgain);


        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDown.cancel();
                Intent intent = new Intent(End_Activity.this, MainActivity.class);
                startActivity(intent);
//                finish();
                 mCountDown.cancel();
            }
        });


        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            int score = extra.getInt("SCORE");
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECT");

            //Update 2.0
            int playCount = 0;
            if(totalQuestion == 38) // EASY MODE
            {
                playCount = db.Receive_PlayCount(0);
                playCount++;
                db.Update_PlayCount(0,playCount); // Set PlayCount ++
            }
            else if(totalQuestion == 58) // MEDIUM MODE
            {
                playCount = db.Receive_PlayCount(1);
                playCount++;
                db.Update_PlayCount(1,playCount); // Set PlayCount ++
            }
            else if(totalQuestion == 108) // HARD MODE
            {
                playCount = db.Receive_PlayCount(2);
                playCount++;
                db.Update_PlayCount(2,playCount); // Set PlayCount ++
            }
            else if(totalQuestion == 208) // HARDEST MODE
            {
                playCount = db.Receive_PlayCount(3);
                playCount++;
                db.Update_PlayCount(3,playCount); // Set PlayCount ++
            }

            double subtract = ((5.0/(float)score)*100)*(playCount-1); //-1 because we playCount++ before we calculate result
            double finalScore = score - subtract;

            txtResultScore.setText(String.format("SCORE : %.1f (-%d)%%", finalScore,5*(playCount-1)));
            txtResultQuestion.setText(String.format("PASSED : %d/%d", correctAnswer, totalQuestion));

            progressBarResult.setMax(totalQuestion);
            progressBarResult.setProgress(correctAnswer);

            //save score
            db.insert_Score(finalScore);
        }
    }
        @Override
    public void onBackPressed() {

        Intent intent = new Intent(End_Activity.this, MainActivity.class);
        startActivity(intent);
            mCountDown.cancel();
    }
}
