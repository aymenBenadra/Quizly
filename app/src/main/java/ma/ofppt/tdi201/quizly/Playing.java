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

import com.squareup.picasso.Picasso;

import ma.ofppt.tdi201.quizly.Commun.Commun;

public class Playing extends AppCompatActivity implements View.OnClickListener {

    final static long INTERVAL = 1000;  //1 second
    final static long TIMEOUT = 10000;   //10 seconds

    int progressValue = 0;
    CountDownTimer mCountDown;

    int index = 0, score = 0, thisQuestion = 0, totalQuestion, correctAnswers;

    ProgressBar progressBar;
    ImageView questionImage;
    Button btnA, btnB, btnC, btnD;
    TextView textScore, textQuestionNum, questionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        textScore = (TextView) findViewById(R.id.txt_score);
        textQuestionNum = (TextView) findViewById(R.id.txt_questions_total);
        questionText = (TextView) findViewById(R.id.question_text);
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
            if (clickButton.getText().equals(Commun.questionsList.get(index).getCorrectAnswer())) //he/she choose correct answer
            {
                score += 10;
                correctAnswers++;
                ShowQuestion(++index); //go to next question
            }
            else {
                Intent gameOver = new Intent(this,GameOver.class);
                Bundle dataSend = new Bundle();
                dataSend.putInt("score",score);
                dataSend.putInt("total",totalQuestion);
                dataSend.putInt("correct", correctAnswers);
                gameOver.putExtras(dataSend);
                startActivity(gameOver);
                finish();
            }

            textScore.setText(""+score);
        }

    }

    private void ShowQuestion(int index) {
        if (index < totalQuestion){
            thisQuestion++;
            textQuestionNum.setText(thisQuestion + " / " + totalQuestion);
            progressBar.setProgress(0);
            progressValue = 0;

            if(Commun.questionsList.get(index).getIsImageQuestion().equals("true")) {   //it's an image
                Picasso.with(getBaseContext())
                        .load(Commun.questionsList.get(index).getQuestion())
                        .into(questionImage);
                questionImage.setVisibility(View.VISIBLE);
                questionText.setVisibility(View.INVISIBLE);
            }
            else {
                questionText.setText(Commun.questionsList.get(index).getQuestion());

                questionImage.setVisibility(View.INVISIBLE);
                questionText.setVisibility(View.VISIBLE);
            }

            btnA.setText(Commun.questionsList.get(index).getAnswerA());
            btnB.setText(Commun.questionsList.get(index).getAnswerB());
            btnC.setText(Commun.questionsList.get(index).getAnswerC());
            btnD.setText(Commun.questionsList.get(index).getAnswerD());

            mCountDown.start();
        }
        else //if it's the final question
        {
            Intent gameOver = new Intent(this,GameOver.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("score",score);
            dataSend.putInt("total",totalQuestion);
            dataSend.putInt("correct", correctAnswers);
            gameOver.putExtras(dataSend);
            startActivity(gameOver);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        totalQuestion = Commun.questionsList.size();

        mCountDown = new CountDownTimer(TIMEOUT, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
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
