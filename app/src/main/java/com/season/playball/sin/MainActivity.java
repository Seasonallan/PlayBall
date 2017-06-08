package com.season.playball.sin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

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
