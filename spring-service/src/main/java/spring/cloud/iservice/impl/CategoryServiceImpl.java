package spring.cloud.iservice.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import spring.cloud.dao.CategoryDao;
import spring.cloud.entity.Category;
import spring.cloud.iservice.CategoryService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author allnas
 * @since 2019-02-27
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {
}
