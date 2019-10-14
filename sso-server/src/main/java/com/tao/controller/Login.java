package com.tao.controller;

import com.tao.util.JVMCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liutao
 * @path src/com.tao.controller
 * @date 2019/10/15  0:28
 */
@Controller
@RequestMapping(value = "/v1")
public class Login {
    @RequestMapping(value = "/login",method = {RequestMethod.POST})
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String service = request.getParameter("service");

        if ("cloud".equals(username) && "cloud".equals(password)) {
            Cookie cookie = new Cookie("sso", username);
            cookie.setPath("/");
            response.addCookie(cookie);

            long time = System.currentTimeMillis();
            String timeString = username + time;
            JVMCache.TICKET_AND_NAME.put(timeString, username);

            if (null != service) {
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
                response.sendRedirect("/sso/index.jsp");
            }
        } else {
            response.sendRedirect("/sso/index.jsp?service=" + service);
        }
    }
}
