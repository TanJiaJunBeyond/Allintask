package com.allintask.lingdao.ui.activity.demand;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.demand.ApplyForArbitramentRequestBean;
import com.allintask.lingdao.bean.demand.ArbitramentReasonBean;
import com.allintask.lingdao.bean.demand.ImageRequestsBean;
import com.allintask.lingdao.bean.user.CheckUploadIsSuccessBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.demand.ApplyForArbitramentPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.ui.adapter.recyclerview.RecyclerItemClickListener;
import com.allintask.lingdao.ui.adapter.service.PhotoAdapter;
import com.allintask.lingdao.utils.EmojiUtils;
import com.allintask.lingdao.utils.ImageUtils;
import com.allintask.lingdao.utils.SocketUploader;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.demand.IApplyForArbitramentView;
import com.allintask.lingdao.widget.SelectArbitramentReasonDialog;
import com.allintask.lingdao.widget.SelectPhotoMethodDialog;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.DensityUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.common.utils.FileUtils;
import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/3/10.
 */

public class ApplyForArbitramentActivity extends BaseActivity<IApplyForArbitramentView, ApplyForArbitramentPresenter> implements IApplyForArbitramentView {

    private static final String PIC_TYPE_JPG = ".jpg";
    private static final String CACHE_PIC_PATH = "CachePics";

