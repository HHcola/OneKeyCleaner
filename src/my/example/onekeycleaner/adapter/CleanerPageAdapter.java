/**
 * 
 */
/**
 * @author hewei05
 *
 */
package my.example.onekeycleaner.adapter;

import my.example.onekeycleaner.fragment.HomeFragment;
import my.example.onekeycleaner.fragment.TrashCleanFragment;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CleanerPageAdapter extends FragmentPagerAdapter {
	private static final int HOME_PAGE = 0;
	private static final int TRASH_CLEAR_PAGE = 1;
	private static final int  OTHER_PAGE = 2;
	private static final int PAGE_NUM = 3;
	private final String[] fChannelNames;
	private Fragment[] mFragments;
	private Context mContent;
    public CleanerPageAdapter(Context mContent,FragmentManager fm,String[] channelNames) {  
        super(fm);  
        this.mContent = mContent;
        this.fChannelNames = channelNames;
        mFragments = new Fragment[PAGE_NUM];
    }  
	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		Fragment fragment = mFragments[position];
		if(fragment == null) {
			switch (position) {
			case HOME_PAGE:
				mFragments[position] = new HomeFragment(mContent);
				break;
			case TRASH_CLEAR_PAGE:
				mFragments[position] = new TrashCleanFragment(mContent);
				break;
			case OTHER_PAGE:
				mFragments[position] = new HomeFragment(mContent);
				break;
			default:
				mFragments[position] = new HomeFragment(mContent);
				break;
			}
			
		}
		return mFragments[position];
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return fChannelNames[position];
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return PAGE_NUM;
	}
	
}