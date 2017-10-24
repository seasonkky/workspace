package com.nexgo.xtms.mvp.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.nexgo.xtms.R;
import com.nexgo.xtms.eventbus.DataEvent;
import com.nexgo.xtms.mvp.base.BaseFragment;
import com.nexgo.xtms.mvp.setting.SettingActivity;
import com.nexgo.xtms.util.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.bumptech.glide.util.Preconditions.checkNotNull;


/**
 * Created by zhouxie on 2017/6/29.
 */

public class HomeFragment extends BaseFragment implements HomeContract.View {

    private HomeContract.Presenter mPresenter;

    public HomeFragment() {
        // Requires empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_update;
    }

    @Override
    public void initView(View view) {
        setHasOptionsMenu(true);
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        mPresenter.destroy();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataReceived(DataEvent event){
        LogUtil.test("onDataReceive");
    }

}
