package ma.ofppt.tdi201.quizly.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ma.ofppt.tdi201.quizly.R;

public class RankingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_name,txtscore;
    public RankingViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_name=(TextView) itemView.findViewById(R.id.txt_name);
        txtscore=(TextView) itemView.findViewById(R.id.txt_score);
    }

    @Override
    public void onClick(View v) {

    }
}
