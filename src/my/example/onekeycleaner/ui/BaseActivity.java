package my.example.onekeycleaner.ui;

import com.example.onekeycleaner.R;

import my.example.onekeycleaner.util.Utils;
import my.example.onekeycleaner.widget.NavigationBar;
import my.example.onekeycleaner.widget.NavigationBar.OnBarActionListener;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class BaseActivity extends FragmentActivity implements  OnBarActionListener, OnClickListener{

    /**
     * 导航bar
     */
    protected NavigationBar mNavigationBar;
    /**
     * 添加内容view的viewgroup
     */
    private FrameLayout mFlContent;

    /**
     * 布局解析器
     */
    protected LayoutInflater mInflater;
    protected Handler mHandler;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO
        // 初始化常用参数
		InitParams();
	    // 隐藏title
        Utils.strategyUi(this, 1);
		setContentView(R.layout.activity_base);	
        //初始化ui
        initBaseUi();
	}
	
	private void InitParams() {
        mInflater = getLayoutInflater();
        mHandler = new Handler();
		
	}
	 private void initBaseUi() {
	        mNavigationBar = (NavigationBar) findViewById(R.id.ll_navigation_bar);
	        mFlContent = (FrameLayout) findViewById(R.id.fl_content);
	        setBarActionListener(this);
	    }
	
    /**
     * 添加content view
     * 
     * @param view
     *            添加的view
     * @return void
     */
    protected void addContentView(final View view) {
        if (mFlContent != null) {
            LayoutParams mLayoutParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mFlContent.addView(view, mLayoutParams);
        }
    }
    
    /**
     * 设置标题
     * 
     * @param obj
     *            标题对象
     * @return void
     */
    protected void setBarActionListener(OnBarActionListener listener) {
        if (mNavigationBar != null) {
            mNavigationBar.setActionListener(listener);
        }
    }

    /**
     * 设置导航类型
     * 
     * @param type
     *            导航类型
     * @return void
     */
    protected void setBarType(int type) {
        if (mNavigationBar != null) {
            mNavigationBar.initBarUi(type);
        }
    }

    public boolean isBarShow() {
        if(mNavigationBar != null) {
            if(mNavigationBar.getVisibility() == View.VISIBLE) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    
    public void setBarHideOrShow(int visibility) {
        if (mNavigationBar != null) {
            mNavigationBar.setVisibility(visibility);
        }
    }

    
    /**
     * 设置标题
     * 
     * @param obj
     *            标题对象
     * @return void
     */
    protected void setBarTitle(Object obj) {
        if (mNavigationBar != null) {
            String mTitleStr;
            if (obj instanceof Integer) {
                mTitleStr = getResources().getString((Integer) obj);
            } else {
                mTitleStr = obj.toString();
            }

            mNavigationBar.setTitle(mTitleStr);
        }
    }
    
    
    protected void setBarBackTitle(Object obj) {
        if (mNavigationBar != null) {
            String mTitleStr;
            if (obj instanceof Integer) {
                mTitleStr = getResources().getString((Integer) obj);
            } else {
                mTitleStr = obj.toString();
            }

            mNavigationBar.setBackTitle(mTitleStr);
        }
    }
    
    public Handler getmHandler() {
        return mHandler;
    }
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBarAction(int action, Object object) {
		// TODO Auto-generated method stub
		
	}
	
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
