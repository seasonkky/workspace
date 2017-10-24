package com.nexgo.xtms.mvp.server;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nexgo.xtms.R;
import com.nexgo.xtms.data.entity.ServerModel;
import com.nexgo.xtms.mvp.base.BaseDialogFragment;
import com.nexgo.xtms.mvp.server.adapter.ServerAdapter;
import com.nexgo.xtms.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhouxie on 2017/6/29.
 */

public class ServerFragment extends BaseDialogFragment implements ServerContract.View {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.rl_dialog_head)
    RelativeLayout rlDialogHead;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.pb_server_loading)
    ProgressBar pbServerLoading;
    private ServerContract.Presenter presenter;
    private ServerAdapter adapter;

    public static final String TAG = ServerFragment.class.getSimpleName();

    @Override
    public int getLayoutId() {
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return R.layout.fragment_server;
    }

    @Override
    public void initView(View view) {
        presenter.loadData();
//        tvTitle.setText(getString(R.string.server_title));
        setTitle(R.string.server_title);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setHasFixedSize(true);
        adapter = new ServerAdapter(getContext(), new ArrayList<>());
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new ServerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                presenter.onItemClick(position);
            }


            @Override
            public void onItemLongClick(int position) {
                presenter.onItemLongClick(position);
            }
        });

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUpgradeServer();
            }
        });

        presenter.loadData();
    }

    public static ServerFragment newInstance() {
        return new ServerFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.sendServerSettingUpdateEvent();
        presenter.destroy();
    }

    public void addUpgradeServer() {
        Context context = getContext();
        View rootView = LayoutInflater.from(context).inflate(R.layout.dialog_server_edittext, null);
        final EditText et_add_country = (EditText) rootView.findViewById(R.id.et_add_country);
        final EditText et_add_domain_url = (EditText) rootView.findViewById(R.id.et_add_domain_url);
        final EditText et_add_ip_url = (EditText) rootView.findViewById(R.id.et_add_ip_url);
        final EditText et_add_company = (EditText) rootView.findViewById(R.id.et_add_company);

        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(context.getString(R.string.add_new_server)).setIcon(
                android.R.drawable.ic_dialog_info).setView(
                rootView);
        builder.setCancelable(false);
        builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if ("".equals(et_add_country.getText().toString()) || "".equals(et_add_company.getText().toString()) || ("".equals(et_add_domain_url.getText().toString()) && "".equals(et_add_ip_url.getText().toString()))) {
                    ToastUtil.ShortToast(context.getString(R.string.empty_tip));
                    return;
                }
                ServerModel serverModel = new ServerModel(null, et_add_company.getText().toString().trim(), et_add_country.getText().toString().trim(), null, et_add_domain_url.getText().toString().trim(), et_add_ip_url.getText().toString().trim(), true);
                presenter.onAddServerConfirm(serverModel);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(context.getString(R.string.cancel), null);
        builder.show();

    }

    @Override
    public void refreshListOnUIThread(List<ServerModel> serverModelList) {
        if (!isAdded())
            return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.refreshList(serverModelList);
            }
        });
    }

    @Override
    public void setLoading(boolean isLoading) {
        if (!isAdded())
            return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerview.setVisibility(isLoading ? View.GONE : View.VISIBLE);
                pbServerLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });

    }

    @Override
    public void disMiss() {
        if (!isAdded())
            return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        });
    }

    @Override
    public void delete(final int position) {
        Context context = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.confirm_delete));
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.onDeleteServerConfirm(position);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void setPresenter(ServerContract.Presenter presenter) {
        this.presenter = presenter;
    }

}
