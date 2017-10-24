package com.nexgo.xtms.eventbus;

/**
 * Created by zhouxie on 2017/7/3.
 */

public class Event {
    protected final String tag;

    protected Event(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
