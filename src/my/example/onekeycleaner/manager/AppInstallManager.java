package my.example.onekeycleaner.manager;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import android.R.integer;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;

import my.example.onekeycleaner.manager.AppStateManager.AppState;
import my.example.onekeycleaner.model.CacheInfo;
import my.example.onekeycleaner.db.operation.AppInstallSqlOp;
import my.example.onekeycleaner.db.operation.BaseOp.OnAsyncOpListener;
import my.example.onekeycleaner.engine.CacheInfoProvider;
import my.example.onekeycleaner.util.AppUtils;
import my.example.onekeycleaner.util.Constants;

public class AppInstallManager {

    private static final String TAG = "AppInstallManager";
    private static final boolean DEBUG =  Constants.IS_DEBUG && true;

    /** Singleton*/
    private static AppInstallManager sInstance;

    /** Application Context */
    private Context mContext;

    private ConcurrentHashMap<String, AppInstall> mInstallApps;
    private ConcurrentHashMap<String, String> mPackageKeyMaps;

    private AppStateManager mAppStateManager;

    /** 下载管理数据库操作对象 */
    private AppInstallSqlOp mAppInstallSqlOp;

    /** 下载管理数据库操作对象 */
    private OnAsyncOpListener mAsyncOpListener;

    private AppInstallManager(Context context) {
        mContext = context.getApplicationContext();
        mAppStateManager = AppStateManager.getInstance(mContext);

        mInstallApps = new ConcurrentHashMap<String, AppInstall>();
        mPackageKeyMaps = new ConcurrentHashMap<String, String>();
        init();
    }

    /**
     * 
     * @param context
     * @return
     */
    public static synchronized AppInstallManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AppInstallManager(context);
        }
        return sInstance;
    }

    /**
     * 
     */
    public void init() {
        long start = System.currentTimeMillis();

        initInstalledAppListAndAppState();

        if (DEBUG) {
            Log.d(TAG, "init take millis:"
                    + (System.currentTimeMillis() - start));
        }
    }

    /**
     * 
     * @return
     */
    public ConcurrentHashMap<String, AppInstall> getInstalledAppList() {
        return mInstallApps;
    }

    
    public int getUninstallableAppCount() {
        int count = 0;
        Collection<AppInstall> appInstalls = mInstallApps.values();
        for (AppInstall appInstall : appInstalls) {
            if (!appInstall.mIsSystemApp) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * 
     * @return
     */
    public AppInstall getInstalledApp(String appKey) {
        return mInstallApps.get(appKey);
    }

    /**
     * 解析PackageInfo信息，返回AppInstall实例
     * 
     * @param info
     * @return
     */
    private AppInstall parsePackageInfo(PackageInfo info) {
        AppInstall appInstall = new AppInstall();

        appInstall.setPackageName(info.packageName);
        appInstall.setVersionName(info.versionName);
        appInstall.setVersionCode(info.versionCode);
        appInstall.setAppName((String) info.applicationInfo.loadLabel(mContext.getPackageManager()));
        String appKey = AppUtils.generateAppKey(info.packageName, info.versionCode);
        appInstall.setAppKey(appKey);

        String dir = info.applicationInfo.publicSourceDir;
        File apkFile = new File(dir);
        long size = apkFile.length();
        appInstall.setSize(size);
        appInstall.setSizeText(Formatter.formatFileSize(mContext, size));
        appInstall.setLastInstallTime(apkFile.lastModified());

        if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)
                == ApplicationInfo.FLAG_SYSTEM) {
            appInstall.setSystemApp(true);
        }

        try {
            Field field = info.getClass().getField("installLocation");
            int location = (Integer) field.get(info);
            if (location == AppUtils.INSTALL_LOCATION_AUTO
                    || location == AppUtils.INSTALL_LOCATION_PREFER_EXTERNAL) {
                // 可移动的应用
                if ((info.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
                    // 安装在sd卡上
                    appInstall.mInstallFlag = AppInstall.FLAG_INSTALL_AUTO_SDCARD;
                } else {
                    // 安装在手机内存
                    appInstall.mInstallFlag = AppInstall.FLAG_INSTALL_AUTO_ROM;
                }
            } else {
                // 不可移动应用
                appInstall.mInstallFlag = AppInstall.FLAG_INSTALL_ONLY_ROM;
            }
        } catch (NoSuchFieldException e) {
            if (DEBUG) {
                Log.d(TAG, e.toString());
            }
        } catch (IllegalArgumentException e) {
            if (DEBUG) {
                Log.d(TAG, e.toString());
            }
        } catch (IllegalAccessException e) {
            if (DEBUG) {
                Log.d(TAG, e.toString());
            }
        }

        return appInstall;
    }

    /**
     * 从系统中加载出所有已安装的APP列表
     */
    public void initInstalledAppListAndAppState() {
        List<PackageInfo> packageInfos = AppUtils.getInstalledPackages(mContext);
        for (PackageInfo info : packageInfos) {
            AppInstall appInstall = parsePackageInfo(info);
            String pName = appInstall.getPackageName();
            String appKey = appInstall.getAppKey();
            mInstallApps.put(appKey, appInstall);
            mPackageKeyMaps.put(pName, appKey);
            // 初始化APP状态管理器中的状态
            mAppStateManager.initAppState(appKey, AppState.INSTALLED);
        }
    }

    /**
     * 
     * @param packageName
     */
    public void addPackage(String packageName) {
        PackageInfo info = AppUtils.getPacakgeInfo(mContext, packageName);
        if (info != null) {
            AppInstall appInstall = parsePackageInfo(info);
            String pName = appInstall.getPackageName();
            String appKey = appInstall.getAppKey();
            mInstallApps.put(appKey, appInstall);
            mPackageKeyMaps.put(pName, appKey);

            mAppStateManager.notifyStateChange(appKey, AppState.INSTALLED);
        }
    }

    
    
    /**
     * 
     * @param packageName
     */
    public void removePackage(String packageName) {
        String appKey = mPackageKeyMaps.get(packageName);
        if (appKey != null) {
            mPackageKeyMaps.remove(packageName);
            mInstallApps.remove(appKey);

            mAppStateManager.notifyStateChange(appKey, AppState.UNINSTALLED);
        } else if (DEBUG) {
            Log.w(TAG, "---mPackageKeyMaps get null");
        }
    }

    /**
     * 
     * @param packageName
     */
    public void updatePackage(String packageName) {
        String appKey = mPackageKeyMaps.get(packageName);
        if (appKey == null) {
            Log.w(TAG, "---mPackageKeyMaps get null");
            return;
        }

        mPackageKeyMaps.remove(packageName);
        AppInstall oldInstall = mInstallApps.remove(appKey);

        PackageInfo info = AppUtils.getPacakgeInfo(mContext, packageName);
        if (info != null) {
            AppInstall appInstall = parsePackageInfo(info);
            String pName = appInstall.getPackageName();
            String aKey = appInstall.getAppKey();
            mInstallApps.put(aKey, appInstall);
            mPackageKeyMaps.put(pName, aKey);
        }

        if (oldInstall != null) {
            mAppStateManager.notifyStateChange(oldInstall.mAppKey, AppState.UPDATED);
        }
        mAppStateManager.notifyStateChange(appKey, AppState.INSTALLED);
    }

    /**
     * 
     * @author wzx
     *
     */
    private class SQLOpListener implements OnAsyncOpListener {

        @Override
        public void onFinishLoad() {
        }

        @Override
        public void onFinishSaveAll() {
        }

        @Override
        public void onFinishClear() {
        }
    }
}
