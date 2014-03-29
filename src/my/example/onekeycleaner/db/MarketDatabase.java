package my.example.onekeycleaner.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import my.example.onekeycleaner.db.table.AppCategoryTable;
import my.example.onekeycleaner.db.table.AppInstallTable;
import my.example.onekeycleaner.util.Constants;

public class MarketDatabase implements IDbManageTable {

	private MarketDBHelper mDbHelper = null;

	private static MarketDatabase mMarketDatabase = null;
	
	private static final Object mSyncLock = new Object();  

	public static MarketDatabase getMarketDatabase(Context context) {
		if (mMarketDatabase == null) {
			synchronized (mSyncLock) {
				mMarketDatabase = new MarketDatabase(context.getApplicationContext());
			}
		}
		return mMarketDatabase;
	}

	private MarketDatabase(Context context) {
		mDbHelper = new MarketDBHelper(context, Constants.DB_NAME, Constants.DB_VERSION);
		mDbHelper.setmManageTable(this);
	}

	public MarketDBHelper getDbHelper() {
		return mDbHelper;
	}

	public SQLiteDatabase getMarketDb() {
		SQLiteDatabase sqliteDatabase = null;
		if (mDbHelper != null) {
			sqliteDatabase = mDbHelper.getWritableDatabase();
		}

		return sqliteDatabase;
	}

	public void closeMarketDb() {
		if (mDbHelper != null) {
			mDbHelper.close();
			mDbHelper = null;
		}
	}

	@Override
	public void createAllTable(SQLiteDatabase db) {
		if (db != null && db.isOpen()) {
			db.execSQL(new AppCategoryTable().getCreateSQL());
			db.execSQL(new AppInstallTable().getCreateSQL());
		}
	}

	@Override
	public void deleteAllTable(SQLiteDatabase db) {
		if (db != null && db.isOpen()) {
			
		}
	}
}
