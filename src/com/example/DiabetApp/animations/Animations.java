package com.example.DiabetApp.animations;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.TranslateAnimation;

/**
 * Created by максим on 04.11.2016.
 */
public class Animations extends TranslateAnimation {
    private int left;
    private int top;
    private int right;
    private int bottom;

    private View view;
    AnimationListener listener;


    public Animations(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Animations(Context c,AttributeSet attrs,int left,int top,int right,int bottom){
        super(c,attrs);
        this.left =left;
        this.top = top;
        this.right = right;
        this.bottom =bottom;

    }



    @Override
    public void start() {
        super.start();
    }


    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
