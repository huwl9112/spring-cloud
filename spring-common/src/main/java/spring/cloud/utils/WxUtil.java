package spring.cloud.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spring.cloud.entity.WxMessage;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;

/**
 * @Description:微信公众号工具
 * @Author: huwl
 * @Date: 2019/3/13
 */
@Component
public class WxUtil {
    @Value("${wx.config.appId}")
    private  String appId;
    @Value("${wx.config.appSecret}")
    private  String appSecret;

    @Resource(name = "shiroEhcacheManager")
    private  CacheManager ehCacheManager;

    public JSONObject getWxUser(String code) {
        System.out.println("code=" + code);
        //通过code获取openId
        JSONObject jsonDate = getOpenId(code);
        if (jsonDate.get("errcode") == null) {
            JSONObject wxUser = getWxUser(jsonDate.getString("openid"), jsonDate.getString("access_token"));
            return wxUser;
        }
        return null;
    }

    public  void moreSend() {//群发
        String token = getAccessToken();
        JSONObject param = new JSONObject();
        JSONObject filter = new JSONObject();
        filter.put("is_to_all", true);
        filter.put("tag_id", 3);
        param.put("filter", filter);
        JSONObject text = new JSONObject();
        text.put("content", "<a href='www.baidu.com'>点击跳转</a>");
        param.put("text", text);
        param.put("msgtype", "text");

        JSONObject jsonObject = HttpsUtil.httpsRequest("https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=" + token, "POST", JSONObject.toJSONString(param));
        System.out.print(jsonObject.toJSONString());

    }

    public void pSend() {//个人模板消息
        String token = getAccessToken();
        JSONObject param = new JSONObject();
        param.put("touser", "of_8cw3JDkPhOqFrDrSiHKfDqtEI");
        param.put("template_id", "t2X3ID1EAW9bZY3acprMUK_xzRRDQhXaDp5TA3DlnsY");
        param.put("url", "www.baidu.com");//点击跳转的url
        JSONObject data = new JSONObject();
        JSONObject val1 = new JSONObject();
        val1.put("value", "模板消息");
        val1.put("color", "#173177");
        JSONObject val2 = new JSONObject();
        val2.put("value", "keword1");
        val2.put("color", "#173177");
        JSONObject val3 = new JSONObject();
        val3.put("value", "keword2");
        val3.put("color", "#173177");
        JSONObject val4 = new JSONObject();
        val4.put("value", "remark");
        val4.put("color", "#173177");
        data.put("keyword1", val2);
        data.put("keyword2", val3);
        data.put("remark", val4);
        data.put("first", val1);
        param.put("data", data);
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
        JSONObject jsonObject = HttpsUtil.httpsRequest(url + token, "POST", JSONObject.toJSONString(param));
        System.out.print(jsonObject.toJSONString());

    }

    public  void customerInfo() {//客服消息
        String token = getAccessToken();
        JSONObject param = new JSONObject();
        param.put("touser", "of_8cw3JDkPhOqFrDrSiHKfDqtEI");
        param.put("msgtype", "text");
        JSONObject val1 = new JSONObject();
        val1.put("content", "你的意向单已收到<a href=''>点击查看详情</a>");

        param.put("text", val1);
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
        JSONObject jsonObject = HttpsUtil.httpsRequest(url + token, "POST", JSONObject.toJSONString(param));
        System.out.print(jsonObject.toJSONString());

    }

    /**
     * @param [str]
     * @Description:SHA、SHA1加密
     * @Return: java.lang.String
     * @Author: huwl
     * @Date: 2018/12/20
     */
    public String SHA1(String str) {
        try {
            MessageDigest digest = MessageDigest
                  .getInstance("SHA-1"); //如果是SHA加密只需要将"SHA-1"改成"SHA"即可
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexStr = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexStr.append(0);
                }
                hexStr.append(shaHex);
            }
            return hexStr.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 网页授权获取openId
     *
     * @param code
     * @return JSONObject
     * @Title: getOpenId
     * @Description: TODO
     */
    private JSONObject getOpenId(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        String requestUrl = url.replace("APPID", appId).replace("SECRET", appSecret).replace("CODE", code);

        JSONObject jsonObject = HttpsUtil.httpsRequest(requestUrl, "GET", null);
        return jsonObject;
    }

    /**
     * 获取用户微信信息
     *
     * @param openid
     * @return JSONObject
     * @Description: TODO
     */
    private JSONObject getWxUser(String openId, String accessToken) {
        System.out.println("openId=============" + openId);
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        String requestUrl = url.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);

        JSONObject jsonObject = HttpsUtil.httpsRequest(requestUrl, "GET", null);
        return jsonObject;
    }


    /**
     * @param []
     * @Description:获取基础支持accessToken
     * @Return: java.lang.String
     * @Author: huwl
     * @Date: 2018/12/20
     */
    private  String getAccessToken() {
        Cache<String, String> tokenCache = ehCacheManager
              .getCache("accessToken-cache");
        String accessToken = tokenCache.get("wxtoken");// 先从缓存里拿
        if (Objects.nonNull(accessToken)) {
            return accessToken;
        }
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
        url = url.replace("APPID", appId).replace("SECRET",
              appSecret);
        JSONObject jsonObject = HttpsUtil.httpsRequest(url, "GET", null);
        accessToken = jsonObject.getString("access_token");
        tokenCache.put("wxtoken", accessToken);// 重新生成放入缓存，待下次使用，有效期接近2小时
        return accessToken;
    }

    /**
     * 模板消息推送
     *
     * @return JSONObject
     * @Description: TODO
     */
    public void pushMessage(WxMessage infoVo) {
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="
              + getAccessToken();
        JSONObject jsonObject = HttpsUtil.httpsRequest(url, "POST",
              JSONObject.toJSONString(infoVo));
    }

    public JSONObject getSignature(String visiteUrl) {
        JSONObject result = new JSONObject();
        //Cache<String, String> tokenCache = ehCacheManager.getCache("accessToken-cache");
        //String ticket = tokenCache.get("apiticket");// 先从缓存里拿
        String ticket = "123456";
        if (Objects.isNull(ticket)) {
            //获取ticket
            String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
            url = url.replace("ACCESS_TOKEN", getAccessToken());
            JSONObject jsonObject = HttpsUtil.httpsRequest(url, "GET", null);
            ticket = jsonObject.getString("ticket");
        }

        System.out.print(ticket);
        //获取签名signature
        String noncestr = UUID.randomUUID().toString();
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        String str = "jsapi_ticket=" + ticket +
              "&noncestr=" + noncestr +
              "&timestamp=" + timestamp +
              "&url=" + visiteUrl;
        //sha1加密
        String signature = SHA1(str);
        result.put("noncestr", noncestr);
        result.put("timestamp", timestamp);
        result.put("signature", signature);
        result.put("appId", appId);
        return result;
    }

}
