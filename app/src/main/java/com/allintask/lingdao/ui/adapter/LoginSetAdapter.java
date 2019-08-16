package com.allintask.lingdao.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.allintask.lingdao.R;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;

public class LoginSetAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    public LoginSetAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search_history,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindViewHolder(holder,position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder,int position){
        holder.setTextView(R.id.tv_content,"历史");
    }

}
