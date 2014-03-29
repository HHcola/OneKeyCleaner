package my.example.onekeycleaner.ui;

import com.example.onekeycleaner.R;

import my.example.onekeycleaner.adapter.AppInstallListAdapter;
import my.example.onekeycleaner.adapter.AppListTabController;
import my.example.onekeycleaner.adapter.InstallTabController;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class AppInstallManagerActivity extends BaseActivity implements OnClickListener{
	
    private ViewGroup mRoot;
    private LayoutInflater mInflater = null;
    protected ListView mListView;
    protected TextView mActionInfo;
    protected Button mActionButton;
    protected View mLoadingView;
    private AppListTabController mController;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO
		mInflater = LayoutInflater.from(this);
        mRoot = (ViewGroup) mInflater.inflate(R.layout.activity_install_manager, null);
        mActionInfo = (TextView) mRoot.findViewById(R.id.action_all_info);
        mActionButton = (Button) mRoot.findViewById(R.id.action_all_button);

//        mListView = (ListView)mRoot.findViewById(R.id.list_content);
//        mInstallListAdapter = new AppInstallListAdapter(this,mListView);
//        mListView.setAdapter(mInstallListAdapter);
        mController = new InstallTabController(this, mRoot);
		setContentView(mRoot);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
}

