package cn.copoint;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class SessionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        System.out.println(request.getRequestURI());
        System.out.println(request.getContextPath());
        //登录不做拦截
        if (request.getRequestURI().equals(request.getContextPath()+"/login")) {
            return true;
        }
        //验证session是否存在
        Object obj = request.getSession().getAttribute("loginInfo");
        if (obj == null) {
            response.sendRedirect(request.getContextPath()+"/login");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

}