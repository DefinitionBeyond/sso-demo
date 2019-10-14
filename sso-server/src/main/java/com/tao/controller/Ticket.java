package com.tao.controller;

import com.tao.util.JVMCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author liutao
 * @path src/com.tao.controller
 * @date 2019/10/15  0:32
 */
@Controller
@RequestMapping(value = "/v1")
public class Ticket {
    @RequestMapping(value = "/ticket")
    public void ticket(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ticket = request.getParameter("ticket");
        String username = JVMCache.TICKET_AND_NAME.get(ticket);
        JVMCache.TICKET_AND_NAME.remove(ticket);
        PrintWriter writer = response.getWriter();
        writer.write(username);
    }
}
