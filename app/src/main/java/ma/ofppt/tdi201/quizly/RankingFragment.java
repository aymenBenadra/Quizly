package ma.ofppt.tdi201.quizly;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import ma.ofppt.tdi201.quizly.Commun.Commun;
import ma.ofppt.tdi201.quizly.Interface.ItemClickListener;
import ma.ofppt.tdi201.quizly.Interface.RankingCallBack;
import ma.ofppt.tdi201.quizly.Model.QuestionScore;
import ma.ofppt.tdi201.quizly.Model.Ranking;
import ma.ofppt.tdi201.quizly.ViewHolder.RankingViewHolder;


public class RankingFragment extends Fragment {

    View myFragment;
    RecyclerView rankingList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking, RankingViewHolder> adapter;


    FirebaseDatabase db;
    DatabaseReference questionScore,rankingtable;
    int res=0;

    public static RankingFragment newInstance(){
        RankingFragment rankingFragment = new RankingFragment();
        return rankingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db= FirebaseDatabase.getInstance();
        questionScore= db.getReference("Question_Score");
        rankingtable=db.getReference("Ranking");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_ranking, container, false);

        rankingList=(RecyclerView)myFragment.findViewById(R.id.rankingList);
        layoutManager= new LinearLayoutManager(getActivity());
        rankingList.setHasFixedSize(true);
        //using layoutManager to sort list of firebase ordrbychild method and  reverse recycle data because its asc
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(layoutManager);


        updateScore(Commun.currentUser.getUserName(), new RankingCallBack<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
                rankingtable.child(ranking.getUserName()).setValue(ranking);
                //AfficheRank();
            }
        });
//adapter
        adapter=new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(
                Ranking.class,R.layout.layout_ranking,RankingViewHolder.class,
                rankingtable.orderByChild("score")
        ) {
            @Override
            protected void populateViewHolder(RankingViewHolder rankingViewHolder, Ranking ranking, int i) {
                rankingViewHolder.txt_name.setText(ranking.getUserName());
                rankingViewHolder.txtscore.setText(String.valueOf(ranking.getScore()));

                //fix crash
                rankingViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        rankingList.setAdapter(adapter);
        return myFragment;
    }

    private void updateScore(final String userName, final RankingCallBack<Ranking> callBack) {
        questionScore.orderByChild("user").equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){

                    QuestionScore quests=data.getValue(QuestionScore.class);
                    res+=Integer.parseInt(quests.getScore());

                    //call back for process value
                    Ranking ranking = new Ranking(userName,String.valueOf(res));
                    callBack.callBack(ranking);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

//    private void AfficheRank() {
//        rankingtable.orderByChild("score").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot data:dataSnapshot.getChildren()) {
//
//                Ranking r = data.getValue(Ranking.class);
//                Log.d("DEBUG",r.getUserName());
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }


}
