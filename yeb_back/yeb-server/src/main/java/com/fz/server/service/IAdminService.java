package com.fz.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fz.server.pojo.Admin;
import com.fz.server.pojo.RespBean;
import com.fz.server.pojo.Role;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fz
 * @since 2021-09-10
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 登录之后返回token
     * @param userName
     * @param password
     * @param request
     * @return
     */
    RespBean login(String userName,String password,String code, HttpServletRequest request);

    /**
     * 更具用户名获取用户信息
     * @param userName
     * @return
     */
    Admin getAdminByUserName(String userName);

    /**
     * 根据用户id查询角色列表
     * @param adminId
     * @return
     */
    List<Role> getRoles(Integer adminId);

    /**
     * 根据关键词匹配操作员 登录用户除外
     * @param keyword
     * @return
     */
    List<Admin> getAllAdmin(String keyword);

    RespBean updateAdminRole(Integer adminId, Integer[] rids);

    RespBean updateAdminPassword(String oldPass, String pass, Integer adminId);

    /**
     * 更新用户头像
     * @param url
     * @param id
     * @param authentication
     * @return
     */
    RespBean updateAdminUserFace(String url, Integer id, Authentication authentication);
}
