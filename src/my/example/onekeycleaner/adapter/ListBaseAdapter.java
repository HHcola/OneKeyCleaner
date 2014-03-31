package my.example.onekeycleaner.adapter;

import my.example.onekeycleaner.imgcache.ImageFetcher;

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
    public static final int ITEM_ACTION_CACHE_CLEAR = 13;
    protected Context mContext;
    protected ListView mListView;
    protected LayoutInflater mLayoutInflater;
    protected OnItemActionListener mOnActionListener;
    protected OnItemSelectedListener mOnSelectedListener;
    protected ImageFetcher mImageFetcher;
    protected int padding;
    
    public ListBaseAdapter(Context context, ListView listView,ImageFetcher imageFetcher
    		, OnItemActionListener listener) {
        mContext = context;
        mListView = listView;
        mLayoutInflater = LayoutInflater.from(context);
        mImageFetcher = imageFetcher;
        mOnActionListener = listener;
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
    
    public View findViewWithData(Object data, View contextView) {
        Object contextData = null;
        if (contextView != null) {
            contextData = contextView.getTag(ITEM_VIEW_TAG_KEY);
        }
        if (contextData != data) {
            int count = mListView.getChildCount();
            for (int i = 0; i < count; i++) {
                View itemView = mListView.getChildAt(i);
                Object viewData = itemView.getTag(ITEM_VIEW_TAG_KEY);
                if (viewData != null) {
                    if (viewData == data) {
                        return itemView;
                    }
                }
            }
        } else {
            return contextView;
        }
        return null;
    }

}
