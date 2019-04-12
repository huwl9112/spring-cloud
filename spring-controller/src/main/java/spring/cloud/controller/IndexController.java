package spring.cloud.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Date: 2018/9/4 15:44
 * @Author: huwl
 * @Description:# spring.mvc.static-path-pattern=默认值为 /**
 * spring.resources.static-locations=这里设置要指向的路径，多个使用英文逗号隔开,默认值为 classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/,    classpath:/public/
 * 比如我把spring.mvc.static-path-pattern=/demo/**，那么上面我们的那个图片就需要通过http://localhost:8080/demo/image/timg.jpg才能访问的到，如果我改了下面的locations，那么我图片依然在static就不能访问的到了。
 */
@Controller
public class IndexController {

    /**
     * 微信回调并获取微信openId
     * @param code
     * @return
     */
    @GetMapping("/index")
    public String index(@RequestParam("code") String code) {
        //通过code获取openId
        return "home"; //当浏览器输入/index时，会返回 /templates/home.html页面
    }

    @GetMapping("/info")
    public String info(ModelMap map) {
        map.addAttribute("name", "huwl");
        return "home";
    }

    /**
     * @Description:前端调用微信的js-sdk
     * @param [visiteUrl]
     * @Return: com.alibaba.fastjson.JSONObject
     * @Author: huwl 
     * @Date: 2019/3/13 
     */
    @RequestMapping("getSignature")
    @ResponseBody
    public JSONObject getSignature(String visiteUrl){
        JSONObject result=new JSONObject();
        //Cache<String, String> tokenCache = ehCacheManager.getCache("accessToken-cache");
        //String ticket = tokenCache.get("apiticket");// 先从缓存里拿
        return result;
    }
}
