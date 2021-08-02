package com.eth.filecoin.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.eth.filecoin.admin.AuthenContext;
import com.eth.filecoin.admin.AuthenRequest;
import com.eth.filecoin.annotation.AuthToken;
import com.eth.filecoin.common.CodeConstants;
import com.eth.filecoin.utils.CacheRedis;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Autowired
    private CacheRedis redisService;
    /**
     * 存放鉴权信息的Header名称，默认是AuthorToken
     */
    private String httpHeaderName = "AuthorToken";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 如果打上了AuthToken注解则不需要验证token
        if (method.getAnnotation(AuthToken.class) != null || handlerMethod.getBeanType().getAnnotation(AuthToken.class) != null) {
            return true;
        } else {
            String token = request.getHeader(httpHeaderName);
            log.info("token is {}", token);

            if (null != token && redisService.exists(token)) {
                //登录数据初始化
                AuthenRequest sysAdmin = redisService.getCacheObject("filKey");
                AuthenContext.set(sysAdmin);
                return true;
            } else {
                JSONObject jsonObject = new JSONObject();
                PrintWriter out = null;
                try {
                    response.setContentType("application/json;charset=UTF-8");
                    jsonObject.put("code", CodeConstants.FAIL);
                    jsonObject.put("data", "");
                    jsonObject.put("msg", "token verification fail");
                    out = response.getWriter();
                    out.println(jsonObject);
                    return false;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (null != out) {
                        out.flush();
                        out.close();
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //执行完成清理当前线程
        AuthenContext.clean();
    }

}
