package com.example.bottommenu.HelperClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.bottommenu.R;



public class HelpSliderAdapter extends PagerAdapter {
    Context context;

    LayoutInflater layoutInflater;
    public HelpSliderAdapter(Context contect) {
        this.context = contect;
    }

    int images[]={
            R.drawable.home_daun,
            R.drawable.home_daun,
            R.drawable.home_daun,
            R.drawable.home_daun
    };
    String headings[]={
            "test 1",
            "test 2",
            "test 3",
            "test 4"
    };
    String desc[]={
            "desc 1",
            "desc 2",
            "desc 3",
            "desc 4",
    };


    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService((context.LAYOUT_INFLATER_SERVICE));
        View view =layoutInflater.inflate(R.layout.activity_help_onboarding_item, container, false);

        //Hooks From Design
        ImageView imageView=view.findViewById(R.id.slider_image);
        TextView headingView=view.findViewById(R.id.slider_heading);
        TextView descView=view.findViewById(R.id.slider_desc);

        imageView.setImageResource(images[position]);
        headingView.setText(headings[position]);
        descView.setText(desc[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
