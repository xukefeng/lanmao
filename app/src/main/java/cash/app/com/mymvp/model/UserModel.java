package cash.app.com.mymvp.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cash.app.com.mymvp.Callback;
import cash.app.com.mymvp.api.UserApi;
import cash.app.com.mymvp.contract.UserContract;
import cash.app.com.mymvp.net.HttpManager;

public class UserModel extends BaseModel implements UserContract.IUserModel {
    public static UserModel newInstance() {
        UserModel model = new UserModel();
        return model;
    }

    @Override
    public void login(String userName, String pwd, String token, final Callback callback) {
        subscribe(HttpManager.getRetrofit().create(UserApi.class).login(userName, pwd, token), callback);
    }

    @Override
    public void uploadFile(File[] files, String[] keys, final Callback callback) {
        Map<String, String> commonMap = new HashMap<>();
        commonMap.put("mobileType", "2");
        commonMap.put("versionNumber", "1.3.6");
        commonMap.put("channelId", "1");
        commonMap.put("langType", "en");
        subscribe(HttpManager.getRetrofit().create(UserApi.class).uploadFile(paresHeads(commonMap), parseMultipartBodys(files, keys, commonMap)), callback);
    }

}
