package spring.cloud.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * @Date: 2019/5/23 16:49
 * @Author: huwl
 * @Description:捕捉控制层异常并统一返回
 */
@Component
@Aspect
@Slf4j
public class CatchControllerException {

    //定义切入点范围
    @Pointcut("execution(* com.abcft.fund..*.*(..))")
    public void pointcut() {
    }

    @AfterThrowing(value = "pointcut()", throwing = "e")
    public void handleThrowing(JoinPoint jp, Exception e) {
        StackTraceElement[] traceElements = e.getStackTrace();
        StackTraceElement root = traceElements[0];
        if (!root.getClassName().contains("com.abcft.fund")) {
            for (StackTraceElement element : traceElements) {
                if (element.getClassName().equals("com.abcft.fund")) {
                    root = element;
                    break;
                }
            }
        }
        //获取类名
        String className = root.getClassName();
        //获取方法名
        String methodName = root.getMethodName();
        //获取异常所在行
        int lineNumber = root.getLineNumber();
        String errorMsg = String.format("==ERROR HAPPEND IN CLASS==>%s,METHOD==>%s,MESSAGE==>%s,LINENUMBER==>%d,TIME==>%s", className, methodName, e, lineNumber, DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        log.error(errorMsg);
        responseClient(errorMsg);
    }

    private void responseClient(String errorMsg) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.setHeader("icop-content-type", "exception");
        JSONObject result = new JSONObject();
        try (OutputStream writer = response.getOutputStream()) {
            writer.write(JSON.toJSONString(result).getBytes("utf-8"));
            writer.flush();
        } catch (IOException e) {
            log.error("CatchControllerException==>responseClient{}", e);
        }

    }
}
