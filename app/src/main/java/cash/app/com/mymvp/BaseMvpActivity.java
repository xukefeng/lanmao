package cash.app.com.mymvp;
import cash.app.com.mymvp.presenter.BasePresenter;

public abstract class BaseMvpActivity<P extends BasePresenter> extends BaseActivity implements IView {
    protected P mPresenter;

    protected abstract P createPresenter();

    @Override
    protected void initData() {
        super.initData();
        mPresenter = createPresenter();
        //绑定生命周期
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }
}
