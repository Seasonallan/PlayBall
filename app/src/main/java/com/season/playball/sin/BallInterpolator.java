package com.season.playball.sin;

import java.util.Random;

/**
 * Disc: 速度控制
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-06-08 14:19
 */
public class BallInterpolator {
    float speed = 10;

    void randomSet(){
        speed = new Random().nextInt(8) + 8;
    }

    float getSpeed(){
        speed -= 0.01;
        return speed;
    }


}
