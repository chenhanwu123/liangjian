package com.example.raj_liangjian.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.raj_liangjian.Utils.SMSUtils;
import com.example.raj_liangjian.Utils.ValidateCodeUtils;
import com.example.raj_liangjian.common.BaseContext;
import com.example.raj_liangjian.common.CustomException;
import com.example.raj_liangjian.common.R;
import com.example.raj_liangjian.entity.User;
import com.example.raj_liangjian.exception.GlobalExceptionHandler;
import com.example.raj_liangjian.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("sendMsg")
    public R<String> sendMsg(HttpSession session, @RequestBody User user) throws IOException {
        log.info("发送验证码");
        //获取手机号
        String phone = user.getPhone().trim();
        //判断手机号是否合法
        if (phone.length()!=11){
            throw new CustomException("手机号异常");
        }

        //生成验证码
        String s = ValidateCodeUtils.generateValidateCode(4).toString();
        /*给phone手机号发送验证码*/
        log.info("给手机号:{}发送验证码:{}", phone, s);
        //调用阿里云提供的短信服务api完成发送短信
        /*SMSUtils.sendMessage(“短信签名”,“模板code”,手机号,动态验证码code);*/

        //将验证码保存到session中
        session.setAttribute(phone, s);


        return R.success("手机验证码短信发送成功"+s);

    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        //获取手机号验证码
        String phone = (String) map.get("phone");
        String code = (String) map.get("code");
        log.info("给手机号:{}发送验证码:{}", phone, code);
        //从session取出手机号验证码
        Object codeInSession = session.getAttribute(phone);

        //比对
        if (codeInSession != null && codeInSession.equals(code)) {
            //如果能比对成功，说明登入成功
            //判断手机号是否为新用户。如果是就自动完成注册
            LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
            lqw.eq(User::getPhone, phone);
            User user = userService.getOne(lqw);
            if (user == null) {
                //新用户自动注册
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            return R.success(user);
        }

        return R.error("登入失败");
    }
    @PostMapping("loginout")
    public R<String> loginout(HttpSession httpSession){
        httpSession.removeAttribute("user");
        return R.success("退出成功");
    }
}
