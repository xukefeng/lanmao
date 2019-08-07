package cash.app.com.mymvp.net;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpManager {
    public volatile static Retrofit retrofit;
    public volatile static OkHttpClient mOkHttpClient;
    private static int DEFAULT_TIMEOUT = 60;
    private static int DEFAULT_TIMEOUT_WRITE = 60;
    private static String mBaseUrl = "https://apitest.wificash.in";

    private static OkHttpClient getmOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (HttpManager.class) {
                if (mOkHttpClient == null) {
                    try {
                        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                                .writeTimeout(DEFAULT_TIMEOUT_WRITE, TimeUnit.SECONDS)
                                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                                //支持https
                                .sslSocketFactory(createSSLSocketFactory())
                                .addNetworkInterceptor(new LogInterceptor())
                                .addInterceptor(new CommonParameterInterceptor())
                                .retryOnConnectionFailure(true);

                        mOkHttpClient = builder.build();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return mOkHttpClient;
    }

    /**
     * 获取Retrofit实例
     */
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (HttpManager.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(mBaseUrl)
                            //增加返回值为Gson的支持(以实体类返回)
                            .addConverterFactory(GsonConverterFactory.create())
                            //增加返回值为Oservable<T>的支持
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .client(getmOkHttpClient())
                            .build();
                }
            }
        }
        return retrofit;
    }

    /**
     * 拦截器
     */
    public static class LogInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            //the request url
            String url = request.url().toString();
            //the request method
            String method = request.method();
            long t1 = System.nanoTime();
            //                LogPrint.w(String.format(Locale.getDefault(), "1. Sending %s request [url = %s]", method, url));
            LogUtils.w(String.format(Locale.getDefault(), "1. Sending %s request [url = %s]", method, url));
            //the request body
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                StringBuilder sb = new StringBuilder("Request Body [");
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                Charset charset = Charset.forName("UTF-8");
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(charset);
                }
                if (contentType != null && isPlaintext(buffer)) {
                    sb.append(buffer.readString(charset));
                    sb.append(" (Content-Type = ").append(contentType.toString()).append(",")
                            .append(requestBody.contentLength()).append("-byte body)");
                } else {
                    sb.append(" (Content-Type = ").append(contentType.toString())
                            .append(",binary ").append(requestBody.contentLength()).append("-byte body omitted)");
                }
                sb.append("]");
                LogUtils.w(String.format(Locale.getDefault(), "2. %s  %s", method, sb.toString()));
            }
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            //the response time
            LogUtils.w(String.format(Locale.getDefault(), "3. Received response for [url = %s] in %.1fms",
                    url, (t2 - t1) / 1e6d));
            LogUtils.w(String.format(Locale.CHINA, "4. Received response is %s ,message[%s],code[%d]",
                    response.isSuccessful() ? "success" : "fail", response.message(), response.code()));
            //the response data
            ResponseBody body = response.body();

            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            Charset charset = Charset.defaultCharset();
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            String bodyString = buffer.clone().readString(charset);
            LogUtils.w(String.format("5. Received response json string [%s]", bodyString));
            return response;
        }

        static boolean isPlaintext(Buffer buffer) {
            try {
                Buffer prefix = new Buffer();
                long byteCount = buffer.size() < 64 ? buffer.size() : 64;
                buffer.copyTo(prefix, 0, byteCount);
                for (int i = 0; i < 16; i++) {
                    if (prefix.exhausted()) {
                        break;
                    }
                    int codePoint = prefix.readUtf8CodePoint();
                    if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                        return false;
                    }
                }
                return true;
            } catch (EOFException e) {
                return false; // Truncated UTF-8 sequence.
            }
        }
    }

    public static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

}
