package com.tao.filter;

import com.tao.util.JVMCache;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liutao
 * @path src/com.tao.filter
 * @date 2019/10/15  0:15
 */
@WebFilter(urlPatterns = "/*",filterName = "sSOServerFilter")
public class SSOServerFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String service = request.getParameter("service");
        String ticket = request.getParameter("ticket");
        Cookie[] cookies = request.getCookies();
        String username = "";
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if ("sso".equals(cookie.getName())) {
                    username = cookie.getValue();
                    break;
                }
            }
        }

        if (null == service && null != ticket) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (null != username && !"".equals(username)) {
            long time = System.currentTimeMillis();
            String timeString = username + time;
            JVMCache.TICKET_AND_NAME.put(timeString, username);
            StringBuilder url = new StringBuilder();
            url.append(service);
            if (0 <= service.indexOf("?")) {
                url.append("&");
            } else {
                url.append("?");
            }
            url.append("ticket=").append(timeString);
            response.sendRedirect(url.toString());
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
