package com.free.fs.controller;

import cn.hutool.core.util.StrUtil;
import com.free.fs.common.model.R;
import com.free.fs.system.dto.InsertUserDto;
import com.free.fs.system.dto.LoginDto;
import com.free.fs.system.service.UserService;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * LoginController
 */
@RestController
@RequiredArgsConstructor
public class LoginController extends BaseController {

    private final UserService userService;

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
    public R login(@Validated @RequestBody LoginDto dto, HttpServletRequest request) {
        String sessionCode = (String) request.getSession().getAttribute("captcha");
        if (!sessionCode.equals(dto.getCode().trim().toLowerCase())) {
            return R.failed("验证码不正确");
        }
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(dto.getUsername(), dto.getPassword(), dto.getRememberMe());
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
    public R register(@Validated @RequestBody InsertUserDto userDto, HttpServletRequest request) {
        String sessionCode = (String) request.getSession().getAttribute("captcha");
        if (!sessionCode.equals(userDto.getCode().trim().toLowerCase())) {
            return R.failed("验证码不正确");
        }
        if (userService.addUser(userDto)) {
            return R.succeed("注册成功");
        }
        return R.failed("注册失败");
    }
}
