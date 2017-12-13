package cn.andy.study.customview.viewinject;

/**
 * 注解异常
 * <p>
 * Created by yangzhizhong
 */

public class ViewInjectException extends RuntimeException {
    private static final long serialVersionUID = -3957558668444762668L;

    public ViewInjectException(String string) {
        super(string);
    }
}
