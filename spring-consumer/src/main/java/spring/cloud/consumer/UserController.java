package spring.cloud.consumer;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Date: 2018/8/7 14:42
 * @Author: huwl
 * @Description:
 */
@RestController
@RequestMapping("user")
@Api(description = "用户服务")
public class UserController {
    @Value("${server.port}")
    private Integer port;

    @Autowired
    private IFeignClient iFeignClient;
    @RequestMapping("/getUser")
    @ApiOperation("获取用户")
    public JSONObject getUser() {

        //调用远程服务
//		ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://sbc-order/getOrder", String.class);
//        logger.info("res="+JSON.toJSONString(responseEntity));
        return iFeignClient.getUser(port);
    }
}
