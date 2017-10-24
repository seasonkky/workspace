package com.nexgo.xtms.eventbus;

/**
 * Created by zhouxie on 2017/7/20.
 */

public class ScreenEvent extends Event {

    private boolean isScreenOn;

    protected ScreenEvent(String tag) {
        super(tag);
    }

    public ScreenEvent(String tag, boolean isScreenOn) {
        super(tag);
        this.isScreenOn = isScreenOn;
    }

    public boolean isScreenOn() {
        return isScreenOn;
    }

    public void setScreenOn(boolean screenOn) {
        isScreenOn = screenOn;
    }
}
