package my.example.onekeycleaner.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.example.onekeycleaner.R;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import my.example.onekeycleaner.adapter.AppCacheClearAdapter;
import my.example.onekeycleaner.adapter.ListBaseAdapter;
import my.example.onekeycleaner.adapter.ListBaseAdapter.OnItemActionListener;
import my.example.onekeycleaner.adapter.ListBaseAdapter.OnItemSelectedListener;
import my.example.onekeycleaner.data.InstallAppCacheMapList;
import my.example.onekeycleaner.engine.CacheInfoProvider;
import my.example.onekeycleaner.imgcache.ImageFetcher;
import my.example.onekeycleaner.manager.AppInstall;
import my.example.onekeycleaner.manager.AppStateManager.AppState;
import my.example.onekeycleaner.manager.AppStateManager.AppStateChangeListener;
import my.example.onekeycleaner.model.CacheInfo;
import my.example.onekeycleaner.ui.LoadingViewController;
import my.example.onekeycleaner.util.AppUtils;

public class AppCacheClearController extends AppListTabController implements
OnItemActionListener, OnItemSelectedListener{
	private static final int LOADING = 0;
	private static final int FINISH = 1;

    private CheckBox mActionChecked;
    private CacheInfoProvider mCachaInfoProvider;
    private InstallAppCacheMapList mCacheMap;
    private LoadingViewController mLoadingViewController;
    
    
	public AppCacheClearController(Context context, ViewGroup root,
			ImageFetcher imageFetcher,LoadingViewController loadingViewController) {
			super(context, root, imageFetcher);
		// TODO Auto-generated constructor stub
			mLoadingViewController = loadingViewController;

	        mCachaInfoProvider = new CacheInfoProvider(handler, context);
	        mActionChecked = (CheckBox) mRoot.findViewById(R.id.action_all_checked);
	        mActionChecked.setVisibility(View.VISIBLE);
	        mActionChecked.setOnCheckedChangeListener(mActionCheckedListener);
	        mActionInfo.setOnClickListener(mActionInfoClickListener);

	        mActionButton.setText(R.string.tab_action_clearner_all);

	        loadData();
	}
	
	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case LOADING:
					break;

				case FINISH:
					// 当加载完成之后，就调用provider里面的get方法，
					// 这样就可以得到一个加载完成后的数据了
					mLoadingViewController.endLoading(true);
			        mCacheMap = new InstallAppCacheMapList(mCachaInfoProvider.getCacheInfoList());
			        updateListViewData(true);
			        break;
				default:
					break;
			}
		}
	};
	
	private void loadData()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
		    	mCachaInfoProvider.initCacheInfos();
			}
		}).start();
	}
	
	@Override
	public ListBaseAdapter createAdapter() {
		// TODO Auto-generated method stub
		return new AppCacheClearAdapter(mContext, mListView,mImageFetcher,this,this);
	}

	@Override
	public void onAttachedFragment() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDetachedFragment() {
		// TODO Auto-generated method stub
		
	}
	
	private void updateListViewData(boolean refreshList) {
		AppCacheClearAdapter adapter = (AppCacheClearAdapter) mAdapter;
        if (refreshList) {
        	mCacheMap.refresh();
            adapter.setInstallList(mCacheMap.getList());

            // 刷新被选中的数据
            HashMap<String, CacheInfo> selects = adapter.getSelectedAppList();
            Collection<CacheInfo> installs = selects.values();
            ArrayList<String> toRemoves = new ArrayList<String>();
            for (CacheInfo installcache : installs) {
                String appKey = installcache.packageName;
                if (mCachaInfoProvider.getInstalledApp(appKey) == null) {
                    toRemoves.add(appKey);
                }
            }
            for (String appKey : toRemoves) {
                selects.remove(appKey);
            }
        }

        adapter.notifyDataSetChanged();
    }
	
    
    /**
     * 刷新当前下载数量显示，样式化显示
     */
    private void updateTabActionInfo(int selected, int selectable) {
        Resources res = mContext.getResources();
        String btnText = mContext.getString(R.string.tab_action_uninstall);
        if (selected > 0) {
            /*
             * / 某些机型QuantityString可能会失效，后续直接使用判断 String info =
             * res.getQuantityString( R.plurals.tab_show_selected_apps,
             * selected, selected); int index = info.indexOf(' ');
             * SpannableString spanInfo = new SpannableString(info); int color =
             * res.getColor(R.color.color_downloading_num); int size =
             * res.getDimensionPixelSize(R.dimen.dimen_downloading_size);
             * spanInfo.setSpan(new ForegroundColorSpan(color), 0, index,
             * Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); spanInfo.setSpan(new
             * AbsoluteSizeSpan(size), 0, index,
             * Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
             * mActionInfo.setText(spanInfo);
             * mActionButton.setTextColor(res.getColor
             * (R.color.color_textview_bule_text));
             * mActionButton.setEnabled(true);
             */

            StringBuilder sb = new StringBuilder(btnText);
            sb.append("(").append(selected).append(")");
            mActionButton.setText(sb.toString());
            mActionButton.setTextColor(res
                    .getColor(R.color.color_textview_bule_text));
            mActionButton.setEnabled(true);

        } else {
            mActionButton.setText(btnText);
            mActionButton.setTextColor(res
                    .getColor(R.color.color_textview_text));
            mActionButton.setEnabled(false);
        }

        if (selectable > 0) {
            mActionInfo.setText(selected >= selectable ?
                    R.string.tab_action_unselect_all
                    : R.string.tab_action_select_all);
            mActionInfo.setOnClickListener(mActionInfoClickListener);
            mActionChecked.setEnabled(true);
            mActionChecked.setOnCheckedChangeListener(null);
            mActionChecked.setChecked(selected >= selectable);
            mActionChecked.setOnCheckedChangeListener(mActionCheckedListener);
        } else {
            mActionInfo.setText(R.string.tab_action_select_all);
            mActionInfo.setOnClickListener(null);
            mActionChecked.setChecked(false);
            mActionChecked.setEnabled(false);
        }
    }
    
    private OnCheckedChangeListener mActionCheckedListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {
            AppCacheClearAdapter adapter = (AppCacheClearAdapter) mAdapter;
            if (isChecked) {
                adapter.selectAll();
            } else {
                adapter.clearSelection();
            }
            updateListViewData(false);
        }
    };
    
    private OnClickListener mActionInfoClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            mActionChecked.setChecked(!mActionChecked.isChecked());
        }
    };
    
    
    @Override
    public void onTabActionClick(View tabActionView) {
        AppCacheClearAdapter adapter = (AppCacheClearAdapter) mAdapter;
        HashMap<String, CacheInfo> selectedApps = adapter.getSelectedAppList();
        for (CacheInfo cahceinfo : selectedApps.values()) {
            AppUtils.clearApkCacheBySystemUI(mContext, cahceinfo.packageName);
        }
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        // 先关闭其他菜单
    }
    
    @Override
    public void onItemAction(View itemView, int position, int action,
            Object object) {
        CacheInfo cahceinfo = (CacheInfo) object;

        switch (action) {
        // 按下安装按钮
		case ListBaseAdapter.ITEM_ACTION_CACHE_CLEAR:
			AppUtils.clearApkCacheBySystemUI(mContext, cahceinfo.packageName);
			break;
        default:
            break;
        }
    }
    
    @Override
    public void onItemSelected(Object object, int count) {
        updateTabActionInfo(count,mCachaInfoProvider.getAppCacheClearCount());
    }
    
    /**
     * App状态监听器
     */
    private AppStateChangeListener mStateChangeListener = new AppStateChangeListener() {

        @Override
        public void onStateChanged(String appKey, AppState state) {
            switch (state) {
            case INSTALLED:
            case UNINSTALLED:
            case UPDATED:
                // APP状态发生变化，需刷新整个数据源的列表和排列顺序
                updateListViewData(true);
                break;
            default:
                break;
            }
        }
    };
    
    
    private static class ActionMoreContext {
        public View view;
        public AppInstall data;

        public ActionMoreContext(View itemView, AppInstall appInstall) {
            view = itemView;
            data = appInstall;
        }
    }

}
