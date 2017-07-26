package com.shileiyu;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @auther by yushilei.
 * @time 2017/7/26-21:40
 * @desc
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface RetrofitTarget {
}
