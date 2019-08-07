package cash.app.com.mymvp;


import android.content.Context;

public class DataManager {
    private volatile static DataManager dataManager;
    private LoginManager loginManager;

    public static DataManager getInstance() {
        if (dataManager == null) {
            synchronized (DataManager.class) {
                if (dataManager == null) {
                    dataManager = new DataManager(App.getmContext());
                }
            }
        }
        return dataManager;
    }

    private DataManager(Context context) {
        loginManager = new LoginManager(context);
    }

    /**
     * 设置登录数据
     */
    public void setLoginInfo(LoginBean loginInfo) {
        loginManager.setLoginBean(loginInfo);
    }

    /**
     * 得到登录信息
     */
    public LoginBean getLoginInfo() {
        return loginManager.getLoginBean();
    }

    /**
     * 清除登录数据
     */
    public void clearLoginInfo() {
        loginManager.logout();
    }
}
