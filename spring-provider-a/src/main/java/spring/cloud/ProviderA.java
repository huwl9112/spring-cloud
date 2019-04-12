package spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Date: 2019/4/8 17:08
 * @Author: huwl
 * @Description:
 */
@SpringBootApplication
@EnableEurekaClient
public class ProviderA {
    public static void main(String[] args) {
        SpringApplication.run(ProviderA.class, args);
    }
}