    private static final int REQUEST_CODE_TAKE_PHOTO = 3866;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.tv_right_first)
    TextView rightFirstTv;
    @BindView(R.id.ll_arbitrament_reason)
    LinearLayout arbitramentReasonLL;
    @BindView(R.id.tv_arbitrament_reason)
    TextView arbitramentReasonTv;
    @BindView(R.id.et_arbitrament_description)
    EditText arbitramentDescriptionEt;
    @BindView(R.id.tv_number_of_words)
    TextView numberOfWordsTv;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.btn_submit)
    Button submitBtn;

    private int userId;
    private int demandId;
    private int orderId;

    private int myUserId;
    private int arbitramentReasonId;

    private List<ArbitramentReasonBean> arbitramentReasonList;
    private SelectArbitramentReasonDialog selectArbitramentReasonDialog;

    private List<File> filesList;
    private List<String> imageUrlList;
    private SocketUploader socketUploader;

    private PhotoAdapter photoAdapter;
    private SelectPhotoMethodDialog selectPhotoMethodDialog;
    private File selectPhotoFile;
    private String uploadFlagId;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_apply_for_arbitrament;
    }

    @Override
    protected ApplyForArbitramentPresenter CreatePresenter() {
        return new ApplyForArbitramentPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            userId = intent.getIntExtra(CommonConstant.EXTRA_USER_ID, -1);
            demandId = intent.getIntExtra(CommonConstant.EXTRA_DEMAND_ID, -1);
            orderId = intent.getIntExtra(CommonConstant.EXTRA_ORDER_ID, -1);
        }

        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.apply_for_arbitrament));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        filesList = new ArrayList<>();
        imageUrlList = new ArrayList<>();

        socketUploader = new SocketUploader(getParentContext());
        socketUploader.setOnUploadStatusListener(new SocketUploader.OnUploadStatusListener() {
            @Override
            public void onSuccess(String flagId) {
                if (!TextUtils.isEmpty(flagId) && filesList.size() != 0 && imageUrlList.size() != 0) {
                    uploadFlagId = flagId;
                    mPresenter.checkUploadIsSuccessRequest(uploadFlagId, filesList.size());
                }
            }

            @Override
            public void onFail() {
                dismissProgressDialog();
                showToast(getString(R.string.upload_fail));
            }
        });

        arbitramentDescriptionEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String arbitramentDescription = arbitramentDescriptionEt.getText().toString().trim();
                int index = arbitramentDescriptionEt.getSelectionStart() - 1;

                if (index >= 0) {
                    if (EmojiUtils.noEmoji(arbitramentDescription)) {
                        Editable editable = arbitramentDescriptionEt.getText();
                        editable.delete(index, index + 1);
                    }
                }

                int arbitramentDescriptionLength = arbitramentDescription.length();
                numberOfWordsTv.setText(String.valueOf(arbitramentDescriptionLength) + "/500");

                checkConfirmSubmitEnable();
            }
        });

        initRecyclerView();

        submitBtn.setEnabled(false);
        submitBtn.setClickable(false);
    }

    private void initData() {
        myUserId = UserPreferences.getInstance().getUserId();
        mPresenter.fetchArbitrationReasonListRequest();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getParentContext(), 3));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DensityUtil.dip2px(getParentContext(), 10), DensityUtil.dip2px(getParentContext(), 10), DensityUtil.dip2px(getParentContext(), 10), DensityUtil.dip2px(getParentContext(), 10));
            }
        });

        photoAdapter = new PhotoAdapter(getParentContext());
        recyclerView.setAdapter(photoAdapter);

        photoAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position <= imageUrlList.size() - 1) {
                    String[] imageUrlArray = imageUrlList.toArray(new String[imageUrlList.size()]);
                } else {
                    showSelectPhotoMethodDialog();
                }
            }
        });

        photoAdapter.setOnDeletePhotoListener(new PhotoAdapter.OnDeletePhotoListener() {
            @Override
            public void onDeletePhoto(int position) {
                imageUrlList.remove(position);
                photoAdapter.setDateList(imageUrlList);
                checkConfirmSubmitEnable();
            }
        });
    }

    private void showSelectArbitramentReasonDialog(final List<ArbitramentReasonBean> arbitramentReasonList) {
        selectArbitramentReasonDialog = new SelectArbitramentReasonDialog(getParentContext(), arbitramentReasonList);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.BOTTOM;

        Window window = selectArbitramentReasonDialog.getWindow();

        if (null != window) {
            window.setAttributes(layoutParams);
        }

        selectArbitramentReasonDialog.show();
        selectArbitramentReasonDialog.setOnClickListener(new SelectArbitramentReasonDialog.OnClickListener() {
            @Override
            public void onItemClick(int position) {
                if (null != selectArbitramentReasonDialog && selectArbitramentReasonDialog.isShowing()) {
                    selectArbitramentReasonDialog.dismiss();
                }

                ArbitramentReasonBean arbitramentReasonBean = arbitramentReasonList.get(position);

                if (null != arbitramentReasonBean) {
                    arbitramentReasonId = TypeUtils.getInteger(arbitramentReasonBean.id, -1);
                    String name = TypeUtils.getString(arbitramentReasonBean.name, "");
                    arbitramentReasonTv.setText(name);

                    checkConfirmSubmitEnable();
                }
            }
        });
    }

    private void showSelectPhotoMethodDialog() {
        selectPhotoMethodDialog = new SelectPhotoMethodDialog(this);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.BOTTOM;

        Window window = selectPhotoMethodDialog.getWindow();

        if (null != window) {
            window.setAttributes(layoutParams);
        }

        selectPhotoMethodDialog.show();
        selectPhotoMethodDialog.setOnSelectPhotoMethodDialogClickListener(new SelectPhotoMethodDialog.OnSelectPhotoMethodDialogClickListener() {
            @Override
            public void onClickTakePhoto() {
                pickPhotoFromCamera();
            }

            @Override
            public void onClickPhotoAlbum() {
                pickPhotoFromAlbum();
            }
        });
    }

    /**
     * 拍照
     */
    private void pickPhotoFromCamera() {
        if (!ImageUtils.checkPermission(getParentContext(), ImageUtils.CAMERA)) {
            return;
        }

        selectPhotoFile = FileUtils.getCacheFile(this, CACHE_PIC_PATH, System.currentTimeMillis() + PIC_TYPE_JPG);

        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", selectPhotoFile);
        Intent openTakePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openTakePic.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(openTakePic, REQUEST_CODE_TAKE_PHOTO);
    }

    /**
     * 选择相册
     */
    private void pickPhotoFromAlbum() {
        if (!ImageUtils.checkPermission(getParentContext(), ImageUtils.ALBUM)) {
            return;
        }

        ImageUtils.pickPhotoFromAlbum(this);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public Uri[] getUrisFromClipData(Intent data) {
        ArrayList<Uri> urlList = new ArrayList<>();
        if (data.getClipData() != null) {
            ClipData clipData = data.getClipData();
            int size = clipData.getItemCount();
            for (int i = 0; i < size; i++) {
                if (clipData.getItemAt(i).getUri() != null) {
                    if (!ImageUtils.getAbsoluteImagePath(this, clipData.getItemAt(i).getUri()).endsWith("gif")) {
                        urlList.add(clipData.getItemAt(i).getUri());
                    } else {
                        showToast(R.string.error_file_format_pause);
                    }
                }
            }
        }
        if (!urlList.isEmpty()) {
            Uri[] uris = new Uri[urlList.size()];
            urlList.toArray(uris);
            return uris;
        }
        return new Uri[]{};
    }

    public void handlePhoto(final Uri[] uris) {
        String imageUrl = ImageUtils.getAbsolutePathFromUri(this, uris[0]);

        if (isValidImgType(imageUrl)) {
            if (null != selectPhotoMethodDialog && selectPhotoMethodDialog.isShowing()) {
                selectPhotoMethodDialog.dismiss();
            }

            File file = new File(imageUrl);
            filesList.add(file);

            imageUrlList.add(imageUrl);
            photoAdapter.setDateList(imageUrlList);

            checkConfirmSubmitEnable();
        } else {
            showToast(getString(R.string.image_type_support));
        }
    }

    /**
     * 检验图片有效性
     *
     * @param imgUrl
     * @return
     */
    private boolean isValidImgType(String imgUrl) {
        if (imgUrl.endsWith("jpg") || imgUrl.endsWith("jpeg") || imgUrl.endsWith("png")) {
            return true;
        }
        return false;
    }

    private boolean saveEncodeJpegImageFile(File file, Bitmap imageBitmap, int encodePercent) {
        return saveEncodeImageFile(file, imageBitmap, Bitmap.CompressFormat.JPEG, encodePercent);
    }

    private boolean saveEncodeImageFile(File file, Bitmap imageBitmap, Bitmap.CompressFormat format, int encodePercent) {
        boolean isSaved = true;

        encodePercent = (encodePercent > 0 && encodePercent <= 100 ? encodePercent : 100);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        imageBitmap.compress(format, encodePercent, out);// (quality:0-100)压缩文件
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.flush();
        } catch (FileNotFoundException e) {
            isSaved = false;
            e.printStackTrace();
        } catch (IOException e) {
            isSaved = false;
            e.printStackTrace();
        } finally {
            if (null != fileOutputStream) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return isSaved;
    }

    private int getEncodePercent(Bitmap bitmap) {
        int encodePercent = 100;
        if (bitmap.getByteCount() > 100 * 1000 * 1000) {
            encodePercent = 5;
        } else if (bitmap.getByteCount() > 50 * 1000 * 1000) {
            encodePercent = 10;
        } else if (bitmap.getByteCount() > 30 * 1000 * 1000) {
            encodePercent = 30;
        } else if (bitmap.getByteCount() > 10 * 1000 * 1000) {
            encodePercent = 50;
        } else if (bitmap.getByteCount() > 1000 * 1000) {
            encodePercent = 70;
        } else if (bitmap.getByteCount() > 500 * 1000) {
            encodePercent = 90;
        }

        Log.i("TanJiaJun", "bitmap.getByteCount() = " + bitmap.getByteCount());
        Log.i("TanJiaJun", "encodePercent = " + encodePercent);
        return encodePercent;
    }

    private void checkConfirmSubmitEnable() {
        int myUserId = UserPreferences.getInstance().getUserId();
        String arbitramentDescription = arbitramentDescriptionEt.getText().toString().trim();

        if (myUserId != -1 && orderId != -1 && arbitramentReasonId != -1 && !TextUtils.isEmpty(arbitramentDescription) && imageUrlList.size() != 0) {
            submitBtn.setEnabled(true);
            submitBtn.setClickable(true);
        } else {
            submitBtn.setEnabled(false);
            submitBtn.setClickable(false);
        }
    }

    private void sendApplyForArbitramentMessage(int sendUserId, int demandId) {
        String nickname = UserPreferences.getInstance().getNickname();
        String headPortraitUrl = UserPreferences.getInstance().getHeadPortraitUrl();

        EMMessage emMessage = EMMessage.createTxtSendMessage(getString(R.string.message_sender_apply_for_arbitrament_content), String.valueOf(sendUserId));

        if (!TextUtils.isEmpty(nickname)) {
            emMessage.setAttribute(CommonConstant.EMCHAT_NICKNAME, nickname);

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put(CommonConstant.EMCHAT_EXTERN, nickname);
                emMessage.setAttribute(CommonConstant.EMCHAT_EM_APNS_EXT, jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(headPortraitUrl)) {
            String tempHeadPortrait = headPortraitUrl.replace("https:", "");
            emMessage.setAttribute(CommonConstant.EMCHAT_HEAD_PORTRAIT_URL, tempHeadPortrait);
        }

        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_TYPE, CommonConstant.MESSAGE_ATTRIBUTE_TYPE_MESSAGE);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE, CommonConstant.MESSAGE_STATUS_APPLY_FOR_ARBITRAMENT);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_ID, String.valueOf(demandId));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE_TITLE, getString(R.string.message_apply_for_arbitrament_title));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_SENDER, getString(R.string.message_sender_apply_for_arbitrament_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_RECEIVE, getString(R.string.message_receiver_apply_for_arbitrament_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_SENDER, getString(R.string.message_details));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_RECEIVE, getString(R.string.message_details));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_UNDERWAY);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_USER_TYPE, CommonConstant.MESSAGE_STATUS_USER_TYPE_EMPLOYER);
        EMClient.getInstance().chatManager().sendMessage(emMessage);
    }

    @OnClick({R.id.ll_arbitrament_reason, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_arbitrament_reason:
                if (null != arbitramentReasonList && arbitramentReasonList.size() > 0) {
                    showSelectArbitramentReasonDialog(arbitramentReasonList);
                }
                break;

            case R.id.btn_submit:
                String arbitramentDescription = arbitramentDescriptionEt.getText().toString().trim();

                if (myUserId != -1 && orderId != -1) {
                    if (arbitramentReasonId != -1) {
                        if (!TextUtils.isEmpty(arbitramentDescription)) {
                            if (null != filesList && filesList.size() > 0) {
                                showProgressDialog("正在提交");
                                socketUploader.uploadFileList(myUserId, filesList, ApplyForArbitramentActivity.this);
                            } else {
                                showToast("请选择图片");
                            }
                        } else {
                            showToast("请填写仲裁详细描述");
                        }
                    } else {
                        showToast("请选择仲裁原因");
                    }
                }
                break;
        }
    }

    @Override
    public void onShowArbitramentReasonList(List<ArbitramentReasonBean> arbitramentReasonList) {
        this.arbitramentReasonList = arbitramentReasonList;
    }

    @Override
    public void onShowCheckUploadIsSuccessList(List<CheckUploadIsSuccessBean> checkUploadIsSuccessList) {
        if (null != checkUploadIsSuccessList && checkUploadIsSuccessList.size() > 0) {
            ApplyForArbitramentRequestBean applyForArbitramentRequestBean = new ApplyForArbitramentRequestBean();

            if (orderId != -1) {
                applyForArbitramentRequestBean.orderId = orderId;
            }

            if (arbitramentReasonId != -1) {
                applyForArbitramentRequestBean.operateReasonId = arbitramentReasonId;
            }

            String arbitramentDescription = arbitramentDescriptionEt.getText().toString().trim();

            if (!TextUtils.isEmpty(arbitramentDescription)) {
                applyForArbitramentRequestBean.arbitrationDescribe = arbitramentDescription;
            }

            List<ImageRequestsBean> imageRequestsList = new ArrayList<>();

            for (int i = 0; i < checkUploadIsSuccessList.size(); i++) {
                CheckUploadIsSuccessBean checkUploadIsSuccessBean = checkUploadIsSuccessList.get(i);

                if (null != checkUploadIsSuccessBean) {
                    int codeId = TypeUtils.getInteger(checkUploadIsSuccessBean.codeId, 0);
                    String imgUrl = TypeUtils.getString(checkUploadIsSuccessBean.imgUrl, "");

                    ImageRequestsBean imageRequestsBean = new ImageRequestsBean();
                    imageRequestsBean.tmpUrl = imgUrl;

                    for (int j = 0; j < filesList.size(); j++) {
                        if (j == codeId) {
                            File file = filesList.get(j);
                            String imageUrl = TypeUtils.getString(imageUrlList.get(j), "");

                            if (null != file) {
                                Bitmap bitmap = BitmapFactory.decodeFile(imageUrl);

                                if (null != bitmap) {
                                    int width = bitmap.getWidth();
                                    int height = bitmap.getHeight();

                                    imageRequestsBean.width = width;
                                    imageRequestsBean.height = height;

                                    String format = null;

                                    if (imgUrl.endsWith("jpg")) {
                                        format = "jpg";
                                    } else if (imgUrl.endsWith("jpeg")) {
                                        format = "jpeg";
                                    } else if (imgUrl.endsWith("png")) {
                                        format = "png";
                                    }

                                    if (!TextUtils.isEmpty(format)) {
                                        imageRequestsBean.format = format;
                                    }

                                    imageRequestsList.add(imageRequestsBean);
                                }
                            }
                        }
                    }

                    applyForArbitramentRequestBean.imageRequests = imageRequestsList;
                }
            }

            mPresenter.applyArbitramentRequest(applyForArbitramentRequestBean);
        }
    }

    @Override
    public void onUploading() {
        ProgressDialog progressDialog = getProgressDialog();

        if (null != progressDialog) {
            boolean isShowProgressDialog = progressDialog.isShowing();

            if (isShowProgressDialog) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(uploadFlagId) && filesList.size() != 0 && imageUrlList.size() != 0) {
                            mPresenter.checkUploadIsSuccessRequest(uploadFlagId, filesList.size());
                        }
                    }
                }, 1000);
            }
        }
    }

    @Override
    public void onUploadFail() {
        dismissProgressDialog();
        showToast(getString(R.string.upload_fail));
    }

    @Override
    public void onApplyForArbitramentSuccess() {
        dismissProgressDialog();
        sendApplyForArbitramentMessage(userId, demandId);
        setResult(CommonConstant.REFRESH_RESULT_CODE);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        dismissProgressDialog();

        if (requestCode == SocketUploader.UPLOAD_FILE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showProgressDialog("正在提交");
                socketUploader.uploadFileList(myUserId, filesList, ApplyForArbitramentActivity.this);
            } else {
                showToast("拒绝权限导致上传失败");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 拍照结果
                case REQUEST_CODE_TAKE_PHOTO:
                    FileInputStream fileInputStream = null;

                    try {
                        Log.i("TanJiaJun", "selectImageFile.length() = " + selectPhotoFile.length());

                        // 获取输入流
                        fileInputStream = new FileInputStream(selectPhotoFile);
                        // 把流解析成bitmap
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 5;
                        Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream, null, options);

                        if (null != bitmap) {
                            saveEncodeJpegImageFile(selectPhotoFile, bitmap, getEncodePercent(bitmap));

                            Log.i("TanJiaJun", "bitmap.getByteCount() = " + bitmap.getByteCount());
                            Log.i("TanJiaJun", "selectImageFile.length() = " + selectPhotoFile.length());

                            if (null != selectPhotoMethodDialog && selectPhotoMethodDialog.isShowing()) {
                                selectPhotoMethodDialog.dismiss();
                            }

                            String imageAbsolutePath = selectPhotoFile.getAbsolutePath();
                            File file = new File(imageAbsolutePath);
                            filesList.add(file);
                            imageUrlList.add(imageAbsolutePath);
                            photoAdapter.setDateList(imageUrlList);

                            checkConfirmSubmitEnable();
                        }
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        // 关闭流
                        try {
                            if (null != fileInputStream) {
                                fileInputStream.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                // 相册选择照片结果
                case ImageUtils.REQUEST_CODE_GET_IMAGE_BY_ALBUM:
                    if (data != null) {
                        if (data.getData() != null) {
                            if (ImageUtils.getAbsolutePathFromUri(this, data.getData()).endsWith("gif")) {
                                showToast(R.string.error_file_format);
                                return;
                            }
                            handlePhoto(new Uri[]{data.getData()});
                        } else {
                            handlePhoto(getUrisFromClipData(data));
                        }
                    } else {
                        showToast(R.string.get_image_error);
                    }
                    break;

                // 用户从系统设置页面返回后重新请求相册
                case ImageUtils.CODE_PERMISSION_FOR_ALBUM:
                    pickPhotoFromAlbum();
                    break;

                // 用户从系统设置页面返回后重新请求相机
                case ImageUtils.CODE_PERMISSION_FOR_CAMERA:
                    pickPhotoFromCamera();
                    break;
            }
        }
    }

}
