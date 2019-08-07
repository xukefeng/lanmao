package cash.app.com.mymvp.presenter;

import cash.app.com.mymvp.IView;
import cash.app.com.mymvp.model.IModel;

public interface IBasePresenter<V extends IView> {
    /**
     * 依附生命view
     *
     * @param view
     */
    void attachView(V view);

    /**
     * 分离View
     */
    void detachView();

    /**
     * 判断View是否已经销毁
     *
     * @return
     */
    boolean isViewAttached();

}
