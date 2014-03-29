package my.example.onekeycleaner.db.operation;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import my.example.onekeycleaner.manager.AppInstall;
import my.example.onekeycleaner.db.table.AppInstallTable;
import my.example.onekeycleaner.db.table.AppInstallTable.InstallColumns;
import my.example.onekeycleaner.util.Constants;

public class AppInstallSqlOp extends BaseOp {
    private static final String TAG = "AppInstallSqlOp";
    private static final boolean DEBUG =  Constants.IS_DEBUG && true;

    public AppInstallSqlOp(Context context) {
        super(context);
    }

    private boolean checkCacheUpdated(Cursor cursor, AppInstall appInstall, ContentValues values) {
        return true;
    }

    /**
     * 
     * @param appInstall
     */
    public void save(AppInstall appInstall) {
        if (DEBUG) {
            Log.d(TAG, "save sourcekey=" + appInstall.mAppKey);
        }

        SQLiteDatabase database = getDatabase();
        String tableName = AppInstallTable.TABLE_NAME;

        Cursor cursor = null;
        try {
            // 查询是否存在数据
            cursor = database.query(tableName, AppInstallTable.CONTENT_PROJECTION, 
                    InstallColumns.APP_KEY + "=?",
                    new String[] { appInstall.mAppKey }, null, null, null);

            ContentValues values = toContentValues(appInstall);
            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(AppInstallTable.COLUMN_ID);

                if (checkCacheUpdated(cursor, appInstall, values)) {
                    // 更新数据库中的数据
                    database.update(tableName, values, whereWithId(id, null), null);
                } else {
                    if (DEBUG) {
                        Log.d(TAG, "duplicate cache in DB");
                    }
                }
            } else {
                database.insert(tableName, "nullcol", values);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 
     * @param appInstall
     */
    public void asyncSave(final AppInstall appInstall) {
        if (DEBUG) {
            Log.d(TAG, "asyncSave sourcekey=" + appInstall.mAppKey);
        }

        sExecutor.execute(new Runnable() {
            @Override
            public void run() {
                save(appInstall);
            }
        });
    }

    /**
     * 
     * @param appInstall
     */
    public void update(AppInstall appInstall) {
        if (DEBUG) {
            Log.d(TAG, "update sourcekey=" + appInstall.mAppKey);
        }

        SQLiteDatabase database = getDatabase();
        String tableName = AppInstallTable.TABLE_NAME;

        ContentValues values = toContentValues(appInstall);
        // 更新数据库中的数据
        database.update(tableName, values, InstallColumns.APP_KEY + "=?",
                new String[] { appInstall.mAppKey });
    }

    /**
     * 
     * @param sourceKey
     */
    public void asyncUpdate(final AppInstall appInstall) {
        if (DEBUG) {
            Log.d(TAG, "asyncUpdate sourcekey=" + appInstall.mAppKey);
        }

        sExecutor.execute(new Runnable() {
            @Override
            public void run() {
                update(appInstall);
            }
        });
    }

    /**
     * 
     * @param sourceKey
     */
    public void delete(String sourceKey) {
        if (DEBUG) {
            Log.d(TAG, "delete sourcekey=" + sourceKey);
        }

        SQLiteDatabase database = getDatabase();
        String tableName = AppInstallTable.TABLE_NAME;

        database.delete(tableName, InstallColumns.APP_KEY + "=?",
                new String[] { sourceKey });
    }

    /**
     * 
     * @param sourceKey
     */
    public void asyncDelete(final String sourceKey) {
        if (DEBUG) {
            Log.d(TAG, "asyncDelete sourcekey=" + sourceKey);
        }

        sExecutor.execute(new Runnable() {
            @Override
            public void run() {
                delete(sourceKey);
            }
        });
    }

    /**
     * 
     * @param installs
     */
    public void load(ConcurrentHashMap<String, AppInstall> installs) {
        long nano = 0L;
        if (DEBUG) {
            Log.d(TAG, "load");
            nano = System.nanoTime();
        }

        SQLiteDatabase database = getDatabase();
        String tableName = AppInstallTable.TABLE_NAME;

        Cursor cursor = null;
        ArrayList<AppInstall> installList = new ArrayList<AppInstall>();

        try {
            cursor = database.query(tableName, AppInstallTable.CONTENT_PROJECTION, 
                    null, null, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {

                    AppInstall appInstall = toAppInstall(cursor);
                    installList.add(appInstall);
                }
            }

            for (AppInstall appInstall : installList) {
                    installs.put(appInstall.getAppKey(), appInstall);
            }
        } finally {
            if (DEBUG) {
                int count = -1;
                if (cursor != null) {
                    count = cursor.getCount();
                }
                Log.d(TAG, "load all the records, count " + count +
                        " take nano " + (System.nanoTime() - nano));
            }

            if (cursor != null) {
                cursor.close();
            }
        }

        if (mAyncOpListener != null) {
            mAyncOpListener.onFinishLoad();
        }
    }

    public void asyncLoad(final ConcurrentHashMap<String, AppInstall> installs) {
        if (DEBUG) {
            Log.d(TAG, "asyncLoad");
        };

        sExecutor.execute(new Runnable() {
            @Override
            public void run() {
                load(installs);
            }
        });
    }

    /**
     * 
     */
    public void clear() {
        int count = 0;
        long time = 0L;
        if (DEBUG) {
            Log.d(TAG, "clear");
            time = System.nanoTime();
        }

        SQLiteDatabase database = getDatabase();
        String tableName = AppInstallTable.TABLE_NAME;

        count = database.delete(tableName, null, null);

        if (DEBUG) {
            Log.d(TAG, "delete all the records, count " + count +
                    " take nano " + (System.nanoTime() - time));
        }

        if (mAyncOpListener != null) {
            mAyncOpListener.onFinishClear();
        }
    }

    /**
     * 
     * @param type
     */
    public void asyncClear() {
        if (DEBUG) {
            Log.d(TAG, "asyncClear");
        }

        sExecutor.execute(new Runnable() {
            @Override
            public void run() {
                clear();
            }
        });
    }

    /**
     * 
     * @param appInstall
     * @return
     */
    public ContentValues toContentValues(AppInstall appInstall) {
        ContentValues values = new ContentValues();

        values.put(InstallColumns.PACKAGE_NAME, appInstall.mPackageName);
        values.put(InstallColumns.SIGN_MD5, appInstall.mSignMd5);
        values.put(InstallColumns.APP_NAME, appInstall.mAppName);
        values.put(InstallColumns.VERSION_CODE, appInstall.mVersionCode);
        values.put(InstallColumns.VERSION_NAME, appInstall.mVersionName);
        values.put(InstallColumns.SIZE, appInstall.mSize);
        values.put(InstallColumns.ICON_URI, appInstall.mIconUri);
        
        values.put(InstallColumns.APP_KEY, appInstall.mAppKey);
        values.put(InstallColumns.SIZE_TEXT, appInstall.mSizeText);
        values.put(InstallColumns.LAST_INSTALL_TIME, appInstall.mLastInstallTime);
        values.put(InstallColumns.INSTALL_APK_DIR, appInstall.mInstallApkDir);
        values.put(InstallColumns.IS_SYSTEM_APP, appInstall.mIsSystemApp);

        return values;
    }

    public AppInstall toAppInstall(Cursor cursor) {
        AppInstall appInstall = new AppInstall();
        appInstall.mPackageName = cursor.getString(AppInstallTable.COLUMN_PACKAGE_NAME);
        appInstall.mSignMd5 = cursor.getString(AppInstallTable.COLUMN_SIGN_MD5);
        appInstall.mAppName = cursor.getString(AppInstallTable.COLUMN_APP_NAME);
        appInstall.mVersionCode = cursor.getInt(AppInstallTable.COLUMN_VERSION_CODE);
        appInstall.mVersionName = cursor.getString(AppInstallTable.COLUMN_VERSION_NAME);
        appInstall.mSize = cursor.getLong(AppInstallTable.COLUMN_SIZE);
        appInstall.mIconUri = cursor.getString(AppInstallTable.COLUMN_ICON_URI);

        appInstall.mAppKey = cursor.getString(AppInstallTable.COLUMN_APP_KEY);
        appInstall.mSizeText = cursor.getString(AppInstallTable.COLUMN_SIZE_TEXT);
        appInstall.mLastInstallTime = cursor.getLong(AppInstallTable.COLUMN_LAST_INSTALL_TIME);
        appInstall.mInstallApkDir = cursor.getString(AppInstallTable.COLUMN_INSTALL_APK_DIR);
        appInstall.mIsSystemApp = cursor.getInt(AppInstallTable.COLUMN_IS_SYSTEM_APP) != 0;


        return appInstall;
    }
}
