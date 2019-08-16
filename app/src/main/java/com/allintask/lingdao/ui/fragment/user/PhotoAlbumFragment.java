package com.allintask.lingdao.ui.fragment.user;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.ui.activity.user.PhotoAlbumActivity;
import com.allintask.lingdao.ui.fragment.BaseFragment;

import butterknife.BindView;
import cn.tanjiajun.sdk.component.custom.image.GestureSupportedImageView;
import cn.tanjiajun.sdk.component.custom.image.imageloader.IImageOnLoadingCompleteListener;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/2/14.
 */

public class PhotoAlbumFragment extends BaseFragment {

    @BindView(R.id.gsiv_photo)
    GestureSupportedImageView photoGSIV;

    private String imageUrl;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_photo_album;
    }

    @Override
    protected BasePresenter CreatePresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if (null != bundle) {
            imageUrl = bundle.getString(CommonConstant.EXTRA_IMAGE_URL);
        }

        initUI();
    }

    private void initUI() {
        ImageViewUtil.setImageView(getContext(), photoGSIV, imageUrl, R.mipmap.ic_default, new IImageOnLoadingCompleteListener() {
            @Override
            public void onLoadingComplete(ImageView imageView, Bitmap bitmap) {
                if (null != getParentContext() && getParentContext() instanceof PhotoAlbumActivity) {
                    ((PhotoAlbumActivity) getParentContext()).setBitmap(bitmap);
                }
            }
        });
    }

}
