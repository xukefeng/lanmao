package cash.app.com.mymvp;

import java.io.IOException;

/**
 * Created by Administrator on 2017/7/21.
 */

public interface DataSerializeInterface<T> {
    /**
     * 序列化对象
     *
     * @param object
     * @return
     */
    String serialize(T object) throws IOException;

    /**
     * 反序列化
     *
     * @param str
     * @return
     */
    T deSerialization(String str) throws IOException, ClassNotFoundException;
}
