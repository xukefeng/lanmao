package cash.app.com.mymvp.presenter;

import java.io.File;

import cash.app.com.mymvp.Callback;
import cash.app.com.mymvp.LoginBean;
import cash.app.com.mymvp.UploadImgBean;
import cash.app.com.mymvp.contract.UserContract;
import cash.app.com.mymvp.model.UserModel;
import cash.app.com.mymvp.net.HttpResult;
import io.reactivex.disposables.Disposable;

public class UserPresenter extends BasePresenter<UserContract.IUserView> implements UserContract.UserPresenter {
    private UserModel model;

    public UserPresenter() {
        model = UserModel.newInstance();
    }

    @Override
    public void login(String userName, String pwd, String token) {
        model.login(userName, pwd, token, new Callback<HttpResult<LoginBean>>() {

            @Override
            public void onSuccess(HttpResult<LoginBean> data) {
                if (isViewAttached())
                    mView.login(data);
            }

            @Override
            public void onFail(String data) {
                if (isViewAttached())
                    mView.onFail(data);
            }

            @Override
            public void onDisposable(Disposable disposable) {
                setDisposable(disposable);
            }
        });
    }

    @Override
    public void uploadFile(File[] files, String[] keys) {
        model.uploadFile(files, keys, new Callback<HttpResult<UploadImgBean>>() {

            @Override
            public void onSuccess(HttpResult<UploadImgBean> data) {
                if (isViewAttached())
                    mView.uploadFile(data);
            }

            @Override
            public void onFail(String data) {
                if (isViewAttached())
                    mView.onFail(data);
            }

            @Override
            public void onDisposable(Disposable disposable) {
                //这里需要与activity的生命周期进行绑定，防止rxjava内存泄漏
                setDisposable(disposable);
            }
        });
    }
}
