package com.example.jwtserver.filter;


import javax.servlet.*;
import java.io.IOException;

public class MyFilter2 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        System.out.println("Filter2");
        // 아래를 작성해주지 않으면 프로그램이 진행되지 않고 종료될 수 있음. chain에 다시 연결해줄 필요가 있음
        chain.doFilter(request, response);

    }

}
