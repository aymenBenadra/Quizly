package ma.ofppt.tdi201.quizly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class GameOverTempActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000; //1000=1s
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_temp);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            final int score = extras.getInt("SCORE");
            final int totalQuestions = extras.getInt("TOTAL");
            final int correctAnswers = extras.getInt("CORRECT");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent welcomintent = new Intent(GameOverTempActivity.this,GameOver.class);
                Bundle dataSend = new Bundle();
                dataSend.putInt("SCORE",score);
                dataSend.putInt("TOTAL",totalQuestions);
                dataSend.putInt("CORRECT", correctAnswers);
                welcomintent.putExtras(dataSend);

                startActivity(welcomintent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}}
