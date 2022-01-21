package com.example.jwtserver.config.jwt;

import com.example.jwtserver.config.auth.PrincipalDetails;
import com.example.jwtserver.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있음.
// /login 요처애서 username, password 전송하면 (post)
// UsernamePasswordAuthenticationFilter가 동작을 함.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 토큰 암호화키


    // /login 요청을하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        System.out.println("JwtAUthentication : 로그인 시도중");

        // 1.username, password 받아서
        // request에 담겨져있음
        try {
//         form-url 방식일때
//            BufferedReader br = request.getReader();
//            String input = null;
//            while ((input = br.readLine()) != null) {
//                System.out.println(input);
//            }
//         JSON일때 처리하는 방법
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println(user);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

//            PrincipalDetailsService 의 loadUserByUsername() 함수가 실행됨
//            정상이면 authentication이 리턴됨
//            DB에 있는 username과 password가 일치한다.
//            인증을 위해 사용, authentication 에 로그인한 정보가 쌓임
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("로그인 완료됨 :"+principalDetails.getUser().getUsername());
//            authentication 객체가 session 영역에 저장됨
            return authentication;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // attemptAuthentication 실행 후 인증이 정상적으로 되었으면, 아래에 있는 successfulAuthentication 함수가 실행된다.
    // JWT TOKEN 을 만들어서 request 요청한 사용자에게 JWT TOKEN 을 response 해주면 됨.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication() 실행됨 : 인증이 완료되었다는 뜻");


        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        // Hash 암호방식
        String jwtToken = Jwts.builder()
                .setSubject(principalDetails.getUsername())                         // token 이름
                .setExpiration(new Date(System.currentTimeMillis() + (60000*10)))   // token 만료시간
                .claim("id", principalDetails.getUser().getId())                // 넣고싶은 key value 값
                .claim("username",principalDetails.getUser().getUsername())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        System.out.println("::::::" + jwtToken);
        response.addHeader("Authorization", "Bearer " + jwtToken);
        System.out.println("Authorization Header : "+response.getHeader("Authorization"));
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
