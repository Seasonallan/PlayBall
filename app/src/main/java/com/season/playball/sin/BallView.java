package com.season.playball.sin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.season.playball.LogConsole;
import com.season.playball.sin.interpolator.BallInterpolatorFactory;
import com.season.playball.sin.interpolator.IInterpolator;

import java.util.ArrayList;
import java.util.List;


/**
 * Disc: 单个View多个球体
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-06-08 11:59
 */
public class BallView extends View {

    Paint paint;
    List<Ball> ballList;

    public BallView(Context context) {
        super(context);
        ballList = new ArrayList<>();
        paint = new Paint();
        paint.setStrokeWidth(10);
    }


    private boolean running = true;
    int time = 10;

    public void stop() {
        running = false;
        handler.removeMessages(1);
    }

    public void start() {
        running = false;
        if (touchBall != null && touchBall.isTouched) {
            running = true;
        }
        for (Ball ballModel : ballList) {
            if (ballModel.hasSpeed()){
                ballModel.move();
                running = true;
            }
        }
        crashCheck();
        invalidate();
        handler.sendEmptyMessageDelayed(1, time);
    }

    void crashCheck() {
        for (Ball currentBall : ballList) {
            for (Ball checkBall : ballList) {
                if (currentBall.id != checkBall.id) {
                    if (currentBall.isCrash(checkBall)) {
                        currentBall.crashChanged(checkBall);
                    }
                }
            }
        }
    }

    Ball touchBall;
    //VelocityTracker mVelocityTracker;
    float x, y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchBall = null;
                touchBall = getTouchBall(x, y);
                LogConsole.log("ACTION_DOWN " + touchBall);
                if (touchBall != null) {
                    ballTouchExpand = new BallTouchExpand();
                    ballTouchExpand.ball = touchBall;
//                    if (mVelocityTracker == null) {
//                        mVelocityTracker = VelocityTracker.obtain();//获得VelocityTracker类实例
//                    }
                    touchBall.onTouch();
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (touchBall != null) {
//                    mVelocityTracker.addMovement(event);
                    touchBall.onMove(x, y);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                LogConsole.log("ACTION_CANCEL " + touchBall);
                if (touchBall != null) {
//                    mVelocityTracker.computeCurrentVelocity(1, (float) 0.01);
//                    mVelocityTracker.computeCurrentVelocity(1000);
//                    float speed = mVelocityTracker.getXVelocity() * mVelocityTracker.getXVelocity() + mVelocityTracker.getYVelocity()*mVelocityTracker.getYVelocity();
//                    speed = (float) Math.sqrt(speed);
//                    double degree = Math.atan2(mVelocityTracker.getYVelocity(), mVelocityTracker.getXVelocity());
//                    degree = 180 * degree / Math.PI;
//                    mVelocityTracker.recycle();
//                    LogConsole.log("speed=" + speed/100 + "  degree=" + degree);
//                    touchBall.onRelease(speed/100, degree);
//                    mVelocityTracker = null;
//                    touchBall = null;
                    int index = getTouchIndex();
                    if (index < 0){
                        touchBall.onRelease(x, y);
                    }else{
                        if (index == 0){
                            removeBall(touchBall);
                        }else if(index == 1){
                            touchBall.stop();
                        }else if(index == 2){
                            touchBall.resume();
                        }else if(index == 3){
                            touchBall.ballInterpolator = BallInterpolatorFactory.getInterpolator(BallInterpolatorFactory.ACCELERATE);
                            touchBall.resume();
                        }else if(index == 4){
                            touchBall.ballInterpolator = BallInterpolatorFactory.getInterpolator(BallInterpolatorFactory.LINEAR);
                            touchBall.resume();
                        }
                    }
                    touchBall = null;
                }
                break;

        }

        return super.onTouchEvent(event);
    }

    Ball getTouchBall(float x, float y) {
        for (Ball ballModel : ballList) {
            if (ballModel.isTouched(x, y)) {
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
        }

        ;
    };



    public int getTouchIndex(){
        if (ballTouchExpand != null){
            return ballTouchExpand.getTouchIndex();
        }
        return -1;
    }

    BallTouchExpand ballTouchExpand;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Ball ballModel : ballList)
            ballModel.draw(canvas);

        if (touchBall != null && touchBall.isTouched) {
            paint.setColor(touchBall.color);
            canvas.drawLine(touchBall.cx, touchBall.cy, x, y, paint);
            ballTouchExpand.onDraw(canvas, x, y);
        }

    }


    public void clear() {
        ballList.clear();
    }

    public void removeBall(Ball ball){
        ballList.remove(ball);
    }

    /**
     * 添加一个球
     *
     * @param parentView
     */
    public void addOneBall(View parentView) {
        String interpolatorFlag = BallInterpolatorFactory.LINEAR;
        if (ballList.size() == 3) {
            interpolatorFlag = BallInterpolatorFactory.ACCELERATE;
        }
        if (ballList.size() == 2) {
            interpolatorFlag = BallInterpolatorFactory.KEEP;
        }
        IInterpolator interpolator = BallInterpolatorFactory.getInterpolator(interpolatorFlag);
        Ball ballModel = new Ball.Builder()
                .setId(System.currentTimeMillis())
                .setEdge(parentView.getWidth(), parentView.getHeight())
                .setInterpolator(interpolator)
                .build();
        ballModel.randomSetUp();
        ballList.add(ballModel);
        if (!running){
            start();
        }
    }

}
