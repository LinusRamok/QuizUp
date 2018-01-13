package com.example.kamlesh.frd;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quiz.up.RankingPagePOJO.Top5;

/**
 * Created by RAJA on 12-01-2018.
 */

public class RankinglistAdapter extends RecyclerView.Adapter<RankinglistAdapter.RankingViewHolder>{
    private RecyclerView mListener;
    private Context context;
    private Top5[] data;
    public RankinglistAdapter(Context context, Top5[] data){
        this.context=context;
        this.data=data;
    }

    @Override
    public RankingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.ranking_list_item,parent,false);
        return new RankingViewHolder(view);
    }



    @Override
    public void onBindViewHolder(RankingViewHolder holder, int position) {
        final Top5 top5=data[position];
        holder.name.setText(top5.getName());
        holder.accuracy.setText(top5.getAccuracy());
//        holder.country.setText(top5.getCountry());
        holder.rank.setText(String.valueOf(top5.getRank()));
//        holder.email.setText(top5.getEmail());

//
//        Drawable d= context.getResources().getDrawable(R.drawable.ic_launcher_background);
//        holder.rank.setBackground(d);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, top5.getName(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @Override
    public int getItemCount()
    {
        return data.length;
    }


    public class RankingViewHolder extends RecyclerView.ViewHolder{
        TextView name,rank,email,country,accuracy;
        LinearLayout linearLayout;
        public RankingViewHolder(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            rank = (TextView) itemView.findViewById(R.id.rank);
            accuracy = (TextView) itemView.findViewById(R.id.accuracy1);
//            email = (TextView) itemView.findViewById(R.id.email);
//            country = (TextView) itemView.findViewById(R.id.country);
            linearLayout=(LinearLayout) itemView.findViewById(R.id.ranking_onclick);
        }
    }

}