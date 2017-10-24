package com.nexgo.xtms.eventbus;

/**
 * Created by zhouxie on 2017/7/3.
 *
 * 更新请求的事件
 */

public class UpdateRequestEvent extends Event {

    public UpdateRequestEvent(String tag) {
        super(tag);
    }

    public UpdateRequestEvent(String tag, long delayTime) {
        super(tag);
        this.delayTime = delayTime;
    }

    private long delayTime = 0L;

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }
}
