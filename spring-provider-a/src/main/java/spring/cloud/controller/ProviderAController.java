package spring.cloud.controller;

import io.swagger.annotations.Api;
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
    @RequestMapping("getUser")
    public Map<String,Object> getUser(){
        Map<String,Object> result=new HashMap<>();
        result.put("name","huwl");
        result.put("from","provider-a");
        return result;
    }
}
