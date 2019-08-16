package com.allintask.lingdao.ui.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allintask.lingdao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/6/28.
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<CommonRecyclerViewHolder> {

    private List<T> mItems;
    private List<T> mPreItems;
    private LayoutInflater mInflater;
    private Context mContext;

    /**
     * 刷新加载状态
     */
    private int mRecyclerState;
    /**
     * 隐藏状态状态
     */
    public static final int STATE_HIDE = 0;
    /**
     * 全部加载状态
     */
    public static final int STATE_NO_MORE = 1;
    /**
     * 正在加载状态
     */
    public static final int STATE_LOADING = 2;
    /**
     * 加载更多状态
     */
    public static final int STATE_LOAD_MORE = 3;
    /**
     * 加载失败状态
     */
    public static final int STATE_LOAD_ERROR = 4;
    /**
     * 正在刷新状态
     */
    public static final int STATE_REFRESHING = 5;
    /**
     * 无效网络状态
     */
    public static final int STATE_INVALID_NETWORK = 6;
    /**
     * 请求失败状态
     */
    public static final int STATE_REQUEST_NETWORK = 7;

    /**
     * 界面状态模式
     */
    private int mBehaviorMode;
    /**
     * 不要头尾控件
     */
    public static final int MODE_BOTH_NEITHER = 0;
    /**
     * 只要头部控件
     */
    public static final int MODE_ONLY_HEADER = 1;
    /**
     * 只要尾部控件
     */
    public static final int MODE_ONLY_FOOTER = 2;
    /**
     * 头尾控件都要
     */
    public static final int MODE_HEADER_AND_FOOTER = 3;

    /**
     * RecyclerView控件
     */
    public static final int VIEW_TYPE_NORMAL = 0;
    /**
     * Recycler头部控件
     */
    public static final int VIEW_TYPE_HEADER = -1;
    /**
     * Recycler尾部控件
     */
    public static final int VIEW_TYPE_FOOTER = -2;

    private OnClickListener onClickListener;
    private OnLongClickListener onLongClickListener;
    private BaseRecyclerViewItemClickListener onItemClickListener;
    private BaseRecyclerViewItemLongClickListener onItemLongClickListener;

    private View mHeaderView;
    private OnLoadingHeaderCallBack onLoadingHeaderCallBack;

    public BaseRecyclerViewAdapter(Context context, int mode) {
        mItems = new ArrayList<>();
        this.mBehaviorMode = mode;
        this.mRecyclerState = STATE_HIDE;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        initListener();
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        onClickListener = new OnClickListener() {
            @Override
            public void onClick(int position, RecyclerView.ViewHolder holder) {
                if (onItemClickListener != null)
                    onItemClickListener.setOnItemClick(holder, position);
            }
        };

        onLongClickListener = new OnLongClickListener() {
            @Override
            public boolean onLongClick(int position, RecyclerView.ViewHolder holder) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.setOnItemLongClick(holder, position);
                    return true;
                }
                return false;
            }
        };
    }


    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                if (onLoadingHeaderCallBack != null) {
                    return onLoadingHeaderCallBack.onCreateHeaderHolder(mInflater, parent);
                } else {
                    return onCreateHeaderViewHolder(mInflater, parent);
                }
            case VIEW_TYPE_FOOTER:
                return new CommonRecyclerViewHolder(mInflater.inflate(R.layout.view_recycler_view_footer, parent, false));
            default:
                final CommonRecyclerViewHolder holder = onCreateDefaultViewHolder(mInflater, parent, viewType);
                if (holder != null) {
                    holder.itemView.setTag(holder);
                    holder.itemView.setOnLongClickListener(onLongClickListener);
                    holder.itemView.setOnClickListener(onClickListener);
                }
                return holder;
        }
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEADER:
                if (onLoadingHeaderCallBack != null) {
                    onLoadingHeaderCallBack.onBindHeaderHolder(holder, position);
                } else {
                    onBindHeaderViewHolder(holder, position);
                }
                break;
            case VIEW_TYPE_FOOTER:
                View itemView = holder.itemView;
                ProgressBar progressBar = holder.getChildView(R.id.pb_footer);
                TextView textView = holder.getChildView(R.id.tv_footer);
                itemView.setVisibility(View.VISIBLE);
                switch (mRecyclerState) {
                    case STATE_INVALID_NETWORK:
                        textView.setText(R.string.state_not_network_error);
                        progressBar.setVisibility(View.GONE);
                        break;
                    case STATE_REQUEST_NETWORK:
                        textView.setText(R.string.state_network_request_error);
                        progressBar.setVisibility(View.GONE);
                        break;
                    case STATE_LOAD_MORE:
                    case STATE_LOADING:
                        textView.setText(R.string.state_loading);
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case STATE_NO_MORE:
                        textView.setText(R.string.state_not_more);
                        progressBar.setVisibility(View.GONE);
                        break;
                    case STATE_REFRESHING:
                        textView.setText(R.string.state_refreshing);
                        progressBar.setVisibility(View.GONE);
                        break;
                    case STATE_LOAD_ERROR:
                        textView.setText(R.string.state_loading_error);
                        progressBar.setVisibility(View.GONE);
                        break;
                    case STATE_HIDE:
                        itemView.setVisibility(View.GONE);
                        break;
                }
                break;
            default:
                if (null != mItems && mItems.size() > 0) {
                    onBindDefaultViewHolder(holder, getItems().get(getIndex(position)), position);
                }
                break;
        }
    }

    /**
     * 当添加到RecyclerView时获取GridLayoutManager布局管理器，修正header和footer显示整行
     *
     * @param recyclerView the mRecyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == VIEW_TYPE_HEADER || getItemViewType(position) == VIEW_TYPE_FOOTER
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    /**
     * 当RecyclerView在windows活动时获取StaggeredGridLayoutManager布局管理器，修正header和footer显示整行
     *
     * @param holder the RecyclerView.ViewHolder
     */
    @Override
    public void onViewAttachedToWindow(CommonRecyclerViewHolder holder) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            if (mBehaviorMode == MODE_ONLY_HEADER) {
                p.setFullSpan(holder.getLayoutPosition() == 0);
            } else if (mBehaviorMode == MODE_ONLY_FOOTER) {
                p.setFullSpan(holder.getLayoutPosition() == mItems.size() + 1);
            } else if (mBehaviorMode == MODE_HEADER_AND_FOOTER) {
                if (holder.getLayoutPosition() == 0 || holder.getLayoutPosition() == mItems.size() + 1) {
                    p.setFullSpan(true);
                }
            }
        }
    }

    /**
     * 获取item控件类型
     */
    @Override
    public int getItemViewType(int position) {
        switch (mBehaviorMode) {
            case MODE_ONLY_HEADER:
                if (position == 0) {
                    return VIEW_TYPE_HEADER;
                } else {
                    return VIEW_TYPE_NORMAL;
                }

            case MODE_ONLY_FOOTER:
                if (position == mItems.size()) {
                    return VIEW_TYPE_FOOTER;
                } else {
                    return VIEW_TYPE_NORMAL;
                }

            case MODE_HEADER_AND_FOOTER:
                if (position == 0) {
                    return VIEW_TYPE_HEADER;
                } else if (position == mItems.size() + 1) {
                    return VIEW_TYPE_FOOTER;
                } else {
                    return VIEW_TYPE_NORMAL;
                }

            default:
                return VIEW_TYPE_NORMAL;
        }
    }

    /**
     * 获取item控件索引角标
     */
    protected int getIndex(int position) {
        switch (mBehaviorMode) {
            case MODE_ONLY_HEADER:
            case MODE_HEADER_AND_FOOTER:
                return position - 1;

            default:
                return position;
        }
    }

    /**
     * 获取item控件数量
     */
    @Override
    public int getItemCount() {
        switch (mBehaviorMode) {
            case MODE_ONLY_HEADER:
            case MODE_ONLY_FOOTER:
                return mItems.size() + 1;

            case MODE_HEADER_AND_FOOTER:
                return mItems.size() + 2;

            default:
                return mItems.size();
        }
    }

    /**
     * 获取item控件数量
     */
    public int getCount() {
        return mItems.size();
    }

    /**
     * 创建新的RecyclerView控件：绑定item视图
     */
    protected abstract CommonRecyclerViewHolder onCreateDefaultViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType);

    /**
     * 绑定RecyclerView控件：绑定item视图数据
     */
    protected abstract void onBindDefaultViewHolder(CommonRecyclerViewHolder holder, T item, int position);

    /**
     * 获取头部控件
     */
    public final View getHeaderView() {
        return this.mHeaderView;
    }

    /**
     * 设置头部控件
     */
    public final void setHeaderView(View view) {
        this.mHeaderView = view;
    }

    /**
     * 获取全部item（list）
     */
    public final List<T> getItems() {
        return mItems;
    }

    /**
     * 添加全部item（list）
     */
    public void addAll(List<T> items) {
        if (items != null) {
            this.mItems.addAll(items);
            notifyItemRangeInserted(this.mItems.size(), items.size());
        }
    }

    /**
     * 添加items, 并剔除与上次添加所重复的数据
     */
    public int addItems(List<T> items) {
        int filterOut = 0;
        if (items != null && !items.isEmpty()) {
            List<T> date = new ArrayList<>();
            if (mPreItems != null) {
                for (T d : items) {
                    if (!mPreItems.contains(d)) {
                        date.add(d);
                    } else {
                        filterOut++;
                    }
                }
                //mPreItems.addAll(date);
            } else {
                date = items;
                //mPreItems = items;
            }

            mPreItems = items;
            addAll(date);
        }
        return filterOut;
    }

    public void clearPreItems() {
        mPreItems = null;
    }

    /**
     * 添加item
     */
    public final void addItem(T item) {
        if (item != null) {
            this.mItems.add(item);
            notifyItemChanged(mItems.size());
        }
    }

    /**
     * 添加item
     */
    public void addItem(int position, T item) {
        if (item != null) {
            this.mItems.add(getIndex(position), item);
            notifyItemInserted(position);
        }
    }

    /**
     * 替换item
     */
    public void replaceItem(int position, T item) {
        if (item != null) {
            this.mItems.set(getIndex(position), item);
            notifyItemChanged(position);
        }
    }

    /**
     * 删除item
     */
    public final void removeItem(T item) {
        if (this.mItems.contains(item)) {
            int position = mItems.indexOf(item);
            this.mItems.remove(item);
            notifyItemRemoved(position);
        }
    }

    /**
     * 删除item
     */
    public final void removeItem(int position) {
        if (this.getItemCount() > position) {
            this.mItems.remove(getIndex(position));
            notifyItemRemoved(position);
            notifyDataSetChanged(); //连续删除2条动态分享，第二次删除没刷新，继续点击删除 点击删除后出现了错位问题，后来加上这句代码 （提示要修改好数据）
        }
    }

    /**
     * 获取item
     */
    public final T getItem(int position) {
        int index = getIndex(position);
        if (index < 0 || index >= getCount())
            return null;
        return mItems.get(index);
    }

    /**
     * 重新添加所有item（List）
     */
    public final void resetItem(List<T> items) {
        if (items != null) {
            this.clear();
            this.addAll(items);
        }
    }

    /**
     * 清除所有item
     */
    public final void clear() {
        this.mItems.clear();
        setState(STATE_HIDE);
        notifyDataSetChanged();
    }

    /**
     * 设置加载刷新状态
     */
    public void setState(int mState) {
        this.mRecyclerState = mState;

        if (null != mItems && mItems.size() > 0) {
            notifyItemChanged(mItems.size());
        }
    }

    /**
     * 获取加载刷新状态
     */
    public int getRecyclerState() {
        return mRecyclerState;
    }

    /**
     * 获取Context
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 添加项点击事件
     *
     * @param onItemClickListener the RecyclerView item click listener
     */
    public void setOnItemClickListener(BaseRecyclerViewItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 添加项点长击事件
     *
     * @param onItemLongClickListener the RecyclerView item long click listener
     */
    public void setOnItemLongClickListener(BaseRecyclerViewItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    /**
     * 添加头部控件
     *
     * @param listener for load header view listener
     */
    public final void setOnLoadingHeaderCallBack(OnLoadingHeaderCallBack listener) {
        onLoadingHeaderCallBack = listener;
    }

    public static abstract class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
            onClick(holder.getAdapterPosition(), holder);
        }

        public abstract void onClick(int position, RecyclerView.ViewHolder holder);
    }

    public static abstract class OnLongClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
            return onLongClick(holder.getAdapterPosition(), holder);
        }

        public abstract boolean onLongClick(int position, RecyclerView.ViewHolder holder);
    }

    /**
     * 添加加载头部控件的回调
     */
    public interface OnLoadingHeaderCallBack {

        CommonRecyclerViewHolder onCreateHeaderHolder(LayoutInflater inflater, ViewGroup parent);

        void onBindHeaderHolder(CommonRecyclerViewHolder holder, int position);

    }

    public class HeaderViewHolder extends CommonRecyclerViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView, mContext);
        }
    }

    public CommonRecyclerViewHolder onCreateHeaderViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return null;
    }

    public void onBindHeaderViewHolder(CommonRecyclerViewHolder holder, int position) {

    }

}
