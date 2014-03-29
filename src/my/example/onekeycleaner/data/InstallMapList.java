package my.example.onekeycleaner.data;

import java.util.concurrent.ConcurrentHashMap;

import my.example.onekeycleaner.manager.AppInstall;

public class InstallMapList extends MapList<AppInstall> {

    /**
     * 构造方法
     */
    public InstallMapList() {
        super();
    }

    /**
     * 构造方法
     * 
     * @param newMap
     *            被排序的hashMap
     */
    public InstallMapList(ConcurrentHashMap<String, AppInstall> newMap) {
        super(newMap);
    }
}
