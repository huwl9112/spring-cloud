package spring.cloud.iservice;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import spring.cloud.dao.CommonDao;

import java.util.List;
import java.util.Map;

/**
 * @Date: 2019/2/27 11:18
 * @Author: huwl
 * @Description:
 */
public interface CommonService<T> extends IService<T> {
    default List<T> selectByPage(Page<T> page, Map<String, Object> params, CommonDao<T> dao){
        return dao.selectByPage(page,params);
    }
}
