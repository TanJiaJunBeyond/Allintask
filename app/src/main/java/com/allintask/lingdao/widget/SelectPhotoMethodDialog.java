package com.allintask.lingdao.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.utils.WindowUtils;

/**
 * Created by TanJiaJun on 2018/2/5.
 */

public class SelectPhotoMethodDialog extends Dialog {

    private OnSelectPhotoMethodDialogClickListener onSelectPhotoMethodDialogClickListener;

    public SelectPhotoMethodDialog(@NonNull Context context) {
        super(context, R.style.SelectPhotoMethodDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(WindowUtils.getScreenWidth(getContext()), -2);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_photo_method, null);
        setContentView(view, layoutParams);

        TextView takePhotoTv = (TextView) view.findViewById(R.id.tv_take_photo);
        TextView photoAlbumTv = (TextView) view.findViewById(R.id.tv_photo_album);
        TextView cancelTv = (TextView) view.findViewById(R.id.tv_cancel);

        takePhotoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onSelectPhotoMethodDialogClickListener) {
                    onSelectPhotoMethodDialogClickListener.onClickTakePhoto();
                }
            }
        });

        photoAlbumTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onSelectPhotoMethodDialogClickListener) {
                    onSelectPhotoMethodDialogClickListener.onClickPhotoAlbum();
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

    public interface OnSelectPhotoMethodDialogClickListener {

        void onClickTakePhoto();

        void onClickPhotoAlbum();

    }

    public void setOnSelectPhotoMethodDialogClickListener(OnSelectPhotoMethodDialogClickListener onSelectPhotoMethodDialogClickListener) {
        this.onSelectPhotoMethodDialogClickListener = onSelectPhotoMethodDialogClickListener;
    }

}
