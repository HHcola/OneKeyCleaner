package my.example.onekeycleaner.adapter;


import java.util.ArrayList;

import com.example.onekeycleaner.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import my.example.onekeycleaner.data.InstallMapList;
import my.example.onekeycleaner.data.InstallSortableList;
import my.example.onekeycleaner.manager.AppInstall;
import my.example.onekeycleaner.manager.AppInstallManager;

public class AppInstallListAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private AppInstallManager mAppInstallManager;
	private InstallSortableList mInstallAppsList;
    private ArrayList<AppInstall> mAppInstallList;
    
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
    
	
	public AppInstallListAdapter(Context context, ListView listView) {
//		super(context, listView);
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
		AppInstall install;
        install = mAppInstallList.get(position);
        if(convertView == null) {
        	convertView = mInflater.inflate(R.layout.listview_install_item, null);
        }
        	
        mAppCheckedHolder = (CheckBox) convertView.findViewById(R.id.app_checked);
        mAppIconHolder = (ImageView) convertView.findViewById(R.id.app_icon);
        mAppNameHolder = (TextView) convertView.findViewById(R.id.app_name);
        mAppVersionHolder = (TextView) convertView.findViewById(R.id.app_version);
        mAppSizeHolder = (TextView) convertView.findViewById(R.id.app_size);
        mAppSystemHolder = (TextView) convertView.findViewById(R.id.app_is_system);
        mIndicatorHolder = (ImageView) convertView.findViewById(R.id.action_more_indicator);
        mActionButtonHolder = (ImageButton) convertView.findViewById(R.id.action_button);
        
        mAppNameHolder.setText(install.mAppName);
        mAppVersionHolder.setText(install.mVersionName);
        mAppSizeHolder.setText(install.mSizeText);
        mActionButtonHolder.setImageResource(install.isSystemApp() ?
                R.drawable.btn_open_bg : R.drawable.btn_uninstall_bg);
        
        if (install.isActionMore()) {
            mIndicatorHolder.setImageResource(R.drawable.action_more_indicator_up);
        } else {
            mIndicatorHolder.setImageResource(R.drawable.action_more_indicator_down);
        }
		return convertView;
	}

    
}
