package com.allintask.lingdao.ui.adapter.user;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.SearchInformationBean;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.allintask.lingdao.utils.EnglishUtils;
import com.allintask.lingdao.utils.KeywordUtils;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.common.utils.Validator;

/**
 * Created by TanJiaJun on 2018/1/29.
 */

public class SearchInformationAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    private String keyword;

    public SearchInformationAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search_information, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        SearchInformationBean searchInformationBean = (SearchInformationBean) getItem(position);

        if (null != searchInformationBean) {
            TextView searchInformationTv = holder.getChildView(R.id.tv_search_information);

            String name = TypeUtils.getString(searchInformationBean.name, "");
            String pyName = TypeUtils.getString(searchInformationBean.pyName, "");
            String pyShort = TypeUtils.getString(searchInformationBean.pyShort, "");

            if (EnglishUtils.isEnglishCharacterString(keyword)) {
                if (keyword.equals(pyName)) {
                    SpannableStringBuilder spannableStringBuilder = KeywordUtils.matcherSearchInformation(context.getResources().getColor(R.color.theme_orange), name, new String[]{keyword});
                    searchInformationTv.setText(spannableStringBuilder);
                } else if (pyShort.contains(keyword)) {
                    int startIndex = pyShort.indexOf(keyword);
                    int endIndex = pyShort.lastIndexOf(keyword) + keyword.length();
                    String cutOutStr = name.substring(startIndex, endIndex);
                    SpannableStringBuilder spannableStringBuilder = KeywordUtils.matcherSearchInformation(context.getResources().getColor(R.color.theme_orange), name, new String[]{cutOutStr});
                    searchInformationTv.setText(spannableStringBuilder);
                }
            } else if (Validator.isChineseCharacter(keyword)) {
                SpannableStringBuilder spannableStringBuilder = KeywordUtils.matcherSearchInformation(context.getResources().getColor(R.color.theme_orange), name, new String[]{keyword});
                searchInformationTv.setText(spannableStringBuilder);
            } else {
                holder.setTextView(R.id.tv_search_information, name);
            }
        }
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
