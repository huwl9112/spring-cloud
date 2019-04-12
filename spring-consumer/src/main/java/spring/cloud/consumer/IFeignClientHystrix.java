package spring.cloud.consumer;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Date: 2019/4/10 14:45
 * @Author: huwl
 * @Description:
 */
@Component
public class IFeignClientHystrix implements IFeignClient{
    @Override
    public Map<String, Object> getUser() {
        Map<String,Object> result=new HashMap<>();
        result.put("name","default");
        result.put("from","hystrix");
        return result;
    }
}
