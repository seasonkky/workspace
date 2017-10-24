package com.nexgo.xtms.mvp.server.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nexgo.xtms.R;
import com.nexgo.xtms.constant.SPConstant;
import com.nexgo.xtms.data.entity.ServerModel;
import com.nexgo.xtms.util.CountryUtil;
import com.nexgo.xtms.util.LogUtil;
import com.nexgo.xtms.util.PreferenceUtil;

import java.util.List;

/**
 * Created by zhouxie on 2017/7/5.
 */

public class ServerAdapter extends RecyclerView.Adapter<ServerAdapter.ServerViewHolder> {

    public static final String TAG = ServerAdapter.class.getSimpleName();

    private Context context;
    private List<ServerModel> serverModelList;

    public ServerAdapter(Context context, List<ServerModel> serverModelList) {
        this.context = context;
        this.serverModelList = serverModelList;
    }

    public void refreshList(List<ServerModel> serverModelList) {
        this.serverModelList = serverModelList;
        notifyDataSetChanged();
    }


    @Override
    public ServerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_recyclerview_item, parent, false);
        return new ServerViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ServerViewHolder holder, final int position) {
        if (serverModelList == null)
            return;
        // 点击事件
        if (itemClickListener != null) {
            holder.rl_rootview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(position);
                }
            });
            holder.rl_rootview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemClickListener.onItemLongClick(position);
                    return true;
                }
            });
        }

        String countryName = serverModelList.get(position).getCountryname() == null ? CountryUtil.getCountryNameFromCountryCode(context, serverModelList.get(position).getCountryCode()) : serverModelList.get(position).getCountryname();
        String companyName = serverModelList.get(position).getCompanyName() == null ? "" : serverModelList.get(position).getCompanyName();
        holder.item_tv_countryname.setText(countryName == null ? companyName : (countryName + "   " + companyName));

        String url = "";
        if (serverModelList.get(position).getDomain() == null || serverModelList.get(position).getDomain().equals("")) {
            if (serverModelList.get(position).getIp() == null || serverModelList.get(position).getIp().equals("")) {

            } else {
                url = serverModelList.get(position).getIp();
            }
        } else {
            url = serverModelList.get(position).getDomain();
        }
        holder.item_tv_url.setText(url);
        holder.item_tv_url.setVisibility(serverModelList.get(position).getIsShowIp() ? View.VISIBLE : View.GONE);

        // 获取设置的服务器地址
        long serverId = PreferenceUtil.getLong(SPConstant.KEY_SERVER, 0L);

        LogUtil.d(TAG, " serverModelList.get(position).getId() = " + serverModelList.get(position).getId() + " serverId = " + serverId);
        holder.item_iv_gou.setVisibility(serverModelList.get(position).getId() == serverId ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return serverModelList == null ? 0 : serverModelList.size();
    }


    class ServerViewHolder extends RecyclerView.ViewHolder {
        TextView item_tv_countryname;
        TextView item_tv_url;
        ImageView item_iv_gou;
        RelativeLayout rl_rootview;

        ServerViewHolder(View itemView) {
            super(itemView);
            rl_rootview = (RelativeLayout) itemView.findViewById(R.id.rl_rootview);
            item_tv_countryname = (TextView) itemView.findViewById(R.id.item_tv_countryname);
            item_tv_url = (TextView) itemView.findViewById(R.id.item_tv_url);
            item_iv_gou = (ImageView) itemView.findViewById(R.id.item_iv_gou);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    private OnItemClickListener itemClickListener = null;

    public interface OnItemClickListener {
        void onItemLongClick(int position);

        void onItemClick(int position);
    }
}

