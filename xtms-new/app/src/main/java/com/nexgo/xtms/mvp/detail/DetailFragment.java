package com.nexgo.xtms.mvp.detail;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.nexgo.xtms.R;
import com.nexgo.xtms.data.entity.DownloadTaskModel;
import com.nexgo.xtms.mvp.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

/**
 * Created by zhouxie on 2017/7/17.
 */

public class DetailFragment extends BaseFragment implements DetailContract.View {
    public static final String TAG = DetailFragment.class.getSimpleName();

    @BindView(R.id.tv_app_name)
    TextView tvAppName;
    @BindView(R.id.tv_app_info)
    TextView tvAppInfo;
    @BindView(R.id.btn_update)
    Button btnUpdate;
    @BindView(R.id.iv_app_icon)
    ImageView ivAppIcon;
    @BindView(R.id.tv_app_version)
    TextView tvAppVersion;
    private DetailContract.Presenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_detail;
    }

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }


    @Override
    public void initView(View view) {
        mPresenter.start();
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void reFreshView(DownloadTaskModel downloadTaskModel) {
        switch (downloadTaskModel.getStatus()) {
            case STATUS_NOT_DOWNLOAD:
                loadAppIcon(getContext(), downloadTaskModel,ivAppIcon);
                tvAppName.setText(downloadTaskModel.getName());
                tvAppVersion.setText(downloadTaskModel.getVersion());
                tvAppInfo.setText(downloadTaskModel.getDesc());
                btnUpdate.setText(getActivity().getString(R.string.update));
                btnUpdate.setEnabled(true);
                break;
            case STATUS_START:
                loadAppIcon(getContext(), downloadTaskModel,ivAppIcon);
                tvAppName.setText(downloadTaskModel.getName());
                tvAppVersion.setText(downloadTaskModel.getVersion());
                tvAppInfo.setText(downloadTaskModel.getDesc());
                btnUpdate.setText(getActivity().getString(R.string.connectting));
                btnUpdate.setEnabled(false);
                break;
            case STATUS_CONNECTTED:
                loadAppIcon(getContext(), downloadTaskModel,ivAppIcon);
                tvAppName.setText(downloadTaskModel.getName());
                tvAppInfo.setText(downloadTaskModel.getDesc());
                tvAppVersion.setText(downloadTaskModel.getVersion());
                btnUpdate.setText(getActivity().getString(R.string.connectted));
                btnUpdate.setEnabled(false);
                break;
            case STATUS_CONNECT_ERROR:
                loadAppIcon(getContext(), downloadTaskModel,ivAppIcon);
                tvAppName.setText(downloadTaskModel.getName());
                tvAppInfo.setText(downloadTaskModel.getDesc());
                tvAppVersion.setText(downloadTaskModel.getVersion());
                btnUpdate.setText(getActivity().getString(R.string.connect_error));
                btnUpdate.setEnabled(true);
                break;
            case STATUS_DOWNLOADING:
                loadAppIcon(getContext(), downloadTaskModel,ivAppIcon);
                tvAppName.setText(downloadTaskModel.getName());
                tvAppInfo.setText(downloadTaskModel.getDesc());
                tvAppVersion.setText(downloadTaskModel.getVersion());
                btnUpdate.setText(getActivity().getString(R.string.downloading) + "  " + downloadTaskModel.getProgress() + " %");
                btnUpdate.setEnabled(true);
                break;
            case STATUS_PAUSED:
                loadAppIcon(getContext(), downloadTaskModel,ivAppIcon);
                tvAppName.setText(downloadTaskModel.getName());
                tvAppInfo.setText(downloadTaskModel.getDesc());
                tvAppVersion.setText(downloadTaskModel.getVersion());
                btnUpdate.setText(getActivity().getString(R.string.continue_));
                btnUpdate.setEnabled(true);
                break;
            case STATUS_CANCELED:
                loadAppIcon(getContext(), downloadTaskModel,ivAppIcon);
                tvAppName.setText(downloadTaskModel.getName());
                tvAppVersion.setText(downloadTaskModel.getVersion());
                tvAppInfo.setText(downloadTaskModel.getDesc());
                btnUpdate.setText(getActivity().getString(R.string.update));
                btnUpdate.setEnabled(true);
                break;
            case STATUS_DOWNLOAD_ERROR:
                loadAppIcon(getContext(), downloadTaskModel,ivAppIcon);
                tvAppName.setText(downloadTaskModel.getName());
                tvAppInfo.setText(downloadTaskModel.getDesc());
                tvAppVersion.setText(downloadTaskModel.getVersion());
                btnUpdate.setText(getActivity().getString(R.string.dowload_erro));
                btnUpdate.setEnabled(true);
                break;
            case STATUS_COMPLETE:
                loadAppIcon(getContext(), downloadTaskModel,ivAppIcon);
                tvAppName.setText(downloadTaskModel.getName());
                tvAppInfo.setText(downloadTaskModel.getDesc());
                tvAppVersion.setText(downloadTaskModel.getVersion());
                btnUpdate.setText(getActivity().getString(R.string.install));
                btnUpdate.setEnabled(true);
                break;
            case STATUS_INSTALLING:
                loadAppIcon(getContext(), downloadTaskModel,ivAppIcon);
                tvAppName.setText(downloadTaskModel.getName());
                tvAppInfo.setText(downloadTaskModel.getDesc());
                tvAppVersion.setText(downloadTaskModel.getVersion());
                btnUpdate.setText(getActivity().getString(R.string.installing));
                btnUpdate.setEnabled(false);
                break;
            case STATUS_CHECKING:
                loadAppIcon(getContext(), downloadTaskModel,ivAppIcon);
                tvAppName.setText(downloadTaskModel.getName());
                tvAppInfo.setText(downloadTaskModel.getDesc());
                tvAppVersion.setText(downloadTaskModel.getVersion());
                btnUpdate.setText(getActivity().getString(R.string.checking));
                btnUpdate.setEnabled(false);
                break;
            case STATUS_INSTALLED:
                loadAppIcon(getContext(), downloadTaskModel,ivAppIcon);
                tvAppName.setText(downloadTaskModel.getName());
                tvAppVersion.setText(downloadTaskModel.getVersion());
                tvAppInfo.setText(downloadTaskModel.getDesc());
                btnUpdate.setText(getActivity().getString(R.string.install_finished));
                btnUpdate.setEnabled(false);
                break;
            case STATUS_CHECKING_ERROR:
                loadAppIcon(getContext(), downloadTaskModel,ivAppIcon);
                tvAppName.setText(downloadTaskModel.getName());
                tvAppInfo.setText(downloadTaskModel.getDesc());
                tvAppVersion.setText(downloadTaskModel.getVersion());
                btnUpdate.setText(getActivity().getString(R.string.md5check_erro));
                btnUpdate.setEnabled(true);
            case STATUS_INSTALL_ERROR:
                loadAppIcon(getContext(), downloadTaskModel,ivAppIcon);
                tvAppName.setText(downloadTaskModel.getName());
                tvAppInfo.setText(downloadTaskModel.getDesc());
                tvAppVersion.setText(downloadTaskModel.getVersion());
                btnUpdate.setText(getActivity().getString(R.string.install_fail));
                btnUpdate.setEnabled(true);
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

    @OnClick(R.id.btn_update)
    public void onViewClicked() {
        mPresenter.click();
    }

}
