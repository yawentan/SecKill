package top.yawentan.springbootseckill.util;

/**
 * 定义了一些常用的字符串功能函数
 */
public class StringUtil {
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
