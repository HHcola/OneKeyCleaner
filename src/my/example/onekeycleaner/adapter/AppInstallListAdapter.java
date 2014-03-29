package my.example.onekeycleaner.adapter;


import java.util.ArrayList;
import java.util.HashMap;

import com.example.onekeycleaner.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import my.example.onekeycleaner.data.InstallMapList;
import my.example.onekeycleaner.data.InstallSortableList;
import my.example.onekeycleaner.manager.AppInstall;
import my.example.onekeycleaner.manager.AppInstallManager;
import my.example.onekeycleaner.widget.ActionMoreItemView;

public class AppInstallListAdapter extends ListBaseAdapter{

	private LayoutInflater mInflater;
	private AppInstallManager mAppInstallManager;
	private InstallSortableList mInstallAppsList;
    private ArrayList<AppInstall> mAppInstallList;
    private HashMap<String, AppInstall> mSelectedAppList;
    
	
	public AppInstallListAdapter(Context context, ListView listView) {
		super(context, listView);
		// TODO Auto-generated constructor stub
		mAppInstallManager = AppInstallManager.getInstance(context);
        InstallMapList mapList;
        mapList = new InstallMapList(mAppInstallManager.getInstalledAppList());
        mInstallAppsList = new InstallSortableList(mapList);
        mAppInstallList = new ArrayList<AppInstall>();
        mAppInstallList = mInstallAppsList.getList();
        mInflater = LayoutInflater.from(context);
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAppInstallList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mAppInstallList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 	View itemView;
	        ItemViewHolder holder;
	        AppInstall install;

	        install = mAppInstallList.get(position);

	        itemView = convertView;
	        Object tag = (itemView != null ? itemView.getTag() : null);

	        if (tag != null) {
	            holder = (ItemViewHolder) itemView.getTag();
	        } else {
	            itemView = mLayoutInflater.inflate(R.layout.listview_action_more_item_view, parent, false);
	            ((ActionMoreItemView) itemView).inflateMainView(R.layout.listview_install_item);

	            holder = new InstallItemViewHolder(itemView);
	            itemView.setTag(holder);
	        }

	        // 刷新Item的各个View数据
	        holder.setData(install, position);

	        // 绑定数据到View
	        itemView.setTag(ITEM_VIEW_TAG_KEY, install);

	        if(position == 0) {
	            itemView.setPadding(0, padding, 0, 0);
	        } else {
	            itemView.setPadding(0, 0, 0, 0);
	        }

	        return itemView;
	}

	
	
	 private class InstallItemViewHolder extends ItemViewHolder {
	        public CheckBox mAppCheckedHolder;
	        public ImageView mAppIconHolder;
	        public TextView mAppNameHolder;
	        public TextView mAppVersionHolder;
	        public TextView mAppSizeHolder;
	        public TextView mAppSystemHolder;
	        public ImageView mIndicatorHolder;
	        public ImageButton mActionButtonHolder;
	        public ImageButton mActionDetailHolder;
	        public ImageButton mActionMoveHolder;

	        public View mInfoContainerHolder;
	        public View mActionMoreHolder;

	        public InstallItemViewHolder(View view) {
	            super(view);

	            mAppCheckedHolder = (CheckBox) view.findViewById(R.id.app_checked);
	            mAppIconHolder = (ImageView) view.findViewById(R.id.app_icon);
	            mAppNameHolder = (TextView) view.findViewById(R.id.app_name);
	            mAppVersionHolder = (TextView) view.findViewById(R.id.app_version);
	            mAppSizeHolder = (TextView) view.findViewById(R.id.app_size);
	            mAppSystemHolder = (TextView) view.findViewById(R.id.app_is_system);
	            mIndicatorHolder = (ImageView) view.findViewById(R.id.action_more_indicator);
	            mActionButtonHolder = (ImageButton) view.findViewById(R.id.action_button);
	            mActionMoveHolder = (ImageButton) view.findViewById(R.id.action_more_first);

	            mInfoContainerHolder = view.findViewById(R.id.app_info_container);
	            mActionMoreHolder = view.findViewById(R.id.action_more_layout);

//	            ActionMoreItemView actionMoreItemView = (ActionMoreItemView) mItemView;
//	            actionMoreItemView.setOneMoreAction(R.drawable.btn_delete_action);
	        }

