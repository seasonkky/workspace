package com.nexgo.xtms.mvp.update.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.nexgo.xtms.R;
import com.nexgo.xtms.constant.EventConstant;
import com.nexgo.xtms.data.entity.DownloadTaskModel;
import com.nexgo.xtms.eventbus.DownloadRequestEvent;
import com.nexgo.xtms.util.CommonUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhouxie on 2017/6/30.
 */

public class UpdateAdapter extends RecyclerView.Adapter<UpdateAdapter.UpdateViewHolder> {

    public static final String TAG = UpdateAdapter.class.getSimpleName();
    private Context context;
    private List<DownloadTaskModel> downloadTaskModelList;

    public UpdateAdapter(Context context, List<DownloadTaskModel> downloadTaskModelList) {
        this.context = context;
        this.downloadTaskModelList = downloadTaskModelList;
    }

    public void refreshList(List<DownloadTaskModel> downloadTaskModelList) {
        this.downloadTaskModelList = downloadTaskModelList;
//        LogUtil.test("refreshList size = " + downloadTaskModelList.size() + "  tosString = " + downloadTaskModelList.toString());
        notifyDataSetChanged();
    }

    @Override
    public UpdateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_update_list_item, parent, false);
        return new UpdateViewHolder(root);
    }

    @Override
    public void onBindViewHolder(UpdateViewHolder holder, int position) {
        DownloadTaskModel downloadTaskModel = downloadTaskModelList.get(position);
        holder.rlListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(downloadTaskModel,position);
                }
            }
        });

        holder.rlListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemLongClick(position);
                }
                return true;
            }
        });

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateBtnAction(downloadTaskModel);
            }
        });
        switch (downloadTaskModel.getStatus()) {
            case STATUS_NOT_DOWNLOAD:
                holder.rlAppinfo.setVisibility(View.VISIBLE);
                holder.rlDownloadinfo.setVisibility(View.GONE);
                holder.tvAppFilesize.setVisibility(View.GONE);
                loadAppIcon(context, downloadTaskModel,holder.ivAppicon);
                holder.tvAppName.setText(downloadTaskModel.getName());
                holder.tvAppFilesize.setText(CommonUtil.getDataSize(downloadTaskModel.getTotalSize()));
                holder.tvAppVersion.setText(downloadTaskModel.getVersion());
                holder.btnUpdate.setText(context.getString(R.string.update));
                holder.btnUpdate.setEnabled(true);
                break;
            case STATUS_START:
                holder.rlAppinfo.setVisibility(View.VISIBLE);
                holder.rlDownloadinfo.setVisibility(View.GONE);
                holder.tvAppFilesize.setVisibility(View.VISIBLE);
                loadAppIcon(context, downloadTaskModel,holder.ivAppicon);
                holder.tvAppName.setText(downloadTaskModel.getName());
                holder.tvAppFilesize.setText(CommonUtil.getDataSize(downloadTaskModel.getTotalSize()));
                holder.tvAppVersion.setText(downloadTaskModel.getVersion());
                holder.btnUpdate.setText(context.getString(R.string.connectting));
                holder.btnUpdate.setEnabled(false);
                break;
            case STATUS_CONNECTTED:
                holder.rlAppinfo.setVisibility(View.VISIBLE);
                holder.rlDownloadinfo.setVisibility(View.GONE);
                holder.tvAppFilesize.setVisibility(View.VISIBLE);
                loadAppIcon(context, downloadTaskModel,holder.ivAppicon);
                holder.tvAppName.setText(downloadTaskModel.getName());
                holder.tvAppFilesize.setText(CommonUtil.getDataSize(downloadTaskModel.getTotalSize()));
                holder.tvAppVersion.setText(downloadTaskModel.getVersion());
                holder.btnUpdate.setText(context.getString(R.string.connectted));
                holder.btnUpdate.setEnabled(false);
                break;
            case STATUS_CONNECT_ERROR:
                holder.rlAppinfo.setVisibility(View.VISIBLE);
                holder.rlDownloadinfo.setVisibility(View.GONE);
                holder.tvAppFilesize.setVisibility(View.VISIBLE);
                loadAppIcon(context, downloadTaskModel,holder.ivAppicon);
                holder.tvAppName.setText(downloadTaskModel.getName());
                holder.tvAppFilesize.setText(CommonUtil.getDataSize(downloadTaskModel.getTotalSize()));
                holder.tvAppVersion.setText(downloadTaskModel.getVersion());
                holder.btnUpdate.setText(context.getString(R.string.connect_error));
                holder.btnUpdate.setEnabled(true);
                break;
            case STATUS_DOWNLOADING:
                holder.rlAppinfo.setVisibility(View.GONE);
                holder.rlDownloadinfo.setVisibility(View.VISIBLE);
                holder.tvAppFilesize.setVisibility(View.GONE);
                holder.tvAppName.setText(downloadTaskModel.getName());
                loadAppIcon(context, downloadTaskModel,holder.ivAppicon);
                holder.progressBar.setProgress(downloadTaskModel.getProgress());
                holder.tvAppFilesize.setText(CommonUtil.getDataSize(downloadTaskModel.getTotalSize()));
                holder.tvFileProgress.setText(CommonUtil.getDataSize(downloadTaskModel.getDownloadSize()) + "/" + CommonUtil.getDataSize(downloadTaskModel.getTotalSize()));
                holder.tvProgress.setText(downloadTaskModel.getProgress() + " %");
                holder.btnUpdate.setText(context.getString(R.string.pause));
                holder.btnUpdate.setEnabled(true);
                break;
            case STATUS_PAUSED:
                holder.rlAppinfo.setVisibility(View.GONE);
                holder.rlDownloadinfo.setVisibility(View.VISIBLE);
                holder.tvAppFilesize.setVisibility(View.GONE);
                holder.tvAppName.setText(downloadTaskModel.getName());
                loadAppIcon(context, downloadTaskModel,holder.ivAppicon);
                holder.progressBar.setProgress(downloadTaskModel.getProgress());
                holder.tvAppFilesize.setText(CommonUtil.getDataSize(downloadTaskModel.getTotalSize()));
                holder.tvFileProgress.setText(CommonUtil.getDataSize(downloadTaskModel.getDownloadSize()) + "/" + CommonUtil.getDataSize(downloadTaskModel.getTotalSize()));
                holder.tvProgress.setText(downloadTaskModel.getProgress() + " %");
                holder.btnUpdate.setText(context.getString(R.string.continue_));
                holder.btnUpdate.setEnabled(true);
                break;
            case STATUS_CANCELED:
                holder.rlAppinfo.setVisibility(View.VISIBLE);
                holder.rlDownloadinfo.setVisibility(View.GONE);
                holder.tvAppFilesize.setVisibility(View.VISIBLE);
                loadAppIcon(context, downloadTaskModel,holder.ivAppicon);
                holder.tvAppName.setText(downloadTaskModel.getName());
                holder.tvAppFilesize.setText(CommonUtil.getDataSize(downloadTaskModel.getTotalSize()));
                holder.tvAppVersion.setText(downloadTaskModel.getVersion());
                holder.btnUpdate.setText(context.getString(R.string.update));
                holder.btnUpdate.setEnabled(true);
                break;
            case STATUS_DOWNLOAD_ERROR:
                holder.rlAppinfo.setVisibility(View.VISIBLE);
                holder.rlDownloadinfo.setVisibility(View.GONE);
                holder.tvAppFilesize.setVisibility(View.VISIBLE);
                loadAppIcon(context, downloadTaskModel,holder.ivAppicon);
                holder.tvAppName.setText(downloadTaskModel.getName());
                holder.tvAppFilesize.setText(CommonUtil.getDataSize(downloadTaskModel.getTotalSize()));
                holder.tvAppVersion.setText(downloadTaskModel.getVersion());
                holder.btnUpdate.setText(context.getString(R.string.dowload_erro));
                holder.btnUpdate.setEnabled(true);
                break;
            case STATUS_COMPLETE:
                holder.rlAppinfo.setVisibility(View.VISIBLE);
                holder.rlDownloadinfo.setVisibility(View.GONE);
                holder.tvAppFilesize.setVisibility(View.VISIBLE);
                loadAppIcon(context, downloadTaskModel,holder.ivAppicon);
                holder.tvAppName.setText(downloadTaskModel.getName());
                holder.tvAppFilesize.setText(CommonUtil.getDataSize(downloadTaskModel.getTotalSize()));
                holder.tvAppVersion.setText(downloadTaskModel.getVersion());
                holder.tvAppFilesize.setText(CommonUtil.getDataSize(downloadTaskModel.getTotalSize()));
                holder.btnUpdate.setText(context.getString(R.string.install));
                holder.btnUpdate.setEnabled(true);
                break;
            case STATUS_INSTALLING:
                holder.rlAppinfo.setVisibility(View.VISIBLE);
                holder.rlDownloadinfo.setVisibility(View.GONE);
                holder.tvAppFilesize.setVisibility(View.VISIBLE);
                loadAppIcon(context, downloadTaskModel,holder.ivAppicon);
                holder.tvAppName.setText(downloadTaskModel.getName());
                holder.tvAppFilesize.setText(CommonUtil.getDataSize(downloadTaskModel.getTotalSize()));
                holder.tvAppVersion.setText(downloadTaskModel.getVersion());
                holder.tvAppFilesize.setText(CommonUtil.getDataSize(downloadTaskModel.getTotalSize()));
                holder.btnUpdate.setText(context.getString(R.string.installing));
                holder.btnUpdate.setEnabled(false);
                break;
            case STATUS_CHECKING:
                holder.rlAppinfo.setVisibility(View.VISIBLE);
                holder.rlDownloadinfo.setVisibility(View.GONE);
                holder.tvAppFilesize.setVisibility(View.VISIBLE);
                loadAppIcon(context, downloadTaskModel,holder.ivAppicon);
                holder.tvAppName.setText(downloadTaskModel.getName());
                holder.tvAppFilesize.setText(CommonUtil.getDataSize(downloadTaskModel.getTotalSize()));
                holder.tvAppVersion.setText(downloadTaskModel.getVersion());
                holder.tvAppFilesize.setText(CommonUtil.getDataSize(downloadTaskModel.getTotalSize()));
                holder.btnUpdate.setText(context.getString(R.string.checking));
                holder.btnUpdate.setEnabled(false);
                break;
            case STATUS_INSTALLED:
                holder.rlAppinfo.setVisibility(View.VISIBLE);
                holder.rlDownloadinfo.setVisibility(View.GONE);
                holder.tvAppFilesize.setVisibility(View.VISIBLE);
                loadAppIcon(context, downloadTaskModel,holder.ivAppicon);
                holder.tvAppName.setText(downloadTaskModel.getName());
                holder.tvAppFilesize.setText(CommonUtil.getDataSize(downloadTaskModel.getTotalSize()));
                holder.tvAppVersion.setText(downloadTaskModel.getVersion());
                holder.btnUpdate.setText(context.getString(R.string.install_finished));
                holder.btnUpdate.setEnabled(false);
                break;
            case STATUS_CHECKING_ERROR:
                holder.rlAppinfo.setVisibility(View.VISIBLE);
                holder.rlDownloadinfo.setVisibility(View.GONE);
                holder.tvAppFilesize.setVisibility(View.VISIBLE);
                loadAppIcon(context, downloadTaskModel,holder.ivAppicon);
                holder.tvAppName.setText(downloadTaskModel.getName());
                holder.tvAppFilesize.setText(CommonUtil.getDataSize(downloadTaskModel.getTotalSize()));
                holder.tvAppVersion.setText(downloadTaskModel.getVersion());
                holder.btnUpdate.setText(context.getString(R.string.md5check_erro));
                holder.btnUpdate.setEnabled(true);
                break;
            case STATUS_INSTALL_ERROR:
                holder.rlAppinfo.setVisibility(View.VISIBLE);
                holder.rlDownloadinfo.setVisibility(View.GONE);
                holder.tvAppFilesize.setVisibility(View.VISIBLE);
                loadAppIcon(context, downloadTaskModel,holder.ivAppicon);
                holder.tvAppName.setText(downloadTaskModel.getName());
                holder.tvAppFilesize.setText(CommonUtil.getDataSize(downloadTaskModel.getTotalSize()));
                holder.tvAppVersion.setText(downloadTaskModel.getVersion());
                holder.btnUpdate.setText(context.getString(R.string.install_fail));
                holder.btnUpdate.setEnabled(true);
                break;
        }
    }

    public void loadAppIcon(Context context, DownloadTaskModel downloadTaskModel, ImageView imageView){
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_app_icon)
                .error(R.drawable.default_app_icon)
                .priority(Priority.NORMAL)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context)
                .setDefaultRequestOptions(options)
                .load(downloadTaskModel.getImage())
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return downloadTaskModelList == null ? 0 : downloadTaskModelList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    private OnItemClickListener itemClickListener = null;

    public interface OnItemClickListener {
        void onItemLongClick(int position);

        void onItemClick(DownloadTaskModel downloadTaskModel, int position);
    }

    class UpdateViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_appicon)
        ImageView ivAppicon;
        @BindView(R.id.tv_app_name)
        TextView tvAppName;
        @BindView(R.id.tv_app_version)
        TextView tvAppVersion;
        @BindView(R.id.tv_app_filesize)
        TextView tvAppFilesize;
        @BindView(R.id.rl_appinfo)
        RelativeLayout rlAppinfo;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.tv_file_progress)
        TextView tvFileProgress;
        @BindView(R.id.tv_progress)
        TextView tvProgress;
        @BindView(R.id.rl_downloadinfo)
        RelativeLayout rlDownloadinfo;
        @BindView(R.id.fl_appinfo_container)
        RelativeLayout flAppinfoContainer;
        @BindView(R.id.btn_update)
        Button btnUpdate;
        @BindView(R.id.rl_list_item)
        RelativeLayout rlListItem;

        UpdateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void onUpdateBtnAction(DownloadTaskModel downloadTaskModel) {
        switch (downloadTaskModel.getStatus()) {
            case STATUS_NOT_DOWNLOAD:
                EventBus.getDefault().post(new DownloadRequestEvent(EventConstant.DOWNLOAD_START_EVENT, downloadTaskModel, null));
                break;
            case STATUS_START:
                EventBus.getDefault().post(new DownloadRequestEvent(EventConstant.DOWNLOAD_PAUSE_EVENT, downloadTaskModel, null));
                break;
            case STATUS_CONNECTING:
                break;
            case STATUS_CONNECTTED:
                break;
            case STATUS_CONNECT_ERROR:
                EventBus.getDefault().post(new DownloadRequestEvent(EventConstant.DOWNLOAD_START_EVENT, downloadTaskModel, null));
                break;
            case STATUS_DOWNLOADING:
                EventBus.getDefault().post(new DownloadRequestEvent(EventConstant.DOWNLOAD_PAUSE_EVENT, downloadTaskModel, null));
                break;
            case STATUS_PAUSED:
                EventBus.getDefault().post(new DownloadRequestEvent(EventConstant.DOWNLOAD_START_EVENT, downloadTaskModel, null));
                break;
            case STATUS_CANCELED:
                EventBus.getDefault().post(new DownloadRequestEvent(EventConstant.DOWNLOAD_START_EVENT, downloadTaskModel, null));
                break;
            case STATUS_DOWNLOAD_ERROR:
                EventBus.getDefault().post(new DownloadRequestEvent(EventConstant.DOWNLOAD_START_EVENT, downloadTaskModel, null));
                break;
            case STATUS_COMPLETE:
                EventBus.getDefault().post(new DownloadRequestEvent(EventConstant.DOWNLOAD_INSTALL_EVENT, downloadTaskModel, null));
                break;
            case STATUS_INSTALLED:
                break;
            case STATUS_CHECKING:
                break;
            case STATUS_INSTALL_ERROR:
                EventBus.getDefault().post(new DownloadRequestEvent(EventConstant.DOWNLOAD_INSTALL_EVENT, downloadTaskModel, null));
                break;
        }

    }
}
