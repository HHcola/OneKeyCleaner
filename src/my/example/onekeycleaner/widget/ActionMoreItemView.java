package my.example.onekeycleaner.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.onekeycleaner.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;

public class ActionMoreItemView extends RelativeLayout {

    private ViewGroup mMainItemView;
    private View mActionMore;
    private ImageView mActionMoreFirst;
    private ImageView mActionMoreSecond;
    private ImageView mIndicator;

    protected long mAnimationDuration = 150;

    private boolean mIsActionMoreFirstShow = true;
    private int mOriginalHeight;
    private int mOriginalTopMargin;

    public ActionMoreItemView(Context context) {
        super(context);
    }

    public ActionMoreItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActionMoreItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置动画时间
     * 
     * @param duration
     */
    public void setmAnimationTime(long duration) {
        mAnimationDuration = duration;
    }

    @Override
    protected void onFinishInflate() {
        mMainItemView = (ViewGroup) findViewById(R.id.app_main_item_view);
        mActionMore = findViewById(R.id.action_more_layout);
        mActionMoreFirst = (ImageView) findViewById(R.id.action_more_first);
        mActionMoreSecond = (ImageView) findViewById(R.id.action_more_second);
    }

    /**
     * 
     * @param animationListener
     */
    public void openActionMoreMenu(final AnimationListener animationListener) {

        if (mIsActionMoreFirstShow) {
            // 测量ActionMore菜单的宽度和高度
            mActionMore.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            // ActionMore菜单的高度
            mOriginalHeight = mActionMore.getMeasuredHeight();

            LayoutParams params = (LayoutParams) mActionMore.getLayoutParams();
            // 获取ActionMore菜单原始的顶部偏移
            mOriginalTopMargin = params.topMargin;

            mIsActionMoreFirstShow = false;
        }

        // 计算需被往上推的Item
        final boolean pushLastItem;
        final ListView listView = (ListView) getParent();
        final int index = listView.getPositionForView(this);
        final int count = listView.getLastVisiblePosition() - 1;
        pushLastItem = index >= count;

        ValueAnimator animator = ValueAnimator.ofInt(-mOriginalHeight, mOriginalTopMargin)
                .setDuration(mAnimationDuration);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                animationListener.onAnimationStart();
                mActionMore.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animationListener.onAnimationEnd();

                if (mIndicator != null) {
                    mIndicator.setImageResource(R.drawable.action_more_indicator_up);
                }
            }
        });

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                LayoutParams params = (LayoutParams) mActionMore.getLayoutParams();
                params.topMargin = (Integer) valueAnimator.getAnimatedValue();
                mActionMore.setLayoutParams(params);

                if (pushLastItem) {
                    listView.smoothScrollToPosition(index + 1);
                }
            }
        });

        animator.start();
    }

    /**
     * 
     * @param animationListener
     */
    public void closeActionMoreMenu(final AnimationListener animationListener) {
        if (mIsActionMoreFirstShow) {
            // 测量ActionMore菜单的宽度和高度
            mActionMore.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            // ActionMore菜单的高度
            mOriginalHeight = mActionMore.getMeasuredHeight();

            LayoutParams params = (LayoutParams) mActionMore.getLayoutParams();
            // 获取ActionMore菜单原始的顶部偏移
            mOriginalTopMargin = params.topMargin;

            mIsActionMoreFirstShow = false;
        }

        ValueAnimator animator = ValueAnimator.ofInt(mOriginalTopMargin, -mOriginalHeight)
                .setDuration(mAnimationDuration);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                animationListener.onAnimationStart();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mActionMore.setVisibility(View.GONE);
                // 恢复偏移
                LayoutParams params = (LayoutParams) mActionMore.getLayoutParams();
                params.topMargin = mOriginalTopMargin;
                mActionMore.setLayoutParams(params);

                if (mIndicator != null) {
                    mIndicator.setImageResource(R.drawable.action_more_indicator_down);
                }

                animationListener.onAnimationEnd();
            }
        });

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                LayoutParams params = (LayoutParams) mActionMore.getLayoutParams();
                params.topMargin = (Integer) valueAnimator.getAnimatedValue();
                mActionMore.setLayoutParams(params);
            }
        });

        animator.start();
    }

    public void openActionMoreMenuWithoutAnimation() {
        mActionMore.setVisibility(View.VISIBLE);
        if (mIndicator != null) {
            mIndicator.setImageResource(R.drawable.action_more_indicator_up);
        }
    }

    public void closeActionMoreMenuWithoutAnimation() {
        mActionMore.setVisibility(View.GONE);
        if (mIndicator != null) {
            mIndicator.setImageResource(R.drawable.action_more_indicator_down);
        }
    }

    public void setOneMoreAction(int resId) {
        mActionMoreFirst.setImageResource(resId);
        mActionMoreFirst.setVisibility(View.VISIBLE);
        mActionMoreSecond.setVisibility(View.GONE);
    }

    public void setTwoMoreAction(int firstResId, int secondResId) {
        mActionMoreFirst.setImageResource(firstResId);
        mActionMoreFirst.setVisibility(View.VISIBLE);
        mActionMoreSecond.setImageResource(secondResId);
        mActionMoreSecond.setVisibility(View.VISIBLE);
    }

    public void inflateMainView(int layoutId) {
        View.inflate(getContext(), layoutId, mMainItemView);
        mIndicator = (ImageView) mMainItemView.findViewById(R.id.action_more_indicator);
    }

    public interface AnimationListener {
        public void onAnimationStart();
        public void onAnimationEnd();
    }
}
