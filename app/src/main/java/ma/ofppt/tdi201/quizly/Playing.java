package ma.ofppt.tdi201.quizly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import ma.ofppt.tdi201.quizly.Common.Common;


public class Playing extends AppCompatActivity implements View.OnClickListener {

    final static long INTERVAL = 1000;  //1 second
    final static long TIMEOUT = 7000;   //7 seconds

    int progressValue = 0;
    CountDownTimer mCountDown;

    int index = 0, score = 0, thisQuestion = 0, totalQuestion, correctAnswers;
//    //firebase
//    FirebaseDatabase database;
//    DatabaseReference questions;

    ProgressBar progressBar;
    ImageView questionImage;
    Button btnA, btnB, btnC, btnD;
    TextView textScore, textQuestionNum, question_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
            //firebase
//        database= FirebaseDatabase.getInstance();
//        questions=database.getReference("Questions");

        textScore = (TextView) findViewById(R.id.txt_score);
        textQuestionNum = (TextView) findViewById(R.id.txt_questions_total);
        question_text = (TextView) findViewById(R.id.question_text);
        questionImage = (ImageView) findViewById(R.id.question_image);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        btnA = (Button) findViewById(R.id.btn_answer_A);
        btnB = (Button) findViewById(R.id.btn_answer_B);
        btnC = (Button) findViewById(R.id.btn_answer_C);
        btnD = (Button) findViewById(R.id.btn_answer_D);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        mCountDown.cancel();

        if (index < totalQuestion) // there's still some questions in the list
        {
            Button clickButton = (Button)view;
            if (clickButton.getText().equals(Common.questionsList.get(index).getCorrectAnswer())) //he/she choose correct answer
            {
                score += 10;
                correctAnswers++;
                ShowQuestion(++index); //go to next question
            }
            else {
                Intent gameOver = new Intent(this,GameOver.class);
                Bundle dataSend = new Bundle();
                dataSend.putInt("SCORE",score);
                dataSend.putInt("TOTAL",totalQuestion);
                dataSend.putInt("CORRECT", correctAnswers);
                gameOver.putExtras(dataSend);
                startActivity(gameOver);
                finish();
            }

            textScore.setText(String.format("%d",score));
        }

    }

    private void ShowQuestion(int index) {
        if (index < totalQuestion){
            thisQuestion++;
            textQuestionNum.setText(thisQuestion + " / " + totalQuestion);
            progressBar.setProgress(0);
            progressValue = 0;

            if(Common.questionsList.get(index).getIsImageQuestion().equals("true")) {   //it's an image
                Picasso.with(getBaseContext())
                        .load(Common.questionsList.get(index).getQuestion())
                        .into(questionImage);
                questionImage.setVisibility(View.VISIBLE);
                question_text.setVisibility(View.INVISIBLE);
            }
            else {
                question_text.setText(Common.questionsList.get(index).getQuestion());

                questionImage.setVisibility(View.INVISIBLE);
                question_text.setVisibility(View.VISIBLE);
            }

            btnA.setText(Common.questionsList.get(index).getAnswerA());
            btnB.setText(Common.questionsList.get(index).getAnswerB());
            btnC.setText(Common.questionsList.get(index).getAnswerC());
            btnD.setText(Common.questionsList.get(index).getAnswerD());

            mCountDown.start();
        }
        else //if it's the final question
        {
            Intent gameOver = new Intent(this,GameOver.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("SCORE",score);
            dataSend.putInt("TOTAL",totalQuestion);
            dataSend.putInt("CORRECT", correctAnswers);
            gameOver.putExtras(dataSend);
            startActivity(gameOver);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        totalQuestion = Common.questionsList.size();

        mCountDown = new CountDownTimer(TIMEOUT, INTERVAL) {
            @Override
            public void onTick(long minisec) {
                progressBar.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish() {
                mCountDown.cancel();
                ShowQuestion(++index);
            }
        };
        ShowQuestion(index);
    }
}
