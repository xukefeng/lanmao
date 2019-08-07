package cash.app.com.mymvp;

import io.reactivex.disposables.Disposable;

/**
 * @Description: 所有model中的回调
 */
public interface Callback<K> {
    void onSuccess(K data);

    void onFail(String data);

    void onDisposable(Disposable disposable);
}
