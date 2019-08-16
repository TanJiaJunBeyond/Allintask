package cn.tanjiajun.sdk.common.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图像工具类
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class ImageUtils {

    /**
     * 获得图片文件名
     *
     * @param sourceImageLocalURIStr 图片路径
     * @return 图片文件名
     * @since 0.0.1
     */
    private static String genFileName(final String sourceImageLocalURIStr) {
        String fileName = null;

        if (Validator.isLocalFilePathValid(sourceImageLocalURIStr)) {
            File file = new File(sourceImageLocalURIStr);
            fileName = file.getName();
        }

        if (TextUtils.isEmpty(fileName)) {
            fileName = System.currentTimeMillis() + ".jpg";
        }

        return fileName;
    }


    public static String getScaledImageLocalURIStr(final Context context, final String sourceImageLocalURIStr) {
        String scaledImageLocalURIStr = sourceImageLocalURIStr;

        int degree = readPictureDegree(sourceImageLocalURIStr);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(sourceImageLocalURIStr, options);
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        options.inSampleSize = computeSampleSize(options, 640, -1);
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(sourceImageLocalURIStr, options);
        if (null != bitmap) {
            Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (null != bitmap2) {
                File file = FileUtils.getCacheFile(context, "pic", genFileName(sourceImageLocalURIStr));

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 把数据写入文件
                    scaledImageLocalURIStr = file.getAbsolutePath();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (null != fos) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } // if (null != fos)

                    if (null != bitmap && !bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    }

                    if (null != bitmap2 && !bitmap2.isRecycled()) {
                        bitmap2.recycle();
                        bitmap2 = null;
                    }

                } // finally
            } // if (null != bitmap2)
        } // if (null != bitmap)

        return scaledImageLocalURIStr;
    }

    private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static boolean saveImageFile(File file, Bitmap imageBitmap) {
        boolean isSaved = true;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);// (quality:0-100)压缩文件
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

    public static boolean saveImageFile(File file, Bitmap imageBitmap, int percent) {
        boolean isSaved = true;

        percent = (percent > 0 && percent <= 100 ? percent : 100);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, percent, out);// (quality:0-100)压缩文件
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

    /**
     * 根据Uri或者照片路径
     */
    public static String getAbsoluteImagePathByUri(Context context, Uri uri) {
        String imagePath = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            imagePath = cursor.getString(column_index);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return imagePath;
    }

}
