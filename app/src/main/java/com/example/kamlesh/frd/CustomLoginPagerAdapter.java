package com.example.kamlesh.frd;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by admin on 24-Jan-18.
 */

public class CustomLoginPagerAdapter extends PagerAdapter {

    int[] loginImagesArray = {
            R.drawable.login_image_one,
            R.drawable.login_image_two,
            R.drawable.login_image_three
    };

    int[] loginTextsArray = {
            R.string.login_text_one,
            R.string.login_text_two,
            R.string.login_text_three
    };

    Context mContext;
    LayoutInflater mLayoutInflater;

    public CustomLoginPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return loginImagesArray.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item_login_page, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        TextView textView = (TextView) itemView.findViewById(R.id.textView);
        TextView welcomeText = (TextView) itemView.findViewById(R.id.welcomeText);

        Typeface ourLightFont = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/primelight.otf");
        Typeface ourBoldFont = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/primebold.otf");
        textView.setTypeface(ourLightFont);
        welcomeText.setTypeface(ourBoldFont);

        imageView.setImageResource(loginImagesArray[position]);
        textView.setText(loginTextsArray[position]);

        if (position == 0)
            welcomeText.setVisibility(View.VISIBLE);
        else
            welcomeText.setVisibility(View.INVISIBLE);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
