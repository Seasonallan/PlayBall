package com.season.playball.sin;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.BaseInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener, View.OnLongClickListener {

    private RelativeLayout mContaintView;
    private BallView mBallView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mContaintView = new RelativeLayout(this);
        mContaintView.setOnClickListener(this);
        mContaintView.setOnLongClickListener(this);

        TextView textView = new TextView(this);
        textView.setText("Touch the Screen");
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.CENTER_IN_PARENT);
        mContaintView.addView(textView, param);

        mBallView = new BallView(this);
        mBallView.start();
        mContaintView.addView(mBallView);

        setContentView(mContaintView);
    }

    @Override
    public void onClick(View v) {
        addBall();
    }

    private void addBall() {
        mBallView.add(mContaintView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBallView.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBallView.start();
    }

    @Override
    public boolean onLongClick(View v) {
        mBallView.clear();
        return true;
    }
}
