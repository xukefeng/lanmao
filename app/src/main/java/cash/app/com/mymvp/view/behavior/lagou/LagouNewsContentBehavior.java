package cash.app.com.mymvp.view.behavior.lagou;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import cash.app.com.mymvp.App;
import cash.app.com.mymvp.BuildConfig;
import cash.app.com.mymvp.R;
import cash.app.com.mymvp.view.behavior.helper.HeaderScrollingViewBehavior;


/**
 * 可滚动的新闻列表Behavior
 * <p/>
 * Created by chensuilun on 16/7/24.
 */
public class LagouNewsContentBehavior extends HeaderScrollingViewBehavior {
    private static final String TAG = "UcNewsContentBehavior";
    private float lastX, lastY;

    public LagouNewsContentBehavior() {
    }

    public LagouNewsContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return isDependOn(dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onDependentViewChanged");
        }
        offsetChildAsNeeded(parent, child, dependency);
        return false;
    }

    private void offsetChildAsNeeded(CoordinatorLayout parent, View child, View dependency) {
        if (onContentListener != null) {
            int value = Math.abs((int) (-dependency.getTranslationY() / (getHeaderOffsetRange() * 1.0f) * getScrollRange(dependency)));
            onContentListener.onProgress(value);
        }
        child.setTranslationY((int) (-dependency.getTranslationY() / (getHeaderOffsetRange() * 1.0f) * getScrollRange(dependency)));

    }

    /**
     * 在被依赖的head属于打开状态的时候是否允许ViewPager可以左右滑动，如果允许滑动可以注释掉拦截器，否则ViewPager将不能滑动，知道head关闭
     * 这里只针对可以横向滑动的容器
     *
     * @return
     */

//    @Override
//    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
//        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
//            lastX = ev.getX();
//            lastY = ev.getY();
//        }
//
//        if (ev.getActionMasked() == MotionEvent.ACTION_MOVE) {
//            float currX = ev.getX();
//            float currY = ev.getY();
//            boolean horizontalScroll = Math.abs(currX - lastX) / Math.abs(currY - lastY) > 1;
//            if (horizontalScroll && child.getTranslationY() == 0)
//                return true;
//        }
//
//        return super.onInterceptTouchEvent(parent, child, ev);
//    }
    @Override
    protected View findFirstDependency(List<View> views) {
        for (int i = 0, z = views.size(); i < z; i++) {
            View view = views.get(i);
            if (isDependOn(view))
                return view;
        }
        return null;
    }

    @Override
    protected int getScrollRange(View v) {
        if (isDependOn(v)) {
            return Math.max(0, v.getMeasuredHeight() - getFinalHeight());
        } else {
            return super.getScrollRange(v);
        }
    }

    private int getHeaderOffsetRange() {
        return App.getmContext().getResources().getDimensionPixelOffset(R.dimen.lagou_news_header_pager_offset);
    }

    private int getFinalHeight() {
        return App.getmContext().getResources().getDimensionPixelOffset(R.dimen.lagou_news_header_title_height);
    }


    private boolean isDependOn(View dependency) {
        return dependency != null && dependency.getId() == R.id.id_uc_news_header_pager;
    }

    private OnContentListener onContentListener;

    public void setOnContentListener(OnContentListener onContentListener) {
        this.onContentListener = onContentListener;
    }

    public interface OnContentListener {
        /**
         * Progress value
         */
        void onProgress(int offset);
    }
}
