package cn.tanjiajun.sdk.component.custom.image.imageloader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import cn.tanjiajun.sdk.common.utils.Validator;

/**
 * 拓展的图片下载器
 *
 * @author TanJiaJun
 * @version 0.0.1
 */

public class ExtendedImageDownloader extends BaseImageDownloader {

    public ExtendedImageDownloader(Context context) {
        super(context);
    }

    public ExtendedImageDownloader(Context context, int connectTimeout, int readTimeout) {
        super(context, connectTimeout, readTimeout);
    }

    @Override
    protected InputStream getStreamFromOtherSource(String imageUri, Object extra) throws IOException {

        if (Validator.isLocalFilePathValid(imageUri)) {
            return new BufferedInputStream(new FileInputStream(imageUri), BUFFER_SIZE);
        }
        return super.getStreamFromOtherSource(imageUri, extra);
    }

}
