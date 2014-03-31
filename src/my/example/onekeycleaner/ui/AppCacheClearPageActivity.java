package my.example.onekeycleaner.ui;

import com.example.onekeycleaner.R;

import my.example.onekeycleaner.adapter.AppInstallListAdapter;
import my.example.onekeycleaner.controller.AppCacheClearController;
import my.example.onekeycleaner.controller.AppListTabController;
import my.example.onekeycleaner.controller.InstallTabController;
import my.example.onekeycleaner.widget.NavigationBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class AppCacheClearPageActivity extends BaseActivity implements OnClickListener{
    private ViewGroup mRoot;
    private LayoutInflater mInflater = null;
    protected ListView mListView;
    protected TextView mActionInfo;
    protected Button mActionButton;
    protected View mLoadingView;
    private AppListTabController mController;
    protected LoadingViewController mLoadingViewController;
    
    private int mTabType;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO

		Intent intent = getIntent();
		mTabType = intent.getIntExtra("type", TAB_TYPE_CACHE_CLEAR);
		
		mInflater = LayoutInflater.from(this);
        mRoot = (ViewGroup) mInflater.inflate(R.layout.activity_install_manager, null);
        mActionInfo = (TextView) mRoot.findViewById(R.id.action_all_info);
        mActionButton = (Button) mRoot.findViewById(R.id.action_all_button);

        initImgWorker(true);
        mImageFetcher = getImageFetcher();
        setBarType(NavigationBar.SHOW_BACK_BAR);
        if(mTabType == TAB_TYPE_CACHE_CLEAR) {
            setBarBackTitle("Cache Clear");
        }else if(mTabType == TAB_TYPE_INSTALLED) {
            setBarBackTitle("Install Manager");
        }
		addContentView(mRoot);
        mLoadingView = mRoot.findViewById(R.id.ll_empty);

        mLoadingViewController = new LoadingViewController(null);
        mLoadingViewController.initLoading(mLoadingView);

        mLoadingViewController.startLoading();
        mController = createController(mTabType);        
	}
	
	
    private AppListTabController createController(int tabType) {
        switch (tabType) {
        case TAB_TYPE_CACHE_CLEAR:
            return new AppCacheClearController(this, mRoot, mImageFetcher,mLoadingViewController);
        case TAB_TYPE_UPDATE:
//            return new UpdateTabController(getActivity(), mRoot, mImageFetcher);
        case TAB_TYPE_INSTALLED:
            return new InstallTabController(this, mRoot, mImageFetcher);
        default:
            return null;
        }
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
}

