package com.season.playball.sin;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.season.playball.LogConsole;
import com.season.playball.sin.interpolator.BallInterpolatorFactory;

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

    BallModel touchBall;
    VelocityTracker mVelocityTracker;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchBall = getTouchBall(x, y);
                if (touchBall != null){
                    if (mVelocityTracker == null) {
                        mVelocityTracker = VelocityTracker.obtain();//获得VelocityTracker类实例
                    }
                    touchBall.onTouch();
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (touchBall != null){
                    mVelocityTracker.addMovement(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (touchBall != null){
                    mVelocityTracker.computeCurrentVelocity(1, (float) 0.01); //设置maxVelocity值为0.1时，速率大于0.01时，显示的速率都是0.01,速率小于0.01时，显示正常
                    mVelocityTracker.computeCurrentVelocity(1000); //设置units的值为1000，意思为一秒时间内运动了多少个像素
                    float speed = mVelocityTracker.getXVelocity() * mVelocityTracker.getXVelocity() + mVelocityTracker.getYVelocity()*mVelocityTracker.getYVelocity();
                    speed = (float) Math.sqrt(speed);
                    double degree = Math.atan2(mVelocityTracker.getYVelocity(), mVelocityTracker.getXVelocity());
                    degree = 180 * degree / Math.PI;
                    mVelocityTracker.recycle();
                    LogConsole.log("speed=" + speed/100 + "  degree=" + degree);
                    touchBall.onRelease(speed/100, degree);
                    mVelocityTracker = null;
                    touchBall = null;
                }
                break;

        }

        return super.onTouchEvent(event);
    }

    BallModel getTouchBall(float x, float y){
        for (BallModel ballModel:ballList){
            if (ballModel.isTouched(x, y)){
                return ballModel;
            }
        }
        return null;
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
        if (ballList.size() == 3){
            ballModel.buildInterpolator(BallInterpolatorFactory.ACCELERATE);
        }
        if (ballList.size() == 2){
            ballModel.buildInterpolator(BallInterpolatorFactory.KEEP);
        }
        ballModel.randomSetUp();
        ballList.add(ballModel);
    }

}
