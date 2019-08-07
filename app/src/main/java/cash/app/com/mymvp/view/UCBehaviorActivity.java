package cash.app.com.mymvp.view;

import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import cash.app.com.mymvp.BaseActivity;
import cash.app.com.mymvp.R;
import cash.app.com.mymvp.utils.QMUI.QMUIStatusBarHelper;
import cash.app.com.mymvp.view.adapter.TestFragmentAdapter;
import cash.app.com.mymvp.view.behavior.uc.UcNewsHeaderPagerBehavior;

public class UCBehaviorActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, UcNewsHeaderPagerBehavior.OnPagerStateListener {
    private ViewPager mNewsPager;
    private TabLayout mTableLayout;
    private List<TestFragment> mFragments;
    private String[] titles = new String[4];
    private UcNewsHeaderPagerBehavior mPagerBehavior;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ucbehavior;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        QMUIStatusBarHelper.setTransparent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        mPagerBehavior = (UcNewsHeaderPagerBehavior) ((CoordinatorLayout.LayoutParams) findViewById(R.id.id_uc_news_header_pager).getLayoutParams()).getBehavior();
        mPagerBehavior.setPagerStateListener(this);
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
    public void onBackPressed() {
        if (mPagerBehavior != null && mPagerBehavior.isClosed()) {
            mPagerBehavior.openPager();
        } else {
            super.onBackPressed();
        }
    }
}
