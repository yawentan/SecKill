package top.yawentan.springbootseckill.util;

public class UserThreadLocal {
    /**
     * 私有构造器保证不能倍外部创建，因为主要是工具类没必要实例化
     */
    private UserThreadLocal() {}
    /**
     * 单例模式
     */
    private static final ThreadLocal<Long> LOCAL = new ThreadLocal<>();

    public static void put(Long id){
        LOCAL.set(id);
    }

    public static Long get(){
        return LOCAL.get();
    }

    public static void remove(){
        LOCAL.remove();
    }
}
