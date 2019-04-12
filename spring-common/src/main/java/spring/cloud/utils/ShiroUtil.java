package spring.cloud.utils;

import org.apache.shiro.SecurityUtils;
import spring.cloud.entity.User;

/**
 * @Date: 2018/7/31 18:55
 * @Author: huwl
 * @Description:
 */
public class ShiroUtil {

    private static final String CURRENTUSER="currentUser";

    public static User getCurrentUser(){
        return (User) SecurityUtils.getSubject().getSession().getAttribute(CURRENTUSER);
    }

    public static void setCurrentUser(User user){
        SecurityUtils.getSubject().getSession().setAttribute(CURRENTUSER,user);
    }
}
