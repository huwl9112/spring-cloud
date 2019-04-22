package spring.cloud.controller;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.cloud.iservice.UserService;

import javax.annotation.PostConstruct;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author huwl
 * @since 2019-03-28
 */
@Controller
@RequestMapping("/adminuser")
public class AdminuserController implements InitializingBean {
    @Autowired
    private UserService userService;
    private String name="huel";


    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("=========2");
        this.name="huwl";

    }

    @PostConstruct
    public void setName(){
        System.out.println("=========1");
        this.name="wdm";
    }

}

