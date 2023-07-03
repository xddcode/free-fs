package com.free.fs.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.free.fs.model.User;
import com.free.fs.service.UserService;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OauthController {

    private final UserService userService;
    private final AuthRequestFactory factory;

    @RequestMapping("/render/{source}")
    public void renderAuth(@PathVariable("source") String source, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = factory.get(source);
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        response.sendRedirect(authorizeUrl);
    }

    @RequestMapping("/callback/{source}")
    public String callback(@PathVariable("source") String source, AuthCallback callback, Model model) {
        AuthRequest authRequest = factory.get(source);
        AuthResponse<AuthUser> response = authRequest.login(callback);
        AuthUser authUser = response.getData();
        if (response.ok()) {
            //判断用户是否已经存在
            String uuid = authUser.getUuid();
            User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUuid, uuid));
            if (user == null) {
                //首次登录需完善密码
                model.addAttribute("authUser", authUser);
                return "informationPass";
            }
            StpUtil.login(user.getId(), true);
            //登录成功
            return "redirect:/index";
        }
        return "redirect:/login";
    }
}
