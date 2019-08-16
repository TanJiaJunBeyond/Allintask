package com.allintask.lingdao.ui.activity.user;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.CheckBasicPersonalInformationBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.activity.main.MainActivity;
import com.allintask.lingdao.ui.activity.main.PublishDemandActivity;
import com.allintask.lingdao.ui.activity.main.PublishServiceActivity;
import com.allintask.lingdao.utils.Crop;
import com.allintask.lingdao.presenter.user.CompletePersonalInformationPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.utils.ImageUtils;
import com.allintask.lingdao.utils.SocketUploader;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.user.ICompletePersonalInformationView;
import com.allintask.lingdao.widget.CircleImageView;
import com.allintask.lingdao.widget.EditTextWithDelete;
import com.allintask.lingdao.widget.SelectPhotoMethodDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.common.utils.AgeUtils;
import cn.tanjiajun.sdk.common.utils.FileUtils;
import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;


/**
 * 完善资料
 * Created by zping on 2017/11/29.
 * Rebuild by TanJiaJun on 2018/1/2.
 */

public class CompletePersonalInformationActivity extends BaseActivity<ICompletePersonalInformationView, CompletePersonalInformationPresenter> implements ICompletePersonalInformationView {

    private static final String PIC_TYPE_JPG = ".jpg";
    private static final String CACHE_PIC_PATH = "CachePics";

    public static final int REQUEST_CODE_TAKE_PHOTO = 3866;

    @BindView(R.id.civ_head_portrait)
    CircleImageView headPortraitCIV;
    @BindView(R.id.etwd_real_name)
    EditTextWithDelete realNameETWD;
    @BindView(R.id.rg_gender)
    RadioGroup genderRG;
    @BindView(R.id.rb_male)
    RadioButton maleRB;
    @BindView(R.id.rb_female)
    RadioButton femaleRB;
    @BindView(R.id.rl_birthday)
    RelativeLayout birthdayRL;
    @BindView(R.id.tv_birthday)
    TextView birthdayTv;
    @BindView(R.id.btn_complete_information)
    Button completeInformationBtn;
    @BindView(R.id.tv_complete_later)
    TextView completeLaterTv;

    private int completePersonalInformationType = CommonConstant.COMPLETE_PERSONAL_INFORMATION_TYPE_SET;
    private int publishType = -1;
    private int avatarImageId = -1;
    private String avatarImageUrl;
    private Date birthdayDate;
    private int gender = CommonConstant.MALE;
    private boolean isComplete;
    private String name;
    private boolean realNameLock;

    private int userId;
    private Calendar calendar;
    private int imageId = -1;
    private SocketUploader socketUploader;

    private SelectPhotoMethodDialog selectPhotoMethodDialog;
    private File selectPhotoFile;
    private String imageUrl;
    private String uploadFlagId;

    private int left;
    private int top;
    private int right;
    private int bottom;
    private int width;
    private int height;

