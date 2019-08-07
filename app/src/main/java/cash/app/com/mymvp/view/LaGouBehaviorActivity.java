package cash.app.com.mymvp.view;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import cash.app.com.mymvp.BaseActivity;
import cash.app.com.mymvp.R;
import cash.app.com.mymvp.utils.QMUI.QMUIStatusBarHelper;
import cash.app.com.mymvp.view.adapter.TestFragmentAdapter;
import cash.app.com.mymvp.view.behavior.lagou.LagouNewsContentBehavior;
import cash.app.com.mymvp.view.behavior.lagou.LagouNewsHeaderPagerBehavior;
import cash.app.com.mymvp.view.behavior.oppo.OppoNewsHeaderPagerBehavior;

public class LaGouBehaviorActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, LagouNewsHeaderPagerBehavior.OnPagerStateListener, LagouNewsContentBehavior.OnContentListener {
    private ViewPager mNewsPager;
    private TabLayout mTableLayout;
    private List<TestFragment> mFragments;
    private String[] titles = new String[4];
    private LagouNewsHeaderPagerBehavior mPagerBehavior;
    private LagouNewsContentBehavior mContentBehavior;
    private FrameLayout fl_title;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_la_gou_behavior;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        QMUIStatusBarHelper.setTransparent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        fl_title = findViewById(R.id.fl_title);
        mPagerBehavior = (LagouNewsHeaderPagerBehavior) ((CoordinatorLayout.LayoutParams) findViewById(R.id.id_uc_news_header_pager).getLayoutParams()).getBehavior();
        mPagerBehavior.setPagerStateListener(this);
        mContentBehavior = (LagouNewsContentBehavior) ((CoordinatorLayout.LayoutParams) findViewById(R.id.ll_content).getLayoutParams()).getBehavior();
        mContentBehavior.setOnContentListener(this);
        mNewsPager = findViewById(R.id.id_uc_news_content);
        mTableLayout = findViewById(R.id.id_uc_news_tab);
        mFragments = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mFragments.add(TestFragment.newInstance(String.valueOf(i), false));
            titles[i] = "Tab " + i;
        }
        mTableLayout.setTabMode(TabLayout.MODE_FIXED);
        mNewsPager.setAdapter(new TestFragmentAdapter(mFragments, titles, getSupportFragmentManager()));
        mTableLayout.setupWithViewPager(mNewsPager);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mNewsPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        mNewsPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onPagerClosed() {
        for (int i = 0; i < mFragments.size(); i++) {
            mFragments.get(i).setRefreshEnable(true);
        }
        Snackbar.make(mNewsPager, "pager closed", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onPagerOpened() {
        for (int i = 0; i < mFragments.size(); i++) {
            mFragments.get(i).setRefreshEnable(false);
        }
        Snackbar.make(mNewsPager, "pager opened", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onProgress(int scrollY) {
        if (scrollY <= 0) {
            fl_title.setBackgroundColor(Color.argb(0, 255, 153, 52));
        } else if (scrollY > 0 && scrollY <= 200) {
            float scale = (float) scrollY / 200;
            float alpha = (255 * scale);
            fl_title.setBackgroundColor(Color.argb((int) alpha, 255, 153, 52));

        } else {
            fl_title.setBackgroundColor(Color.argb(255, 255, 153, 52));
        }
    }


    @Override
    public void onBackPressed() {
        if (mPagerBehavior != null && mPagerBehavior.isClosed()) {
            mPagerBehavior.openPager();
        } else {
            super.onBackPressed();
        }
    }
}
