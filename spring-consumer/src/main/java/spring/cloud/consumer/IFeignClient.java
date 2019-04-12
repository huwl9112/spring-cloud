package spring.cloud.consumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @Date: 2018/8/7 14:31
 * @Author: huwl
 * @Description:
 */
@FeignClient(name = "provider",fallback = IFeignClientHystrix.class)
public interface IFeignClient {
    @RequestMapping(value="provide/getUser",method = RequestMethod.GET)
    Map<String,Object> getUser();
}
