package my.example.onekeycleaner.data;
import java.util.ArrayList;

public abstract class AbstractMapList<T> {

    /**
     * 刷新列表，当数据变化时才会实行有效操作
     */
    public abstract void refresh();

    /**
     * 清空列表
     */
    public abstract void clear();

    /**
     * 返回所有的List数据。
     * 
     * @return 所有数据列表
     */

    public abstract ArrayList<T> getList();
    
    /**
     * 获取指定位置的数据
     * 
     * @param index
     *            数据的位置
     * @return value
     */

    public abstract T get(int index);

    /**
     * 根据key获取value
     * 
     * @param key
     *            Key
     * @return value
     */
    public abstract T get(String key);

    /**
     * 根据value获取位置index
     * 
     * @param value
     *            value
     * @return index
     */
    public abstract int indexOf(T value);

    /**
     * 添加一个数据
     * 
     * @param key
     *            key
     * @param value
     *            要添加的value
     */
    public abstract void put(String key, T value);

    /**
     * 删除一个应用
     * 
     * @param key
     *            key
     * @return 要删除的value
     */
    public abstract T remove(String key);

    /**
     * 获取数据的大小
     * 
     * @return 数据的size
     */

    public abstract int size();
}
