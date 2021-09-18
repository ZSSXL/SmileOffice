package com.zss.smile.controller;

import com.zss.smile.common.SmileConstant;
import com.zss.smile.common.anno.RequiredPermission;
import com.zss.smile.common.response.ResponseCode;
import com.zss.smile.common.response.ServerResponse;
import com.zss.smile.models.entities.User;
import com.zss.smile.models.vo.LoginVo;
import com.zss.smile.models.vo.ModifyVo;
import com.zss.smile.models.vo.UserVo;
import com.zss.smile.service.UserService;
import com.zss.smile.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZSS
 * @date 2021/9/6 13:37
 * @desc
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ServerResponse<String> login(@RequestBody @Valid LoginVo loginVo, BindingResult result) {
        if (result.hasErrors()) {
            return ServerResponse.error(ResponseCode.PARAMETER_ERROR.getCode(),
                    ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            User user = userService.login(loginVo.getLoginName());
            if (user == null) {
                return ServerResponse.error("登录失败, 登录名错误");
            } else {
                if (StringUtils.equals(user.getPassword(), EncryptionUtil.encrypt(loginVo.getPassword()))) {
                    String token = TokenUtil.createToken(MapUtil.create(
                            "userId", user.getUserId(),
                            "username", user.getUsername(),
                            "loginName", user.getLoginName()));
                    return ServerResponse.success("登录成功", token);
                } else {
                    return ServerResponse.error("登录失败, 密码错误");
                }
            }
        }
    }

    @GetMapping("/info")
    @RequiredPermission
    public ServerResponse<Map<String, String>> getUserInfo(
            @RequestHeader(SmileConstant.SMILE_TOKEN) String token) {
        String userId = TokenUtil.getClaim(token, "userId").asString();
        User userInfo = userService.getUserById(userId);
        if (userInfo == null) {
            return ServerResponse.error("获取个人信息失败");
        } else {
            Map<String, String> map = new HashMap<>(2);
            map.put("userId", userInfo.getUserId());
            map.put("username", userInfo.getUsername());
            return ServerResponse.success(map);
        }
    }

    /**
     * 普通用户注册
     *
     * @return String
     */
    @PostMapping("/signup")
    public ServerResponse<String> signUp(@RequestBody @Valid UserVo userVo, BindingResult result) {
        if (result.hasErrors()) {
            return ServerResponse.error(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            Boolean exist = userService.isExistInDb(userVo.getLoginName());
            if (exist) {
                return ServerResponse.error("登录名[" + userVo.getLoginName() + "]已存在");
            } else {
                User newUser = User.builder()
                        .userId(IdUtil.getId())
                        .username(userVo.getUsername())
                        .loginName(userVo.getLoginName())
                        .password(EncryptionUtil.encrypt(userVo.getPassword()))
                        .createTime(DateUtil.currentTimestamp()).build();
                userService.createUser(newUser);
                return ServerResponse.success("[" + userVo.getLoginName() + "]注册成功");
            }
        }
    }

    /**
     * 修改密码
     *
     * @param modifyVo 修改密码实体
     * @param token    用户token
     * @return result
     */
    @PostMapping("/modify")
    @RequiredPermission
    public ServerResponse<String> modify(@RequestBody @Valid ModifyVo modifyVo,
                                         @RequestHeader(SmileConstant.SMILE_TOKEN) String token,
                                         BindingResult result) {
        if (result.hasErrors()) {
            return ServerResponse.error(ResponseCode.PARAMETER_ERROR.getCode(), ResponseCode.PARAMETER_ERROR.getDesc());
        } else {
            String userId = TokenUtil.getClaim(token, "userId").asString();
            User userInfo = userService.getUserById(userId);
            if (!StringUtils.equals(userInfo.getPassword(), EncryptionUtil.encrypt(modifyVo.getOldPassword()))) {
                return ServerResponse.error("旧密码不匹配");
            } else if (StringUtils.equals(userInfo.getPassword(), EncryptionUtil.encrypt(modifyVo.getNewPassword()))) {
                return ServerResponse.error("新密码与当前密码相同");
            }
            userInfo.setPassword(EncryptionUtil.encrypt(modifyVo.getNewPassword()));
            userService.createUser(userInfo);
            return ServerResponse.success("密码修改成功");
        }
    }
}
