package my.example.onekeycleaner.adapter;

import my.example.onekeycleaner.imgcache.ImageFetcher;

import com.example.onekeycleaner.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public abstract class AppListTabController implements OnClickListener,
		OnItemClickListener {
    protected Context mContext;
    protected ViewGroup mRoot;
    protected ListView mListView;
    protected TextView mActionInfo;
    protected Button mActionButton;
    protected View mActionButtonLayout;
    protected ViewStub mNoResultViewStub;
    protected View mNoResultView;
    protected View mActionAllPanel;

    protected ListBaseAdapter mAdapter;
    
    protected ImageFetcher mImageFetcher;
    
    public AppListTabController(Context context, ViewGroup root,ImageFetcher imageFetcher) {
        mContext = context;
        mRoot = root;
        mImageFetcher = imageFetcher;

        mListView = (ListView) mRoot.findViewById(R.id.list_content);
        mActionInfo = (TextView) mRoot.findViewById(R.id.action_all_info);
        mActionButton = (Button) mRoot.findViewById(R.id.action_all_button);
        mActionButtonLayout = mRoot.findViewById(R.id.action_all_button_layout);
//        mNoResultViewStub = (ViewStub) mRoot.findViewById(R.id.no_result_viewstub);
        mActionAllPanel = mRoot.findViewById(R.id.action_all_panel);

        mActionButton.setOnClickListener(this);

        LayoutInflater inflater = LayoutInflater.from(context);
        View footer = inflater.inflate(R.layout.listview_footer_spacer, null, false);
        mListView.addFooterView(footer, null, false); // 增加底部占位

        mAdapter = createAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }
    
    public abstract ListBaseAdapter createAdapter();

    /**
     * 清理释放Controller持有的资源
     */
    public abstract void onAttachedFragment();

    
    public abstract void onTabActionClick(View tabActionView);
    /**
     * 清理释放Controller持有的资源
     */
    public abstract void onDetachedFragment();

    public void onDestroy() {
        // do nothing
    }
    
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
        onTabActionClick(v);
	}

}
