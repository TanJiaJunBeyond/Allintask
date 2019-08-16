package cn.tanjiajun.sdk.common.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.math.BigDecimal;

/**
 * 文件工具类
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class FileUtils {

    private static final String cacheBaseDirName = "core";
    private static File cacheBaseDir;

    /**
     * 获得缓存父文件夹
     *
     * @param context 上下文环境
     * @return 缓存父文件夹
     * @since 0.0.1
     */
    public static File getCacheBaseDir(final Context context) {
        if (null == cacheBaseDir) {
            File baseDir = null;
            if (Build.VERSION.SDK_INT >= 8) {
                baseDir = context.getExternalCacheDir();
            } else {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    baseDir = Environment.getExternalStorageDirectory();
                }
            }

            if (null == baseDir) {
                baseDir = context.getCacheDir();
            }

            if (null != baseDir) {
                cacheBaseDir = new File(baseDir, cacheBaseDirName);
                if (!cacheBaseDir.exists()) {
                    cacheBaseDir.mkdirs();
                }
            } // if (null != baseDir)

        } // if (null == cacheBaseDir) 

        return cacheBaseDir;
    }

    /**
     * 获得缓存文件夹
     *
     * @param context      上下文环境
     * @param cacheDirName 缓存文件夹名
     * @return 缓存文件夹
     * @see #getCacheBaseDir(Context)
     * @since 0.0.1
     */
    public static File getCacheDir(final Context context, final String cacheDirName) {
        File cacheDir = null;

        cacheBaseDir = getCacheBaseDir(context);
        if (null != cacheBaseDir) {
            if (null != cacheDirName && 0 != cacheDirName.trim().length()) {
                cacheDir = new File(cacheBaseDir, cacheDirName);
            } else {
                cacheDir = cacheBaseDir;
            }
        }

        if (null != cacheDir && !cacheDir.exists()) {
            if (!cacheDir.mkdirs()) {
                cacheDir = null;
            }
        }

        if (null == cacheDir) {
            cacheDir = cacheBaseDir;
        }

        return cacheDir;
    }

    /**
     * 获得缓存文件
     *
     * @param context       上下文环境
     * @param cacheDirName  缓存文件夹名
     * @param cacheFileName 缓存文件名
     * @return 缓存文件
     * @see #getCacheDir(Context, String)
     * @since 0.0.1
     */
    public static File getCacheFile(final Context context, final String cacheDirName, final String cacheFileName) {
        File cacheFile = null;

        File cacheDir = getCacheDir(context, cacheDirName);
        if (null != cacheDir && null != cacheFileName && 0 != cacheFileName.trim().length()) {
            cacheFile = new File(cacheDir, cacheFileName);
        }

        return cacheFile;
    }

    public static boolean deleteFile(String filePath) {
        File deleteFile = new File(filePath);
        return deleteFile.delete();
    }

    /**
     * 获取文件夹所占空间大小
     *
     * @param dir 文件夹
     * @return 内存大小
     * @since 0.0.1
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 如果遇到目录则通过递归调用继续统计  
            }
        }
        return dirSize;
    }

    /**
     * 删除文件夹
     *
     * @param path 文件夹路径
     * @since 0.0.1
     */
    public static void deleteFolder(File path) {
        if (!path.exists())
            return;
        if (path.isFile()) {
            path.delete();
            return;
        }
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            deleteFolder(files[i]);
        }
        //        path.delete();  
    }

    /**
     * 获取外部缓存文件夹
     *
     * @param context 上下文环境
     * @param dirName 文件夹名称
     * @return
     * @since 0.0.2
     */
    public static File getExternalDir(final Context context, final String dirName) {
        File externalCacheDir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            externalCacheDir = new File(Environment.getExternalStorageDirectory(), dirName);
        }

        if (null != externalCacheDir && !externalCacheDir.exists()) {
            externalCacheDir.mkdirs();
        }

        if (null == externalCacheDir) {
            externalCacheDir = cacheBaseDir;
        }

        return externalCacheDir;
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     * @since 0.0.1
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }

        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

}
