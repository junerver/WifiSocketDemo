package com.xxzx.wifisocketdemo;

/**
 * Created by Junerver on 2016/9/19.
 */
public class PostMsgEvent {
    private String mMessage;

    public PostMsgEvent(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    @Override
    public String toString() {
        return "发送的数据为"+mMessage;
    }
}
