package cn.andy.study.customview.viewinject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用方法 @OnClick({id1,id2,...}) 多个控件实现同一个点击事件
 * 使用方法@OnClick(id) 实现一个点击事件
 * <p>
 * Created by yangzhizhong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnClick {
    int[] value();
}
