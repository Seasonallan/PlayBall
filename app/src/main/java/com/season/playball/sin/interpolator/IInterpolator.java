package com.season.playball.sin.interpolator;

/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-06-08 15:44
 */
public interface IInterpolator {

    String getDescription();

    void randomSet();

    void speedCost();

    float getSpeed();

    void speedChange(int speedCost, IInterpolator ballInterpolator);
}
