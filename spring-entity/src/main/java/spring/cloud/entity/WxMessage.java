package spring.cloud.entity;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * @Date: 2018/12/7 15:03
 * @Author: huwl
 * @Description:微信个人消息推送S实体
 */
public class WxMessage implements Serializable {
    private String touser;
    private String template_id;
    private String url;
    private JSONObject data;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
