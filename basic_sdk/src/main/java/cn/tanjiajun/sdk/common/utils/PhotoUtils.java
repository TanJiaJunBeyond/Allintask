package cn.tanjiajun.sdk.common.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * 照片工具类
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class PhotoUtils {
    private static final String IMAGE_UNSPECIFIED = "image/*";

    /**
     * 拍照
     *
     * @param savePath 保存路径
     * @return
     * @since 0.0.1
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
     * @since 0.0.1
     */
    public static Intent getStartGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setAction(Intent.ACTION_PICK);
        intent.setType(IMAGE_UNSPECIFIED);

        return intent;
    }

    /**
     * 切割图片
     *
     * @param uri 照片路径
     * @since 0.0.1
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

}
