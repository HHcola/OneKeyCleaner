package my.example.onekeycleaner.ui;

import com.example.onekeycleaner.R;

import my.example.onekeycleaner.adapter.AppInstallListAdapter;
import my.example.onekeycleaner.adapter.AppListTabController;
import my.example.onekeycleaner.adapter.InstallTabController;
import my.example.onekeycleaner.widget.NavigationBar;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class AppInstallManagerActivity extends BaseActivity implements OnClickListener{

    public static final int TAB_TYPE_DOWNLOAD = 0;
    public static final int TAB_TYPE_UPDATE = 1;
    public static final int TAB_TYPE_INSTALLED = 2;

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

		mInflater = LayoutInflater.from(this);
        mRoot = (ViewGroup) mInflater.inflate(R.layout.activity_install_manager, null);
        mActionInfo = (TextView) mRoot.findViewById(R.id.action_all_info);
        mActionButton = (Button) mRoot.findViewById(R.id.action_all_button);

        initImgWorker(true);
        mImageFetcher = getImageFetcher();
        setBarType(NavigationBar.SHOW_BACK_BAR);
        setBarBackTitle("Install Manager");
		addContentView(mRoot);

        mTabType = TAB_TYPE_INSTALLED;
        mLoadingView = mRoot.findViewById(R.id.ll_empty);

        mLoadingViewController = new LoadingViewController(null);
        mLoadingViewController.initLoading(mLoadingView);

        mLoadingViewController.startLoading();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mLoadingViewController.endLoading(true);
                mController = createController(mTabType);
            }
        }, 400);
        
	}
	
	
    private AppListTabController createController(int tabType) {
        switch (tabType) {
        case TAB_TYPE_DOWNLOAD:
//            return new DownloadTabController(getActivity(), mRoot, mImageFetcher);
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