	        /**
	         * 由于ItemView会被循环使用，需根据Item数据设置好View的状态
	         * 
	         * @param install
	         */
	        @Override
	        public void setData(final Object object, final int position) {
	            final AppInstall install = (AppInstall) object;

	            // 系统APP无法被卸载，CheckBox需被disabled
	            if (install.isSystemApp()) {
	                mAppCheckedHolder.setEnabled(false);
	                // setChecked中会回调setOnCheckedChangeListener，先置空
	                mAppCheckedHolder.setOnCheckedChangeListener(null);
	                mAppCheckedHolder.setChecked(install.isChecked());
	                mAppSystemHolder.setVisibility(View.VISIBLE);

	            } else {
	                mAppCheckedHolder.setEnabled(true);
	                // setChecked中会回调setOnCheckedChangeListener，先置空
	                mAppCheckedHolder.setOnCheckedChangeListener(null);
	                mAppCheckedHolder.setChecked(install.isChecked());
	                mAppCheckedHolder.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	                    @Override
	                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	                        install.setChecked(isChecked);
	                        if (isChecked) {
	                            mSelectedAppList.put(install.mAppKey, install);
	                        } else {
	                            mSelectedAppList.remove(install.mAppKey);
	                        }
	                        mOnSelectedListener.onItemSelected(install, mSelectedAppList.size());
	                    }
	                });
	                mAppSystemHolder.setVisibility(View.GONE);
	            }
//
//	            if (!TextUtils.isEmpty(install.mAppKey)) {
//	                mImageFetcher.loadImageFromPackageManger(install.mAppKey, mAppIconHolder);
//	            }

	            mAppNameHolder.setText(install.mAppName);
	            mAppVersionHolder.setText(install.mVersionName);
	            mAppSizeHolder.setText(install.mSizeText);

	            mActionButtonHolder.setImageResource(install.isSystemApp() ?
	                    R.drawable.btn_open_bg : R.drawable.btn_uninstall_bg);
	            mActionButtonHolder.setOnClickListener(new OnClickListener() {
	                @Override
	                public void onClick(View v) {
	                    int action = install.isSystemApp() ?
	                            ITEM_ACTION_OPEN : ITEM_ACTION_UNINSTALL;
	                    mOnActionListener.onItemAction(mItemView, position, action, install);
	                }
	            });

	            if (install.mInstallFlag == AppInstall.FLAG_INSTALL_AUTO_SDCARD
	                    || install.mInstallFlag == AppInstall.FLAG_INSTALL_AUTO_ROM) {
	                mActionMoveHolder.setImageResource(R.drawable.btn_move_app);
	                mActionMoveHolder.setEnabled(true);
	            } else {
	                mActionMoveHolder.setImageResource(R.drawable.btn_move_app_disable);
	                mActionMoveHolder.setEnabled(false);
	            }
	            mActionMoveHolder.setOnClickListener(new OnClickListener() {
	                @Override
	                public void onClick(View v) {
	                    if (install.mInstallFlag == AppInstall.FLAG_INSTALL_AUTO_SDCARD) {
	                        mOnActionListener.onItemAction(mItemView,
	                                position, ITEM_ACTION_MOVE_ROM, install);
	                    } else if (install.mInstallFlag == AppInstall.FLAG_INSTALL_AUTO_ROM) {
	                        mOnActionListener.onItemAction(mItemView,
	                                position, ITEM_ACTION_MOVE_SDCARD, install);
	                    }
	                }
	            });

	            if (install.isActionMore()) {
	                mActionMoreHolder.setVisibility(View.VISIBLE);
	                mIndicatorHolder.setImageResource(R.drawable.action_more_indicator_up);
	            } else {
	                mActionMoreHolder.setVisibility(View.GONE);
	                mIndicatorHolder.setImageResource(R.drawable.action_more_indicator_down);
	            }
	        }
	    }
	 
    
}
