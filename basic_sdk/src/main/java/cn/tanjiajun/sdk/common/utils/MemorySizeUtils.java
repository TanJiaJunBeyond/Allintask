package cn.tanjiajun.sdk.common.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;

/**
 * 内存工具类
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class MemorySizeUtils {

    /**
     * 获得全部内存卡的路径数组
     *
     * @param context 上下文环境
     * @return 全部内存卡的路径数组
     * @since 0.0.1
     */
    public static String[] getStorageList(Context context) {
        String[] paths = null;
        if (null != context) {
            try {
                StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
                paths = (String[]) sm.getClass().getMethod("getVolumePaths").invoke(sm);
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return paths;
    }

    /**
     * 获得SD卡总大小
     *
     * @return SD卡总大小
     * @since 0.0.1
     */
    public static long getSDTotalSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return blockSize * totalBlocks;
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return sd卡剩余容量
     * @since 0.0.1
     */
    public static long getSDAvailableSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
    }

    /**
     * 获得机身内存总大小
     *
     * @return 机身内存总大小
     * @since 0.0.1
     */
    public static long getRomTotalSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return blockSize * totalBlocks;
    }

    /**
     * 获得机身可用内存
     *
     * @return 机身可用内存
     * @since 0.0.1
     */
    public static long getRomAvailableSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
    }

    /**
     * 转换内存单位
     *
     * @param oriSize 内存大小
     * @return 转换后的内存
     * @since 0.0.1
     */
    public static String tranSizeUnit(long oriSize) {
        double tempSize = (double) oriSize;
        String[] size = new String[2];
        DecimalFormat decFormat = new DecimalFormat("0.00");

        if (tempSize > 1024) {
            tempSize /= 1024;
            size[0] = decFormat.format(tempSize);
            size[1] = "KB";

            if (tempSize > 1024) {
                tempSize /= 1024;
                size[0] = decFormat.format(tempSize);
                size[1] = "MB";

                if (tempSize > 1024) {
                    tempSize /= 1024;
                    size[0] = decFormat.format(tempSize);
                    size[1] = "GB";
                }
            }
        } else {
            size[0] = decFormat.format(tempSize);
            size[1] = "KB";
        }
        return size[0] + size[1];
    }

}
