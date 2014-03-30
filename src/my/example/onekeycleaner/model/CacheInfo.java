package my.example.onekeycleaner.model;

import android.graphics.drawable.Drawable;

public class CacheInfo
{
    public static final int FLAG_INSTALL_AUTO_SDCARD = 1;
    public static final int FLAG_INSTALL_AUTO_ROM = 2;
    public static final int FLAG_INSTALL_ONLY_ROM = 3;
    
	public String name;
	public String packageName;
	public Drawable icon;
	public boolean mChecked;
	public boolean mIsSystemApp;
	public int mInstallFlag;
	private boolean mIsActionMore = false;
	//应用大小
	public String codeSize;
	//数据大小
	public String dataSize;
	//缓存大小
	public String cacheSize;
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getPackageName()
	{
		return packageName;
	}
	public void setPackageName(String packageName)
	{
		this.packageName = packageName;
	}
	public Drawable getIcon()
	{
		return icon;
	}
	public void setIcon(Drawable icon)
	{
		this.icon = icon;
	}
	public String getCodeSize()
	{
		return codeSize;
	}
	public void setCodeSize(String codeSize)
	{
		this.codeSize = codeSize;
	}
	public String getDataSize()
	{
		return dataSize;
	}
	public void setDataSize(String dataSize)
	{
		this.dataSize = dataSize;
	}
	public String getCacheSize()
	{
		return cacheSize;
	}
	public void setCacheSize(String cacheSize)
	{
		this.cacheSize = cacheSize;
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
