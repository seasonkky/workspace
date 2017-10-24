package com.nexgo.xtms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志打印工具类
 */
public class LogUtil {

    private static final String TAG = "LogUtil";

    private static boolean isShow = true;
    private static Logger log;


    /**
     * 初始化日志
     */
    public static void init() {
        log = LoggerFactory.getLogger(LogUtil.class);
    }

    /**
     * 日志打印
     *
     * @return
     */

    public static boolean isShow() {

        return isShow;
    }

    public static void setShow(boolean show) {

        isShow = show;
    }

    public static void i(String tag, String msg) {

        if (isShow) {
            log.debug(TAG + "_" + tag + "_" + msg);
        }

    }

    public static void w(String tag, String msg) {
        if (isShow) {
            log.debug(TAG + "_" + tag + "_" + msg);
        }
    }

    public static void d(String tag, String msg) {

        if (isShow) {
            log.debug(TAG + "_" + tag + "_" + msg);
        }
    }

    public static void e(String tag, String msg) {

        if (isShow) {
            log.debug(TAG + "_" + tag + "_" + msg);
        }
    }

    public static void all(String msg) {

        if (isShow) {
            log.debug(TAG + "_" + "_" + msg);
        }
    }

    public static void i(String msg) {

        if (isShow) {
            log.debug(TAG + "_" + "_" + msg);
        }
    }

    public static void w(String msg) {

        if (isShow) {
            log.debug(TAG + "_" + "_" + msg);
        }
    }

    public static void e(String msg) {
        if (isShow) {
            log.debug(TAG + "_" + "_" + msg);
        }
    }

    public static void v(String msg) {
        e(msg);
    }

    public static void d(String msg) {
        v(msg);
    }

    public static void test(String msg) {
        if (isShow) {
            log.debug(TAG + "_" + "test_" + msg);
        }
    }
}

