package com.example.kamlesh.frd;



import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.kamlesh.frd.Models.Topic;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


class topic_adapter extends RecyclerView.Adapter<topic_adapter.MyViewHolder> {

   // Declare Variables
   FirebaseStorage storage =FirebaseStorage.getInstance();
   Context mContext;
   LayoutInflater inflater;
   private List<Topic> topiclist;


   public topic_adapter(Context context, ArrayList<Topic> topiclist) {
       mContext = context;
       this.topiclist = topiclist;
       inflater = LayoutInflater.from(mContext);

   }

    public class ViewHolder {
        TextView name;
        ImageView img;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        Typeface ourFont = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/primelight.otf");
        vh.name.setTypeface(ourFont);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.name.setText(topiclist.get(position).name);
        StorageReference storageRef = storage.getReference(topiclist.get(position).url);

        Glide.with(mContext)
                .load(storageRef)
                .into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("item clicked...");
                System.out.println(topiclist.get(position).name);
                System.out.println(topiclist.get(position).url);
                System.out.println(topiclist.get(position).description);
                Intent intent = new Intent(mContext,topic_page.class);
                intent.putExtra("topic_details", new Gson().toJson(topiclist.get(position)));
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return topiclist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name;
        ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.name);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

}


