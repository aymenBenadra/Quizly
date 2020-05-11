package ma.ofppt.tdi201.quizly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ma.ofppt.tdi201.quizly.Model.QuestionScore;
import ma.ofppt.tdi201.quizly.ViewHolder.ScoreDetailViewHolder;

public class ScoreDetail extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference Question_score;
    RecyclerView scorelist;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<QuestionScore, ScoreDetailViewHolder> adapter;



     String Viewuser="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_detail);
        //firebase
        database=FirebaseDatabase.getInstance();
        Question_score=database.getReference("Question_Score");
         //View
         scorelist=(RecyclerView)findViewById(R.id.scoreList);
         scorelist.setHasFixedSize(true);
          layoutManager=new LinearLayoutManager(this);
          scorelist.setLayoutManager(layoutManager);



        if(getIntent()!=null)
        Viewuser=getIntent().getStringExtra("ViewUser");
        if(!Viewuser.isEmpty())
            loadScorDetail(Viewuser);
    }

    private void loadScorDetail(String viewuser) {
        adapter= new FirebaseRecyclerAdapter<QuestionScore, ScoreDetailViewHolder>(
                QuestionScore.class,R.layout.score_detail_layout,ScoreDetailViewHolder.class,
                Question_score.orderByChild("user").equalTo(Viewuser)) {
            @Override
            protected void populateViewHolder(ScoreDetailViewHolder scoreDetailViewHolder, QuestionScore questionScore, int i) {
                scoreDetailViewHolder.txt_name.setText(questionScore.getCategoryName());
                scoreDetailViewHolder.txt_score.setText(questionScore.getScore());
            }
        };
        adapter.notifyDataSetChanged();
        scorelist.setAdapter(adapter);
    }

}
