package com.example.kamlesh.frd;



import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


class topic_adapter extends BaseAdapter {

   // Declare Variables
   FirebaseStorage storage =FirebaseStorage.getInstance();
   Context mContext;
   LayoutInflater inflater;
   private List<Topic> topiclist = null;

   private ArrayList<Topic> arraylist;

   public topic_adapter(Context context, ArrayList<Topic> topiclist) {
       mContext = context;
       this.topiclist = topiclist;
       inflater = LayoutInflater.from(mContext);
       this.arraylist = new ArrayList<Topic>();
       this.arraylist.addAll(topiclist);

   }

    public class ViewHolder {
        TextView name;
        ImageView img;

    }

   @Override
   public int getCount() {
       return topiclist.size();
   }

   @Override
   public Topic getItem(int position) {
       return topiclist.get(position);
   }

   @Override
   public long getItemId(int position) {
       return position;
   }

   public View getView(final int position, View view, ViewGroup parent) {
       final ViewHolder holder;
       if (view == null) {
           holder = new ViewHolder();
           view = inflater.inflate(R.layout.list_item, parent, false);
           // Locate the TextViews in listview_item.xml
           holder.name = (TextView) view.findViewById(R.id.name);
           holder.img=(ImageView)view.findViewById(R.id.image);
           view.setTag(holder);
       } else {
           holder = (ViewHolder) view.getTag();
       }
       // Set the results into TextViews
       holder.name.setText(topiclist.get(position).name);

       StorageReference storageRef = storage.getReference(topiclist.get(position).url);

       Glide.with(view)
              // .using(new FirebaseImageLoader())
               .load(storageRef)
               .into(holder.img);
//       String leciv="http://fypesystem.com/upload/9c419dd5659b0af4d14f642f454dcb07.jpg";
//       Glide.with(view).load(leciv)
//               .thumbnail(0.5f)
//               .into(holder.img);

       return view;
   }

   // Filter Class
   public void filter(String charText) {
       charText = charText.toLowerCase(Locale.getDefault());
       topiclist.clear();
       ArrayList<Topic> arrayList2=new ArrayList<Topic>();


       if (charText.length() == 0) {
           topiclist.addAll(arraylist);
       }
       else {
           for (Topic wp : arraylist) {

             if (wp.name.toLowerCase(Locale.getDefault()).startsWith(charText) ) {

                     topiclist.add(wp);
               }
               else if(wp.name.toLowerCase(Locale.getDefault()).contains(" "+charText)){
                 arrayList2.add(wp);
                }
           }

           topiclist.addAll(arrayList2);
       }


       notifyDataSetChanged();
   }

}


