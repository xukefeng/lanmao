package cash.app.com.mymvp.presenter;


import cash.app.com.mymvp.IView;
import cash.app.com.mymvp.net.LogUtils;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<V extends IView> implements IBasePresenter<V> {
    protected V mView;
    private CompositeDisposable disposables = new CompositeDisposable();// 管理订阅者者

    public void setDisposable(Disposable disposable) {
        this.disposables.add(disposable);
    }

    @Override
    public void attachView(@NonNull V view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        //取消订阅防止内存泄漏
        if (disposables != null && !disposables.isDisposed()) {
            disposables.dispose();
            disposables = null;
        }
    }

    @Override
    public boolean isViewAttached() {
        return mView != null;
    }
}
