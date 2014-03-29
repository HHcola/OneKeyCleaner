package my.example.onekeycleaner.adapter;

import com.example.onekeycleaner.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

public abstract class ListBaseAdapter extends BaseAdapter {

	public static final int ITEM_VIEW_TAG_KEY = R.id.app_main_item_view;
    public static final int ITEM_ACTION_UNKOWN = -1;
    public static final int ITEM_ACTION_PAUSE = 0;
    public static final int ITEM_ACTION_RESUME = 1;
    public static final int ITEM_ACTION_RETRY = 2;
    public static final int ITEM_ACTION_INSTALL = 3;
    public static final int ITEM_ACTION_UNINSTALL = 4;
    public static final int ITEM_ACTION_OPEN = 5;
    public static final int ITEM_ACTION_UPDATE = 6;
    public static final int ITEM_ACTION_DETAIL = 7;
    public static final int ITEM_ACTION_DELETE = 8;
    public static final int ITEM_ACTION_MOVE_SDCARD = 9;
    public static final int ITEM_ACTION_MOVE_ROM = 10;
    public static final int ITEM_ACTION_IGNORE_UPDATE = 11;
    public static final int ITEM_ACTION_CLEAR_HISTORY = 12;
    protected Context mContext;
    protected ListView mListView;
    protected LayoutInflater mLayoutInflater;
    protected OnItemActionListener mOnActionListener;
    protected OnItemSelectedListener mOnSelectedListener;
    
    protected int padding;
    
    public ListBaseAdapter(Context context, ListView listView) {
        mContext = context;
        mListView = listView;
        mLayoutInflater = LayoutInflater.from(context);
    }

    /**
     * 
     * Item 控件事件的回调接口
     * 
     *
     */
    public static interface OnItemActionListener {
        /**
         * Item中的操作事件回调
         * 
         * @param appDownload
         *            Item对应的APP KEY
         * @param action
         *            Action 类型
         */
        public void onItemAction(View itemView, int position, int action, Object object);
    }
    
    /**
     * 
     * Item 控件事件的回调接口
     * 
     *
     */
    public interface OnItemSelectedListener {
        /**
         * Item中的选中事件回调
         * 
         * @param appDownload
         *            Item对应的APP KEY
         * @param action
         *            Action 类型
         */
        void onItemSelected(Object object, int count);
    }
    
    /**
     * ListView ItemView的Holder抽象类
     * 
     *
     */
    public static abstract class ItemViewHolder {
        protected View mItemView;

        public ItemViewHolder(View view) {
            mItemView = view;
        }

        abstract void setData(final Object object, final int position);
    }

}
