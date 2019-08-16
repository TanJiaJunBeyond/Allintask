package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.MyPhotoAlbumListBean;
import com.allintask.lingdao.bean.user.PhotoAlbumBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.PhotoAlbumPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.ui.adapter.CommonFragmentPagerAdapter;
import com.allintask.lingdao.ui.adapter.CommonFragmentStatePagerAdapter;
import com.allintask.lingdao.ui.fragment.user.PhotoAlbumFragment;
import com.allintask.lingdao.utils.PictureUploader;
import com.allintask.lingdao.utils.TemporaryDataCachePool;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.user.IPhotoAlbumView;
import com.allintask.lingdao.widget.SelectPhotoAlbumMoreDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/2/24.
 */

public class PhotoAlbumActivity extends BaseActivity<IPhotoAlbumView, PhotoAlbumPresenter> implements IPhotoAlbumView {

    protected static final String IMAGE_TYPE_JPG = ".jpg";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.iv_right_second)
    ImageView rightIv;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tv_photo_description)
    TextView photoDescriptionTv;

    private int userId;
    private int albumId;

    private int type = CommonConstant.SELECT_PHOTO_ALBUM_MORE_NO_DELETE;

    private int mPosition = 1;
    private List<PhotoAlbumBean.ImageBean> imageList;
    private String description;

    private List<Fragment> photoAlbumFragmentList;
    private CommonFragmentStatePagerAdapter fragmentStatePagerAdapter;
    private SelectPhotoAlbumMoreDialog selectPhotoAlbumMoreDialog;
    private Bitmap bitmap;
    private int imageId = -1;
    private String qualityUrl = null;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_photo_album;
    }

    @Override
    protected PhotoAlbumPresenter CreatePresenter() {
        return new PhotoAlbumPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            userId = intent.getIntExtra(CommonConstant.EXTRA_USER_ID, -1);
            albumId = intent.getIntExtra(CommonConstant.EXTRA_ALBUM_ID, -1);
        }

        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        rightIv.setImageResource(R.mipmap.ic_more);
        rightIv.setVisibility(View.VISIBLE);
        rightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectPhotoAlbumMoreDialog();
            }
        });

        setSupportActionBar(toolbar);
    }

    private void initData() {
        if (albumId != -1) {
            mPresenter.fetchAlbumDetailsRequest(albumId);
        }
    }

    private void initUI() {
        int myUserId = UserPreferences.getInstance().getUserId();

        if (myUserId == userId) {
            type = CommonConstant.SELECT_PHOTO_ALBUM_MORE_HAVE_DELETE;
        } else {
            type = CommonConstant.SELECT_PHOTO_ALBUM_MORE_NO_DELETE;
        }

        fragmentStatePagerAdapter = new CommonFragmentStatePagerAdapter(getSupportFragmentManager());
        fragmentStatePagerAdapter.setFragmentList(getPhotoAlbumFragmentList());
        viewPager.setAdapter(fragmentStatePagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPosition = position;

                if (null != imageList && imageList.size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder(String.valueOf(mPosition + 1)).append("/").append(String.valueOf(imageList.size()));
                    titleTv.setText(stringBuilder);

                    PhotoAlbumBean.ImageBean imagesBean = imageList.get(position);

                    if (null != imagesBean) {
                        imageId = TypeUtils.getInteger(imagesBean.imageId, -1);
                        qualityUrl = TypeUtils.getString(imagesBean.qualityUrl, "");
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        photoDescriptionTv.setText(description);
    }

    private List<Fragment> getPhotoAlbumFragmentList() {
        photoAlbumFragmentList = new ArrayList<>();

        if (null != imageList && imageList.size() > 0) {
            for (int i = 0; i < imageList.size(); i++) {
                PhotoAlbumBean.ImageBean imagesBean = imageList.get(i);

                if (null != imagesBean) {
                    int imageId = TypeUtils.getInteger(imagesBean.imageId, -1);
                    String qualityUrl = TypeUtils.getString(imagesBean.qualityUrl, "");
                    String imageUrl = null;

                    if (!TextUtils.isEmpty(qualityUrl)) {
                        imageUrl = "https:" + qualityUrl;
                    }

                    PhotoAlbumFragment photoAlbumFragment = new PhotoAlbumFragment();

                    Bundle bundle = new Bundle();
                    bundle.putInt(CommonConstant.EXTRA_IMAGE_ID, imageId);
                    bundle.putString(CommonConstant.EXTRA_IMAGE_URL, imageUrl);
                    photoAlbumFragment.setArguments(bundle);
                    photoAlbumFragmentList.add(photoAlbumFragment);
                }
            }
        }
        return photoAlbumFragmentList;
    }

    private void showSelectPhotoAlbumMoreDialog() {
        selectPhotoAlbumMoreDialog = new SelectPhotoAlbumMoreDialog(getParentContext(), type);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.BOTTOM;

        Window window = selectPhotoAlbumMoreDialog.getWindow();

        if (null != window) {
            window.setAttributes(layoutParams);
        }

        selectPhotoAlbumMoreDialog.show();
        selectPhotoAlbumMoreDialog.setOnClickListener(new SelectPhotoAlbumMoreDialog.OnClickListener() {
            @Override
            public void onSaveImageTextViewClick() {
                if (null != selectPhotoAlbumMoreDialog && selectPhotoAlbumMoreDialog.isShowing()) {
                    selectPhotoAlbumMoreDialog.dismiss();
                }

                if (null != bitmap) {
                    saveImage(bitmap);
                } else {
                    showToast("图片正在加载中");
                }
            }

            @Override
            public void onDeleteImageTextViewClick() {
                if (null != selectPhotoAlbumMoreDialog && selectPhotoAlbumMoreDialog.isShowing()) {
                    selectPhotoAlbumMoreDialog.dismiss();
                }

                if (albumId != -1 && imageId != -1 && !TextUtils.isEmpty(qualityUrl)) {
                    mPresenter.deletePhotoAlbumRequest(albumId, imageId, qualityUrl);
                }
            }
        });
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private void saveImage(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard", System.currentTimeMillis() + IMAGE_TYPE_JPG);

        if (null != bitmap) {
            boolean isSave = PictureUploader.saveEncodeJpegImageFile(file, bitmap, PictureUploader.getEncodePercent(bitmap));

            if (isSave) {
                showToast("保存图片成功");

                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                sendBroadcast(intent);
            } else {
                showToast("保存图片失败");
            }
        }
    }

    @Override
    public void onShowImageCountAndImageListAndPhotoDescription(int imageCount, List<PhotoAlbumBean.ImageBean> imageList, String description) {
        if (null != imageList && imageList.size() > 0) {
            PhotoAlbumBean.ImageBean imagesBean = imageList.get(0);

            if (null != imagesBean) {
                imageId = TypeUtils.getInteger(imagesBean.imageId, -1);
                qualityUrl = TypeUtils.getString(imagesBean.qualityUrl, "");
            }
        }

        StringBuilder stringBuilder = new StringBuilder(String.valueOf(mPosition)).append("/").append(String.valueOf(imageCount));
        titleTv.setText(stringBuilder);

        this.imageList = imageList;
        this.description = description;

        initUI();
    }

    @Override
    public void onDeleteImageSuccess(int albumId, int imageId) {
        showToast("删除图片成功");

        if (null != imageList && imageList.size() > 0) {
            for (int i = 0; i < imageList.size(); i++) {
                PhotoAlbumBean.ImageBean imagesBean = imageList.get(i);

                if (null != imagesBean) {
                    int tempImageId = TypeUtils.getInteger(imagesBean.imageId, -1);

                    if (null != photoAlbumFragmentList && photoAlbumFragmentList.size() > 0 && tempImageId == imageId) {
                        imageList.remove(i);
                        photoAlbumFragmentList.remove(i);

                        StringBuilder stringBuilder = new StringBuilder(String.valueOf(mPosition)).append("/").append(String.valueOf(imageList.size()));
                        titleTv.setText(stringBuilder);

                        if (imageList.size() == 0) {
                            fragmentStatePagerAdapter.setFragmentList(photoAlbumFragmentList);
                            fragmentStatePagerAdapter.notifyDataSetChanged();

                            setResult(CommonConstant.REFRESH_RESULT_CODE);
                            finish();
                        } else {
                            fragmentStatePagerAdapter.setFragmentList(photoAlbumFragmentList);
                            fragmentStatePagerAdapter.notifyDataSetChanged();

                            setResult(CommonConstant.REFRESH_RESULT_CODE);
                        }
                    }
                }
            }
        }
    }

}
