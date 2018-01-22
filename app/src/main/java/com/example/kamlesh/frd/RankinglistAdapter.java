package com.example.kamlesh.frd;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;
import com.quiz.up.RankingPagePOJO.Top5;

import java.lang.reflect.Array;

import de.hdodenhof.circleimageview.CircleImageView;

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

        Glide.with(holder.pic.getContext()).load(top5.getPicUrl()).into(holder.pic);

        holder.name.setText(top5.getName());
//      holder.country.setText(top5.getCountry());
        holder.rank.setText(String.valueOf(top5.getRank()));
        holder.accuracy.setText(top5.getAccuracy()+"%");
//        holder.testing.setProgressText(String.valueOf(top5.getAccuracy())+"%");
//        holder.testing.setMax(100);
//        holder.testing.setProgress(Float.parseFloat(top5.getAccuracy()));

//      holder.email.setText(top5.getEmail());
//
//        if (top5.getRank()==1){
//      Drawable first= context.getResources().getDrawable(R.drawable.crown);
//      holder.crown.setBackground(first);
//     ViewGroup.LayoutParams s= holder.crown.getLayoutParams();
//     s.width=150;
//     s.height=150;
//     holder.crown.setLayoutParams(s);
//      holder.rank.setText("");
//        }
//      else if (top5.getRank()==2){
//            Drawable second= context.getResources().getDrawable(R.drawable.silver);
//            holder.crown.setBackground(second);
//            ViewGroup.LayoutParams s= holder.crown.getLayoutParams();
//            s.width=150;
//            s.height=150;
//            holder.crown.setLayoutParams(s);
//            holder.rank.setText("");
//        }
//        else if (top5.getRank()==3){
//            Drawable third= context.getResources().getDrawable(R.drawable.bronze);
//            ViewGroup.LayoutParams s= holder.crown.getLayoutParams();
//            s.width=150;
//            s.height=150;
//            holder.crown.setLayoutParams(s);
//            holder.crown.setBackground(third);
//            holder.rank.setText("");
//        }
//        else
//        {
////            Drawable all= context.getResources().getDrawable(R.drawable.circle);
////        holder.all.setBackground(all);
//            }


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, top5.getName(), Toast.LENGTH_SHORT).show();
            }
        });
//
//        Dim lvw as listiview
//        Set lvw = me.myLvw.object
//        lvw.listItems.Item(lvw.listItems.Count - 1).Selected = True
//        Set lvw = nothing



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
        LinearLayout linearLayout,crown,all;
        TextRoundCornerProgressBar testing;
        CircleImageView pic;
        public RankingViewHolder(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            rank = (TextView) itemView.findViewById(R.id.rank);
//          email = (TextView) itemView.findViewById(R.id.email);
//          country = (TextView) itemView.findViewById(R.id.country);
            accuracy = (TextView) itemView.findViewById(R.id.accuracy1);
            linearLayout=(LinearLayout) itemView.findViewById(R.id.ranking_onclick);
            all=(LinearLayout) itemView.findViewById(R.id.all);
            pic=itemView.findViewById(R.id.profile_image);
//            crown=(LinearLayout) itemView.findViewById(R.id.crown);
//            testing=itemView.findViewById(R.id.pro);
//            StartSmartAnimation.startAnimation(itemView.findViewById(R.id.ranking_onclick) , AnimationType.SlideInUp , 2000 , 0 , true );
        }
    }

}