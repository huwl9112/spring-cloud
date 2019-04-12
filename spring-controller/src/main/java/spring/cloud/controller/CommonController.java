package spring.cloud.controller;

import com.baomidou.mybatisplus.plugins.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.cloud.dao.CommonDao;
import spring.cloud.entity.ReturnResult;
import spring.cloud.utils.SpringContextUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Date: 2019/2/1 15:30
 * @Author: huwl
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("common/{module}")
public class CommonController {

    @RequestMapping("getDataList")
    public ReturnResult getDataList(@RequestParam Map<String, Object> params, @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize, @PathVariable String module) throws InvocationTargetException, IllegalAccessException {
        ReturnResult result = new ReturnResult();
        Page<?> page = new Page<>(pageNumber, pageSize);
        List<?> list = new LinkedList<>();
        try {
            Class<?> clazz = Class.forName("spring.cloud.iservice." + StringUtils.uppercaseFirstChar(module) + "Service");
            Class<?> daoclass = Class.forName("spring.cloud.dao." + StringUtils.uppercaseFirstChar(module) + "Dao");
            Method method = clazz.getMethod("selectByPage", Page.class, Map.class,CommonDao.class);
            Object service = SpringContextUtil.getBean(clazz);
            Object dao = SpringContextUtil.getBean(daoclass);
            list = (List<?>) method.invoke(service, page, params,dao);
        } catch (ClassNotFoundException e) {
            log.error(e + "ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        result.setCode("success");
        result.setData(list);
        result.setMsg("获取成功");
        return result;
    }
}
