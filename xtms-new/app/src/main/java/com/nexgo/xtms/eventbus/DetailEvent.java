package com.nexgo.xtms.eventbus;

import com.nexgo.xtms.data.entity.DownloadTaskModel;

import java.util.List;

/**
 * Created by zhouxie on 2017/7/17.
 */

public class DetailEvent extends Event {

    private List<DownloadTaskModel> downloadTaskModelList;
    private int position;
    private DownloadTaskModel downloadTaskModel;

    public DetailEvent(String tag, int position) {
        super(tag);
        this.position = position;
    }

    public DetailEvent(String tag, DownloadTaskModel downloadTaskModel) {
        super(tag);
        this.downloadTaskModel = downloadTaskModel;
    }

    public DetailEvent(String tag, List<DownloadTaskModel> downloadTaskModelList) {
        super(tag);
        this.downloadTaskModelList = downloadTaskModelList;
    }

    public List<DownloadTaskModel> getDownloadTaskModelList() {
        return downloadTaskModelList;
    }

    public void setDownloadTaskModelList(List<DownloadTaskModel> downloadTaskModelList) {
        this.downloadTaskModelList = downloadTaskModelList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public DownloadTaskModel getDownloadTaskModel() {
        return downloadTaskModel;
    }

    public void setDownloadTaskModel(DownloadTaskModel downloadTaskModel) {
        this.downloadTaskModel = downloadTaskModel;
    }
}
