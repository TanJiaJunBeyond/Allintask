package cn.tanjiajun.sdk.component.custom.image.imageloader;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * ImageLoader图片加载完成监听器
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public interface IImageOnLoadingCompleteListener {

    public void onLoadingComplete(ImageView imageView, Bitmap bitmap);

}
