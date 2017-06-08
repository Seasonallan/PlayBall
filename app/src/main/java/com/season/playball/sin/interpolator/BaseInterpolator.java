package com.season.playball.sin.interpolator;

import java.util.Random;

/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-06-08 15:58
 */
public abstract class BaseInterpolator implements IInterpolator {
    float speed = 10;

    @Override
    public void randomSet(){
        speed = new Random().nextInt(8) + 8;
    }

    @Override
    public float getSpeed(){
        return speed;
    }

    @Override
    public void speedChange(int speedCost, IInterpolator ballInterpolator) {
        if (true){
            speed = (speed + ballInterpolator.getSpeed())/2;
            return;
        }
        if (speedCost< 180){//same area, speed up
            speed += ballInterpolator.getSpeed() * speedCost/360;
        }else{//speed down
            speed -= speed * speedCost/360;
        }

    }

}