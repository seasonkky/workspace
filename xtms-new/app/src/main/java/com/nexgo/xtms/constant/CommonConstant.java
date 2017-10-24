package com.nexgo.xtms.constant;

import android.os.Environment;

/**
 * Created by zhouxie on 2017/7/3.
 */

public interface CommonConstant {
    String UPDATE_SERVICE_ACTION = "action_start_update_service";

    int SDCARDMIN = 400;  // SD卡最小空闲容量

    String DETAIL_DATA = "detail_data";
    String DETAIL_POSITION_DATA = "detail_position_data";
    // SD卡路径
    String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    // 设置存储文件夹
    String BASE_PATH = "xtms";
    // 基础存储文件夹
    String BASE_SAVE_PATH = SD_PATH + BASE_PATH;
    // 参数文件基础文件夹
    String BASE_PARAM_PATH = "xtms_param";
    String BASE_PARAM_SAVE_PATH = SD_PATH + BASE_PARAM_PATH;
    // 基础参数配置文件名称
    String BASE_PARAM_LIST_FILENAME = "param.json";
    // 百度地图AK
    String BAIDU_AK = "Hp2CijOTSHoxkaq26BkXPogyGZ7fqnHI";

}
