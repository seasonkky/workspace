package com.nexgo.xtms.eventbus;

import com.nexgo.xtms.data.entity.DownloadTaskModel;

import java.util.List;

/**
 * Created by zhouxie on 2017/7/3.
 */

public class DownloadRequestEvent extends Event {

    private DownloadTaskModel downloadTaskModel;

    private List<DownloadTaskModel> downloadTaskModelList;

    public DownloadRequestEvent(String tag, DownloadTaskModel downloadTaskModel, List<DownloadTaskModel> downloadTaskModelList) {
        super(tag);
        this.downloadTaskModel = downloadTaskModel;
    }

    public DownloadTaskModel getDownloadTaskModel() {
        return downloadTaskModel;
    }

    public List<DownloadTaskModel> getDownloadTaskModelList() {
        return downloadTaskModelList;
    }
}
