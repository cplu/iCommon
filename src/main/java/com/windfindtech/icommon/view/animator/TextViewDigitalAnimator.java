package com.windfindtech.icommon.view.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.widget.TextView;


/**
 * Created by cplu on 2015/3/3.
 */
public class TextViewDigitalAnimator {
    private static final int TEXT_ANIMATOR_DURATION = 1000;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static ValueAnimator get_animator(final TextView view, int start_value, final int target){
        try {
            ValueAnimator animator = ValueAnimator.ofInt(start_value, target);
//            final String target = view.getText().toString();
            animator.setDuration(TEXT_ANIMATOR_DURATION);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer val = (Integer)animation.getAnimatedValue();
                    String carplateValue=String.format("%,d",val);
//                    view.setText(val.toString());
                    view.setText(carplateValue);
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {

                private String carplateValue = String.format("%,d",target);

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    view.setText(carplateValue);
//                  view.setText(String.valueOf(target));
                    //callback.on_end();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    view.setText(carplateValue);
//                  view.setText(String.valueOf(target));
                    //callback.on_end();
                }
            });
            //callback.on_start();
            //animator.start();
            return animator;
        }
        catch (Exception e){
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static ValueAnimator get_animator(final TextView view, float start_value, final float target){
        try {
            ValueAnimator animator = ValueAnimator.ofFloat(start_value, target);
//            final String target = view.getText().toString();
            animator.setDuration(TEXT_ANIMATOR_DURATION);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float val = (Float) animation.getAnimatedValue();
                    view.setText(String.format("%.2f", val));
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setText(String.format("%.2f", target));
                    //callback.on_end();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    view.setText(String.format("%.2f", target));
                    //callback.on_end();
                }
            });
            //callback.on_start();
            //animator.start();
            return animator;
        }
        catch (Exception e){
            return null;
        }
    }
}
