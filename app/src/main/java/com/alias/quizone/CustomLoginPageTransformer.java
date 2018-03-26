package com.alias.quizone;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by admin on 28-Jan-18.
 */

public class CustomLoginPageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(@NonNull View view, float position) {

        //yahoo weather kinda animation, i.e. parallax
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
        final float MIN_SCALE = 0.85f;
        final float MIN_ALPHA = 0.5f;
        ImageView dummyImageView = view.findViewById(R.id.imageView);
        TextView dummyTextView = view.findViewById(R.id.textView0);
        TextView welcomeText = view.findViewById(R.id.welcomeText);



        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(1);
        } else if (position <= 1) { // [-1,1]
            dummyImageView.setTranslationX(-position * (pageWidth / 2)); //Half the normal speed
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(1);
        }




        //fade in/fade out view pager animation
        if (position <= -1.0F || position >= 1.0F) {
            welcomeText.setTranslationX(view.getWidth() * position);
            welcomeText.setAlpha(0.0F);
        } else if (position == 0.0F) {
            welcomeText.setTranslationX(view.getWidth() * position);
            welcomeText.setAlpha(1.0F);
        } else {
            // position is between -1.0F & 0.0F OR 0.0F & 1.0F
            welcomeText.setTranslationX(view.getWidth() * -position);
            welcomeText.setAlpha(1.0F - Math.abs(position));
        }




        //zoom transformation
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            dummyTextView.setAlpha(0);
        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                dummyTextView.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                dummyTextView.setTranslationX(-horzMargin + vertMargin / 2);
            }
            // Scale the page down (between MIN_SCALE and 1)
            dummyTextView.setScaleX(scaleFactor);
            dummyTextView.setScaleY(scaleFactor);
            // Fade the page relative to its size.
            dummyTextView.setAlpha(MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                            (1 - MIN_SCALE) * (1 - MIN_ALPHA));
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            dummyTextView.setAlpha(0);
        }


    }
}
