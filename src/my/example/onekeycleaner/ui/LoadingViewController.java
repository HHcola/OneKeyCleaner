package my.example.onekeycleaner.ui;

import com.example.onekeycleaner.R;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class LoadingViewController {

    private ImageView mIvLoadAnim;
    private AnimationDrawable mLoadingAnim;
    private TextView mTvText;
    private Button mBtnTry;

    private OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    public LoadingViewController(OnClickListener listener) {
        mOnClickListener = listener;
    }

    public void initLoading(View view) {
        mBtnTry = (Button) view.findViewById(R.id.btn_refresh);
        mTvText = (TextView) view.findViewById(R.id.tv_loading_text);
        mIvLoadAnim = (ImageView) view.findViewById(R.id.iv_loading_anim);
    }

    public void startLoading() {
        if (mBtnTry != null) {
            if (mOnClickListener != null) {
                mBtnTry.setOnClickListener(mOnClickListener);
            }
            mBtnTry.setVisibility(View.GONE);
        }
        if (mIvLoadAnim != null) {
            mIvLoadAnim.setVisibility(View.VISIBLE);
            mIvLoadAnim.setBackgroundResource(R.anim.anim_loading);
            mLoadingAnim = (AnimationDrawable) mIvLoadAnim.getBackground();
            mLoadingAnim.setOneShot(false);

            mIvLoadAnim.post(new Runnable() {

                @Override
                public void run() {
                    mLoadingAnim.start();
                }
                
            });
        }
        if(mTvText != null) {
            mTvText.setVisibility(View.VISIBLE);
        }
    }

    public void endLoading(boolean isHide) {
    	if(mBtnTry == null  || mLoadingAnim == null || mIvLoadAnim == null) {
    		return;
    	}
    	
		if (isHide) {
			mIvLoadAnim.setVisibility(View.GONE);
			mIvLoadAnim.setVisibility(View.GONE);
			mLoadingAnim.stop();
			mTvText.setVisibility(View.GONE);
		} else {
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					mBtnTry.setVisibility(View.VISIBLE);
					mIvLoadAnim.setVisibility(View.VISIBLE);
					mIvLoadAnim.setBackgroundResource(R.drawable.loading_failed);
					mLoadingAnim.stop();
					mTvText.setVisibility(View.GONE);
				}
				
			}, 700);
		
		}
    }
}
