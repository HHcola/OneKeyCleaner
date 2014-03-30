/**
 * 
 */
/**
 * @author hewei05
 *
 */
package my.example.onekeycleaner.widget;

import com.example.onekeycleaner.R;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/** 
 * 通用NavigationBar，用于导航栏控制显示以及相关逻辑控制。
 */
public final class NavigationBar extends LinearLayout implements OnClickListener {

	private final String TAG = "NavigationBar";
    public static final int HIDE_BAR = 0;
    public static final int SHOW_NORMAL_BAR = 1;
    public static final int SHOW_BACK_BAR = 2;
    
    
    private Activity mContext;

    private Button mBtnSeting;
    private TextView mTvSettingMsg;
    private View mBtnSettingLayout;
    private View mBackLayout;


    private ViewStub mViewStubNormal;
    private ViewStub mViewStubSetting;
    private ViewStub mViewStubBack;
    
    private Button mBtnBack;
    private MarqueeTextView mTvTitle;
    private MarqueeTextView mTabBackTitle;
    
    private OnBarActionListener mActionListener;
    
	public NavigationBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = (Activity) context;
	}
	
    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = (Activity) context;
    }

    public void setActionListener(OnBarActionListener listener) {
        mActionListener = listener;
    }

    
    public void initBarUi(int showType) {
        if(showType == HIDE_BAR) {
            setVisibility(View.GONE);
            return;
        }
        
        if (mViewStubNormal != null && showType == SHOW_NORMAL_BAR) { 
        	HideBarBack();
            mViewStubNormal.inflate();
            mViewStubSetting.inflate();
            setBarTitle();
            setSettingTag();
        }else if (mViewStubNormal != null && showType == SHOW_BACK_BAR) {
        	mViewStubBack.inflate();
            setBackButton();
        }
    }

    private void setSettingTag() {
    	mBtnSettingLayout = findViewById(R.id.btn_bar_setting_layout);
    	mBtnSeting = (Button) findViewById(R.id.btn_bar_setting);
    	if(mBtnSeting !=null) {
        	mBtnSeting.setOnClickListener(this);
    	}
        if(mBtnSeting != null) {
        	mTvSettingMsg = (TextView) findViewById(R.id.tv_bar_setting_msg);  
        }
    }
    
    private void setBackButton() {
    	mBackLayout = findViewById(R.id.banner_bar_back_layout);
        mBtnBack = (Button) findViewById(R.id.btn_bar_back);
        if(mBtnBack != null) {
            mBtnBack.setOnClickListener(this);       
        }
        mTabBackTitle = (MarqueeTextView) findViewById(R.id.banner_tv_bar_back_title);
        if(mTabBackTitle != null) {
        	mTabBackTitle.setOnClickListener(this);
        }
    }
    
    private void setBarTitle() {
        mTvTitle = (MarqueeTextView) findViewById(R.id.banner_tv_bar_title);
        if(mTvTitle != null) {
            mTvTitle.setOnClickListener(this);
        }
    }
    
    private void HideBarTitle() {
        if(mTvTitle != null) {
            mTvTitle.setVisibility(View.GONE);
        }
    }
    
    private void HideBarSetting() {
        if(mBtnSettingLayout != null) {
        	mBtnSettingLayout.setVisibility(View.GONE);
        }
    }
    
    private void HideBarBack() {
        if(mBackLayout != null) {
        	mBackLayout.setVisibility(View.GONE);
        }
    }
    public void setTitle(String title) {
        if(mTvTitle != null) {
            mTvTitle.setText(title);
        }
    }
    
    public void setBackTitle(String title) {
        if(mTabBackTitle != null) {
        	mTabBackTitle.setText(title);
        }
    }
    
    public void setSettingTitle(String title) {
        if(mTvSettingMsg != null) {
        	mTvSettingMsg.setText(title);
        }
    }
    
    @Override
    protected void onFinishInflate() {
        // TODO Auto-generated method stub
        super.onFinishInflate();
        mViewStubNormal = (ViewStub) findViewById(R.id.viewstub_normal);
        mViewStubSetting = (ViewStub)findViewById(R.id.viewstub_bar_tag_normal);
        mViewStubBack = (ViewStub)findViewById(R.id.viewstub_normal_back);
    }

    // Bar Click
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 final int id = v.getId();
	        switch (id) {
	        case R.id.btn_bar_back:
	            mContext.finish();
	            break;
	        case R.id.banner_tv_bar_title:
	        	break;
	        default:
	            break;
	        }
	}
	
	
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }

    public static final int ACTION_SEARCH = 0;
    public static final int ACTION_BACK = 1;
    public static final int ACTION_SEARCH_INPUT = 2;

    public interface OnBarActionListener {
        public void onBarAction(int action, Object object);
    }
    
}
