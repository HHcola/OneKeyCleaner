package my.example.onekeycleaner.util;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import my.example.onekeycleaner.controller.AppCacheClearController;

import com.example.onekeycleaner.R;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;


public class AppUtils {
    private static final boolean DEBUG = true && Constants.IS_DEBUG;
    private static final String TAG = "AppUtils";

    public static final String EXTRA_INSTALLER_PACKAGE_NAME =
            "android.intent.extra.INSTALLER_PACKAGE_NAME";

    public static final int INSTALL_LOCATION_AUTO = 0;
    public static final int INSTALL_LOCATION_INTERNAL_ONLY = 1;
    public static final int INSTALL_LOCATION_PREFER_EXTERNAL = 2;

    /**
     * 通过系统的PackageInstaller安装APK
     * 
     * @param context
     *            Context
     * @param apkfile
     *            APK文件对象
     */
//    public static void installApkBySystemUI(Context context, String filePath) {
//        File file = new File(filePath);
//        if (file.exists()) {
//            installApkBySystemUI(context, file);
//        } else {
//        	Toast.makeText(context, R.string.toast_package_isnot_exist,
//                    Toast.LENGTH_SHORT).show();
//        }
//    }

    /**
     * 通过系统的PackageInstaller安装APK
     * 
     * @param context
     *            Context
     * @param apkfile
     *            APK文件对象
     */
    public static boolean installApkBySystemUI(Context context, File apkfile) {
        if (DEBUG) {
            Log.d(TAG, "installApkBySystemUI安装文件存在:" + apkfile.exists() + ":"
                    + apkfile.getPath());
        }
        if (!apkfile.exists()) {
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType((Uri.fromFile(apkfile)),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(EXTRA_INSTALLER_PACKAGE_NAME, context.getPackageName());
        intent.setComponent(new ComponentName("com.android.packageinstaller",
                "com.android.packageinstaller.PackageInstallerActivity"));
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "启动系统安装界面失败");
            }
            intent.setComponent(null);
            try {
                context.startActivity(intent);
                return true;
            } catch (Exception e1) {
                if (DEBUG) {
                    Log.e(TAG, "再次启动系统安装界面失败");
                }
            }
        }
        return false;
    }

    /**
     * 调用系统UI卸载应用
     * 
     * @param context
     *            Context
     * @param packageName
     *            APP Package Name
     */
    public static void uninstallApkBySystemUI(Context context,
            String packageName) {
        Uri packageUri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, packageUri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    
    /**
     * 调用系统UI卸载应用
     * 
     * @param context
     *            Context
     * @param packageName
     *            APP Package Name
     */
    public static void clearApkCacheBySystemUI(Context context,
            String packageName) {
    	/** 
    	 * Android2.3打开settings里面的那个应用的详细界面 
    	 * 后来我又查了一个Android4.1的，也是这样写的，所有应该是2.3之后，都是这样写的了， 
    	 * 但是这只是猜测，各位有空的可以去下载Android Settings的代码看一下 
    	 * 这样就可以做成多个版本的适配了 
    	 * <intent-filter>  
    	 * <action android:name="android.settings.APPLICATION_DETAILS_SETTINGS" />  
    	 * <category android:name="android.intent.category.DEFAULT" />  
    	 * <data android:scheme="package" />  
    	 * </intent-filter> 
    	 */  
    	  
    	/** 
    	 * Android2.2打开settings里面的那个应用的详细界面 
    	 * 用这个版本来打开的话，就要加多一句把包名设置进去的 
    	 * intent.putExtra("pkg", packageName); 
    	 * <intent-filter>  
    	 * <action android:name="android.intent.action.VIEW" />  
    	 * <category android:name="android.intent.category.DEFAULT" />  
    	 * <category android:name="android.intent.category.VOICE_LAUNCH" /> 
    	 * </intent-filter> 
    	 */  
        Intent intent = new Intent();  
        Uri packageUri = Uri.parse("package:" + packageName);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");  
        intent.addCategory("android.intent.category.DEFAULT");  
        intent.setData(packageUri);
        context.startActivity(intent);
    }
    
    /**
     * 获取系统root，清理缓存
     * 
     * @param packageName
     *            APP Package Name
     */
    public static void deleteCache(final String packageName,final Handler handler){

        (new Thread(){

            public void run() {
                try {              
                    File path = new File("/data/data/"+packageName+"/cache/");
                    if(!path.exists()){
                        Log.i("---justyce---", "file not exist");
                        return ;
                    }                  
                    String killer ="rm -r " + path.toString();
                    Process p = Runtime.getRuntime().exec("su");
                    DataOutputStream os =new DataOutputStream(p.getOutputStream());
                    os.writeBytes(killer.toString() +"\n");
                    os.writeBytes("exit\n");
                    os.flush();
                    Message msg = new Message();
                    msg.what = AppCacheClearController.CLEAR_CACHE_FINISH;
                    Bundle bundle = new Bundle();
                    bundle.putString("packageName",packageName);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (IOException e) {
            }
        }}).start();
    }

    /**
     * 获取系统中已安装APP的PackageInfo，过滤自身APP和没有Launcher入口的系统APP
     * 
     * @param context
     *            Context
     * @return packages
     */
    public static List<PackageInfo> getInstalledPackages(Context context) {
        long start = System.currentTimeMillis();

        List<PackageInfo> installed = context.getPackageManager()
                .getInstalledPackages(0);

        // 查询拥有Launcher入口的APP
        final Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> installedactivity = context.getPackageManager()
                .queryIntentActivities(intent, 0);
        HashSet<String> launcherApps = new HashSet<String>();
        for (ResolveInfo ri : installedactivity) {
            launcherApps.add(ri.activityInfo.packageName);
        }

        ArrayList<PackageInfo> packageInfos = new ArrayList<PackageInfo>();
        for (PackageInfo info : installed) {
            // 过滤自身APP
            if (info.packageName.equalsIgnoreCase(context.getPackageName())) {
                continue;
            }

            // 过滤没有Launcher入口的系统APP
            if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)
                    == ApplicationInfo.FLAG_SYSTEM
                    && !launcherApps.contains(info.packageName)) {
                continue;
            }

            packageInfos.add(info);
        }

        if (DEBUG) {
            Log.d(TAG,
                    "getInstalledPackages take millis:"
                            + (System.currentTimeMillis() - start));
        }
        return packageInfos;
    }

    /**
     * 获取packageName 关联的PacakgeInfo
     * 
     * @param context
     *            Context
     * @param packageName
     *            应用包名
     * @return PackageInfo
     */
    public static PackageInfo getPacakgeInfo(Context context, String packageName) {
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);
            return info;
        } catch (NameNotFoundException e) {
            if (DEBUG) {
                Log.w(TAG, "error:" + e.getMessage());
            }
            return null;
        }
    }

    /**
     * 获取应用的名称
     * 
     * @param context 
     *            Context
     * @param info
     *            应用的PackageInfo
     * @return String
     *            应用名称
     */
    public static String getApplicationName(Context context, PackageInfo info) {
        return (String) info.applicationInfo.loadLabel(context.getPackageManager());
    }

    /**
     * 获取应用的最后安装或更新时间
     * 
     * @param context 
     *            Context
     * @param info
     *            应用的PackageInfo
     * @return long
     *            应用的最后安装或更新时间
     */
    public static long getApplicationTime(Context context, PackageInfo info) {
        String dir = info.applicationInfo.publicSourceDir;
        File apkFile = new File(dir);
        return apkFile.lastModified();
    }

    /**
     * 根据APP的包名和版本号生成AppKey
     * 
     * @param packagename
     *            包名
     * @param versioncode
     *            版本号
     * @return
     *            APP KEY
     */
    public static String generateAppKey(String packageName, int versionCode) {
        StringBuilder sb = new StringBuilder();
        sb.append(packageName).append("@").append(versionCode);
        return sb.toString();
    }

    public static String getPackageNameByAppKey(String appKey) {
        String pName = null;
        if (appKey != null) {
            pName = appKey.split("@")[0];
        }
        return pName;
    }

    /**
     * 打开应用
     * 
     * @param context
     *            Context
     * @param packageName
     *            packagename
     */
    public static void openInstalledApp(Context context, String packageName) {

        Intent queryIntent = new Intent(Intent.ACTION_MAIN, null);
        queryIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        queryIntent.setPackage(packageName);

        List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(queryIntent, 0);

        if (apps != null && apps.size() > 0 && apps.iterator().next() != null) {
            ResolveInfo ri = apps.iterator().next();
            String className = ri.activityInfo.name;

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
            	Toast.makeText(context, context.getResources().getText(R.string.toast_cannot_open_app),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
        	Toast.makeText(context, context.getResources().getText(R.string.toast_cannot_open_app),
                    Toast.LENGTH_SHORT).show();
        }
    }


    private static final String SCHEME = "package";
    /**
     * 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.1及之前版本)
     */
    private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
    /**
     * 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.2)
     */
    private static final String APP_PKG_NAME_22 = "pkg";
    /**
     * InstalledAppDetails所在包名
     */
    private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
    /**
     * InstalledAppDetails类名
     */
    private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";
    /**
     * 调用系统InstalledAppDetails界面显示已安装应用程序的详细信息。 对于Android 2.3（Api Level
     * 9）以上，使用SDK提供的接口； 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）。
     * 
     * @param context
     * 
     * @param packageName
     *            应用程序的包名
     */
    public static void openInstalledAppDetails(Context context, String packageName) {
        Intent intent = new Intent();
        final int apiLevel = Build.VERSION.SDK_INT;
        if (apiLevel >= 9) { // 2.3（ApiLevel 9）以上，使用SDK提供的接口
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts(SCHEME, packageName, null);
            intent.setData(uri);
        } else { // 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）
            // 2.2和2.1中，InstalledAppDetails使用的APP_PKG_NAME不同。
            final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22
                    : APP_PKG_NAME_21);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName(APP_DETAILS_PACKAGE_NAME,
                    APP_DETAILS_CLASS_NAME);
            intent.putExtra(appPkgName, packageName);
        }

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
        	Toast.makeText(context, context.getResources().getText(R.string.toast_cannot_open_app_detail),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取应用签名的md5
     * 
     * @param packageName 包名
     * @param context Context
     * @return 返回应用签名的md5
     */
    public static String getSignMd5(Context context, String packageName) {
        String mSignmd5 = "";
        PackageInfo packageinfo = AppUtils.getPacakgeInfo(context, packageName);

        if (packageinfo != null) {
            mSignmd5 = Utils.getBytesMd5(packageinfo.signatures[0].toCharsString().getBytes());
        }
        return mSignmd5;
    }
}