    private String birthdayString;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_complete_personal_information;
    }

    @Override
    protected CompletePersonalInformationPresenter CreatePresenter() {
        return new CompletePersonalInformationPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            completePersonalInformationType = intent.getIntExtra(CommonConstant.EXTRA_COMPLETE_PERSONAL_INFORMATION_TYPE, CommonConstant.COMPLETE_PERSONAL_INFORMATION_TYPE_SET);
            publishType = intent.getIntExtra(CommonConstant.EXTRA_PUBLISH_TYPE, -1);
            CheckBasicPersonalInformationBean checkBasicPersonalInformationBean = (CheckBasicPersonalInformationBean) intent.getSerializableExtra(CommonConstant.EXTRA_CHECK_BASIC_PERSONAL_INFORMATION_BEAN);

            if (null != checkBasicPersonalInformationBean) {
                avatarImageId = TypeUtils.getInteger(checkBasicPersonalInformationBean.avatarImageId, 0);

                if (avatarImageId == 0) {
                    avatarImageId = -1;
                }

                avatarImageUrl = TypeUtils.getString(checkBasicPersonalInformationBean.avatarImageUrl, "");

                if (!TextUtils.isEmpty(avatarImageUrl)) {
                    avatarImageUrl = "https:" + avatarImageUrl;
                }

                birthdayDate = checkBasicPersonalInformationBean.birthday;
                Integer tempGender = checkBasicPersonalInformationBean.gender;

                if (null == tempGender) {
                    gender = CommonConstant.MALE;
                }

                isComplete = TypeUtils.getBoolean(checkBasicPersonalInformationBean.isComplete, false);
                name = TypeUtils.getString(checkBasicPersonalInformationBean.name, "");
                realNameLock = TypeUtils.getBoolean(checkBasicPersonalInformationBean.realNameLock, false);
            }
        }

        initUI();
        initData();
    }

    private void initUI() {
        if (completePersonalInformationType == CommonConstant.COMPLETE_PERSONAL_INFORMATION_TYPE_COMPLETE) {
            ImageViewUtil.setImageView(getParentContext(), headPortraitCIV, avatarImageUrl, R.mipmap.ic_head_portrait);

            if (!TextUtils.isEmpty(name)) {
                realNameETWD.setText(name);
            }

            if (gender == CommonConstant.MALE) {
                genderRG.check(R.id.rb_male);
            } else if (gender == CommonConstant.FEMALE) {
                genderRG.check(R.id.rb_female);
            }

            if (null != birthdayDate) {
                birthdayString = CommonConstant.commonDateFormat.format(birthdayDate);
                birthdayTv.setText(birthdayString);
            }
        }

        if (realNameLock) {
            realNameETWD.setFocusable(false);
            realNameETWD.setFocusableInTouchMode(false);
        } else {
            realNameETWD.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    checkCompleteInformationEnable();
                }
            });
        }

        genderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.rb_male:
                        gender = CommonConstant.MALE;
                        break;

                    case R.id.rb_female:
                        gender = CommonConstant.FEMALE;
                        break;
                }

                checkCompleteInformationEnable();
            }
        });

        checkCompleteInformationEnable();
    }

    private void initData() {
        userId = UserPreferences.getInstance().getUserId();
        calendar = Calendar.getInstance();
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

    private void checkCompleteInformationEnable() {
        String realName = realNameETWD.getText().toString().trim();

        if (!TextUtils.isEmpty(realName) && !TextUtils.isEmpty(birthdayString)) {
            completeInformationBtn.setEnabled(true);
            completeInformationBtn.setClickable(true);
        } else {
            completeInformationBtn.setEnabled(false);
            completeInformationBtn.setClickable(false);
        }
    }

    private void showBirthdayDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getParentContext(), DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                try {
                    String yearStr = String.valueOf(year);
                    String monthStr = String.valueOf(month + 1);
                    String dateStr = String.valueOf(dayOfMonth);

                    if (monthStr.length() == 1) {
                        monthStr = "0" + monthStr;
                    }

                    if (dateStr.length() == 1) {
                        dateStr = "0" + dateStr;
                    }

                    birthdayString = yearStr + "-" + monthStr + "-" + dateStr;
                    Date birthdayDate = CommonConstant.commonDateFormat.parse(birthdayString);
                    int age = AgeUtils.getAge(birthdayDate);

                    if (age >= 0) {
                        if (age >= 16) {
                            birthdayTv.setText(birthdayString);
                            birthdayTv.setTextColor(getResources().getColor(R.color.text_dark_black));

                            checkCompleteInformationEnable();
                        } else {
                            showToast("未满16周岁不允许注册");
                        }
                    } else {
                        showToast("请选择正确的生日日期");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

        datePickerDialog.show();
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
        Uri oldUri = uris[0];
        String imageUrl = ImageUtils.getAbsolutePathFromUri(this, uris[0]);
        selectPhotoFile = new File(imageUrl);

        if (isValidImgType(imageUrl)) {
            if (null != selectPhotoMethodDialog && selectPhotoMethodDialog.isShowing()) {
                selectPhotoMethodDialog.dismiss();
            }

            ImageViewUtil.setImageView(getParentContext(), headPortraitCIV, imageUrl, R.mipmap.ic_head_portrait);

            File newFile = FileUtils.getCacheFile(this, CACHE_PIC_PATH, System.currentTimeMillis() + PIC_TYPE_JPG);
            Uri newUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", newFile);

            Crop.of(oldUri, newUri).asSquare().start(CompletePersonalInformationActivity.this, Crop.REQUEST_CROP);
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

    @OnClick({R.id.civ_head_portrait, R.id.rl_birthday, R.id.btn_complete_information, R.id.tv_complete_later})
    public void onClick(View view) {
        String realName = realNameETWD.getText().toString().trim();

        switch (view.getId()) {
            case R.id.civ_head_portrait:
                showSelectPhotoMethodDialog();
                break;

            case R.id.rl_birthday:
                showBirthdayDatePickerDialog();
                break;

            case R.id.btn_complete_information:
                if (completePersonalInformationType == CommonConstant.COMPLETE_PERSONAL_INFORMATION_TYPE_SET) {
                    if (null != selectPhotoFile) {
                        showProgressDialog("正在提交");
                        socketUploader.uploadFile(userId, selectPhotoFile, CompletePersonalInformationActivity.this);
                    } else {
                        showToast("请上传头像");
                    }
                } else if (completePersonalInformationType == CommonConstant.COMPLETE_PERSONAL_INFORMATION_TYPE_COMPLETE) {
                    if (null != selectPhotoFile) {
                        int realNameLength = realName.length();

                        if (realNameLength >= 2) {
                            showProgressDialog("正在提交");
                            socketUploader.uploadFile(userId, selectPhotoFile, CompletePersonalInformationActivity.this);
                        } else {
                            showToast("真实姓名长度必须是2~4位");
                        }
                    } else {
                        if (avatarImageId != -1) {
                            int realNameLength = realName.length();

                            if (realNameLength >= 2) {
                                mPresenter.updateBasicPersonalInformationRequest(-1, null, -1, -1, "", -1, -1, -1, -1, realName, gender, birthdayString);
                            } else {
                                showToast("真实姓名长度必须是2~4位");
                            }
                        } else {
                            showToast("请上传头像");
                        }
                    }
                }
                break;

            case R.id.tv_complete_later:
                if (completePersonalInformationType == CommonConstant.COMPLETE_PERSONAL_INFORMATION_TYPE_SET) {
                    Intent mainIntent = new Intent(getParentContext(), MainActivity.class);
                    startActivity(mainIntent);
                }

                finish();
                break;
        }
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

                String realName = realNameETWD.getText().toString().trim();

                if (completePersonalInformationType == CommonConstant.COMPLETE_PERSONAL_INFORMATION_TYPE_SET) {
                    if (pictureWidth != 0 && pictureHeight != 0 && !TextUtils.isEmpty(format) && width != 0 && height != 0 && !TextUtils.isEmpty(realName) && !TextUtils.isEmpty(birthdayString)) {
                        mPresenter.completePersonalInformationRequest(imageUrl, pictureWidth, pictureHeight, format, left, top, width, height, realName, gender, birthdayString);
                    } else {
                        dismissProgressDialog();
                        showToast("请求失败");
                    }
                } else if (completePersonalInformationType == CommonConstant.COMPLETE_PERSONAL_INFORMATION_TYPE_COMPLETE) {
                    imageId = avatarImageId;

                    if (null == selectPhotoFile) {
                        mPresenter.updateBasicPersonalInformationRequest(imageId, null, -1, -1, null, -1, -1, -1, -1, realName, gender, birthdayString);
                    } else {
                        if (pictureWidth != 0 && pictureHeight != 0 && !TextUtils.isEmpty(format) && width != 0 && height != 0 && !TextUtils.isEmpty(realName) && !TextUtils.isEmpty(birthdayString)) {
                            mPresenter.updateBasicPersonalInformationRequest(imageId, imageUrl, pictureWidth, pictureHeight, format, left, top, width, height, realName, gender, birthdayString);
                        } else {
                            dismissProgressDialog();
                            showToast("请求失败");
                        }
                    }
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
    public void onCompletePersonalInformationSuccess() {
        Intent completeInformationIntent = new Intent(getParentContext(), SelectSkillsActivity.class);
        startActivity(completeInformationIntent);

        finish();
    }

    @Override
    public void onUpdatePersonalInformationSuccess() {
        if (publishType == CommonConstant.PUBLISH_TYPE_PUBLISH_SERVICE) {
            Intent intent = new Intent(getParentContext(), PublishServiceActivity.class);
            startActivity(intent);
        } else if (publishType == CommonConstant.PUBLISH_TYPE_PUBLISH_DEMAND) {
            Intent intent = new Intent(getParentContext(), PublishDemandActivity.class);
            startActivity(intent);
        }

        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        dismissProgressDialog();

        if (requestCode == SocketUploader.UPLOAD_FILE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showProgressDialog("正在提交");
                socketUploader.uploadFile(userId, selectPhotoFile, CompletePersonalInformationActivity.this);
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
                        Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream, null, options);

                        if (null != bitmap) {
                            saveEncodeJpegImageFile(selectPhotoFile, bitmap, 100);

                            Log.i("TanJiaJun", "bitmap.getByteCount() = " + bitmap.getByteCount());
                            Log.i("TanJiaJun", "selectImageFile.length() = " + selectPhotoFile.length());

                            if (null != selectPhotoMethodDialog && selectPhotoMethodDialog.isShowing()) {
                                selectPhotoMethodDialog.dismiss();
                            }

                            imageUrl = selectPhotoFile.getAbsolutePath();
                            ImageViewUtil.setImageView(getParentContext(), headPortraitCIV, imageUrl, R.mipmap.ic_head_portrait);

                            Uri oldUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", selectPhotoFile);

                            File file = FileUtils.getCacheFile(this, CACHE_PIC_PATH, System.currentTimeMillis() + PIC_TYPE_JPG);
                            Uri newUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);

                            Crop.of(oldUri, newUri).asSquare().start(CompletePersonalInformationActivity.this, Crop.REQUEST_CROP);
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

                case Crop.REQUEST_CROP:
                    if (null != data) {
                        left = data.getIntExtra(CommonConstant.EXTRA_CROP_IMAGE_LEFT, 0);
                        top = data.getIntExtra(CommonConstant.EXTRA_CROP_IMAGE_TOP, 0);
                        right = data.getIntExtra(CommonConstant.EXTRA_CROP_IMAGE_RIGHT, 0);
                        bottom = data.getIntExtra(CommonConstant.EXTRA_CROP_IMAGE_BOTTOM, 0);
                        width = data.getIntExtra(CommonConstant.EXTRA_CROP_IMAGE_WIDTH, 0);
                        height = data.getIntExtra(CommonConstant.EXTRA_CROP_IMAGE_HEIGHT, 0);

                        Uri uri = Crop.getOutput(data);
                        ImageViewUtil.setImageView(getParentContext(), headPortraitCIV, uri.toString(), R.mipmap.ic_head_portrait);
                    }
                    break;
            }
        }
    }

}
