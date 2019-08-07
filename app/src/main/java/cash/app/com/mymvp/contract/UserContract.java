package cash.app.com.mymvp.contract;

import java.io.File;
import java.util.Map;

import cash.app.com.mymvp.Callback;
import cash.app.com.mymvp.IView;
import cash.app.com.mymvp.LoginBean;
import cash.app.com.mymvp.UploadImgBean;
import cash.app.com.mymvp.model.IModel;
import cash.app.com.mymvp.net.HttpResult;
import okhttp3.RequestBody;

public interface UserContract {
    interface IUserView extends IView {
        void login(HttpResult<LoginBean> result);

        void uploadFile(HttpResult<UploadImgBean> result);
    }

    interface UserPresenter {
        void login(String userName, String pwd, String token);

        void uploadFile(File[] files, String[] keys);
    }

    interface IUserModel extends IModel {
        void login(String userName, String pwd, String token, Callback callback);

        void uploadFile(File[] files, String[] keys, Callback callback);
    }
}
