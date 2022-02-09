package com.fz.server.config.security.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义
 * JWT 登录授权过滤器
 *
 * @author Bo.Fan
 * @date 2021/9/13 11:02
 */

public class JwtAuthencationTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String header = httpServletRequest.getHeader(tokenHeader);
        // 携带了token 且token的前缀符合要求  前缀必须为yml文件中设置的Bearer
        if (null != header && header.startsWith(tokenHead)) {
            String authToken = header.substring(tokenHead.length());
            System.out.println("-----------------------------------");
            System.out.println("获取到的token为："+authToken);
            System.out.println("请求的url为："+httpServletRequest.getRequestURL());
            System.out.println("-----------------------------------");
            String userName = null;
            if(authToken.length()!=0) {
                userName = jwtTokenUtils.getUserNameFromToken(authToken);
            }
            //该token存在且该用户并没有登录
            if (null != userName && null == SecurityContextHolder.getContext().getAuthentication()) {
                //获取到该用户名userDetails
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                // 验证该token是否有效 重新设置用户对象
                if (jwtTokenUtils.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    // 将权限的信息写进上下文对象中
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
