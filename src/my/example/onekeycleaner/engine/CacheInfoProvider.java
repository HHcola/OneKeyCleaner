package my.example.onekeycleaner.engine;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.RemoteException;

import my.example.onekeycleaner.model.CacheInfo;
import my.example.onekeycleaner.util.AppUtils;
import my.example.onekeycleaner.util.TextFormater;


public class CacheInfoProvider
{
	private Handler handler;
	private PackageManager packageManager;
	private Vector<CacheInfo> cacheInfos;
	private int size = 0;
	private Context context;
	private ConcurrentHashMap<String, CacheInfo> mCacheInfos;

    
	public CacheInfoProvider(Handler handler, Context context)
	{
		// 拿到一个包管理器
		packageManager = context.getPackageManager();
		this.handler = handler;
		this.context = context;
		cacheInfos = new Vector<CacheInfo>();
        mCacheInfos = new ConcurrentHashMap<String, CacheInfo>();
	}

	public void initCacheInfos()
	{
		// 获取到所有安装了的应用程序的信息，包括那些卸载了的，但没有清除数据的应用程序
		List<PackageInfo> packageInfos = packageManager
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		
        // 查询拥有Launcher入口的APP
        final Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> installedactivity = context.getPackageManager()
                .queryIntentActivities(intent, 0);
        HashSet<String> launcherApps = new HashSet<String>();
        for (ResolveInfo ri : installedactivity) {
            launcherApps.add(ri.activityInfo.packageName);
        }
        
		size = packageInfos.size();
		for (int i = 0; i < size; i++)
		{
			PackageInfo packageInfo = packageInfos.get(i);
            if (packageInfo.packageName.equalsIgnoreCase(context.getPackageName())) {
                continue;
            }

            // 过滤没有Launcher入口的系统APP
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)
                    == ApplicationInfo.FLAG_SYSTEM
                    && !launcherApps.contains(packageInfo.packageName)) {
                continue;
            }
			CacheInfo cacheInfo = new CacheInfo();
			// 拿到包名
			String packageName = packageInfo.packageName;
			cacheInfo.setPackageName(packageName);
			// 拿到应用程序的信息
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			// 拿到应用程序的程序名
			String name = applicationInfo.loadLabel(packageManager).toString();
			cacheInfo.setName(name);
			cacheInfo.setVersion(packageInfo.versionName);
			// 拿到应用程序的图标
			Drawable icon = applicationInfo.loadIcon(packageManager);
			cacheInfo.setIcon(icon);

	        String appKey = AppUtils.generateAppKey(packageInfo.packageName, packageInfo.versionCode);
	        cacheInfo.setAppKey(appKey);
	        
			initDataSize(cacheInfo, i);
		}
	}

	/**
	 * 通过AIDL的方法来获取到应用的缓存信息，getPackageSizeInfo是PackageManager里面的一个私有方法来的
	 * 我们通过反射就可以调用到它的了，但是这个方法里面会传递一个IPackageStatsObserver.Stub的对象
	 * 里面就可能通过AIDL来获取我们想要的信息了
	 * 
	 * 因为这样的调用是异步的，所以当我们完成获取完这些信息之后，我们就通过handler来发送一个消息
	 * 来通知我们的应用，通过getCacheInfos来获取到我们的Vector
	 * 
	 * 为什么要用Vector呢，因为下面的方法是异步的，也就是有可能是多线程操作，所以我们就用了线程安全的Vector
	 * 
	 * @param cacheInfo
	 * @param position
	 */
	private void initDataSize(final CacheInfo cacheInfo, final int position)
	{
		try
		{
			Method method = PackageManager.class.getMethod(
					"getPackageSizeInfo", new Class[] { String.class,
							IPackageStatsObserver.class });
			method.invoke(packageManager,
					new Object[] { cacheInfo.getPackageName(),
							new IPackageStatsObserver.Stub()
							{
								@Override
								public void onGetStatsCompleted(
										PackageStats pStats, boolean succeeded)
										throws RemoteException
								{
									System.out.println("onGetStatsCompleted" + position);
									long cacheSize = pStats.cacheSize;
									long codeSize = pStats.codeSize;
									long dataSize = pStats.dataSize;

									if(cacheSize > 0) {
										cacheInfo.setCacheSize(TextFormater
												.dataSizeFormat(cacheSize));
										cacheInfo.setCodeSize(TextFormater
												.dataSizeFormat(codeSize));
										cacheInfo.setDataSize(TextFormater
												.dataSizeFormat(dataSize));

										cacheInfos.add(cacheInfo);
									}
									if (position == (size - 1))
									{
										// 当完全获取完信息之后，发送一个成功的消息
										// 1对应的就是CacheClearActivity里面的FINISH
										handler.sendEmptyMessage(1);
									}
								}
							} });
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Vector<CacheInfo> getCacheInfos()
	{
		return cacheInfos;
	}

	public void setCacheInfos(Vector<CacheInfo> cacheInfos)
	{
		this.cacheInfos = cacheInfos;
	}
	
	public void removeCacheInfos(String packageName)
	{
    	for(int i = 0; i < cacheInfos.size(); i++) {
    		CacheInfo cacheInfo = cacheInfos.get(i);
            String pName = cacheInfo.getPackageName();
            if(pName.equals(packageName)) {
        		mCacheInfos.remove(pName);
            }
    	}
	}
	
    public void refreshCacheInfoList() {
    	initCacheInfos();
    	for(int i = 0; i < cacheInfos.size(); i++) {
    		CacheInfo cacheInfo = cacheInfos.get(i);
            String pName = cacheInfo.getPackageName();
            if(!pName.isEmpty()) {
                mCacheInfos.put(pName, cacheInfo);
            }
    	}
    }

    public ConcurrentHashMap<String, CacheInfo> getCacheInfoList() {
    	if(mCacheInfos.isEmpty()) {
        	for(int i = 0; i < cacheInfos.size(); i++) {
        		CacheInfo cacheInfo = cacheInfos.get(i);
                String pName = cacheInfo.getPackageName();
                if(!pName.isEmpty()) {
                    mCacheInfos.put(pName, cacheInfo);
                }
        	}
    	}
        return mCacheInfos;
    }

    /**
     * 
     * @return
     */
    public CacheInfo getInstalledApp(String appKey) {
        return mCacheInfos.get(appKey);
    }
    
    public int getAppCacheClearCount() {
        int count = 0;
        Collection<CacheInfo> appInstalls = mCacheInfos.values();
        for (CacheInfo appInstall : appInstalls) {
            if (!appInstall.mIsSystemApp) {
                count++;
            }
        }
        return count;
    }
    
}
