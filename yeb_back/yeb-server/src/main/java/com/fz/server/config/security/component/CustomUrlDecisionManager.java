package com.fz.server.config.security.component;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 权限控制
 * 判断用户角色
 * @author Bo.Fan
 * @date 2021/9/14 20:30
 */
@Component
public class CustomUrlDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        for (ConfigAttribute configAttribute :collection){
            //当前url所需角色
            String needRole = configAttribute.getAttribute();
            //判断角色是否登录即可访问的角色，此角色在CustomerFilter中设置
            System.out.println("======================================");
            System.out.println("当前url所需要的角色为："+ needRole);
            System.out.println("======================================");
            if("ROLE_LOGIN".equals(needRole)){
                //判断是否登录
                if(authentication instanceof AnonymousAuthenticationToken){
                    throw new AccessDeniedException("尚未登录，请登录");
                }else {
                    return;
                }
            }
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            //判断用户角色是否为url所需角色
            for (GrantedAuthority authority:authorities){
                if(authority.getAuthority().equals(needRole)){
                    return;
                }
            }
        }
       throw new AccessDeniedException("权限不足，请联系管理员");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
