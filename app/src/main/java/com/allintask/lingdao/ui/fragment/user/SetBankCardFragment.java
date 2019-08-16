package com.allintask.lingdao.ui.fragment.user;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.SaveBankCardRequestBean;
import com.allintask.lingdao.bean.user.DepositBankBean;
import com.allintask.lingdao.bean.user.ImageRequestBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.SetBankCardPresenter;
import com.allintask.lingdao.ui.activity.user.BankCardSettingActivity;
import com.allintask.lingdao.ui.fragment.BaseFragment;
import com.allintask.lingdao.utils.BankCardNumberTextWatcher;
import com.allintask.lingdao.utils.ImageUtils;
import com.allintask.lingdao.utils.SocketUploader;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.user.ISetBankCardView;
import com.allintask.lingdao.widget.SelectDepositBankDialog;
import com.allintask.lingdao.widget.SelectPhotoMethodDialog;

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
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

import static android.app.Activity.RESULT_OK;

/**
 * Created by TanJiaJun on 2018/2/26.
 */

public class SetBankCardFragment extends BaseFragment<ISetBankCardView, SetBankCardPresenter> implements ISetBankCardView {

    private static final String PIC_TYPE_JPG = ".jpg";
    private static final String CACHE_PIC_PATH = "CachePics";

    public static final int REQUEST_CODE_TAKE_PHOTO = 3866;

    @BindView(R.id.ll_deposit_bank)
    LinearLayout depositBankLL;
    @BindView(R.id.tv_deposit_bank)
    TextView depositBankTv;
    @BindView(R.id.et_bank_card_number)
    EditText bankCardNumberET;
    @BindView(R.id.rl_upload_bank_card_photo)
    RelativeLayout uploadBankCardPhotoRL;
    @BindView(R.id.iv_bank_card)
    ImageView bankCardIv;
    @BindView(R.id.ll_upload_bank_card_photo)
    LinearLayout uploadBankCardPhotoLL;
    @BindView(R.id.view_bank_card_background)
    View bankCardBackgroundView;
    @BindView(R.id.rl_click_to_upload_bank_card_again)
    RelativeLayout clickToUploadBankCardAgainRL;
    @BindView(R.id.btn_add_bank_card)
    Button addBankCardBtn;

    private int userId;
    private String bankCode;
    private String bankCardNumber;
    private List<DepositBankBean> depositBankList;
    private SelectDepositBankDialog selectDepositBankDialog;
    private SocketUploader socketUploader;

