package com.allintask.lingdao.widget.chatrow;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.message.EaseImageCache;
import com.allintask.lingdao.utils.EaseImageUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;

import java.io.File;

public class EaseChatRowImage extends EaseChatRowFile {

    protected ImageView imageView;
    private EMImageMessageBody imgBody;

    public EaseChatRowImage(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ? R.layout.ease_row_received_picture : R.layout.ease_row_sent_picture, this);
    }

    @Override
    protected void onFindViewById() {
        percentageView = (TextView) findViewById(R.id.percentage);
        imageView = (ImageView) findViewById(R.id.image);
    }


    @Override
    protected void onSetUpView() {
        imgBody = (EMImageMessageBody) message.getBody();

        // received messages
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            return;
        }

        String filePath = imgBody.getLocalUrl();
        String thumbPath = EaseImageUtils.getThumbnailImagePath(imgBody.getLocalUrl());
        showImageView(thumbPath, filePath, message);
    }

    @Override
    protected void onViewUpdate(EMMessage msg) {
        if (msg.direct() == EMMessage.Direct.SEND) {
            if (EMClient.getInstance().getOptions().getAutodownloadThumbnail()) {
                super.onViewUpdate(msg);
            } else {
                if (null != progressBar) {
                    progressBar.setVisibility(View.GONE);
                }

                if (null != percentageView) {
                    percentageView.setVisibility(View.GONE);
                }

                if (imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING || imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING || imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.FAILED) {
                    imageView.setImageResource(R.drawable.ease_default_image);
                } else {
                    imageView.setImageResource(R.drawable.ease_default_image);
                    String thumbPath = imgBody.thumbnailLocalPath();
                    if (!new File(thumbPath).exists()) {
                        // to make it compatible with thumbnail received in previous version
                        thumbPath = EaseImageUtils.getThumbnailImagePath(imgBody.getLocalUrl());
                    }
                    showImageView(thumbPath, imgBody.getLocalUrl(), message);
                }
            }
            return;
        }

        // received messages
        if (imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
            if (EMClient.getInstance().getOptions().getAutodownloadThumbnail()) {
                imageView.setImageResource(R.drawable.ease_default_image);
            } else {
                if (null != progressBar) {
                    progressBar.setVisibility(View.GONE);
                }

                if (null != percentageView) {
                    percentageView.setVisibility(View.GONE);
                }

                imageView.setImageResource(R.drawable.ease_default_image);
            }
        } else if (imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.FAILED) {
            if (EMClient.getInstance().getOptions().getAutodownloadThumbnail()) {
                if (null != progressBar) {
                    progressBar.setVisibility(View.GONE);
                }

                if (null != percentageView) {
                    percentageView.setVisibility(View.VISIBLE);
                }
            } else {
                if (null != progressBar) {
                    progressBar.setVisibility(View.GONE);
                }

                if (null != percentageView) {
                    percentageView.setVisibility(View.GONE);
                }
            }
        } else {
            if (null != progressBar) {
                progressBar.setVisibility(View.GONE);
            }

            if (null != percentageView) {
                percentageView.setVisibility(View.GONE);
            }

            imageView.setImageResource(R.drawable.ease_default_image);
            String thumbPath = imgBody.thumbnailLocalPath();
            if (!new File(thumbPath).exists()) {
                // to make it compatible with thumbnail received in previous version
                thumbPath = EaseImageUtils.getThumbnailImagePath(imgBody.getLocalUrl());
            }
            showImageView(thumbPath, imgBody.getLocalUrl(), message);
        }
    }

    /**
     * load image into image view
     */
    private void showImageView(final String thumbernailPath, final String localFullSizePath, final EMMessage message) {
        // first check if the thumbnail image already loaded into cache s
        Bitmap bitmap = EaseImageCache.getInstance().get(thumbernailPath);

        if (bitmap != null) {
            // thumbnail image is already loaded, reuse the drawable
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.ease_default_image);
            AsyncTaskCompat.executeParallel(new AsyncTask<Object, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Object... args) {
                    File file = new File(thumbernailPath);
                    if (file.exists()) {
                        return EaseImageUtils.decodeScaleImage(thumbernailPath, 160, 160);
                    } else if (new File(imgBody.thumbnailLocalPath()).exists()) {
                        return EaseImageUtils.decodeScaleImage(imgBody.thumbnailLocalPath(), 160, 160);
                    } else {
                        if (message.direct() == EMMessage.Direct.SEND) {
                            if (localFullSizePath != null && new File(localFullSizePath).exists()) {
                                return EaseImageUtils.decodeScaleImage(localFullSizePath, 160, 160);
                            } else {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    }
                }

                protected void onPostExecute(Bitmap image) {
                    if (image != null) {
                        imageView.setImageBitmap(image);
                        EaseImageCache.getInstance().put(thumbernailPath, image);
                    }
                }
            });
        }
    }

}
