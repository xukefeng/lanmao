package cash.app.com.mymvp;

/**
 * @Description: 公共接口 是用于给View的接口继承的，注意，不是View本身继承。
 * 因为它定义的是接口的规范， 而接口才是定义的类的规范
 */
public interface IView {
    void onFail(String msg);
}
