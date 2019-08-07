package cash.app.com.mymvp.api;

import java.util.Map;

import cash.app.com.mymvp.LoginBean;
import cash.app.com.mymvp.UploadImgBean;
import cash.app.com.mymvp.net.HttpResult;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface UserApi {
    /**
     * 登录 String mobileType, String versionNumber, String loginName, String loginPwd, String deviceTokens,
     */
    @FormUrlEncoded
    @POST("api/user/login.htm")
    Observable<HttpResult<LoginBean>> login(@Field("loginName") String loginName, @Field("loginPwd") String loginPwd, @Field("deviceTokens") String deviceTokens);

    /**
     * 单图片上传
     *
     * @param headMap
     * @param map
     * @param part
     * @return
     */
    @Multipart
    @POST("api/act/mine/userInfo/updateHeadImg.htm")
    Observable<HttpResult<UploadImgBean>> uploadFile(@HeaderMap Map<String, String> headMap, @PartMap Map<String, RequestBody> map, @Part MultipartBody.Part part);

    /**
     * 这种写法，单图片多图片都支持
     *
     * @param headMap
     * @param body
     * @return
     */
    @POST("api/act/mine/userInfo/updateHeadImg.htm")
    Observable<HttpResult<UploadImgBean>> uploadFile(@HeaderMap Map<String, String> headMap, @Body MultipartBody body);
}
