package com.example.raj_liangjian.common;

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();



    /**
     * 获取
     * @return threadLocal
     */
    public static Long getThreadLocal() {
        return threadLocal.get();
    }

    /**
     * 设置
     * @param
     */
    public static void setThreadLocal(Long id) {
        threadLocal.set(id);
    }
}
