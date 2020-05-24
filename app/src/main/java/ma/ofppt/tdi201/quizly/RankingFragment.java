package ma.ofppt.tdi201.quizly;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.net.ConnectivityManager;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import ma.ofppt.tdi201.quizly.Common.Common;
import ma.ofppt.tdi201.quizly.Interface.ItemClickListener;
import ma.ofppt.tdi201.quizly.Interface.RankingCallBack;
import ma.ofppt.tdi201.quizly.Model.Category;
import ma.ofppt.tdi201.quizly.Model.QuestionScore;
import ma.ofppt.tdi201.quizly.Model.Ranking;
import ma.ofppt.tdi201.quizly.ViewHolder.RankingViewHolder;


public class RankingFragment extends Fragment {

    View myFragment;
    RecyclerView rankingList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking, RankingViewHolder> adapter;
    private TextView txttname,txttscore,txttFiliere;
    NetReciever netReciever;


    FirebaseDatabase db;
    DatabaseReference questionScore,rankingtable;
    int res=0;

    public static RankingFragment newInstance(){
        RankingFragment rankingFragment = new RankingFragment();
        return rankingFragment;
    }
    //set the context of this activity
    private Context mContext;

    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db= FirebaseDatabase.getInstance();
        questionScore= db.getReference("Question_Score");
        rankingtable=db.getReference("Ranking");
        //register broadcast of internet detector


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        netReciever= new NetReciever();

        IntentFilter intentFilter= new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(netReciever,intentFilter);

        myFragment = inflater.inflate(R.layout.fragment_ranking, container, false);

        rankingList=(RecyclerView)myFragment.findViewById(R.id.rankingList);
        layoutManager= new LinearLayoutManager(getActivity());
        rankingList.setHasFixedSize(true);
        //using layoutManager to sort list of firebase ordrbychild method and  reverse recycle data because its asc
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(layoutManager);


        updateScore(Common.currentUser.getUserName(),Common.currentUser.getPrenom(),Common.currentUser.getFiliere(), new RankingCallBack<Ranking>() {
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
            protected void populateViewHolder(final RankingViewHolder rankingViewHolder, final Ranking ranking, int i) {
                rankingViewHolder.txt_name.setText(ranking.getUserName());
                rankingViewHolder.txtscore.setText(String.valueOf(ranking.getScore()));


                //fix crash
                rankingViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override

                    public void onClick(View view, int position, boolean isLongClick) {

                        showdetail(ranking);
                    }

                });
            }
        };



        adapter.notifyDataSetChanged();
        rankingList.setAdapter(adapter);
        return myFragment;

    }
    //methode for detail
    private void showdetail(final Ranking ranking){
        LayoutInflater inflater = this.getLayoutInflater();
        View detail = inflater.inflate(R.layout.layout_user_score_detail,null);

        txttname =(TextView) detail.findViewById(R.id.txtt_name);
        txttscore =(TextView) detail.findViewById(R.id.txtt_Score);
        txttFiliere=(TextView) detail.findViewById(R.id.txtt_Filiere);


        txttname.setText(ranking.getUserName()+" "+ranking.getPrenom());
        txttscore.setText(ranking.getScore());
        txttFiliere.setText(ranking.getFiliere());

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("INFO");
        alertDialog.setView(detail);

        alertDialog.setPositiveButton("view score detail", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent scrorDetail=new Intent(getActivity(),ScoreDetail.class);
                scrorDetail.putExtra("ViewUser",ranking.getUserName());
                startActivity(scrorDetail);
            }
        });
        alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.create();
        alertDialog.show();

    }

    private void updateScore(final String userName,final String Prenom,final String Filiere, final RankingCallBack<Ranking> callBack) {
        questionScore.orderByChild("user").equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){

                    QuestionScore quests=data.getValue(QuestionScore.class);
                    res+=Integer.parseInt(quests.getScore());

                    //call back for process value
                    Ranking ranking = new Ranking(userName,Prenom,Filiere,String.valueOf(res));
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
