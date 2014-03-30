package my.example.onekeycleaner.model;

import android.graphics.drawable.Drawable;

public class CacheInfo
{
	public String name;
	public String packageName;
	public Drawable icon;
	public boolean mChecked;
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


}
