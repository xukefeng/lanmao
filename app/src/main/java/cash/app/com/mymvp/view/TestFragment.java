package cash.app.com.mymvp.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cash.app.com.mymvp.R;
import cash.app.com.mymvp.view.adapter.RecyclerViewAdapter;


/**
 * Created by HelloCsl(cslgogogo@gmail.com) on 2016/3/1 0001.
 */
public class TestFragment extends Fragment implements RecyclerViewAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private final static String KEY = "key";
    private final static String REFRESH_SUPPORT = "refresh_support";

    /**
     * rootView是否初始化标志，防止回调函数在rootView为空的时候触发
     */
    private boolean hasCreateView;
    /**
     * 当前Fragment是否处于可见状态标志，防止因ViewPager的缓存机制而导致回调函数的触发
     */
    private boolean isFragmentVisible;

    /**
     * onCreateView()里返回的view，修饰为protected,所以子类继承该类时，在onCreateView里必须对该变量进行初始化
     */
    protected ViewGroup rootView;

    /**
     * 是否已经加载过了
     */
    private boolean isFrist = false;

    /**
     * 控制刷新
     */
    private boolean isRefreshEnable = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (rootView == null) {
            return;
        }
        hasCreateView = true;
        if (isVisibleToUser) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
            return;
        }
        if (isFragmentVisible) {
            onFragmentVisibleChange(false);
            isFragmentVisible = false;
        }
    }

    /**
     * @param isVisible true  不可见 -> 可见
     *                  false 可见  -> 不可见
     */
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {
            if (!isFrist) {
                //这里调用接口逻辑
                initData();
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.hasCreateView = false;
        this.isFragmentVisible = false;
    }

    public static TestFragment newInstance(String desc, boolean refreshSupport) {
        Bundle args = new Bundle();
        args.putString(KEY, desc);
        args.putBoolean(REFRESH_SUPPORT, refreshSupport);
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static TestFragment newInstance(String desc) {
        return newInstance(desc, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_test, container, false);
            this.rootView = root;
            initView(root);
            if (!hasCreateView && getUserVisibleHint()) {
                onFragmentVisibleChange(true);
                isFragmentVisible = true;
            }
        }
        return rootView;
    }


    private void initView(ViewGroup root) {
        mRecyclerView = root.findViewById(R.id.test_recycler);
        mSwipeRefreshLayout = root.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setEnabled(isRefreshEnable);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    public void setRefreshEnable(boolean refreshEnable) {
        this.isRefreshEnable = refreshEnable;
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setEnabled(refreshEnable);
        }
    }

    private void initData() {
        String key = getArguments().getString(KEY, "default");
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            res.add(key + ":Fragment item :" + i);
        }
        mRecyclerView.setAdapter(new RecyclerViewAdapter(res).setOnItemClickListener(this));
        this.isFrist = true;
    }

    @Override
    public void onItemClick(View view, int position) {
        Snackbar.make(mRecyclerView, position + ":click", Snackbar.LENGTH_SHORT).show();
    }
}
