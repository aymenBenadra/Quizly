package ma.ofppt.tdi201.quizly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeActivity extends AppCompatActivity {

    //welcome screen timeout
    private static int SPLASH_TIME_OUT = 3000; //1000=1s
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent welcomintent = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(welcomintent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
