package com.nexgo.xtms.eventbus;

/**
 * Created by zhouxie on 2017/7/6.
 */

public class BroadCastEvent extends Event {
    private int power;

    public BroadCastEvent(String tag) {
        super(tag);
    }

    public BroadCastEvent(String tag, int power) {
        super(tag);
        this.power = power;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
