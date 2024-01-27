package com.ayi.ayiojbackendcommom.common;

/**
 * className:CommomUtils
 * Package:com.ayi.ayioj.common
 * Description: TODO
 *
 * @Date: 2023/10/25 10:59
 * @Author:hjy
 */
public class CommomUtils {

    /**
     * 检验分页参数
     * @param t
     * @param <T>
     */
    public static <T> void chenkPage(T t){
        if(t == null){
            return;
        }
        if (t instanceof PageRequest){
            if (((PageRequest) t).getCurrent() < 0){
                ((PageRequest) t).setCurrent(1);
            }
            if (((PageRequest) t).getPageSize() < 0){
                ((PageRequest) t).setPageSize(10);
            }
        }
    }
}
