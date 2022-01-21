package com.example.jwtserver.filter;


import org.apache.catalina.filters.ExpiresFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter1 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        System.out.println("Filter1");
        // 아래를 작성해주지 않으면 프로그램이 진행되지 않고 종료될 수 있음. chain에 다시 연결해줄 필요가 있음
        chain.doFilter(request, response);


    }
}
