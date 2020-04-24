package ma.ofppt.tdi201.quizly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ma.ofppt.tdi201.quizly.Commun.Commun;
import ma.ofppt.tdi201.quizly.Model.QuestionScore;

public class GameOver extends AppCompatActivity {

    Button btnTryAgain;
    TextView txtResultScore, getTxtResultQuestion;
    ProgressBar doneProgressBar;

    FirebaseDatabase database;
    DatabaseReference questionScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question_Score");

        txtResultScore = (TextView) findViewById(R.id.txt_total_score);
        getTxtResultQuestion = (TextView) findViewById(R.id.txt_questions_total);
        doneProgressBar = (ProgressBar) findViewById(R.id.doneProgressBar);
        btnTryAgain = (Button) findViewById(R.id.btnTryAgain);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOver.this,Home.class);
                startActivity(intent);
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            int score = extras.getInt("score");
            int totalQuestions = extras.getInt("total");
            int correctAnswers = extras.getInt("correct");

            txtResultScore.setText("Score: "+score);
            getTxtResultQuestion.setText("Réussi: "+correctAnswers+" / "+totalQuestions);
            doneProgressBar.setMax(totalQuestions);
            doneProgressBar.setProgress(correctAnswers);

            //upload point to DB
            questionScore.child(Commun.currentUser.getUserName()+"_"+Commun.categoryId)
                    .setValue(new QuestionScore(Commun.currentUser.getUserName()+"_"+Commun.categoryId,
                                                    Commun.currentUser.getUserName(),
                                                    String.valueOf(score)));


        }
    }
}
