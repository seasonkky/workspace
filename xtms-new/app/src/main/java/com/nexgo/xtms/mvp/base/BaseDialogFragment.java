package com.nexgo.xtms.mvp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxDialogFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhouxie on 2017/6/29.
 */

public abstract class BaseDialogFragment extends RxDialogFragment{
    private Unbinder bind;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(),container,false);
    }

    public abstract int getLayoutId();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = ButterKnife.bind(this, view);
        initView(view);
    }

    public abstract void initView(View view);

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        bind.unbind();
    }

    public void setTitle(int ResID){
        getDialog().setTitle(ResID);
    }
}
