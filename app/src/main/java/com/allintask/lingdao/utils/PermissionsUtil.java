package com.allintask.lingdao.utils;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

/**
 * Created by TanJiaJun on 2017/3/7.
 */

public class PermissionsUtil {

    public static final int REQUEST_CODE_ASK_PERMISSIONS = 1000;

    public static void hasPermissions(Activity act, String[] permissions, int requestCode) {
        if (permissions != null && permissions.length > 0) {
            ActivityCompat.requestPermissions(act, permissions, requestCode);
        }
    }


    public static boolean hasCameraPermissions() {
        Camera camera = null;

        try {
            camera = Camera.open(0); // attempt to get a Camera instance

            if (camera != null) {
                camera.setDisplayOrientation(90);
                camera.release();
                camera = null;
                return true;
            }
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            //e.printStackTrace();
            if (camera != null) {
                camera.release();
                camera = null;
            }
        }
        return false;
    }

    public static boolean hasReadSmsPermissions(Context ct) {
        TelephonyManager phoneMgr = (TelephonyManager) ct.getSystemService(Context.TELEPHONY_SERVICE);

        try {
            phoneMgr.getLine1Number();
            return true;
        } catch (Exception e) {

        }
        return false;
    }

}
