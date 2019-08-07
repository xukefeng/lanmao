package cash.app.com.mymvp.presenter;

import java.util.ArrayList;
import java.util.List;

import cash.app.com.mymvp.IView;

/**
 * @Description: 单页面多网络请求时 presenter容器
 */
public class MultiplePresenter<V extends IView> extends BasePresenter<V> {
    private V mView;

    private List<IBasePresenter> presenters = new ArrayList<>();

    @SafeVarargs
    public final <K extends BasePresenter> void addPresenter(K... addPresenter) {
        for (K ap : addPresenter) {
            ap.attachView(mView);
            presenters.add(ap);
        }
    }

    public MultiplePresenter(V mView) {
        this.mView = mView;
    }

    @Override
    public void detachView() {
        for (IBasePresenter presenter : presenters) {
            presenter.detachView();
        }
        mView = null;
    }

}
