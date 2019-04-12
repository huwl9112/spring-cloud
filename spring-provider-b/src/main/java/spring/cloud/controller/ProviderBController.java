package spring.cloud.controller;

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
public class ProviderBController {
    @RequestMapping("getUser")
    public Map<String,Object> getUser(){
        Map<String,Object> result=new HashMap<>();
        result.put("name","huwl");
        result.put("from","provider-b");
        return result;
    }
}
