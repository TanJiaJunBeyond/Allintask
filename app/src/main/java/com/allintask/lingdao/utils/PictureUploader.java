package com.allintask.lingdao.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.allintask.lingdao.R;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import cn.tanjiajun.sdk.common.utils.FileUtils;
import cn.tanjiajun.sdk.common.utils.ImageUtils;
import cn.tanjiajun.sdk.component.custom.dialog.BasicDialog;

public abstract class PictureUploader implements OnClickListener {

    protected static final String PIC_TYPE_JPG = ".jpg";
    protected static final String CACHE_PIC_PATH = "CachePics";

    private static final String IMAGE_UNSPECIFIED = "image/*";

    protected boolean isCancelable = true;

    private File selectImageFile;

    private OnSelectPictureListener onSelectPictureListener;

    /**
     * 相机拍照request code
     **/
    public static final int REQUEST_CODE_TAKE_PHOTO = 3866;
    /**
     * 相册选择图片request code
     **/
    public static final int REQUEST_CODE_PICK_PHOTO = 3868;

    protected Context context;
    protected String dialogTitle;

    protected BasicDialog pickerPicDialog;
    protected ProgressDialog baseProgressDialog;

    public PictureUploader(Context context) {
        this(context, null);
    }

    public PictureUploader(Context context, String dialogTitle) {
        super();
        this.context = context;
        this.dialogTitle = dialogTitle;

        init();
    }

    private void init() {
        BasicDialog.Builder bBuilder = new BasicDialog.Builder(context);

        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_picture_picker, null);
        contentView.findViewById(R.id.ll_take_picture).setOnClickListener(this);
        contentView.findViewById(R.id.ll_select_picture).setOnClickListener(this);
        bBuilder.setContentView(contentView);
        bBuilder.setTitle(!TextUtils.isEmpty(dialogTitle) ? dialogTitle : "添加照片");

