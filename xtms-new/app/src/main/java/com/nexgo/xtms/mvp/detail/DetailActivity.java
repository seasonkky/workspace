package com.nexgo.xtms.mvp.detail;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.nexgo.xtms.R;
import com.nexgo.xtms.constant.CommonConstant;
import com.nexgo.xtms.data.entity.DownloadTaskModel;
import com.nexgo.xtms.mvp.base.BaseActivity;
import com.nexgo.xtms.util.ActivityUtils;

/**
 * Created by zhouxie on 2017/7/13.
 */

public class DetailActivity extends BaseActivity {

    private DetailPresenter detailPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        DetailFragment detailFragment =
                (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (detailFragment == null) {
            // Create the fragment
            detailFragment = DetailFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), detailFragment, R.id.contentFrame);
        }
        DownloadTaskModel downloadTaskModel = (DownloadTaskModel) getIntent().getSerializableExtra(CommonConstant.DETAIL_DATA);
        int position = getIntent().getIntExtra(CommonConstant.DETAIL_POSITION_DATA, 0);
        detailPresenter = new DetailPresenter(detailFragment,position, downloadTaskModel);
    }

    @Override
    public void initActionbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.detail);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

}
