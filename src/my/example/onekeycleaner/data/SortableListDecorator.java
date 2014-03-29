package my.example.onekeycleaner.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public abstract class SortableListDecorator<T> extends AbstractMapList<T> {
    /** AbstractMapList */
    private AbstractMapList<T> mMapList = null;

    /** 按照Comparator定义的规则比较 */
    protected Comparator<T> mSortComparator = null;

    /** 是否已经刷新。 */
    private boolean isRefreshed = false;

    /**
     * 构造方法
     * 
     * @param sortblelist
     *            AbstractMapList
     */
    public SortableListDecorator(AbstractMapList<T> sortblelist) {
        mMapList = sortblelist;
    }

    @Override
    public synchronized void refresh() {
        mMapList.refresh();
        ArrayList<T> localArrayList = mMapList.getList();
        Collections.sort(localArrayList, this.mSortComparator);
        isRefreshed = true;
    }

    @Override
    public synchronized void clear() {
        mMapList.clear();
    }

    @Override
    public ArrayList<T> getList() {
        if (!isRefreshed) {
            refresh();
        }
        return mMapList.getList();
    }

    @Override
    public int indexOf(T value) {
        if (!isRefreshed) {
            refresh();
        }
        return mMapList.indexOf(value);
    }

    @Override
    public synchronized T get(int index) {
        if (!isRefreshed) {
            refresh();
        }
        return mMapList.get(index);
    }

    @Override
    public synchronized T get(String key) {
        if (!isRefreshed) {
            refresh();
        }
        return mMapList.get(key);
    }

    @Override
    public int size() {
        return mMapList.size();
    }

    @Override
    public synchronized T remove(String key) {
        isRefreshed = false;
        return mMapList.remove(key);
    }

    @Override
    public synchronized void put(String key, T value) {
        isRefreshed = false;
        mMapList.put(key, value);
    }
}
