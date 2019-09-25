package spring.cloud.consumer;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Date: 2018/8/7 14:31
 * @Author: huwl
 * @Description:
 */
@FeignClient(name = "provider",fallback = IFeignClientHystrix.class)
public interface IFeignClient {
    @GetMapping(value="provide/getUser")
    JSONObject getUser(@RequestParam("consumerPort") Integer port);
}
