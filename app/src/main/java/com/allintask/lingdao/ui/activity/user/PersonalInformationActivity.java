package com.allintask.lingdao.ui.activity.user;

import android.annotation.TargetApi;
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
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.PersonalInformationBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.PersonalInformationPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.ui.adapter.user.PersonalInformationAdapter;
import com.allintask.lingdao.utils.Crop;
import com.allintask.lingdao.utils.ImageUtils;
import com.allintask.lingdao.utils.SocketUploader;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.user.IPersonalInformationView;
import com.allintask.lingdao.widget.CommonRecyclerView;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.tanjiajun.sdk.common.utils.AgeUtils;
import cn.tanjiajun.sdk.common.utils.FileUtils;
import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/1/18.
 */

public class PersonalInformationActivity extends BaseActivity<IPersonalInformationView, PersonalInformationPresenter> implements IPersonalInformationView {

    private static final String PIC_TYPE_JPG = ".jpg";
    private static final String CACHE_PIC_PATH = "CachePics";

    public static final int REQUEST_CODE_TAKE_PHOTO = 3866;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.recycler_view)
    CommonRecyclerView recyclerView;

    private PersonalInformationAdapter personalInformationAdapter;

    private int userId;
    private String name;
    private boolean mRealNameLock;
    private SocketUploader socketUploader;

    private int imageId = -1;

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

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_personal_information;
    }

    @Override
    protected PersonalInformationPresenter CreatePresenter() {
        return new PersonalInformationPresenter();
    }

    @Override
    protected void init() {
        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(CommonConstant.REFRESH_RESULT_CODE);
                finish();
            }
        });

        toolbar.setTitle("");

        titleTv.setText(getString(R.string.personal_information));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        personalInformationAdapter = new PersonalInformationAdapter(getParentContext());
        recyclerView.setAdapter(personalInformationAdapter);

        personalInformationAdapter.setOnConfirmListener(new PersonalInformationAdapter.OnConfirmListener() {
            @Override
            public void onSetOrModifyUserHeadPortrait() {
                showSelectPhotoMethodDialog();
            }

            @Override
            public void onSetOrModifyName() {
                if (mRealNameLock) {
                    showToast("用户处于名字不可修改状态");
                } else {
                    Intent intent = new Intent(getParentContext(), CompileNameActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_NAME, name);
                    startActivityForResult(intent, CommonConstant.REQUEST_CODE);
                }
            }

            @Override
            public void onBirthdayDialogConfirm(String birthday, int age) {
                if (age >= 0) {
                    if (age >= 16) {
                        mPresenter.setBirthdayRequest(birthday, age);
                    } else {
                        showToast("未满16周岁不允许注册");
                    }
                } else {
                    showToast("请选择正确的生日日期");
                }
            }

            @Override
            public void onGenderDialogConfirm(int gender) {
                mPresenter.setGenderRequest(gender);
            }

            @Override
            public void onStartWorkTimeConfirm(String startWorkTime) {
                mPresenter.setStartWorkAtRequest(startWorkTime);
            }

            @Override
            public void onAddPersonalInformation(int position, List<PersonalInformationBean.EducationalExperienceBean> educationalExperienceList, List<PersonalInformationBean.WorkExperienceBean> workExperienceList, List<PersonalInformationBean.HonorAndQualificationBean> honorAndQualificationList) {
                Intent intent = new Intent(getParentContext(), AddPersonalInformationActivity.class);

                if (position == educationalExperienceList.size() + 1) {
                    intent.putExtra(CommonConstant.EXTRA_ADD_PERSONAL_INFORMATION_TYPE, CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_EDUCATION_EXPERIENCE);
                } else if (position == educationalExperienceList.size() + workExperienceList.size() + 2) {
                    intent.putExtra(CommonConstant.EXTRA_ADD_PERSONAL_INFORMATION_TYPE, CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_WORK_EXPERIENCE);
                } else if (position == educationalExperienceList.size() + workExperienceList.size() + honorAndQualificationList.size() + 3) {
                    intent.putExtra(CommonConstant.EXTRA_ADD_PERSONAL_INFORMATION_TYPE, CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_HONOR_AND_QUALIFICATION);
                }

                startActivityForResult(intent, CommonConstant.REQUEST_CODE);
            }

            @Override
            public void onModifyPersonalInformation(String personalInformationType, int id) {
                if (personalInformationType.equals(CommonConstant.EXTRA_EDUCATIONAL_EXPERIENCE_ID)) {
                    Intent intent = new Intent(getParentContext(), EducationalExperienceActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_EDUCATIONAL_EXPERIENCE_ID, id);
                    startActivityForResult(intent, CommonConstant.REQUEST_CODE);
                } else if (personalInformationType.equals(CommonConstant.EXTRA_WORK_EXPERIENCE_ID)) {
                    Intent intent = new Intent(getParentContext(), WorkExperienceActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_WORK_EXPERIENCE_ID, id);
                    startActivityForResult(intent, CommonConstant.REQUEST_CODE);
                } else if (personalInformationType.equals(CommonConstant.EXTRA_HONOR_AND_QUALIFICATION_ID)) {
                    Intent intent = new Intent(getParentContext(), HonorAndQualificationActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_HONOR_AND_QUALIFICATION_ID, id);
                    startActivityForResult(intent, CommonConstant.REQUEST_CODE);
                }
            }

            @Override
            public void onDeletePersonalInformation(String personalInformationType, int id) {
                if (personalInformationType.equals(CommonConstant.EXTRA_EDUCATIONAL_EXPERIENCE_ID)) {
                    mPresenter.removeEducationExperienceRequest(id);
                } else if (personalInformationType.equals(CommonConstant.EXTRA_WORK_EXPERIENCE_ID)) {
                    mPresenter.removeWorkExperienceRequest(id);
                } else if (personalInformationType.equals(CommonConstant.EXTRA_HONOR_AND_QUALIFICATION_ID)) {
                    mPresenter.removeHonorAndQualificationRequest(id);
                }
            }
        });

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

    private void initData() {
        userId = UserPreferences.getInstance().getUserId();
        mPresenter.fetchPersonalInfoRequest();
    }

    private void showSelectPhotoMethodDialog() {
        selectPhotoMethodDialog = new SelectPhotoMethodDialog(this);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.BOTTOM;

        Window window = selectPhotoMethodDialog.getWindow();
        window.setAttributes(layoutParams);

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

            File newFile = FileUtils.getCacheFile(this, CACHE_PIC_PATH, System.currentTimeMillis() + PIC_TYPE_JPG);
            Uri newUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", newFile);

            Crop.of(oldUri, newUri).asSquare().start(PersonalInformationActivity.this, Crop.REQUEST_CROP);
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

                if (pictureWidth != 0 && pictureHeight != 0 && !TextUtils.isEmpty(format) && width != 0 && height != 0) {
                    mPresenter.setUserHeadPortraitRequest(imageId, imageUrl, pictureWidth, pictureHeight, format, left, top, width, height);
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
    public void onSetUserHeadPortraitSuccess() {
        dismissProgressDialog();
        initData();
    }

    @Override
    public void onShowPersonalInformationBean(PersonalInformationBean personalInformationBean) {
        if (null != personalInformationBean) {
            int avatarImageId = TypeUtils.getInteger(personalInformationBean.avatarImageId, -1);
            String avatarImageUrl = TypeUtils.getString(personalInformationBean.avatarImageUrl, "");
            String realName = TypeUtils.getString(personalInformationBean.realName, "");
            mRealNameLock = TypeUtils.getBoolean(personalInformationBean.realNameLock, false);
            Date birthday = personalInformationBean.birthday;
            int gender = TypeUtils.getInteger(personalInformationBean.gender, 0);
            Date startWorkAt = personalInformationBean.startWorkAt;
            List<PersonalInformationBean.EducationalExperienceBean> educationalExperienceList = personalInformationBean.userEduInfos;
            List<PersonalInformationBean.WorkExperienceBean> workExperienceList = personalInformationBean.userWorkInfos;
            List<PersonalInformationBean.HonorAndQualificationBean> honorAndQualificationList = personalInformationBean.userHonorInfos;

            imageId = avatarImageId;

            if (null != personalInformationAdapter) {
                personalInformationAdapter.setUserHeadPortraitUrl(avatarImageUrl);
                personalInformationAdapter.setName(realName);

                if (null != birthday) {
                    int age = AgeUtils.getAge(birthday);
                    personalInformationAdapter.setAge(age);
                }

                personalInformationAdapter.setGender(gender);

                if (null != startWorkAt) {
                    String startWorkTime = CommonConstant.commonDateFormat.format(startWorkAt);
                    personalInformationAdapter.setStartWorkTime(startWorkTime);
                }

                personalInformationAdapter.setEducationalExperienceList(educationalExperienceList);
                personalInformationAdapter.setWorkExperienceList(workExperienceList);
                personalInformationAdapter.setHonorAndQualificationList(honorAndQualificationList);
                personalInformationAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onSetNameSuccess(String name) {
        this.name = name;

        if (null != personalInformationAdapter) {
            personalInformationAdapter.setName(name);
        }
    }

    @Override
    public void onSetBirthdaySuccess(int age) {
        if (null != personalInformationAdapter) {
            personalInformationAdapter.setAge(age);
        }
    }

    @Override
    public void onSetGenderSuccess(int gender) {
        if (null != personalInformationAdapter) {
            personalInformationAdapter.setGender(gender);
        }
    }

    @Override
    public void onSetStartWorkAtSuccess(String startWorkTime) {
        if (null != personalInformationAdapter) {
            personalInformationAdapter.setStartWorkTime(startWorkTime);
        }
    }

    @Override
    public void onRemoveEducationExperienceSuccess() {
        initData();
    }

    @Override
    public void onRemoveWorkExperienceSuccess() {
        initData();
    }

    @Override
    public void onRemoveHonorAndQualificationSuccess() {
        initData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        dismissProgressDialog();

        if (requestCode == SocketUploader.UPLOAD_FILE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showProgressDialog("正在提交");
                socketUploader.uploadFile(userId, selectPhotoFile, PersonalInformationActivity.this);
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

                            Uri oldUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", selectPhotoFile);

                            File file = FileUtils.getCacheFile(this, CACHE_PIC_PATH, System.currentTimeMillis() + PIC_TYPE_JPG);
                            Uri newUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);

                            Crop.of(oldUri, newUri).asSquare().start(PersonalInformationActivity.this, Crop.REQUEST_CROP);
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
                        imageUrl = uri.toString();

                        showProgressDialog(getString(R.string.uploading));

                        if (userId != -1 && null != selectPhotoFile) {
                            socketUploader.uploadFile(userId, selectPhotoFile, PersonalInformationActivity.this);
                        }
                    }
                    break;
            }
        }

        if (requestCode == CommonConstant.REQUEST_CODE && resultCode == CommonConstant.RESULT_CODE) {
            if (null != data) {
                String name = data.getStringExtra(CommonConstant.EXTRA_NAME);

                if (null != personalInformationAdapter) {
                    personalInformationAdapter.setName(name);
                }
            }
        }

        if (requestCode == CommonConstant.REQUEST_CODE && resultCode == CommonConstant.REFRESH_RESULT_CODE) {
            initData();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(CommonConstant.REFRESH_RESULT_CODE);
        super.onBackPressed();
    }

}
