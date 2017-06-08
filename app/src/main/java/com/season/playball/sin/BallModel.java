package com.season.playball.sin;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;

import com.season.playball.LogConsole;
import com.season.playball.sin.interpolator.LinearInterpolator;
import com.season.playball.sin.interpolator.BallInterpolatorFactory;
import com.season.playball.sin.interpolator.IInterpolator;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-06-08 12:08
 */
public class BallModel {

    int width, height;

    int radius;
    int color;
    Paint paint;
    TextPaint textPaint;

    float cx, cy;
    double slopDegree = 3;

    long id;

    IInterpolator ballInterpolator;

    BallModel(int width, int height){
        this.width = width;
        this.height = height;
        ballInterpolator = new LinearInterpolator();
    }

    public BallModel buildInterpolator(String flag){
        ballInterpolator = BallInterpolatorFactory.getInterpolator(flag);
        return this;
    }


    float textX, textY;

    void randomSetUp(){
        if (radius <= 0) {
            radius = new Random().nextInt(width / 5);
            radius = Math.max(20, radius);
        }
        if (color <= 0) {
            color = 0xff000000 | new Random().nextInt(0x00ffffff);
        }
        if (paint == null) {
            paint = new Paint();
            paint.setColor(color);
        }
        if (textPaint ==  null){
            textPaint = new TextPaint();
            textPaint.setTextSize(radius * 2 / 3);
            textPaint.setColor(Color.WHITE);
            Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
            textX = textPaint.measureText("0.00")/2;
            LogConsole.log("descent="+ fontMetrics.descent+"  ascent="+ fontMetrics.ascent);
            textY = ( fontMetrics.descent - fontMetrics.ascent) / 4;
        }
        ballInterpolator.randomSet();
        slopDegree = new Random().nextInt(360);
        cx = new Random().nextInt(width - radius);
        cy = new Random().nextInt(height - radius);
        fixXY();
    }

    void fixXY() {
        if (cx <= radius || cx >= width - radius) {
            slopDegree = 180 - slopDegree;
        }
        if (cy >= height - radius || cy <= radius) {
            slopDegree = -slopDegree;
        }
        while (slopDegree < 0) {
            slopDegree += 360;
        }
        while (slopDegree > 360) {
            slopDegree -= 360;
        }

        cx = Math.max(radius, cx);
        cy = Math.max(radius, cy);
        cx = Math.min(cx, width - radius);
        cy = Math.min(cy, height - radius);
    }

    boolean isCrash(BallModel ballView) {
        double xy = (cx - ballView.cx) * (cx - ballView.cx) + (cy - ballView.cy) * (cy - ballView.cy);
        xy = Math.sqrt(xy);
        if (xy <= radius + ballView.radius) {
            return true;
        }
        return false;
    }

    void crashChanged(BallModel crashModel){
        double degree = Math.atan2((cy - crashModel.cy), (cx - crashModel.cx));
        degree = 180 * degree / Math.PI;

        LogConsole.log(id + " info");
        LogConsole.log(" speed = " + ballInterpolator.getSpeed());
        LogConsole.log(" slopDegree = " + slopDegree);
        LogConsole.log(" degree = " + degree);

        int speedCost = getSpeedCost(degree, slopDegree);
        ballInterpolator.speedChange(speedCost, crashModel.ballInterpolator);
        float speed = ballInterpolator.getSpeed();
        if (speed > 0){

            if (sameArea(degree, slopDegree)){
                slopDegree =  degree;
            }else{
                slopDegree += 180;
                degree += 180;
                slopDegree = degree - slopDegree + degree;
            }
        }else{

            slopDegree = crashModel.slopDegree;
            slopDegree += 180;
            degree += 180;
            slopDegree = degree - slopDegree + degree;
            slopDegree += 180;
        }


    }

    int getSpeedCost(double from, double to){
        int mul = (int) (from - to);
        return mul%360;
    }

    /**
     * 用于重合方向纠正
     * @param from
     * @param to
     * @return
     */
    boolean sameArea(double from, double to){
        int mul = (int) (from - to);
        return mul%360 < 180;
    }

    public void move() {
        ballInterpolator.speedCost();
        if (ballInterpolator.getSpeed() > 0){
            cx += ballInterpolator.getSpeed() * Math.cos(slopDegree * Math.PI / 180);
            cy += ballInterpolator.getSpeed() * Math.sin(slopDegree * Math.PI / 180);
            fixXY();
        }
    }



    void draw(Canvas canvas) {
        canvas.drawCircle(cx, cy, radius, paint);
        DecimalFormat df = new DecimalFormat("###.00");
        String speedStr = df.format(ballInterpolator.getSpeed());
        if (speedStr.length() > 4){
            speedStr = speedStr.substring(0, 4);
        }
        canvas.drawText(speedStr, cx - textX, cy + textY, textPaint);
    }


}
