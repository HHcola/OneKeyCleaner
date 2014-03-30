package my.example.onekeycleaner.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.WindowManager;

public class Utils {
    public static final long SECONDS = 1000;

    public static final long MINUTES = 60 * SECONDS;

    public static final long HOURS = 60 * MINUTES;

    public static final long DAYS = 24 * HOURS;

    private Utils() {
    }

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed
        // behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

	 /**
     * 是否隐藏title，是否全屏等
     * 
     * @param act
     *            需要隐藏界面
     * @param strategy
     *            策略类型 1-隐藏title，2-全屏，3-隐藏title并全屏
     * @return void
     */
    public static void strategyUi(Activity act, int strategy) {
        if (strategy == 1) {
            act.requestWindowFeature(Window.FEATURE_NO_TITLE);
        } else if (strategy == 2) {
            act.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (strategy == 3) {
            act.requestWindowFeature(Window.FEATURE_NO_TITLE);
            act.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
   
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
    
    public static String getBytesMd5(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            byte[] b = md.digest();

            return bytesToHexString(b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * true-当前sd卡可用，false不可用
     */
    public static boolean checkSdCardIsOk() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    
    
    public static String getDeviceId(Context mContext) {
        TelephonyManager mTelephonyManager = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTelephonyManager.getDeviceId();

        return imei;
    }


}
