package com.fz.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fz.server.AdminUtils;
import com.fz.server.config.security.component.JwtTokenUtils;
import com.fz.server.mapper.AdminMapper;
import com.fz.server.mapper.AdminRoleMapper;
import com.fz.server.mapper.RoleMapper;
import com.fz.server.pojo.Admin;
import com.fz.server.pojo.AdminRole;
import com.fz.server.pojo.RespBean;
import com.fz.server.pojo.Role;
import com.fz.server.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fz
 * @since 2021-09-10
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private UserDetailsService userDetailsService;
    /**
     * idea的问题在yml 文件和启动类中都配置Mapper扫描到了
     */
   @Autowired
    private AdminMapper adminMapper;

    /**
     * 这里应该使用全局注入的对象
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Value("${jwt.tokenHead}")
    private String tokenHead;


    @Autowired
    private RoleMapper roleMapper;


    @Autowired
    private AdminRoleMapper adminRoleMapper;
    /**
     * 登录之后返回token
     * @param username
     * @param password
     * @param request
     * @return
     */
    @Override
    public RespBean login(String username, String password,String code, HttpServletRequest request) {
        String captcha = (String) request.getSession().getAttribute("captcha");
//        System.out.println("处理登录请求，存放在session中的code为："+captcha);
        if(StringUtils.isEmpty(code)||!captcha.equalsIgnoreCase(code)){
            return RespBean.error("验证码输入错误，请重新输入");
        }
        System.out.println("用户名："+username);
        System.out.println("密码："+password);
//        System.out.println("code："+code);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(userDetails==null||!passwordEncoder.matches(password,userDetails.getPassword())){
            System.out.println("用户名："+username);
            System.out.println("密码："+password);
            return RespBean.error("用户名或密码不正确");
        }
        if(!userDetails.isEnabled()){
            return RespBean.error("该账号已不能使用，请联系管理员");
        }
        // 更新security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken=
                new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        String token = jwtTokenUtils.generateToken(userDetails);
        Map<String ,String> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);
        return RespBean.success("登录成功",tokenMap);
    }

    /**
     * 根据用户名获取用户信息
     * @param userName
     * @return
     */
    @Override
    public Admin getAdminByUserName(String userName) {
        return adminMapper.selectOne(
                new QueryWrapper<Admin>().eq("username",userName)
                .eq("enabled",true)
        );
    }


    /**
     * 根据用户id查询角色列表
     * @param adminId
     * @return
     */
    @Override
    public List<Role> getRoles(Integer adminId){
        return roleMapper.getRoles(adminId);
    }

    @Override
    public List<Admin> getAllAdmin(String keyword) {
        return adminMapper.getAllAdmin(AdminUtils.getCurrent().getId(), keyword);
    }
    /**
     * 更新操作员角色
     * 先进行删除 然后在进行插入
     * @param adminId
     * @param rids
     * @return
     */
    @Override
    @Transactional
    public RespBean updateAdminRole(Integer adminId, Integer[] rids) {
        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId",adminId));
        Integer result =  adminRoleMapper.addAdminRole(adminId,rids);
        if(rids.length == result){
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }

    @Override
    public RespBean updateAdminPassword(String oldPass, String pass, Integer adminId) {
        Admin admin = adminMapper.selectById(adminId);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(encoder.matches(oldPass,admin.getPassword())){
            admin.setPassword(encoder.encode(pass));
            Integer result = adminMapper.updateById(admin);
            if(result==1){
                return RespBean.success("更新密码成功！");
            }
            return RespBean.error("更新密码失败");
        }
        return RespBean.error("原密码错误，更新密码失败！");
    }

    /**
     * 更新用户头像
     * @param url
     * @param id
     * @param authentication
     * @return
     */
    @Override
    public RespBean updateAdminUserFace(String url, Integer id, Authentication authentication) {
        Admin admin = adminMapper.selectById(id);
        admin.setUserFace(url);
        int result = adminMapper.updateById(admin);
        if(result == 1){
            Admin principal = (Admin) authentication.getPrincipal();
            principal.setUserFace(url);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin,null,authentication.getAuthorities()));
            return RespBean.success("更新用户头像成功！");
        }
        return RespBean.error("更新用户头像失败！");
    }

}
