package com.eventbus.sample;

/**
 * Created by guoxiaodong on 2020/9/3 16:07
 */
class Event {
    private String msg;

    public Event(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
