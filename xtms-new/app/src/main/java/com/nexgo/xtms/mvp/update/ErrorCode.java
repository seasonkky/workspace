package com.nexgo.xtms.mvp.update;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fangqiang_001 on 2017/2/9.
 */

public class ErrorCode {
    private int code;
    private String msg;
    public ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Map<String, ErrorCode> ERROMAP = new HashMap<String, ErrorCode>();
    static {
        ERROMAP.put("OK", new ErrorCode(0, "成功"));

        ERROMAP.put("unknow", new ErrorCode(1, "未知错误"));
    }

    public static final int CUSTOMCODE = -1;  // -1为自定义错误

    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return ("code->"+this.code+", msg->"+this.msg);
    }

}
