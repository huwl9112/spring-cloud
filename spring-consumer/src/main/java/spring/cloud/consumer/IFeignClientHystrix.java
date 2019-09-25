package spring.cloud.consumer;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

/**
 * @Date: 2019/4/10 14:45
 * @Author: huwl
 * @Description:
 */
@Component
public class IFeignClientHystrix implements IFeignClient{
    @Override
    public JSONObject getUser(Integer port) {
        JSONObject result=new JSONObject();
        result.put("name","default");
        result.put("from","hystrix");
        return result;
    }
}
