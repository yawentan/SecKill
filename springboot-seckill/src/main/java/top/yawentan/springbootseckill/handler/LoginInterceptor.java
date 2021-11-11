package top.yawentan.springbootseckill.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import top.yawentan.springbootseckill.service.LoginService;
import top.yawentan.springbootseckill.util.JWTUtils;
import top.yawentan.springbootseckill.util.StringUtils;
import top.yawentan.springbootseckill.util.UserThreadLocal;
import top.yawentan.springbootseckill.vo.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @description 拦截器，验证用户是否有权限登录
 * @author yawen
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginService loginService;

    /**
     * de
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        //1.检查token是否为空
        String token = request.getHeader("Token");
        if(StringUtils.isBlank(token)){
            Result result = Result.failed();
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //2.检查token是否合法
        Integer userId = JWTUtils.checkToken(token);
        if(userId==null) {
            Result result = Result.failed();
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //3.登录成功放行,并将id放入ThreadLocal
        UserThreadLocal.put(Long.valueOf(userId));
        return true;
    }
}