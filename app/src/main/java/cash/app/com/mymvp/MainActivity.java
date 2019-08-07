package cash.app.com.mymvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import cash.app.com.mymvp.contract.UserContract;
import cash.app.com.mymvp.model.UserModel;
import cash.app.com.mymvp.net.HttpResult;
import cash.app.com.mymvp.net.LogUtils;
import cash.app.com.mymvp.presenter.MultiplePresenter;
import cash.app.com.mymvp.presenter.UserPresenter;
import cash.app.com.mymvp.utils.MD5Util;
import cash.app.com.mymvp.view.LaGouBehaviorActivity;
import cash.app.com.mymvp.view.OppoBehaviorActivity;
import cash.app.com.mymvp.view.UCBehaviorActivity;
import cash.app.com.mymvp.view.UploadFileActivity;

public class MainActivity extends BaseMvpActivity<MultiplePresenter> implements UserContract.IUserView {

    private UserPresenter userPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        findViewById(R.id.tvLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPresenter.login("1355420711", MD5Util.Md5("123456"), "123456789");
            }
        });
        startActivity(new Intent(this, OppoBehaviorActivity.class));
    }

    @Override
    public void onFail(String msg) {
        LogUtils.w("===========================================登录失败" + msg);
    }


    @Override
    protected MultiplePresenter createPresenter() {
        userPresenter = new UserPresenter();
        //一个页面访问多个网络请求的时候可以这样用
        MultiplePresenter multiplePresenter = new MultiplePresenter(this);
        multiplePresenter.addPresenter(userPresenter);
        return multiplePresenter;
    }

    @Override
    public void login(HttpResult<LoginBean> result) {
        DataManager.getInstance().setLoginInfo(result.getData());
        LogUtils.w("===========================================登录成功" + result.getData().getRefreshToken());
    }

    @Override
    public void uploadFile(HttpResult<UploadImgBean> result) {

    }
}
