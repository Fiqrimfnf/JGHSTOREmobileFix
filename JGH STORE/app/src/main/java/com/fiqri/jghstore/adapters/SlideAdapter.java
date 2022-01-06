package com.fiqri.jghstore.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.fiqri.jghstore.R;

public class SlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SlideAdapter(Context context) {
        this.context = context;
    }

    int imagesArray[]={
            R.drawable.onboard1,
            R.drawable.onboard2,
            R.drawable.onboard3
    };

    int headingArray[]={
            R.string.head1,
            R.string.head2,
            R.string.head3
    };

    int descripsitionArray[]={
            R.string.desc1,
            R.string.desc2,
            R.string.desc3
    };

    @Override
    public int getCount() {
        return headingArray.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(ConstraintLayout) object;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.sliding_layout,container,false);

        ImageView imageView = view.findViewById(R.id.slider_img);
        TextView heading = view.findViewById(R.id.heading);
        TextView description = view.findViewById(R.id.description);

        imageView.setImageResource(imagesArray[position]);
        heading.setText(headingArray[position]);
        description.setText(descripsitionArray[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
