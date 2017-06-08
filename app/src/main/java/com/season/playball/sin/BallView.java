package com.season.playball.sin;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


/**
 * Disc: 单个View多个球体
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-06-08 11:59
 */
public class BallView extends View{

    public BallView(Context context) {
        super(context);
        ballList = new ArrayList<>();
    }


    List<BallModel> ballList;


    private boolean running = true;
    int time = 10;
    public void stop() {
        running = false;
        handler.removeMessages(1);
    }

    public void start() {
        running = true;
        for (BallModel ballModel:ballList)
            ballModel.move();
        crashCheck();
        invalidate();
        handler.sendEmptyMessageDelayed(1, time);
    }

    void crashCheck() {
        for (BallModel currentBall : ballList) {
            for (BallModel checkBall : ballList) {
                if (currentBall.id != checkBall.id) {
                    if (currentBall.isCrash(checkBall)) {
                        currentBall.crashChanged(checkBall);
                    }
                }
            }
        }
    }


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (running) {
                start();
            }
        };
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (BallModel ballModel:ballList)
            ballModel.draw(canvas);
    }


    public void clear() {
        ballList.clear();
    }

    public void add(View parentView) {
        BallModel ballModel = new BallModel(parentView.getWidth(), parentView.getHeight());
        ballModel.id = System.currentTimeMillis();
        ballModel.randomSetUp();
        ballList.add(ballModel);
    }

}
