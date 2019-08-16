package com.allintask.lingdao.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.utils.WindowUtils;

/**
 * Created by TanJiaJun on 2018/2/24.
 */

public class SelectPhotoAlbumMoreDialog extends Dialog {

    private int type = CommonConstant.SELECT_PHOTO_ALBUM_MORE_NO_DELETE;

    private OnClickListener onClickListener;

    public SelectPhotoAlbumMoreDialog(@NonNull Context context, int type) {
        super(context, R.style.SelectPhotoAlbumMoreDialogStyle);
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(WindowUtils.getScreenWidth(getContext()), -2);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_photo_album_more, null);
        setContentView(contentView, layoutParams);

        TextView saveImageTv = (TextView) contentView.findViewById(R.id.tv_save_image);
        LinearLayout deleteImageLL = (LinearLayout) contentView.findViewById(R.id.ll_delete_image);
        TextView deleteImageTv = (TextView) contentView.findViewById(R.id.tv_delete_image);
        TextView cancelTv = (TextView) contentView.findViewById(R.id.tv_cancel);

        switch (type) {
            case CommonConstant.SELECT_PHOTO_ALBUM_MORE_NO_DELETE:
                deleteImageLL.setVisibility(View.GONE);
                break;

            case CommonConstant.SELECT_PHOTO_ALBUM_MORE_HAVE_DELETE:
                deleteImageLL.setVisibility(View.VISIBLE);
                break;
        }

        saveImageTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClickListener) {
                    onClickListener.onSaveImageTextViewClick();
                }
            }
        });

        deleteImageTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClickListener) {
                    onClickListener.onDeleteImageTextViewClick();
                }
            }
        });

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) {
                    dismiss();
                }
            }
        });
    }

    public interface OnClickListener {

        void onSaveImageTextViewClick();

        void onDeleteImageTextViewClick();

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
