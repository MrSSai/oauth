package com.spring.wx.oauth.conntroller;

import com.alibaba.fastjson.JSONObject;
import com.spring.wx.oauth.entity.UserEntity;
import com.spring.wx.oauth.service.UserService;
import com.spring.wx.oauth.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class WXConntroller {

    @Autowired
    private UserService userService;

    @Value("${oauth.wx.appid}")
    private String appid;

    @Value("${oauth.wx.appsecret}")
    private String appsecret;

    @Value("${oauth.callback.http}")
    private String http;

    @GetMapping("/index")
    public String index() {
        return "/index";
    }

    @GetMapping("/wxlogin")
    public String wxlogin() {
        // 第一步：用户同意授权，获取code
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid +
                "&redirect_uri=" + http +
                "&response_type=code" +
                "&scope=snsapi_userinfo" +
                "&state=STATE#wechat_redirect";
        return "redirect:"  + url;
    }

    @GetMapping("/wxcallback")
    public String wxcallback(String code, ModelMap map) throws IOException {
        // 第二步：通过code换取网页授权access_token
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid +
                "&secret=" + appsecret +
                "&code=" + code +
                "&grant_type=authorization_code";
        JSONObject jsonObject = HttpClientUtils.doGet(url);

        String openid = jsonObject.getString("openid");
        String access_Token = jsonObject.getString("access_token");

        System.out.println(jsonObject);

        // 第四步：拉取用户信息(需scope为 snsapi_userinfo)
        url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_Token +
                "&openid=" + openid +
                "&lang=zh_CN";
        JSONObject userInfoJson = HttpClientUtils.doGet(url);
        System.out.println("UserInfo:" + userInfoJson);

        // 1种情况, 是基于微信授权的账号做为我们本系统的账号体系来使用
        // map.put("userinfo", userInfoJson);
        // return "/home";


        // 2种情况: 我们是有账号体系的，微信帐号做来一个关联，来关联我们的账号体系
        UserEntity userEntity = userService.getOpenid(openid);
        if (userEntity == null) {
            userEntity = new UserEntity();
            userEntity.setType(0);
            userEntity.setOpenid(openid);
            userEntity.setNickname((String)userInfoJson.get("nickname"));
            userEntity.setImage((String)userInfoJson.get("headimgurl"));
            userService.insert(userEntity);
        } else {
            userEntity.setNickname((String)userInfoJson.get("nickname"));
            userEntity.setImage((String)userInfoJson.get("headimgurl"));
            userService.update(userEntity);
        }

        return "redirect:/gohome?openid=" +openid;
    }

    @GetMapping("/gohome")
    public String gohome(String openid, ModelMap map) {
        UserEntity userEntity = userService.getOpenid(openid);
        if (StringUtils.isEmpty(userEntity.getPhone())) {
            return "redirect:/register?openid=" +openid;
        } else {
            map.put("userEntity", userEntity);
            return "/home";
        }
    }

    @GetMapping("/register")
    public String register(String openid, ModelMap map) {
        UserEntity userEntity = userService.getOpenid(openid);
        map.put("userEntity", userEntity);
        return "register";
    }

    @ResponseBody
    @GetMapping("/regsave")
    public String regsave(String openid, String phone, String email) {
        this.userService.register(openid, phone, email);
        return "SUCCESS";
    }

}
