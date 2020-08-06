package com.universal.homear;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.universal.homear.R;

/**
 * Home screen
 */
public class HomeFragment extends Fragment {

    private ImageView profile;
    private RecyclerView prevRecycler;
    ImageView imageViews;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        imageViews = root.findViewById(R.id.home_images);
        /*imageViews.setBackgroundResource(R.drawable.start_up_slideshow);
        imagesAnimation = (AnimationDrawable) imageViews.getBackground();
        imagesAnimation.start();*/
        final int[] imageArray = { R.drawable.home_photo_1, R.drawable.home_photo_2, R.drawable.home_photo_3, R.drawable.home_photo_4};
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i = 0;

            public void run() {
                imageViews.setImageResource(imageArray[i]);
                i++;
                if (i > imageArray.length - 1) {
                    i = 0;
                }
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(runnable, 2000);

        prevRecycler = root.findViewById(R.id.prev_viewed);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        prevRecycler.setLayoutManager(layoutManager);
        prevRecycler.setItemAnimator(new DefaultItemAnimator());

        profile = root.findViewById(R.id.profileButton);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }



}