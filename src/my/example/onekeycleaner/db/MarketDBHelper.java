package my.example.onekeycleaner.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MarketDBHelper extends SQLiteOpenHelper {
	
	private IDbManageTable mManageTable;

	private MarketDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	
	public MarketDBHelper(Context context, String name,
			int version) {
		this(context, name, null, version);
		
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		if (mManageTable != null) {
			mManageTable.createAllTable(db);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion <= oldVersion || mManageTable == null) {
			return;
		} else {
			// *************************************************
			// mManageTable.deleteAllTable(db);
			// mManageTable.createAllTable(db);
			// *************************************************
		}
	}

	public IDbManageTable getmManageTable() {
		return mManageTable;
	}

	public void setmManageTable(IDbManageTable mManageTable) {
		this.mManageTable = mManageTable;
	}
	
}
