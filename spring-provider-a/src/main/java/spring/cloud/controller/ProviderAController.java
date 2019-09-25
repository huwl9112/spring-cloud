package spring.cloud.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Date: 2019/4/8 17:11
 * @Author: huwl
 * @Description:
 */
@RestController
@RequestMapping("provide")
@Api(description = "服务一")
public class ProviderAController {
    @Value("${server.port}")
    private Integer port;

    @GetMapping("getUser")
    public JSONObject getUser(Integer consumerPort){
        Map<String,Object> result=new HashMap<>();
        result.put("providerPort",port);
        result.put("consumerPort",consumerPort);
        //return result;
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("data",result);
        jsonObject.put("code",1);
        return jsonObject;
    }

    @GetMapping("/test")
    public String get(){
        return "test success";
    }
}
