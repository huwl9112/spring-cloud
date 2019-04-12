package spring.cloud.iservice.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import spring.cloud.dao.UserDao;
import spring.cloud.entity.User;
import spring.cloud.iservice.UserService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author allnas
 * @since 2018-07-23
 */
@Service(value = "userService")
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
}
