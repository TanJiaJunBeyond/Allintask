package com.allintask.lingdao.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.io.IOException;

import cn.tanjiajun.sdk.common.utils.DensityUtils;

public class ImageUtils {

    private static final String IMAGE_UNSPECIFIED = "image/*";

    public static final int CAMERA = 1;
    public static final int ALBUM = 2;

    /**
     * 请求相册
     */
    public static final int REQUEST_CODE_GET_IMAGE_BYCORP = 0x01;

    /**
     * 请求相机
     */
    public static final int REQUEST_CODE_GET_IMAGE_BY_CAMERA = 0x02;

    /**
     * 请求裁剪
     */
    public static final int REQUEST_CODE_GET_IMAGE_BY_ALBUM = 0x03;

    // 申请相机权限
    public static final int CODE_PERMISSION_FOR_CAMERA = 0x04;

    // 申请读取内存空间权限
    public static final int CODE_PERMISSION_FOR_ALBUM = 0x05;

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

    /**
     * 打开相册
     *
     * @return
     */
    public static Intent getStartGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setAction(Intent.ACTION_PICK);
        intent.setType(IMAGE_UNSPECIFIED);
        return intent;
    }

    /**
     * 获得图片的绝对值路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getAbsoluteImagePath(Context context, Uri uri) {

        if (null == context || null == uri) {
            return null;
        }

        String type = MediaStore.Images.Media.DATA;

        String[] proj = {type};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);

        if (null == cursor) {
            return null;
        } else {
            int column_index = cursor.getColumnIndexOrThrow(type);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
    }

    /**
     * 切割图片
     *
     * @param uri
     */
    public static Intent getStartPhotoZoomIntent(Context context, Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽、高
        intent.putExtra("outputX", DensityUtils.dip2px(context, 120.0f));
        intent.putExtra("outputY", DensityUtils.dip2px(context, 120.0f));
        intent.putExtra("return-data", true);
        return intent;
    }

    /**
     * 旋转图片
     *
     * @param imgpath
     * @return
     */
    public static Bitmap rotateBitmap(String imgpath) {
        Options opts = new Options();
        opts.inSampleSize = 2;
        Bitmap bm = BitmapFactory.decodeFile(imgpath, opts);
        int digree = 0;
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(imgpath);
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
        }

        if (exif != null) {
            // 读取图片中相机方向信息  
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            // 计算旋转角度  
            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    digree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    digree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    digree = 270;
                    break;
                default:
                    digree = 0;
                    break;
            }
        }

        if (digree != 0) {
            // 旋转图片  
            Matrix m = new Matrix();
            m.postRotate(digree);
            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
        }
        return bm;
    }

    /**
     * 检查相册或者相机需要的权限，并且申请权限
     *
     * @param contextObject Activity或者Fragment
     * @param type          类型checkPermission
     */
    public static boolean checkPermission(final Object contextObject, int type) {
        if (contextObject == null) return false;
        switch (type) {
            case CAMERA:
                if (contextObject instanceof Fragment) { // 未授权
                    Fragment fragment = (Fragment) contextObject;
                    if (ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        return true;
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            fragment.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, CODE_PERMISSION_FOR_CAMERA);
                        } else {
                            fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, CODE_PERMISSION_FOR_CAMERA);
                        }
                        return false;
                    }
                } else if (contextObject instanceof Activity) {// 未授权
                    Activity activity = (Activity) contextObject;
                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        return true;
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, CODE_PERMISSION_FOR_CAMERA);
                        } else {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, CODE_PERMISSION_FOR_CAMERA);
                        }
                        return false;
                    }
                } else
                    return false;
            case ALBUM:
                if (contextObject instanceof Fragment) {// 未授权
                    Fragment fragment = (Fragment) contextObject;
                    if (ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        return true;
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            fragment.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_PERMISSION_FOR_ALBUM);
                        } else {
                            fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_PERMISSION_FOR_ALBUM);
                        }
                        return false;
                    }
                } else if (contextObject instanceof Activity) {// 未授权
                    Activity activity = (Activity) contextObject;
                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        return true;
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_PERMISSION_FOR_ALBUM);
                        } else {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_PERMISSION_FOR_ALBUM);
                        }
                        return false;
                    }
                } else
                    return false;
        }
        return false;
    }

    /**
     * 选择相册
     *
     * @param context 上下文
     */
    public static void pickPhotoFromAlbum(Activity context) {
        if (!checkPermission(context, ALBUM)) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        context.startActivityForResult(Intent.createChooser(intent, "选择照片"), REQUEST_CODE_GET_IMAGE_BY_ALBUM);
    }

    /**
     * 选择拍照
     */
    public static void pickPhotoFromCamera(Activity context, Uri cameraUri) {
        if (!checkPermission(context, CAMERA)) {
            return;
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        context.startActivityForResult(intent, REQUEST_CODE_GET_IMAGE_BY_CAMERA);
    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     */
    public static String getAbsolutePathFromUri(final Activity context, Uri imageUri) {
        if (context == null || imageUri == null) return null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(imageUri.getScheme())) { // MediaStore (and general)
            // Return the remote address
            if (isGooglePhotosUri(imageUri)) return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        } else if ("file".equalsIgnoreCase(imageUri.getScheme())) { // File
            return imageUri.getPath();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);

            if (null != cursor && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return null;
    }

}
