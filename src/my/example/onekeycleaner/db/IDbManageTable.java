package my.example.onekeycleaner.db;

import android.database.sqlite.SQLiteDatabase;

public interface IDbManageTable {

	/**
	 * 初始化表
	 * @param db
	 */
	void createAllTable(SQLiteDatabase db);

	/**
	 * 删除表
	 * @param db
	 */
	void deleteAllTable(SQLiteDatabase db);
}
