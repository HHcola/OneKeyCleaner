package my.example.onekeycleaner.db.operation;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import my.example.onekeycleaner.db.MarketDatabase;
import my.example.onekeycleaner.db.table.BaseTable.BaseColumns;

public abstract class BaseOp {

    private Context mContext;

    private MarketDatabase mMarketDatabase;

    private SQLiteDatabase mSQLiteDatabase;

    static final Object mSyncLock = new Object();  

    protected static final Executor sExecutor = Executors.newSingleThreadExecutor();

    protected OnAsyncOpListener mAyncOpListener;

    public static interface OnAsyncOpListener {
        /**
         * run in worker thread
         */
        public void onFinishLoad();
        /**
         * run in worker thread
         */
        public void onFinishSaveAll();
        /**
         * run in worker thread
         */
        public void onFinishClear();
    }

    public BaseOp(Context context) {
        mContext = context;
        mMarketDatabase = MarketDatabase.getMarketDatabase(context.getApplicationContext());
        mSQLiteDatabase = mMarketDatabase.getMarketDb();
    }

    public Context getContext() {
        return mContext;
    }

    public void setAsyncOpListener(OnAsyncOpListener listener) {
        mAyncOpListener = listener;
    }

    private synchronized boolean checkDbIsOk() {
        if (mSQLiteDatabase != null && mSQLiteDatabase.isOpen()) {
            return true;
        } else {
            return false;
        }
    }

    public void close() {
        mMarketDatabase.closeMarketDb();
    }

    /**
     * 
     * @param context
     * @return
     */
    synchronized SQLiteDatabase getDatabase() {
        if (mSQLiteDatabase == null) {
            mSQLiteDatabase = mMarketDatabase.getMarketDb();
            Log.d("BaseOp", "Database ready.");

        }
        return mSQLiteDatabase;
    }

    public void execSql(String sqlStr) {
        try {
            if (!checkDbIsOk()) {
                mSQLiteDatabase = mMarketDatabase.getMarketDb();
            }

            mSQLiteDatabase.execSQL(sqlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execSql(String sqlStr, Object[] args) {
        try {
            if (!checkDbIsOk()) {
                mSQLiteDatabase = mMarketDatabase.getMarketDb();
            }

            mSQLiteDatabase.execSQL(sqlStr, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Cursor rawQuery(String sqlStr, String[] selectionArgs) {
        Cursor cursor = null;
        try {
            if (!checkDbIsOk()) {
                mSQLiteDatabase = mMarketDatabase.getMarketDb();
            }

            cursor = mSQLiteDatabase.rawQuery(sqlStr, selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cursor;
    }

    public void deleteTable(String tableName) {
        final String deleteSql = "DELETE FROM '".concat(tableName).concat("\'");
        try {
            if (!checkDbIsOk()) {
                mSQLiteDatabase = mMarketDatabase.getMarketDb();
            }
            mSQLiteDatabase.execSQL(deleteSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String whereWithId(long id, String selection) {
        StringBuilder sb = new StringBuilder(256);
        sb.append(BaseColumns.ID).append("=").append(id);
        if (selection != null) {
            sb.append(" AND (");
            sb.append(selection);
            sb.append(')');
        }
        return sb.toString();
    }

}
