package com.season.playball;

import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener, View.OnLongClickListener {

    private RelativeLayout mContaintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mContaintView = new RelativeLayout(this);
        mContaintView.setOnClickListener(this);
        mContaintView.setOnLongClickListener(this);

        setContentView(mContaintView);
    }

    @Override
    public void onClick(View v) {
        addBall();
    }

    private void addBall() {
        BallView ballView = new BallView(this, ballViews.size(), mContaintView){
            public List<BallView> getRunningBalls(){
                return ballViews;
            };
        };
        ballView.start();

        ballViews.add(ballView);
        mContaintView.addView(ballView);
    }

    private List<BallView> ballViews = new ArrayList<>();

    @Override
    public boolean onLongClick(View v) {
        mContaintView.removeAllViews();
        for (BallView ballView : ballViews) {
            ballView.stop();
        }
        ballViews.clear();
        return true;
    }
}
