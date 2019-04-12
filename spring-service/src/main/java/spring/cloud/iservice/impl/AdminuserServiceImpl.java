package spring.cloud.iservice.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import spring.cloud.dao.AdminuserDao;
import spring.cloud.entity.Adminuser;
import spring.cloud.iservice.IAdminuserService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author huwl
 * @since 2019-03-28
 */
@Service
public class AdminuserServiceImpl extends ServiceImpl<AdminuserDao, Adminuser> implements IAdminuserService {

}
