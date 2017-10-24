package com.nexgo.xtms.eventbus;

import com.nexgo.xtms.data.entity.DownloadTaskModel;

import java.util.List;

/**
 * Created by zhouxie on 2017/7/3.
 */

public class UpdateEvent extends Event {

    private List<DownloadTaskModel> downloadTaskModelList;

    public UpdateEvent(String tag, List<DownloadTaskModel> downloadTaskModelList) {
        super(tag);
        this.downloadTaskModelList = downloadTaskModelList;
    }

    public UpdateEvent(String tag) {
        super(tag);
    }

    public List<DownloadTaskModel> getDownloadTaskModelList() {
        return downloadTaskModelList;
    }

    public void setDownloadTaskModelList(List<DownloadTaskModel> downloadTaskModelList) {
        this.downloadTaskModelList = downloadTaskModelList;
    }

}
