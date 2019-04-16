package spring.cloud.config;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * @Date: 2019/4/15 15:00
 * @Author: huwl
 * @Description:接口全局异常处理
 */
@Aspect
@Component
@Slf4j
public class WebExceptionAspect {
    @Pointcut(value = "execution(* spring.cloud..*.*(..))&&(@annotation(org.springframework.web.bind.annotation.RequestMapping)||@annotation(org.springframework.web.bind.annotation.PostMapping)||@annotation(org.springframework.web.bind.annotation.GetMapping))")
    private void webPointcut() {
    }

    @AfterThrowing(pointcut = "webPointcut()", throwing = "e")
    public void handlerWebException(JoinPoint jp, Exception e) {
        String methodNmae = jp.getSignature().getName();
        log.error("方法" + methodNmae + "出现异常：{}", e.getMessage());
        responseClientMsg("服务器端异常");
    }

    private void responseClientMsg(String msg) {
        HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.setHeader("icop-content-type", "exception");
        PrintWriter writer = null;
        try {
            JSONObject result=new JSONObject();
            result.put("isSuccess",false);
            result.put("msg",msg);
            writer = response.getWriter();
            writer.print(result);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
