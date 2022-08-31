package com.example.test_t1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class button4Fragment extends Fragment {
    private View view;
    private ImageView imageView1;
    private ImageButton imageButton2;
    private ImageButton imageButton3;
    private ImageButton imageButton4;

    private Animator currentAnimator;
    private int shortAnimationDuration;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_btn4, container, false);

        imageView1 = view.findViewById(R.id.imageView1);
        imageButton2 = view.findViewById(R.id.imageView2);
        imageButton3 = view.findViewById(R.id.imageView3);
        imageButton4 = view.findViewById(R.id.imageView4);

        imageView1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                zoomImageFromThumb(imageView1, R.drawable._7); //해당 이미지로 바꾼다.

            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                zoomImageFromThumb(imageButton2, R.drawable._7); //해당 이미지로 바꾼다.

            }
        });imageButton3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                zoomImageFromThumb(imageButton3, R.drawable._7); //해당 이미지로 바꾼다.

            }
        });imageButton4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                zoomImageFromThumb(imageButton4, R.drawable._7); //해당 이미지로 바꾼다.

            }
        });


        shortAnimationDuration = getResources().getInteger(com.google.android.material.R.integer.material_motion_duration_short_1); //애니동작시간

        return view;
    }

    private void zoomImageFromThumb(final View thumbView, int imageResid)
    {

        //진행중인 애니메이션있으면 중지
        if(currentAnimator !=null)
        {
            currentAnimator.cancel();
        }

        final ImageView expandedImageView = (ImageView) view.findViewById(R.id.expanded_image); //확장이미지
        expandedImageView.setImageResource(imageResid); //확장이미지 리소스 설정


        final Rect startBounds = new Rect();//시작경계 - 축소그림에서 볼수있는 직사각형
        final Rect finalBounds = new Rect();//종료경계
        final Point globalOffset = new Point();

        thumbView.getGlobalVisibleRect(startBounds);//global좌표값을 가져온다?
        view.findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y); //변위차를 이용하여 위치 설정
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds.width() / startBounds.height()) //종료xy값이 , 시작xy값보다 크면,
        {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        }

        else
        {
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE); //확장이미지 뵤여주기

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        //확장이미지 시작점지정
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet(); //에니메이터셋 설정
        set.play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f));
        set.setDuration(shortAnimationDuration); //동작시간설정
        set.setInterpolator(new DecelerateInterpolator()); //
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator.ofFloat(expandedImageView,View.Y,startBounds.top))
                        .with(ObjectAnimator.ofFloat(expandedImageView,View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator.ofFloat(expandedImageView,View.SCALE_Y, startScaleFinal));
                set.setDuration(shortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation)
                    {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }
                });
                set.start();
                currentAnimator = set;
            }
        });
    }
}

