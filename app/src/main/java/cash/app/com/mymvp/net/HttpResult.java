package cash.app.com.mymvp.net;

/**
 * Created by Administrator on 2017/7/5.
 */

public class HttpResult<T> {
    public static int STATUS = 200;
    private int code;
    private String msg;
    private T data;
    public boolean isSuccess(){
        return code==STATUS;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

}
