package com.fz.server.config.security;

import com.fz.server.config.security.component.*;
import com.fz.server.pojo.Admin;
import com.fz.server.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Bo.Fan
 * @date 2021/9/13 10:39
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private IAdminService adminService;
    @Autowired
    private RestAuthorizationEntryPoint restAuthorizationEntryPoint;
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;


    // 实现相应角色的权限控制
    @Autowired
    private CustomFilter customFilter;
    @Autowired
    private CustomUrlDecisionManager customUrlDecisionManager;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 走自己实现的userDetailsServices
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    /**
     * 重写userDetailsService
     */
    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            System.out.println("JWT接收到的用户名为："+username);
            Admin admin = adminService.getAdminByUserName(username);
            if(null!=admin){
                admin.setRoles(adminService.getRoles(admin.getId()));
                System.out.println("接收到的用户名为："+username);
                System.out.println("经过处理的用户信息为："+admin.toString());
                return  admin;
            }else{
                return null;
            }
//            throw new UsernameNotFoundException("用户名或密码不正确！");
        };
    }

    @Bean
    public JwtAuthencationTokenFilter jwtAuthencationTokenFilter() {
        return new JwtAuthencationTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //使用JWT 不需要csrf
        http.csrf()
                .disable()
                //基于token不需要session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 允许 登录和退出登录不带上token访问
//                .antMatchers("/login", "/logout")
//                .permitAll()
//                //除了上面所有的请求之外都需要token认证
                .anyRequest()
                .authenticated()
                //动态权限配置
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setAccessDecisionManager(customUrlDecisionManager);
                        o.setSecurityMetadataSource(customFilter);
                        return o;
                    }
                })
                .and()
                .headers()
                //不需要使用到缓存 禁用
                .cacheControl();
        //使用JWT 拦截器
        http.addFilterBefore(jwtAuthencationTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);
        //添加自定义的未授权和未登录的结果返回
        http.exceptionHandling()
                //访问拒绝
                .accessDeniedHandler(restfulAccessDeniedHandler)
                //未授权登录
                .authenticationEntryPoint(restAuthorizationEntryPoint);
    }

    /**
     * 放行接口文档等路径 不走拦截链
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/login"
                ,"/logout"
                ,"/css/**",
                "/doc.html"
                ,"/index.html"
                ,"favicon.ico"
                ,"/js/**"
                ,"/webjars/**"
                ,"/swagger-resources/**"
                ,"/v2/api-docs/**"
                ,"/captcha"
                ,"/ws/**"
        );
    }
}
