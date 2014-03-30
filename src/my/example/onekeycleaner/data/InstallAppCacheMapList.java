package my.example.onekeycleaner.data;

import java.util.concurrent.ConcurrentHashMap;

import my.example.onekeycleaner.model.CacheInfo;

public class InstallAppCacheMapList extends MapList<CacheInfo> {

    /**
     * 构造方法
     */
    public InstallAppCacheMapList() {
        super();
    }

    /**
     * 构造方法
     * 
     * @param newMap
     *            被排序的hashMap
     */
    public InstallAppCacheMapList(ConcurrentHashMap<String, CacheInfo> newMap) {
        super(newMap);
    }
}
