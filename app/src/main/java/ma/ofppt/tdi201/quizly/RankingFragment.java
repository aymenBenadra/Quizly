package ma.ofppt.tdi201.quizly;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ma.ofppt.tdi201.quizly.Common.Common;
import ma.ofppt.tdi201.quizly.Interface.RankingCallBack;
import ma.ofppt.tdi201.quizly.Model.QuestionScore;
import ma.ofppt.tdi201.quizly.Model.Ranking;


public class RankingFragment extends Fragment {

    View myFragment;
    FirebaseDatabase db;
    DatabaseReference questScore,rankingtable;
    int res=0;

    public static RankingFragment newInstance(){
        RankingFragment rankingFragment = new RankingFragment();
        return rankingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db= FirebaseDatabase.getInstance();
        questScore= db.getReference("Question_score");
        rankingtable=db.getReference("Ranking");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_ranking, container, false);
        updatingscore(Common.CurrentUser.getUserName(), new RankingCallBack<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
                //put updated values to rankng table
                    rankingtable.child(ranking.getUserName()).setValue(ranking);
                    //show the result to table after upload them
                AfficheRank();

            }
        });
        return myFragment;
    }

    private void AfficheRank() {
        rankingtable.orderByChild("score").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data:dataSnapshot.getChildren()) {

                Ranking r = data.getValue(Ranking.class);
                Log.d("DEBUG",r.getUserName());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
  //HEY DUDE IM WORKING ON IT DONT WORRY
    private void updatingscore(final String userName, final RankingCallBack<Ranking> callback){
        questScore.orderByChild("user").equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    //I WILL ADD THIS CLASS WHEN I CAN DONT WPRRY ABOUT IT
                    QuestionScore quests=data.getValue(QuestionScore.class);
                res+=Integer.parseInt(quests.getScore());

                //call back for process value
                    Ranking ranking = new Ranking(userName,res);
                    callback.callBack(ranking);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
