package spring.cloud.iservice.impl;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.cloud.dao.OperationLogDao;
import spring.cloud.entity.OperationLog;
import spring.cloud.iservice.OperationLogService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author allnas
 * @since 2019-02-27
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogDao, OperationLog> implements OperationLogService {
    @Autowired
    private OperationLogDao operationLogDao;
}
