package my.example.onekeycleaner.util;

/** 
 * 常量
 */
public final class Constants {

    private Constants(){}

    /** 是否在调试状态 */
    public final static boolean IS_DEBUG = false;

    /** 强制使用Release接口 */
    public final static boolean USE_RELEASE_URL = false;

    /***************************************************
     * 网络参数
     ***************************************************/

    /** APP自升级接口地址 */
    public static final String UPGARADE_REQUEST_BASE_URL = "http://update.security.baidu.co.th/cgi-bin/get_mobileassist_update_info.cgi";

    /** APP debug上报地址 */
    public static final String REPORTE_DEBUG_BASE_URL = "http://10.252.23.29:8087/cgi-bin-py/";
    
    /** APP上报线上地址 */
    public static final String REPORTE_RELEASE_BASE_URL = "http:/sync.appreport.baidu.com/cgi-bin-py-appstore/";
    
    /** APP上报地址 */
    public static final String REPORTE_BASE_URL = IS_DEBUG ? REPORTE_DEBUG_BASE_URL : REPORTE_RELEASE_BASE_URL;
    
    /** app debug地址*/ 
    private static final String REQUEST_RELEASE_URL = "http://androidapp.baidu.com";
    
    /** APP 线上地址 */
    private static final String REQUEST_DEBUG_URL = "http://10.252.23.29:8090";
    
    /** 请求地址 */
    public static final String REQUEST_BASE_URL = (IS_DEBUG && !USE_RELEASE_URL) ? REQUEST_DEBUG_URL.concat("/index.php"): REQUEST_RELEASE_URL.concat("/index.php");

    /** 图片下载地址*/ 
    public static final String REQUEST_IMAG_URL = (IS_DEBUG && !USE_RELEASE_URL) ? REQUEST_DEBUG_URL : REQUEST_RELEASE_URL;

    /***************************************************
     * 数据库参数
     ***************************************************/ 
    /** 数据库名 */
    public static final String DB_NAME = "baidu_androidStore.db";
    /** 数据库版本 */
    public static final int DB_VERSION = 1;

    public static final int BYTE_TO_MB = 1024 * 1024;

    public static final String IMAGE_CACHE_DIR = "baidu_thumbs";


    /***************************************************
     * 第一级tab id定义
     ***************************************************/ 
    public static final int TAB_ITEM_HOME = 1;
    public static final int TAB_ITEM_APPS = 2;
    public static final int TAB_ITEM_GAME = 3;
    public static final int TAB_ITEM_SETTING = 4;

    /***************************************************
     * 第二级tab id定义
     ***************************************************/ 
    public static final int TWO_TAB_HOT = 1;
    public static final int TWO_TAB_FREE = 2;
    public static final int TWO_TAB_CATEGORY = 3;

    /***************************************************
     * 排序
     ***************************************************/ 
    public static final int ORDER_BY_DOWNLOAD = 1;
    public static final int ORDER_BY_UPDATETIME = 2;
    public static final int ORDER_BY_COMMENT = 3;


    public static final String APP_TYPE_SOFT = "soft";
    public static final String APP_TYPE_GAME = "game";


    /***************************************************
     *  分享信息  facebook twitter
     ***************************************************/ 
    public static final String SHARE_FACEBOOK_APP_ID = "485397558232366";
    public static final String SHARE_FACEBOOK_APP_SECRET = "7c0faeb9ded11c951682fb304c5dfed5";

    public static final int ANDROID_TYPE = 1;
    
    //public static float DEVICE_DENSITY;//density
    //public static float DEVICE_WIDTHPIXELS;//widthPixels
    //public static float DEVICE_HEIGHTPIXELS;//heightPixels

}
