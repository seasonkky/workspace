package com.nexgo.xtms.mvp.update;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.nexgo.xtms.R;
import com.nexgo.xtms.constant.CommonConstant;
import com.nexgo.xtms.constant.EventConstant;
import com.nexgo.xtms.data.entity.DownloadTaskModel;
import com.nexgo.xtms.mvp.base.BaseFragment;
import com.nexgo.xtms.mvp.detail.DetailActivity;
import com.nexgo.xtms.mvp.setting.SettingActivity;
import com.nexgo.xtms.mvp.update.adapter.UpdateAdapter;
import com.nexgo.xtms.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.bumptech.glide.util.Preconditions.checkNotNull;


/**
 * Created by zhouxie on 2017/6/29.
 */

public class UpdateFragment extends BaseFragment implements UpdateContract.View {

    @BindView(R.id.recyclerview)
    XRecyclerView recyclerview;
    @BindView(R.id.rl_update_empty)
    RelativeLayout rlUpdateEmpty;
    @BindView(R.id.rl_update_no_net)
    RelativeLayout rlUpdateNoNet;
    @BindView(R.id.pb_update_loading)
    ProgressBar pbUpdateLoading;
    @BindView(R.id.rl_update_loading)
    RelativeLayout rlUpdateLoading;
    @BindView(R.id.btn_empty_update)
    Button btnEmptyUpdate;
    @BindView(R.id.btn_no_net_update)
    Button btnNoNetUpdate;
    @BindView(R.id.tv_net_error)
    TextView tvNetError;

    public static final String TAG = UpdateFragment.class.getSimpleName();
    private UpdateContract.Presenter mPresenter;
    private UpdateAdapter updateAdapter;
    private boolean isRefreshing;

    public UpdateFragment() {
        // Requires empty public constructor
    }

    public static UpdateFragment newInstance() {
        return new UpdateFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_update;
    }

    @Override
    public void initView(View view) {

        LogUtil.d(TAG, " UpdateFragment initView ");
        setHasOptionsMenu(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(updateAdapter = new UpdateAdapter(getActivity(), new ArrayList<DownloadTaskModel>()));
        updateAdapter.setOnItemClickListener(onItemClickListener);
        recyclerview.setLoadingListener(loadingListener);
        recyclerview.setLoadingMoreEnabled(false);


    }

    private UpdateAdapter.OnItemClickListener onItemClickListener = new UpdateAdapter.OnItemClickListener() {
        @Override
        public void onItemLongClick(int position) {

        }

        @Override
        public void onItemClick(DownloadTaskModel downloadTaskModel, int position) {
//            LogUtil.d(TAG, " onItemClick = " + position + " downloadTaskModel = " +downloadTaskModel.toString());
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(CommonConstant.DETAIL_POSITION_DATA, position);
            intent.putExtra(CommonConstant.DETAIL_DATA, downloadTaskModel);
            getActivity().startActivity(intent);
        }
    };

    private XRecyclerView.LoadingListener loadingListener = new XRecyclerView.LoadingListener() {
        @Override
        public void onRefresh() {
            isRefreshing = true;
            mPresenter.loadData();
        }

        @Override
        public void onLoadMore() {

        }
    };

    @Override
    public void setPresenter(UpdateContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_update, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_download:
//                startActivity(UpdateActivity.newInstance(getActivity()));
//                break;
//            case R.id.action_log:
//                ToastUtil.ShortToast("action_log");
//                break;
            case R.id.action_setting:
                startActivity(SettingActivity.newInstance(getActivity()));
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.restart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void refreshRecyclerView(List<DownloadTaskModel> downloadTaskModelList) {
        isRefreshing = true;
        if (downloadTaskModelList == null || downloadTaskModelList.size() == 0) {
            empty();
        } else {
            updateAdapter.refreshList(downloadTaskModelList);
        }
        isRefreshing = false;
    }

    @Override
    public boolean isRefreshing() {
        return isRefreshing;
    }

    @Override
    public void empty() {
        isRefreshing = false;
        recyclerview.refreshComplete();
        recyclerview.setVisibility(View.GONE);
        rlUpdateEmpty.setVisibility(View.VISIBLE);
        rlUpdateLoading.setVisibility(View.GONE);
        rlUpdateNoNet.setVisibility(View.GONE);
    }

    @Override
    public void error(String error) {
        isRefreshing = false;
        recyclerview.refreshComplete();
        recyclerview.setVisibility(View.GONE);
        rlUpdateEmpty.setVisibility(View.GONE);
        rlUpdateLoading.setVisibility(View.GONE);
        rlUpdateNoNet.setVisibility(View.VISIBLE);
        LogUtil.test(error);
        if (error.equals(EventConstant.REFRESH_ERROR)) {
            tvNetError.setText(R.string.net_error);
        } else if (error.equals(EventConstant.NET_4G_ERROR)) {
            tvNetError.setText(R.string.net_4G_error);
        }
    }

    @Override
    public void loading() {
        isRefreshing = true;
        recyclerview.setVisibility(View.GONE);
        rlUpdateEmpty.setVisibility(View.GONE);
        rlUpdateLoading.setVisibility(View.VISIBLE);
        rlUpdateNoNet.setVisibility(View.GONE);
    }

    @Override
    public void dataLoadSuccess() {
        isRefreshing = false;
        recyclerview.refreshComplete();
        recyclerview.setVisibility(View.VISIBLE);
        rlUpdateEmpty.setVisibility(View.GONE);
        rlUpdateLoading.setVisibility(View.GONE);
        rlUpdateNoNet.setVisibility(View.GONE);
    }

    @OnClick({R.id.btn_empty_update, R.id.btn_no_net_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_empty_update:
                loading();
                mPresenter.loadData();
                break;
            case R.id.btn_no_net_update:
                loading();
                mPresenter.loadData();
                break;
        }
    }
}