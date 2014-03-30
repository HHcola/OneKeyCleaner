package my.example.onekeycleaner.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.example.onekeycleaner.R;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import my.example.onekeycleaner.adapter.AppInstallListAdapter;
import my.example.onekeycleaner.adapter.ListBaseAdapter;
import my.example.onekeycleaner.adapter.ListBaseAdapter.OnItemActionListener;
import my.example.onekeycleaner.adapter.ListBaseAdapter.OnItemSelectedListener;
import my.example.onekeycleaner.data.InstallMapList;
import my.example.onekeycleaner.data.InstallSortableList;
import my.example.onekeycleaner.imgcache.ImageFetcher;
import my.example.onekeycleaner.manager.AppInstall;
import my.example.onekeycleaner.manager.AppInstallManager;
import my.example.onekeycleaner.manager.AppStateManager;
import my.example.onekeycleaner.manager.AppStateManager.AppState;
import my.example.onekeycleaner.manager.AppStateManager.AppStateChangeListener;
import my.example.onekeycleaner.manager.AppStateManager.AppStateFilter;
import my.example.onekeycleaner.util.AppUtils;
import my.example.onekeycleaner.widget.ActionMoreItemView;
import my.example.onekeycleaner.widget.ActionMoreItemView.AnimationListener;

public class InstallTabController  extends AppListTabController implements
OnItemActionListener, OnItemSelectedListener {

    private AppInstallManager mAppInstallManager;
    private AppStateManager mAppStateManager;
    private InstallSortableList mInstallAppsList;
    private CheckBox mActionChecked;
    private AppInstallListAdapter mInstallListAdapter;
    private int mTotalHeight = 0;
    
    /** Action More 菜单上下文 */
    private ActionMoreContext mActionMoreContext;

    
	public InstallTabController(Context context, ViewGroup root,ImageFetcher imageFetcher) {
		super(context, root,imageFetcher);
		// TODO Auto-generated constructor stub
		 mInstallListAdapter = (AppInstallListAdapter) mAdapter;

	        mAppInstallManager = AppInstallManager.getInstance(context);
	        mAppStateManager = AppStateManager.getInstance(context);
	        AppStateFilter stateFilter = new AppStateFilter();
	        // 注册关心状态
	        stateFilter.listenState(AppState.INSTALLED, AppState.UNINSTALLED,
	                AppState.UPDATED);
	        mAppStateManager.registerAppStateChangeListener(mStateChangeListener,
	                stateFilter);

	        InstallMapList mapList;
	        mapList = new InstallMapList(mAppInstallManager.getInstalledAppList());
	        mInstallAppsList = new InstallSortableList(mapList);

	        mActionChecked = (CheckBox) mRoot.findViewById(R.id.action_all_checked);
	        mActionChecked.setVisibility(View.VISIBLE);
	        mActionChecked.setOnCheckedChangeListener(mActionCheckedListener);
	        mActionInfo.setOnClickListener(mActionInfoClickListener);

	        mActionButton.setText(R.string.tab_action_uninstall);

	        updateListViewData(true);

	        mListView.setOnTouchListener(new OnTouchListener() {

	            @Override
	            public boolean onTouch(View v, MotionEvent event) {
	                if (mTotalHeight > 0) {
	                    int nowHeight = (new Float(event.getY())).intValue(); // 计算高度
	                    if (mTotalHeight < nowHeight) {
	                        closeActionMoreMenu();
	                    }
	                }
	                return false;
	            }
	        });
	}

	@Override
	public ListBaseAdapter createAdapter() {
		// TODO Auto-generated method stub
        return new AppInstallListAdapter(mContext, mListView,mImageFetcher,this,this);
	}

	@Override
	public void onAttachedFragment() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDetachedFragment() {
		// TODO Auto-generated method stub
		
	}
	
    @Override
    public void onDestroy() {
        mInstallListAdapter.clearSelection();

        // 注销监听器
        mAppStateManager.unregisterAppStateChangeListener(mStateChangeListener);

        // 关闭打开的Action More菜单
        closeActionMoreMenuWithoutAnimation();
    }
    
	private void updateListViewData(boolean refreshList) {
        AppInstallListAdapter adapter = (AppInstallListAdapter) mAdapter;
        if (refreshList) {
            mInstallAppsList.refresh();
            adapter.setInstallList(mInstallAppsList.getList());

            // 刷新被选中的数据
            HashMap<String, AppInstall> selects = adapter.getSelectedAppList();
            Collection<AppInstall> installs = selects.values();
            ArrayList<String> toRemoves = new ArrayList<String>();
            for (AppInstall install : installs) {
                String appKey = install.mAppKey;
                if (mAppInstallManager.getInstalledApp(appKey) == null) {
                    toRemoves.add(appKey);
                }
            }
            for (String appKey : toRemoves) {
                selects.remove(appKey);
            }
            initTotalHeight();
        }

//        updateTabActionInfo(adapter.getSelectedAppList().size(),
//                mAppInstallManager.getUninstallableAppCount());
        adapter.notifyDataSetChanged();
    }

    private void initTotalHeight() {
        int size = mAdapter.getCount();
        if (size > 0) {
            View listItem = mAdapter.getView(size - 1, null, mListView);
            listItem.measure(0, 0);
            mTotalHeight = listItem.getMeasuredHeight() * size + 40;
        }
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
            AppInstallListAdapter adapter = (AppInstallListAdapter) mAdapter;
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
   
   
    
    /**
     * 使用动画打开Action More菜单
     * 
     * @param itemView
     * @param install
     */
    private void openActionMoreMenu(final View itemView,
            final AppInstall install) {

        ActionMoreItemView actionMoreItemView = (ActionMoreItemView) itemView;
        actionMoreItemView.openActionMoreMenu(new AnimationListener() {
            @Override
            public void onAnimationStart() {
                install.setActionMore(true);
                mActionMoreContext = new ActionMoreContext(itemView, install);
            }

            @Override
            public void onAnimationEnd() {
            }
        });
    }
    
    /**
     * 使用动画关闭Action More菜单
     * 
     * @param itemView
     * @param install
     */
    private void closeActionMoreMenu(View itemView, final AppInstall install) {
        ActionMoreItemView actionMoreItemView = (ActionMoreItemView) itemView;
        actionMoreItemView.closeActionMoreMenu(new AnimationListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {
                install.setActionMore(false);
            }
        });
    }

    
    /**
     * 检查ActionMoreContext是否有效
     * 
     * @return
     */
    private boolean checkActionMoreContextValid() {
        if (mActionMoreContext != null) {
            String appKey = mActionMoreContext.data.mAppKey;
            if (!TextUtils.isEmpty(appKey)
                    && mAppInstallManager.getInstalledApp(appKey) != null) {
                return true;
            }
        }
        return false;
    }

    
    
    /**
     * 使用ActionMoreContext上下文关闭Action More菜单
     */
    public void closeActionMoreMenu() {
        if (checkActionMoreContextValid()) {
            mActionMoreContext.view = mInstallListAdapter.findViewWithData(
                    mActionMoreContext.data, mActionMoreContext.view);
            if (mActionMoreContext.view != null) {
                closeActionMoreMenu(mActionMoreContext.view,
                        mActionMoreContext.data);
            } else {
                mActionMoreContext.data.setActionMore(false);
            }
        }
        mActionMoreContext = null;
    }

    
    /**
     * 使用ActionMoreContext上下文，不使用动画关闭Action More菜单
     */
    public void closeActionMoreMenuWithoutAnimation() {
        if (checkActionMoreContextValid()) {
            mActionMoreContext.view = mInstallListAdapter.findViewWithData(
                    mActionMoreContext.data, mActionMoreContext.view);
            if (mActionMoreContext.view != null) {
                View itemView = mActionMoreContext.view;
                ActionMoreItemView actionMoreItemView = (ActionMoreItemView) itemView;
                actionMoreItemView.closeActionMoreMenuWithoutAnimation();
            }
            mActionMoreContext.data.setActionMore(false);
        }
        mActionMoreContext = null;
    }

    
    @Override
    public void onTabActionClick(View tabActionView) {
        AppInstallListAdapter adapter = (AppInstallListAdapter) mAdapter;
        HashMap<String, AppInstall> selectedApps = adapter.getSelectedAppList();
        for (AppInstall install : selectedApps.values()) {
            AppUtils.uninstallApkBySystemUI(mContext, install.mPackageName);
        }
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        // 先关闭其他菜单
        closeActionMoreMenu();

        Object o = mListView.getItemAtPosition(position);
        if (o instanceof AppInstall) {
            AppInstall install = (AppInstall) o;
            if (install.isActionMore()) {
                closeActionMoreMenu(view, install);
            } else {
                openActionMoreMenu(view, install);
            }
        }
    }
    
    @Override
    public void onItemAction(View itemView, int position, int action,
            Object object) {
        AppInstall appInstall = (AppInstall) object;

        switch (action) {
        // 按下安装按钮
        case ListBaseAdapter.ITEM_ACTION_UNINSTALL:
            if (!appInstall.isSystemApp()) {
                AppUtils.uninstallApkBySystemUI(mContext,
                        appInstall.mPackageName);
            }
            break;

            // 按下打开按钮
        case ListBaseAdapter.ITEM_ACTION_OPEN:
            AppUtils.openInstalledApp(mContext, appInstall.mPackageName);
            break;

            // 按下移动到SDCARD按钮
        case ListBaseAdapter.ITEM_ACTION_MOVE_SDCARD:
            AppUtils.openInstalledAppDetails(mContext, appInstall.mPackageName);
            break;

            // 按下移动到SDCARD按钮
        case ListBaseAdapter.ITEM_ACTION_MOVE_ROM:
            AppUtils.openInstalledAppDetails(mContext, appInstall.mPackageName);
            break;

        default:
            break;
        }
    }
    
    @Override
    public void onItemSelected(Object object, int count) {
        updateTabActionInfo(count,
                mAppInstallManager.getUninstallableAppCount());
    }
    
    /**
     * App状态监听器
     */
    private AppStateChangeListener mStateChangeListener = new AppStateChangeListener() {

        @Override
        public void onStateChanged(String appKey, AppState state) {
            // 关闭被卸载的ActionMore菜单
            if (state == AppState.UNINSTALLED && mActionMoreContext != null
                    && appKey.equals(mActionMoreContext.data.mAppKey)) {
                closeActionMoreMenu(mActionMoreContext.view,
                        mActionMoreContext.data);
            }

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
