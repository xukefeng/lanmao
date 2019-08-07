package cash.app.com.mymvp.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cash.app.com.mymvp.DataManager;
import cash.app.com.mymvp.utils.MD5Util;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommonParameterInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request.body().contentLength();
        Map<String, String> commonMap = new HashMap<>();
        //这里可以添加公共参数，可以自己稍作封装
        commonMap.put("mobileType", "2");
        commonMap.put("versionNumber", "1.3.6");
        commonMap.put("channelId", "1");
        commonMap.put("langType", "en");
        if (request.method().equalsIgnoreCase("GET")) {
            //Get请求
            String url = request.url().toString();
            StringBuilder builder = new StringBuilder(url);
            for (Map.Entry<String, String> entry : commonMap.entrySet()) {
                builder.append("&" + entry.getKey() + "=" + entry.getValue());
            }
            String surl = builder.toString();
            //判断地址有没有？没有则添加
            if (!surl.contains("?")) {
                surl = surl.replaceFirst("&", "?");
            }
            Set<String> params = request.url().queryParameterNames();
            for (String param : params) {
                String value = request.url().queryParameter(param);
                commonMap.put(param, value);
            }
            //根据老的生成新的
            Request newRequest = request.newBuilder()
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("token", DataManager.getInstance().getLoginInfo().getToken())
                    .addHeader("IMEI", "123456789")
                    .addHeader("signMsg", getSign(commonMap))
                    .url(surl)
                    .build();
            return chain.proceed(newRequest);
        } else {
            //Post请求
            RequestBody body = request.body();
            if (body != null && body instanceof FormBody) {
                FormBody formBody = (FormBody) body;
                //把原来的body添加到新body里
                FormBody.Builder sbuilder = new FormBody.Builder();
                //为了防止添加重复的key 和 value
                for (int i = 0; i < formBody.size(); i++) {
                    commonMap.put(formBody.encodedName(i), formBody.encodedValue(i));
                }
                //把公共参数添加到新的body中
                for (Map.Entry<String, String> entry : commonMap.entrySet()) {
                    sbuilder.add(entry.getKey(), entry.getValue());
                }
                FormBody sformBody = sbuilder.build();
                //根据老的生成新的
                Request newRequest = request.newBuilder()
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("token", DataManager.getInstance().getLoginInfo().getToken())
                        .addHeader("IMEI", "123456789")
                        .addHeader("signMsg", getSign(commonMap))
                        .post(sformBody)
                        .build();
                return chain.proceed(newRequest);
            }
        }
        return chain.proceed(request);
    }

    /**
     * 对map进行排序 由低到高
     */
    public String getSign(Map<String, String> map) {
        if (map == null)
            return "";
        String token = DataManager.getInstance().getLoginInfo() == null ? "" : DataManager.getInstance().getLoginInfo().getToken();
        return MD5Util.Md5("oQIhAP24Kb3Bsf7IE14wpl751bQc9VAPsFZ+LdB4riBgg2TDAiEAsSomOO1v8mK2VWhEQh6mttgN" + token + sortMap(map));
    }

    /**
     * 对Map进行升序
     */
    private String sortMap(Map map) {
        //升序
        List<Map.Entry<String, String>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            //升序排序
            public int compare(Map.Entry<String, String> o1,
                               Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> mapping : list) {
            sb.append(mapping.getKey() + "=" + mapping.getValue() + "|");
        }
        String data = sb.toString().substring(0, sb.toString().length() - 1);
        return data;
    }
}
