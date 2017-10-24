package com.nexgo.xtms.mvp.setting;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nexgo.xtms.R;
import com.nexgo.xtms.activity.UpdateActivity;
import com.nexgo.xtms.constant.SPConstant;
import com.nexgo.xtms.data.entity.LanguageModel;
import com.nexgo.xtms.mvp.base.BaseDialogFragment;
import com.nexgo.xtms.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

/**
 * Created by zhouxie on 2017/9/26.
 */

public class LanguageSettingFragment extends BaseDialogFragment {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private List<LanguageModel> languageModelList;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_language;
    }

    public static LanguageSettingFragment newInstance() {
        return new LanguageSettingFragment();
    }

    @Override
    public void initView(View view) {
        initData();
        setTitle(R.string.language_setting);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setHasFixedSize(true);
        LanguageAdapter adapter = new LanguageAdapter(getContext(), languageModelList);
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                PreferenceUtil.put(SPConstant.START_LANGUAGE, languageModelList.get(position).getTag());
                SettingActivity activity = (SettingActivity) getActivity();
                activity.changeLanguage(PreferenceUtil.getString(SPConstant.START_LANGUAGE, ""), UpdateActivity.class);
            }


            @Override
            public void onItemLongClick(int position) {
            }
        });
    }

    private void initData() {
        languageModelList = new ArrayList<>();
        LanguageModel languageModel1 = new LanguageModel();
        languageModel1.setName("简体中文");
        languageModel1.setTag("zh");
        languageModelList.add(languageModel1);
        LanguageModel languageModel2 = new LanguageModel();
        languageModel2.setName("English");
        languageModel2.setTag("en");
        languageModelList.add(languageModel2);
        LanguageModel languageModel3 = new LanguageModel();
        languageModel3.setName("فارسی");
        languageModel3.setTag("fa");
        languageModelList.add(languageModel3);
    }

    class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {


        private Context context;
        private List<LanguageModel> languageModelList;

        public LanguageAdapter(Context context, List<LanguageModel> languageModelList) {
            this.context = context;
            this.languageModelList = languageModelList;
        }

        public void refreshList(List<LanguageModel> languageModelList) {
            this.languageModelList = languageModelList;
            notifyDataSetChanged();
        }

        @Override
        public LanguageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(context).inflate(R.layout.layout_language_list_item, parent, false);
            return new LanguageViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(LanguageViewHolder holder, final int position) {
            if (languageModelList == null)
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

            String languageName = languageModelList.get(position).getName();
            String languageTag = languageModelList.get(position).getTag();

            holder.item_tv_language.setText(languageName);
            String defLanguage = PreferenceUtil.getString(SPConstant.START_LANGUAGE, "");
            if (TextUtils.isEmpty(defLanguage)){
                String defLan = Locale.getDefault().getLanguage();
                if (defLan.equals("zh")){
                    defLanguage = defLan;
                }else if (defLan.equals("en")){
                    defLanguage = defLan;
                }
            }
            holder.item_iv_gou.setVisibility(languageTag.equals(defLanguage) ? View.VISIBLE : View.GONE);
        }

        @Override
        public int getItemCount() {
            return languageModelList == null ? 0 : languageModelList.size();
        }

        class LanguageViewHolder extends RecyclerView.ViewHolder {
            TextView item_tv_language;
            ImageView item_iv_gou;
            RelativeLayout rl_rootview;

            LanguageViewHolder(View itemView) {
                super(itemView);
                rl_rootview = (RelativeLayout) itemView.findViewById(R.id.rl_rootview);
                item_tv_language = (TextView) itemView.findViewById(R.id.item_tv_language);
                item_iv_gou = (ImageView) itemView.findViewById(R.id.item_iv_gou);
            }
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.itemClickListener = listener;
        }

        private OnItemClickListener itemClickListener = null;

    }

    public interface OnItemClickListener {
        void onItemLongClick(int position);

        void onItemClick(int position);
    }
}
