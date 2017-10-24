package com.nexgo.xtms.task.update.file;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nexgo.xtms.data.entity.DownloadTaskModel;
import com.nexgo.xtms.data.entity.FileModel;
import com.nexgo.xtms.util.FileUtils;
import com.nexgo.xtms.util.LogUtil;

import java.util.List;

import static com.nexgo.xtms.constant.CommonConstant.BASE_PARAM_LIST_FILENAME;
import static com.nexgo.xtms.constant.CommonConstant.BASE_PARAM_SAVE_PATH;

/**
 * Created by zhouxie on 2017/10/18.
 */

public class FileUpdateTask {
    public static final String TAG = FileUpdateTask.class.getSimpleName();
    private DownloadTaskModel downloadTaskModel;
    private Gson gson = new Gson();

    public FileUpdateTask() {
    }

    public FileUpdateTask(DownloadTaskModel downloadTaskModel) {
        this.downloadTaskModel = downloadTaskModel;
    }

    public void updateFileManifest() {
        List<FileModel> fileModelList;
        List<FileModel> allFileModelList;
        try {

            String json = FileUtils.readFile(BASE_PARAM_SAVE_PATH + "/" + downloadTaskModel.getPackageName() + "/" + BASE_PARAM_LIST_FILENAME, "utf-8").toString();
            fileModelList = gson.fromJson(json, new TypeToken<List<FileModel>>() {
            }.getType());

            boolean ifAdd = false;
            if (fileModelList != null) {
                for (FileModel fileModel : fileModelList) {
                    if (downloadTaskModel.getName().equals(fileModel.getFilename())) {
                        // 文件名相同则直接覆盖
                        fileModel.setVersioncode(downloadTaskModel.getVersioncode());
                        fileModel.setFilesize(downloadTaskModel.getFilesize());
                        fileModel.setSavepath(downloadTaskModel.getSavePath());
                        fileModel.setFilelogictype(downloadTaskModel.getFilelogictype());
                        fileModel.setFileowner(downloadTaskModel.getFileowner());
                        break;
                    } else {
                        // 文件名不同则直接添加
                        ifAdd = true;
                    }
                }
            }
            if (ifAdd) {
                FileModel fileModel = new FileModel();
                fileModel.setVersioncode(downloadTaskModel.getVersioncode());
                fileModel.setFilesize(downloadTaskModel.getFilesize());
                fileModel.setSavepath(downloadTaskModel.getSavePath());
                fileModel.setFilelogictype(downloadTaskModel.getFilelogictype());
                fileModel.setFileowner(downloadTaskModel.getFileowner());
                fileModelList.add(fileModel);
            }

            // 首先更新packge下的清单文件
            String rjson = gson.toJson(fileModelList);
            FileUtils.writeFile(BASE_PARAM_SAVE_PATH + "/" + downloadTaskModel.getPackageName() + "/" + BASE_PARAM_LIST_FILENAME, "utf-8");

        } catch (Exception e) {
            LogUtil.d(TAG, "file manifest update error");
            return;
        } finally {

        }
    }
}
