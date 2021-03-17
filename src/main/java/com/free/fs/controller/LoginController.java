package com.free.fs.controller;

import com.free.fs.common.utils.R;
import com.free.fs.common.utils.StringUtil;
import com.free.fs.model.User;
import com.free.fs.service.UserService;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * LoginController
 */
@Controller
@RequiredArgsConstructor
public class LoginController extends BaseController {

    private final UserService userService;

    /**
     * 登录页
     */
    @GetMapping("/login")
    public String login() {
        if (getLoginUser() != null) {
            return "redirect:index";
        }
        return "login";
    }

    /**
     * 注册页
     */
    @GetMapping("/reg")
    public String reg() {

        return "reg";
    }

    /**
     * 生成验证码 算术类型
     */
    @RequestMapping("/assets/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        captcha.setLen(2);
        captcha.getArithmeticString();
        captcha.text();
        request.getSession().setAttribute("captcha", captcha.text());
        captcha.out(response.getOutputStream());
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    @ResponseBody
    public R login(HttpServletRequest request, String username, String password, String code, boolean rememberMe) {
        if (StringUtil.isBlank(username, password)) {
            return R.failed("账号或密码不能为空");
        }
        String sessionCode = (String) request.getSession().getAttribute("captcha");
        if (code == null || !sessionCode.equals(code.trim().toLowerCase())) {
            return R.failed("验证码不正确");
        }
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
            SecurityUtils.getSubject().login(token);
            return R.succeed("登录成功");
        } catch (UnknownAccountException e) {
            return R.failed("用户不存在");
        } catch (IncorrectCredentialsException e) {
            return R.failed("密码错误");
        } catch (ExcessiveAttemptsException eae) {
            return R.failed("操作频繁，请稍后再试");
        }
    }

    /**
     * 注册
     */
    @PostMapping("/reg")
    @ResponseBody
    public R register(User user, HttpServletRequest request, String code) {
        String sessionCode = (String) request.getSession().getAttribute("captcha");
        if (code == null || !sessionCode.equals(code.trim().toLowerCase())) {
            return R.failed("验证码不正确");
        }
        if (userService.addUser(user)) {
            return R.succeed("注册成功");
        }
        return R.failed("注册失败");
    }
}
