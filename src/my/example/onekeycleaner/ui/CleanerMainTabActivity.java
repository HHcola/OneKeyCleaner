package my.example.onekeycleaner.ui;

import java.util.ArrayList;
import java.util.List;

import com.example.onekeycleaner.R;

import my.example.onekeycleaner.adapter.CleanerPageAdapter;
import my.example.onekeycleaner.fragment.HomeFragment;
import my.example.onekeycleaner.fragment.TrashCleanFragment;
import my.example.onekeycleaner.pageindicator.TabPageIndicator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
		setContentView(R.layout.activity_maintab);
		mViewPager = (ViewPager)findViewById(R.id.pager);
		
		// 	Add Fragment To Adapter
        List<Fragment> fragments = new ArrayList<Fragment>();  
        fragments.add(new HomeFragment(this)); 
        fragments.add(new TrashCleanFragment(this));
        
		mPageAdapter = new CleanerPageAdapter(getSupportFragmentManager(),fragments);
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
//			mPageAdapter.getItem(index).check2UpdateListView();
		}
		
		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub
			
		}
		
	};
}
