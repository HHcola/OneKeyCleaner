package my.example.onekeycleaner.db.table;


public class AppInstallTable  extends BaseTable {
    public static final String TABLE_NAME = "installs";

    public static final int COLUMN_ID = 0;
    public static final int COLUMN_PACKAGE_NAME = 1;
    public static final int COLUMN_SIGN_MD5 = 2;
    public static final int COLUMN_APP_NAME = 3;
    public static final int COLUMN_VERSION_CODE = 4;
    public static final int COLUMN_VERSION_NAME = 5;
    public static final int COLUMN_SIZE = 6;
    public static final int COLUMN_ICON_URI = 7;
    
    public static final int COLUMN_APP_KEY = 8;
    public static final int COLUMN_SIZE_TEXT = 9;
    public static final int COLUMN_LAST_INSTALL_TIME = 10;
    public static final int COLUMN_INSTALL_APK_DIR = 11;
    public static final int COLUMN_IS_SYSTEM_APP = 12;

    
    public static final String[] CONTENT_PROJECTION = new String[] {
        InstallColumns.ID,
        InstallColumns.PACKAGE_NAME,
        InstallColumns.SIGN_MD5,
        InstallColumns.APP_NAME,
        InstallColumns.VERSION_CODE,
        InstallColumns.VERSION_NAME,
        InstallColumns.SIZE,
        InstallColumns.ICON_URI,

        InstallColumns.APP_KEY,
        InstallColumns.SIZE_TEXT,
        InstallColumns.LAST_INSTALL_TIME,
        InstallColumns.INSTALL_APK_DIR,
        InstallColumns.IS_SYSTEM_APP
    };

    public AppInstallTable() {
        super(TABLE_NAME);
    }

    @Override
    public String getCreateSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("create table ").append(TABLE_NAME)
            .append(" (").append(InstallColumns.ID).append(" integer primary key autoincrement, ")
            .append(InstallColumns.PACKAGE_NAME).append(" text, ")
            .append(InstallColumns.SIGN_MD5).append(" text, ")
            .append(InstallColumns.APP_NAME).append(" text, ")
            .append(InstallColumns.VERSION_CODE).append(" integer, ")
            .append(InstallColumns.VERSION_NAME).append(" text, ")
            .append(InstallColumns.SIZE).append(" text, ")
            .append(InstallColumns.ICON_URI).append(" text, ")
    
            .append(InstallColumns.APP_KEY).append(" text, ")
            .append(InstallColumns.SIZE_TEXT).append(" text, ")
            .append(InstallColumns.LAST_INSTALL_TIME).append(" integer, ")
            .append(InstallColumns.INSTALL_APK_DIR).append(" text, ")
            .append(InstallColumns.IS_SYSTEM_APP).append(" integer")
            .append(");");

        return sb.toString();
    }

    @Override
    public String getDropSQL() {
        return "drop table if exists " + TABLE_NAME;
    }

    @Override
    public String[] getIndexColumns() {
        return null;
    }

    public interface InstallColumns extends BaseColumns {
        public static final String PACKAGE_NAME = "packagename";
        public static final String SIGN_MD5 = "signmd5";
        public static final String APP_NAME = "sname";
        public static final String VERSION_CODE = "versioncode";
        public static final String VERSION_NAME = "versionname";
        public static final String SIZE = "size";
        public static final String ICON_URI = "iconuri";

        public static final String APP_KEY = "appkey";
        public static final String SIZE_TEXT = "sizetext";
        public static final String LAST_INSTALL_TIME = "lastinstalltime";
        public static final String INSTALL_APK_DIR = "installapkdir";
        public static final String IS_SYSTEM_APP = "missystemapp";
    }
}
