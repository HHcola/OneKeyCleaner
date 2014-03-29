package my.example.onekeycleaner.manager;

public class AppInstall {
    public static final int FLAG_INSTALL_AUTO_SDCARD = 1;
    public static final int FLAG_INSTALL_AUTO_ROM = 2;
    public static final int FLAG_INSTALL_ONLY_ROM = 3;

    public String mPackageName;
    public String mSignMd5;
    public String mAppName;
    public int mVersionCode;
    public String mVersionName;
    public long mSize;
    public String mIconUri;

    public String mAppKey;
    public boolean mChecked;
    public String mSizeText;
    public long mLastInstallTime;
    public String mInstallApkDir;
    public boolean mIsSystemApp;
    public int mInstallFlag;

    /** 是否打开更多菜单模式 */
    private boolean mIsActionMore = false;

    /** 是否当前正在播放菜单打开动画 */
    private boolean mIsPlayingAnimation = false;


    /**
     * @return the mPackage
     */
    public String getPackageName() {
        return mPackageName;
    }

    /**
     * @param mPackage
     *            the mPackage to set
     */
    public void setPackageName(String packageName) {
        mPackageName = packageName;
    }

    /**
     * @return the mSignMd5
     */
    public String getSignMd5() {
        return mSignMd5;
    }

    /**
     * @param mSignMd5
     *            the mSignMd5 to set
     */
    public void setSignMd5(String signMd5) {
        mSignMd5 = signMd5;
    }

    /**
     * @return the mAppName
     */
    public String getAppName() {
        return mAppName;
    }

    /**
     * @param mAppName
     *            the mSname to set
     */
    public void setAppName(String appName) {
        mAppName = appName;
    }

    /**
     * @return the mVersionCode
     */
    public int getVersionCode() {
        return mVersionCode;
    }

    /**
     * @param mVersionCode
     *            the mVersionCode to set
     */
    public void setVersionCode(int versionCode) {
        mVersionCode = versionCode;
    }

    /**
     * @return the mVersionName
     */
    public String getVersionName() {
        return mVersionName;
    }

    /**
     * @param mVersion
     *            the mVersionName to set
     */
    public void setVersionName(String versionName) {
        mVersionName = versionName;
    }

    /**
     * @return the mSize
     */
    public long getSize() {
        return mSize;
    }

    /**
     * @param mApkSize
     *            the mSize to set
     */
    public void setSize(long size) {
        mSize = size;
    }

    /**
     * @return the mIconUrl
     */
    public String getIconUri() {
        return mIconUri;
    }

    /**
     * @param mIconUri
     *            the mIconUrl to set
     */
    public void setIconUri(String iconUri) {
        mIconUri = iconUri;
    }


    /**
     * @return the mAppKey
     */
    public String getAppKey() {
        return mAppKey;
    }

    /**
     * @param appKey the mAppKey to set
     */
    public void setAppKey(String appKey) {
        mAppKey = appKey;
    }

    /**
     * @return the mChecked
     */
    public boolean isChecked() {
        return mChecked;
    }

    /**
     * @param checked the mChecked to set
     */
    public void setChecked(boolean checked) {
        mChecked = checked;
    }

    /**
     * @return the mSizeText
     */
    public String getSizeText() {
        return mSizeText;
    }

    /**
     * @param sizeText the mSizeText to set
     */
    public void setSizeText(String sizeText) {
        mSizeText = sizeText;
    }

    /**
     * @return the mLastInstallTime
     */
    public long getLastInstallTime() {
        return mLastInstallTime;
    }

    /**
     * @param lastInstallTime the mLastInstallTime to set
     */
    public void setLastInstallTime(long lastInstallTime) {
        mLastInstallTime = lastInstallTime;
    }

    /**
     * @return the mInstallApkDir
     */
    public String getInstallApkDir() {
        return mInstallApkDir;
    }

    /**
     * @param installApkDir the mInstallApkDir to set
     */
    public void setmInstallApkDir(String installApkDir) {
        mInstallApkDir = installApkDir;
    }

    /**
     * @return the mIsActionMore
     */
    public boolean isActionMore() {
        return mIsActionMore;
    }

    /**
     * @param isActionMore the mIsActionMore to set
     */
    public void setActionMore(boolean isActionMore) {
        mIsActionMore = isActionMore;
    }

    /**
     * @return the mIsPlayingAnimation
     */
    public boolean isPlayingAnimation() {
        return mIsPlayingAnimation;
    }

    /**
     * @param isPlayingAnimation the mIsPlayingAnimation to set
     */
    public void setPlayingAnimation(boolean isPlayingAnimation) {
        mIsPlayingAnimation = isPlayingAnimation;
    }

    /**
     * @return the mIsSystemApp
     */
    public boolean isSystemApp() {
        return mIsSystemApp;
    }

    /**
     * @param isSystemApp the mIsSystemApp to set
     */
    public void setSystemApp(boolean isSystemApp) {
        mIsSystemApp = isSystemApp;
    }

    /**
     * @return the mInstallStatus
     */
    public int getInstallFlag() {
        return mInstallFlag;
    }

    /**
     * @param installStatus the mInstallStatus to set
     */
    public void setInstallFlag(int installStatus) {
        mInstallFlag = installStatus;
    }
}
