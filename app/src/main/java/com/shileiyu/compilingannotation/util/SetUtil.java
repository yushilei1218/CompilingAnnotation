package com.shileiyu.compilingannotation.util;

import java.util.Collection;

/**
 * @author shilei.yu
 * @since on 2017/7/10.
 */

public class SetUtil {
    public static <T extends Collection> boolean isEmpty(T set) {
        return set == null || set.isEmpty();
    }
}