    private SelectPhotoMethodDialog selectPhotoMethodDialog;
    private File selectPhotoFile;
    private String imageUrl;
    private String uploadFlagId;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_set_bank_card;
    }

    @Override
    protected SetBankCardPresenter CreatePresenter() {
        return new SetBankCardPresenter();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initUI();
        initData();
    }

    private void initUI() {
        bankCardNumberET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                bankCardNumber = bankCardNumberET.getText().toString().trim().replace(" ", "");
                checkAddBankCardEnable();
            }
        });

        BankCardNumberTextWatcher.bind(bankCardNumberET);

        addBankCardBtn.setEnabled(false);
        addBankCardBtn.setClickable(false);
    }

    private void initData() {
        userId = UserPreferences.getInstance().getUserId();
        mPresenter.fetchBankInfoListRequest();

        socketUploader = new SocketUploader(getParentContext());
        socketUploader.setOnUploadStatusListener(new SocketUploader.OnUploadStatusListener() {
            @Override
            public void onSuccess(String flagId) {
                uploadFlagId = flagId;
                mPresenter.checkUploadIsSuccessRequest(uploadFlagId);
            }

            @Override
            public void onFail() {
                dismissProgressDialog();
                showToast(getString(R.string.upload_fail));
            }
        });
    }

    private void checkAddBankCardEnable() {
        if (!TextUtils.isEmpty(bankCode) && !TextUtils.isEmpty(bankCardNumber) && null != selectPhotoFile && !TextUtils.isEmpty(imageUrl)) {
            addBankCardBtn.setEnabled(true);
            addBankCardBtn.setClickable(true);
        } else {
            addBankCardBtn.setEnabled(false);
            addBankCardBtn.setClickable(false);
        }
    }

    private void showSelectDepositBankDialog(List<DepositBankBean> depositBankList) {
        selectDepositBankDialog = new SelectDepositBankDialog(getParentContext(), depositBankList);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.BOTTOM;

        Window window = selectDepositBankDialog.getWindow();

        if (null != window) {
            window.setAttributes(layoutParams);
        }

        selectDepositBankDialog.show();
        selectDepositBankDialog.setOnClickListener(new SelectDepositBankDialog.OnClickListener() {
            @Override
            public void onItemClick(String code, String value) {
                if (null != selectDepositBankDialog && selectDepositBankDialog.isShowing()) {
                    selectDepositBankDialog.dismiss();

                    bankCode = code;
                    depositBankTv.setText(value);

                    checkAddBankCardEnable();
                }
            }
        });
    }

    private void showSelectPhotoMethodDialog() {
        selectPhotoMethodDialog = new SelectPhotoMethodDialog(getParentContext());

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

        selectPhotoFile = FileUtils.getCacheFile(getParentContext(), CACHE_PIC_PATH, System.currentTimeMillis() + PIC_TYPE_JPG);

        Uri uri = FileProvider.getUriForFile(getParentContext(), getParentContext().getPackageName() + ".provider", selectPhotoFile);
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

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "选择照片"), ImageUtils.REQUEST_CODE_GET_IMAGE_BY_ALBUM);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public Uri[] getUrisFromClipData(Intent data) {
        ArrayList<Uri> urlList = new ArrayList<>();
        if (data.getClipData() != null) {
            ClipData clipData = data.getClipData();
            int size = clipData.getItemCount();
            for (int i = 0; i < size; i++) {
                if (clipData.getItemAt(i).getUri() != null) {
                    if (!ImageUtils.getAbsoluteImagePath(getParentContext(), clipData.getItemAt(i).getUri()).endsWith("gif")) {
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
        Uri oldUri = uris[0];
        imageUrl = ImageUtils.getAbsolutePathFromUri(getActivity(), oldUri);

        if (isValidImgType(imageUrl)) {
            if (null != selectPhotoMethodDialog && selectPhotoMethodDialog.isShowing()) {
                selectPhotoMethodDialog.dismiss();
            }

            bankCardBackgroundView.setVisibility(View.VISIBLE);
            uploadBankCardPhotoLL.setVisibility(View.GONE);
            clickToUploadBankCardAgainRL.setVisibility(View.VISIBLE);

            selectPhotoFile = new File(imageUrl);
            ImageViewUtil.setImageView(getParentContext(), bankCardIv, imageUrl);

            checkAddBankCardEnable();
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

    @OnClick({R.id.ll_deposit_bank, R.id.rl_upload_bank_card_photo, R.id.btn_add_bank_card})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_deposit_bank:
                if (null != depositBankList && depositBankList.size() > 0) {
                    showSelectDepositBankDialog(depositBankList);
                } else {
                    showToast("暂无开户银行数据");
                }
                break;

            case R.id.rl_upload_bank_card_photo:
                showSelectPhotoMethodDialog();
                break;

            case R.id.btn_add_bank_card:
                if (userId != -1 && null != socketUploader && null != selectPhotoFile) {
                    int bankCardNumberLength = bankCardNumber.length();

                    if (bankCardNumberLength >= 16 && bankCardNumberLength <= 19) {
                        showProgressDialog("正在提交");
                        socketUploader.uploadFile(userId, selectPhotoFile, getActivity());
                    } else {
                        showToast("银行卡号不够位数");
                    }
                }
                break;
        }
    }

    @Override
    public void onShowDepositBankListRequest(List<DepositBankBean> depositBankList) {
        this.depositBankList = depositBankList;
    }

    @Override
    public void uploadSuccess(int status, String codeId, String imageUrl) {
        if (status == 0) {
            ProgressDialog progressDialog = getProgressDialog();

            if (null != progressDialog) {
                boolean isShowProgressDialog = progressDialog.isShowing();

                if (isShowProgressDialog) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPresenter.checkUploadIsSuccessRequest(uploadFlagId);
                        }
                    }, 1000);
                }
            }
        } else if (status == 1) {
            Bitmap bitmap = BitmapFactory.decodeFile(selectPhotoFile.getAbsolutePath());

            if (null != bitmap) {
                int pictureWidth = bitmap.getWidth();
                int pictureHeight = bitmap.getHeight();

                String format = null;

                if (imageUrl.endsWith("jpg")) {
                    format = "jpg";
                } else if (imageUrl.endsWith("jpeg")) {
                    format = "jpeg";
                } else if (imageUrl.endsWith("png")) {
                    format = "png";
                }

                if (!TextUtils.isEmpty(bankCode) && !TextUtils.isEmpty(bankCardNumber) && pictureWidth != 0 && pictureHeight != 0 && !TextUtils.isEmpty(format)) {
                    SaveBankCardRequestBean saveBankCardRequestBean = new SaveBankCardRequestBean();
                    saveBankCardRequestBean.ghtBankCode = bankCode;
                    saveBankCardRequestBean.bankCardNo = bankCardNumber;

                    ImageRequestBean imageRequestBean = new ImageRequestBean();
                    imageRequestBean.tmpUrl = imageUrl;
                    imageRequestBean.width = pictureWidth;
                    imageRequestBean.height = pictureHeight;
                    imageRequestBean.format = format;
                    saveBankCardRequestBean.imageRequest = imageRequestBean;
                    mPresenter.saveBankCardRequest(saveBankCardRequestBean);
                } else {
                    showToast("请求失败");
                }
            }
        } else {
            dismissProgressDialog();
            showToast(getString(R.string.upload_fail));
        }
    }

    @Override
    public void uploadFail() {
        dismissProgressDialog();
        showToast(getString(R.string.upload_fail));
    }

    @Override
    public void onSetBankCardSuccess() {
        showToast("设置银行卡成功");
        ((BankCardSettingActivity) getParentContext()).setResult(CommonConstant.REFRESH_RESULT_CODE);
        ((BankCardSettingActivity) getParentContext()).finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        dismissProgressDialog();

        if (requestCode == SocketUploader.UPLOAD_FILE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showProgressDialog("正在提交");
                socketUploader.uploadFile(userId, selectPhotoFile, getActivity());
            } else {
                showToast("拒绝权限导致上传失败");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                        Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream, null, options);

                        if (null != bitmap) {
                            saveEncodeJpegImageFile(selectPhotoFile, bitmap, 100);

                            Log.i("TanJiaJun", "bitmap.getByteCount() = " + bitmap.getByteCount());
                            Log.i("TanJiaJun", "selectImageFile.length() = " + selectPhotoFile.length());

                            if (null != selectPhotoMethodDialog && selectPhotoMethodDialog.isShowing()) {
                                selectPhotoMethodDialog.dismiss();
                            }

                            bankCardBackgroundView.setVisibility(View.VISIBLE);
                            uploadBankCardPhotoLL.setVisibility(View.GONE);
                            clickToUploadBankCardAgainRL.setVisibility(View.VISIBLE);

                            Uri uri = FileProvider.getUriForFile(getParentContext(), getParentContext().getPackageName() + ".provider", selectPhotoFile);
                            imageUrl = uri.toString();
                            ImageViewUtil.setImageView(getParentContext(), bankCardIv, imageUrl);

                            checkAddBankCardEnable();
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
                    if (null != data) {
                        if (null != data.getData()) {
                            if (ImageUtils.getAbsolutePathFromUri(getActivity(), data.getData()).endsWith("gif")) {
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
