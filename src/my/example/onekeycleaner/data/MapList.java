package my.example.onekeycleaner.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;


public abstract class MapList<T> extends AbstractMapList<T> {

    /** List用于存储有序数据 */
    private ArrayList<T> list;

    /** 记录所有数据 */
    private ConcurrentHashMap<String, T> map;

    /** 标志数据是否已经被刷新 */
    private boolean isRefreshed = false;

    public MapList() {
        map = new ConcurrentHashMap<String, T>();
    }

    public MapList(ConcurrentHashMap<String, T> newMap) {
        map = newMap;
    }

    /* (non-Javadoc)
     * @see com.baidu.androidstore.data.AbstractMapList#refresh()
     */
    @Override
    public synchronized void refresh() {
        Collection<T> items = map.values();
        if (items == null) {
            return;
        }
        ArrayList<T> arrayList = new ArrayList<T>(items);
        list = arrayList;
        isRefreshed = true;
    }

    /* (non-Javadoc)
     * @see com.baidu.androidstore.data.AbstractMapList#clear()
     */
    @Override
    public synchronized void clear() {
        if (map != null) {
            map.clear();
        }

        if (list != null) {
            list.clear();
        }
    }

    /* (non-Javadoc)
     * @see com.baidu.androidstore.data.AbstractMapList#getList()
     */
    @Override
    public ArrayList<T> getList() {
        if (!isRefreshed) {
            refresh();
        }
        return list;
    }

    /* (non-Javadoc)
     * @see com.baidu.androidstore.data.AbstractMapList#get(int)
     */
    @Override
    public T get(int index) {
        if (!isRefreshed) {
            refresh();
        }
        return list.get(index);
    }

    /* (non-Javadoc)
     * @see com.baidu.androidstore.data.AbstractMapList#get(java.lang.String)
     */
    @Override
    public T get(String key) {
        return map.get(key);
    }

    /* (non-Javadoc)
     * @see com.baidu.androidstore.data.AbstractMapList#indexOf(java.lang.Object)
     */
    @Override
    public int indexOf(T value) {
        if (!isRefreshed) {
            refresh();
        }
        return list.indexOf(value);
    }

    /* (non-Javadoc)
     * @see com.baidu.androidstore.data.AbstractMapList#put(java.lang.String, java.lang.Object)
     */
    @Override
    public void put(String key, T value) {
        isRefreshed = false;
        map.put(key, value);
    }

    /* (non-Javadoc)
     * @see com.baidu.androidstore.data.AbstractMapList#remove(java.lang.String)
     */
    @Override
    public T remove(String key) {
        isRefreshed = false;
        return map.remove(key);
    }

    /* (non-Javadoc)
     * @see com.baidu.androidstore.data.AbstractMapList#size()
     */
    @Override
    public int size() {
        if (this.map == null) {
            return 0;
        }
        return this.map.size();
    }
}
