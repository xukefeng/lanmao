package cash.app.com.mymvp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.IOException;

public class LoginManager extends DataImpl<LoginBean> {
    private final static String LOGIND_INFO = "login_info";
    private final static String CURRENT_PREFERENCE = "current_preference";
    private SharedPreferences sharedPreferences;

    private Context context;

    public LoginManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(CURRENT_PREFERENCE, Context.MODE_PRIVATE);
    }

    /**
     * 添加登录信息
     */
    public void setLoginBean(LoginBean loginBean) {
        try {
            sharedPreferences.edit().putString(LOGIND_INFO, serialize(loginBean)).commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取
     */
    public LoginBean getLoginBean() {
        LoginBean loginBean = null;
        String str = sharedPreferences.getString(LOGIND_INFO, null);
        if (!TextUtils.isEmpty(str)) {
            try {
                loginBean = deSerialization(str);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return loginBean;

    }

    /**
     * 清除数据
     */
    public void logout() {
        sharedPreferences.edit().remove(LOGIND_INFO).commit();
    }
}
