package cash.app.com.mymvp.model;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cash.app.com.mymvp.Callback;
import cash.app.com.mymvp.DataManager;
import cash.app.com.mymvp.net.HttpResult;
import cash.app.com.mymvp.utils.MD5Util;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class BaseModel {
    protected MultipartBody parseMultipartBodys(File[] files, String[] fileKeys, Map<String, String> params) {
        MultipartBody.Builder mbody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                mbody.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                mbody.addFormDataPart(fileKeys[i], file.getName(), RequestBody.create(MediaType.parse(guessMimeType(fileName)), file));

            }
        }
        return mbody.build();
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    /**
     * 发起订阅
     *
     * @param observable
     * @param callback
     * @param <T>
     */
    protected <T> void subscribe(final Observable<T> observable, final Callback callback) {
        observable.subscribeOn(Schedulers.io())//发送事件在子线程
                .unsubscribeOn(Schedulers.io())//解除订阅子线程
                .observeOn(AndroidSchedulers.mainThread())//接收事件主线程（获取到网络的数据进行UI的更新显示故在主线程）
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        callback.onDisposable(d);
                    }

                    @Override
                    public void onNext(T t) {
                        callback.onSuccess(t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 在上传图片时添加附加参数
     *
     * @param params
     * @return
     */
    protected Map<String, RequestBody> parseRequestBody(Map<String, String> params) {
        Map<String, RequestBody> map = new HashMap<>();
        MediaType textType = MediaType.parse("text/plain");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            map.put(entry.getKey(), RequestBody.create(textType, entry.getValue()));
        }
        return map;
    }

    /**
     * 添加文件参数
     *
     * @param file
     * @param fileName
     * @return
     */
    protected MultipartBody.Part parseMultipartBody(File file, String fileName) {
        return MultipartBody.Part.createFormData(fileName, file.getName(), RequestBody.create(MediaType.parse(guessMimeType(fileName)), file));
    }

    /**
     * 添加在定义Head
     */
    protected Map<String, String> paresHeads(Map<String, String> parsms) {
        Map<String, String> heads = new HashMap<>();
        heads.put("token", DataManager.getInstance().getLoginInfo().getToken());
        heads.put("signMsg", getSign(parsms));
        heads.put("IMEI", "123456789");
        return heads;
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
