package my.example.onekeycleaner.db.table;

import android.net.Uri;

public abstract class BaseTable {
    
    public static final String AUTHORITY = "com.baidu.androidstore.database";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    protected String tableName;

    protected final static String ID = "_id"; 

    protected final static String TIME = "time";

    protected final static String VERSION = "version"; 

    public BaseTable(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getId() {
        return ID;
    }

    public String getTime() {
        return TIME;
    }

    public String getmVersion() {
        return VERSION;
    }

    public abstract String getCreateSQL();

    public abstract String getDropSQL();

    public abstract String[] getIndexColumns();

    /**
     * 数据表的基础列接口
     * 
     * @author wuzhixu01
     *
     */
    public interface BaseColumns {
        public static final String ID = "_id";
    }
}
