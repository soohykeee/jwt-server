package com.example.jwtserver.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        /**
         * JWT TOKEN  !!!!
         */
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 토큰 : cos 이걸 만들어줘야 함.
        // id, pw가 정상적으로 들어와서 로그인이 완료되면 토큰을 만들어주고, 그걸 응답해준다.
        // 요청할떄마다 header에 Authorization에 value값으로 토큰을 가지고 온다.
        // 그때 토큰이 넘어오면 이 토큰이 내가 만든 토큰이 맞는지만 검증만 하면 된다. (RSA, HS256)
        if (req.getMethod().equals("POST")) {
            System.out.println("POST 요청됨");
            String headerAuth = req.getHeader("Authorization");
            System.out.println(headerAuth);

            // 토큰이 코스일경우만 chain을 타도록
//            if (headerAuth.equals("cos")) {
//                // 아래를 작성해주지 않으면 프로그램이 진행되지 않고 종료될 수 있음. chain에 다시 연결해줄 필요가 있음
//                chain.doFilter(req, res);
//            } else {
//                PrintWriter out = res.getWriter();
//                out.println("인증안됨");
//            }

            chain.doFilter(req, res);
        }
    }
}
