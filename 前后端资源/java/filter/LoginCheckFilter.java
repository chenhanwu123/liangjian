package com.example.raj_liangjian.filter;

import com.alibaba.fastjson.JSON;
import com.example.raj_liangjian.common.BaseContext;
import com.example.raj_liangjian.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否登入
 */

@Slf4j
@Configuration
/*@WebFilter( urlPatterns = "/*")*/
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("进入过滤器,过滤路径为:{}",request.getRequestURI());
        //1.获取本次请求的URI
        String requestURI = request.getRequestURI();
        //2.判断本次请求是否需要处理
        String[] strings = new String[]{
                "/employee/login" ,
                "/employee/logout" ,
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",//移动端发送短信
                "/user/login"//移动端登入
        };
        //3.如果不需要处理直接放行
        boolean check = check(strings, requestURI);
        if (check) {
            filterChain.doFilter(request, response);
            return;
        }
        //4.判断登入状态如果已登入，则直接放行
        if (request.getSession().getAttribute("employee") != null){
            //将值set到线程
          Long EmpId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setThreadLocal(EmpId);

            filterChain.doFilter(request, response);
            return;
        }
        //4-1.判断登入状态如果已登入，则直接放行
        if (request.getSession().getAttribute("user") != null){
            //将值set到线程
          Long EmpId = (Long) request.getSession().getAttribute("user");
            BaseContext.setThreadLocal(EmpId);

            filterChain.doFilter(request, response);
            return;
        }

        //5.如果未登入返回未登入的结果
        response.getWriter().println(JSON.toJSONString(R.error("NOTLOGIN")));
        return;


    }

    /**
     * 路径匹配，检查是否需要放行
     *
     * @param strings
     * @param requestURI
     * @return
     */
    public boolean check(String[] strings, String requestURI) {
        for (String string : strings) {
            boolean match = PATH_MATCHER.match(string, requestURI);

            if (match) {
                return true;
            }
        }


        return false;
    }

}
