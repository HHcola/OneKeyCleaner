package my.example.onekeycleaner.ui;

import com.example.onekeycleaner.R;

import my.example.onekeycleaner.adapter.CleanerPageAdapter;
import my.example.onekeycleaner.pageindicator.TabPageIndicator;
import my.example.onekeycleaner.widget.NavigationBar;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class CleanerMainTabActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = "CleanerMainTabActivity";
	
	private ViewPager mViewPager;
	private CleanerPageAdapter mPageAdapter;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO
		final View view = mInflater.inflate(R.layout.activity_maintab, null);
		setBarType(NavigationBar.SHOW_NORMAL_BAR);
		setBarTitle("OneKeyClearner");
		addContentView(view);
		
		// View Page
		mViewPager = (ViewPager)findViewById(R.id.pager);
        Resources res = getResources();
		mPageAdapter = new CleanerPageAdapter(this,getSupportFragmentManager(),res.getStringArray(R.array.channel_names));
		mViewPager.setAdapter(mPageAdapter);
		
        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
        indicator.setOnPageChangeListener(onPageChangeListener);
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

		private int mPageIndex;
		
		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if (arg1 == 0 || arg2 == 0) {
				return;
			}
			int index = -1;
			if (mPageIndex == 0) {
				index = mPageIndex + 1;
			} else {
				if (mPageIndex == arg0) {
					index = mPageIndex + 1;
				} else {
					index = arg0;
				}
			}
			Log.v(TAG, "ViewPager onPageScrolled == index = "+index);
		}
		
		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub
		}
		
	};
}
