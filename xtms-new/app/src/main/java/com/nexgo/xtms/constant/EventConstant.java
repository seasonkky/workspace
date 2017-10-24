package com.nexgo.xtms.constant;

/**
 * Created by zhouxie on 2017/7/3.
 */

public interface EventConstant {
    // UpdateEvent
    String UPDATE_EVENT_UPDATEFRAGMENT = "update_event_updatefragment";
    String UPDATE_EVENT_UPDATEFRAGMENT_SPECIAL = "update_event_updatefragment_special";
    String REFRESH_COMPLETE = "refresh_complete";
    String REFRESH_ERROR = "refresh_error";
    String NET_4G_ERROR = "net_4g_error";

    // UpdateRequestEvent
    String UPDATE_REQUEST_EVENT = "update_request_event";

    // DownloadRequestEvent
    String DOWNLOAD_START_EVENT = "download_request_event";
    String DOWNLOAD_PAUSE_EVENT = "download_pause_event";
    String DOWNLOAD_INSTALL_EVENT ="download_install_event";

    // ServerEvent
    String SERVER_SETTING_EVENT = "server_setting_event";

    // DetailEvent
    String DETAIL_REQUEST_EVENT = "detail_request_event";
    String DETAIL_DATA_EVENT = "detail_data_event";

    // BroadcastEvent
    String BROADCAST_RESTART_DOWNLOAD = "broadcast_restart_download";
    String BROADCAST_REQUEST_DOWNLOAD = "broadcast_request_download";
    String BROADCAST_POWER_CHANGE = "broadcast_power_change";
    String BROADCAST_UPDATE_DEVICE_INF = "broadcast_update_device_inf";

    // RebootEvent
    String REBOOT_SETTING_EVENT = "reboot_setting_event";
    String REBOOT_REQUEST_EVENT="reboot_request_event";
    String REBOOT_CANCEL_EVENT="reboot_cancel_event";

    // ScreenEvent
    String SCREEN_EVENT = "screen_event";
}
