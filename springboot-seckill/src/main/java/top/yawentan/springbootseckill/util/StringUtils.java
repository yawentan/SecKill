package top.yawentan.springbootseckill.util;

/**
 * 定义了一些常用的字符串功能函数
 * @author yawen
 */
public class StringUtils {
    /**
     * 私有构造器保证不能倍外部创建，因为主要是工具类没必要实例化
     */
    private StringUtils(){}

    /**
     * 判断字符串是否为空
     * @param s
     * @return
     */
    public static boolean isBlank(String s){
        if(s==null||"".equals(s)||s.length()==0){
            return true;
        }
        return false;
    }
}