        pickerPicDialog = bBuilder.create();
    }

    public void showPicturePickerDialog() {
        if (null != pickerPicDialog && !pickerPicDialog.isShowing()) {
            pickerPicDialog.show();
        }

    }

    public void dismissPicturePickerDialog() {
        if (null != pickerPicDialog && pickerPicDialog.isShowing()) {
            pickerPicDialog.dismiss();
        }
    }

    public void showProgressDialog() {
        showProgressDialog(null);
    }

    public void showProgressDialog(String msg) {
        if (null == baseProgressDialog) {
            baseProgressDialog = new ProgressDialog(context);
            baseProgressDialog.setCancelable(isCancelable);
        }

        if (null != baseProgressDialog && !baseProgressDialog.isShowing()) {
            if (!TextUtils.isEmpty(msg)) {
                baseProgressDialog.setMessage(msg);
            }
            baseProgressDialog.show();
        }
    }

    protected void dismissProgressDialog() {
        if (null != baseProgressDialog && baseProgressDialog.isShowing()) {
            baseProgressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_take_picture:
                boolean hasCameraPermissions = PermissionsUtil.hasCameraPermissions();

                if (hasCameraPermissions) {
                    selectImageFile = FileUtils.getCacheFile(context, CACHE_PIC_PATH, System.currentTimeMillis() + PIC_TYPE_JPG);//创建压缩图片文件

                    Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", selectImageFile);
                    Intent openTakePic = getStartCameraIntent();
                    openTakePic.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    ((BaseActivity) context).startActivityForResult(openTakePic, REQUEST_CODE_TAKE_PHOTO);

                    if (null != onSelectPictureListener) {
                        onSelectPictureListener.onStartSelect(REQUEST_CODE_TAKE_PHOTO);
                    }

                    dismissPicturePickerDialog();
                }
                break;
            case R.id.ll_select_picture:
                selectImageFile = null;
                Intent openGallery = getStartGalleryIntent();
                ((BaseActivity) context).startActivityForResult(openGallery, REQUEST_CODE_PICK_PHOTO);

                if (null != onSelectPictureListener) {
                    onSelectPictureListener.onStartSelect(REQUEST_CODE_PICK_PHOTO);
                }

                dismissProgressDialog();
                break;

            default:
                break;
        }
    }

    public void savePicture(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
//                Bundle bundle = data.getExtras();
//                Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

//                if (null != bitmap) {
//                    if (null != onSelectPictureListener) {
//                        onSelectPictureListener.onFinishSelected(bitmap);
//                    }
//                    ImageUtils.saveEncodeJpegImageFile(selectImageFile, bitmap, getEncodePercent(bitmap));
//
//                }
                FileInputStream fileInputStream = null;

                try {
                    Log.i("cth", "selectImageFile.length() = " + selectImageFile.length());
                    // 获取输入流
                    fileInputStream = new FileInputStream(selectImageFile);
                    // 把流解析成bitmap
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 5;
                    Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream, null, options);

                    if (null != bitmap) {
                        saveEncodeJpegImageFile(selectImageFile, bitmap, getEncodePercent(bitmap));
                        Log.i("cth", "bitmap.getByteCount() = " + bitmap.getByteCount());
                        Log.i("cth", "selectImageFile.length() = " + selectImageFile.length());
                        if (null != onSelectPictureListener) {
                            onSelectPictureListener.onFinishSelected(bitmap);
                        }

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
            } else if (requestCode == REQUEST_CODE_PICK_PHOTO) {
                File sdFile = Environment.getExternalStorageDirectory();

                boolean canWrite = sdFile.canWrite();

                if (canWrite) {
                    Uri imvUri = data.getData();
                    String imagePath = ImageUtils.getAbsoluteImagePathByUri(context, imvUri);

                    if (null != imagePath) {
                        File imageFile = new File(imagePath);
                        if (null != imageFile && imageFile.isFile()) {
                            HashCodeFileNameGenerator generator = new HashCodeFileNameGenerator();
                            String tmpImageFileName = generator.generate(imageFile.getName());
                            selectImageFile = FileUtils.getCacheFile(context, CACHE_PIC_PATH, tmpImageFileName + PIC_TYPE_JPG);//创建压缩图片文件

                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 5;
                            Bitmap imageBitmap = BitmapFactory.decodeFile(imagePath, options);
                            saveEncodeJpegImageFile(selectImageFile, imageBitmap, getEncodePercent(imageBitmap));

                            if (null != onSelectPictureListener) {
                                onSelectPictureListener.onFinishSelected(imageBitmap);
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "SD卡无法写入", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public static int getEncodePercent(Bitmap bitmap) {
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

        Log.i("cth", "bitmap.getByteCount() = " + bitmap.getByteCount());
        Log.i("cth", "encodePercent = " + encodePercent);
        return encodePercent;
    }

    /**
     * 拍照
     *
     * @param savePath
     * @return
     */
    public static Intent getStartCameraIntent(String savePath) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File out = new File(savePath);

        if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
        }

        Uri uri = Uri.fromFile(out);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    private Intent getStartCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        return intent;
    }

    /**
     * 打开相册
     *
     * @return
     */
    private Intent getStartGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setAction(Intent.ACTION_PICK);
        intent.setType(IMAGE_UNSPECIFIED);
        return intent;
    }

    public static boolean saveEncodeJpegImageFile(File file, Bitmap imageBitmap, int encodePercent) {
        return saveEncodeImageFile(file, imageBitmap, Bitmap.CompressFormat.JPEG, encodePercent);
    }

    private static boolean saveEncodeImageFile(File file, Bitmap imageBitmap, Bitmap.CompressFormat format, int encodePercent) {
        boolean isSaved = true;

        encodePercent = (encodePercent > 0 && encodePercent <= 100 ? encodePercent : 100);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        imageBitmap.compress(format, encodePercent, out);// (quality:0-100)压缩文件
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        FileOutputStream fileIn = null;

        try {
            fileIn = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                fileIn.write(buffer, 0, len);
            }
            fileIn.flush();
        } catch (FileNotFoundException e) {
            isSaved = false;
            e.printStackTrace();
        } catch (IOException e) {
            isSaved = false;
            e.printStackTrace();
        } finally {
            if (null != fileIn) {
                try {
                    fileIn.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return isSaved;
    }

    public void sendUploadPictureRequest() {
        if (null != selectImageFile) {
            uploadPictureRequest(selectImageFile);
        } else {
            Toast.makeText(context, "未选择文件", Toast.LENGTH_SHORT).show();
        }
    }

    protected Map<String, Object> convertStringToMap(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        } else {
//            return new CommonJSONParser().parse(str);
            return null;
        }
    }

    public void clear() {
        if (null != selectImageFile && selectImageFile.exists()) {
            selectImageFile.delete();
        }
    }

    public OnSelectPictureListener getOnSelectPictureListener() {
        return onSelectPictureListener;
    }

    public void setOnSelectPictureListener(OnSelectPictureListener onSelectPictureListener) {
        this.onSelectPictureListener = onSelectPictureListener;
    }

    public abstract void uploadPictureRequest(File pictureFile);

    public interface OnUploadFinishCallback {
        void onUpload(File pictureFile);
    }

    public interface OnSelectPictureListener {

        void onStartSelect(int selectMode);

        void onFinishSelected(Bitmap bitmap);

    }

}
