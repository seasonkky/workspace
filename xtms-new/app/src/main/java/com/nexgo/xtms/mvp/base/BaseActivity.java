package com.nexgo.xtms.mvp.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.nexgo.xtms.constant.SPConstant;
import com.nexgo.xtms.util.LogUtil;
import com.nexgo.xtms.util.PreferenceUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends RxAppCompatActivity {

    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局内容
        setContentView(getLayoutId());
        //初始化黄油刀控件绑定框架
        bind = ButterKnife.bind(this);
        //初始化控件
        initViews(savedInstanceState);
        //初始化ToolBar
        initActionbar();
    }

    public abstract int getLayoutId();

    public abstract void initViews(Bundle savedInstanceState);

    public abstract void initActionbar();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeLanguage(String language,Class class1) {
        // 获取语言数据信息
        if (language != null && language.length() != 0) {
            PreferenceUtil.put(SPConstant.START_LANGUAGE,language);
            Intent newIntent = new Intent(this, class1);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(newIntent);
            LogUtil.test(" changeLanguage : " +language);
            // 杀掉进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }
}
