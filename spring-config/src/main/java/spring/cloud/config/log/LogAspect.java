package spring.cloud.config.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.cloud.config.datasource.SqlContextHolder;
import spring.cloud.entity.OperationLog;
import spring.cloud.iservice.OperationLogService;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Date: 2018/7/27 14:59
 * @Author: huwl
 * @Description:
 */
@Component
@Aspect
public class LogAspect {
    Logger logger=LoggerFactory.getLogger(LogAspect.class);
    @Autowired
    private OperationLogService logService;

    @AfterReturning(returning="rvt",pointcut = "execution(* spring.cloud.controller.*.*(..)) && @annotation(logAnontation)")//注解名字取类名首字母小写则无需另外定义
    public void addLogAfter(JoinPoint jp, LogAnontation logAnontation, Object rvt) {
        logger.debug("正在执行的方法名称:" + jp.getSignature().getName());
        Object retValue = null;
        OperationLog log=new OperationLog();
        log.setTime(LocalDateTime.now());
        String className=jp.getTarget().getClass().getName();//获取类名
        String methodName=jp.getSignature().getName();//获取方法名
        Object[] args=jp.getArgs();//获取参数
        Object object=rvt;//返回值
        Class<?> clazz=object.getClass();
        log.setContent(SqlContextHolder.getSql());
        log.setOperMoudel(logAnontation.model());
        log.setTable(clazz.getSimpleName());
        log.setType(logAnontation.operType());
        //log.setOperId(ShiroUtil.getCurrentUser().getUid());
        SqlContextHolder.clearSql();//用完释放，防止内存泄漏
        logService.insert(log);
    }
}
