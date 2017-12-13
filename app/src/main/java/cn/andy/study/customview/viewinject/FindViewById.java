package cn.andy.study.customview.viewinject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用方法 @FindViewById(控件Id)
 * <p>
 * Created by yangzhizhong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface FindViewById {
    int value();
}
