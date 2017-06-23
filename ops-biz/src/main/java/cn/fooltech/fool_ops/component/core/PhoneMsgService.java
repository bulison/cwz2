package cn.fooltech.fool_ops.component.core;

import cn.fooltech.fool_ops.utils.HttpUtil;
import cn.fooltech.fool_ops.utils.StrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 手机短信发送服务类
 *
 * @author xjh
 */
@Service
public class PhoneMsgService {

    public static final String SEND_SUCCESS = "0";
    public static final String SEND_FAIL = "1";
    @Value("${cn.fooltech.fool_ops.phone.url}")
    public String request_url;
    @Value("${cn.fooltech.fool_ops.phone.name}")
    public String request_name;
    @Value("${cn.fooltech.fool_ops.phone.pwd}")
    public String request_pwd;
    @Value("${cn.fooltech.fool_ops.phone.type}")
    public String request_type;
    @Value("${cn.fooltech.fool_ops.phone.sign}")
    public String request_sign;

    /**
     * 发送手机信息
     *
     * @param receiver:手机号码
     * @param content:信息的内容
     * @return SendResponse.responseCode=0表示成功
     * SendResponse.responseCode=1表示失败
     */
    public SendResponse send(String receiver, String content) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("name", request_name);
        paramMap.put("pwd", request_pwd);
        paramMap.put("type", request_type);
        paramMap.put("mobile", receiver);

        try {
            paramMap.put("sign", URLEncoder.encode(request_sign, "UTF-8"));
            paramMap.put("content", URLEncoder.encode(content, "UTF-8"));

            String resoponse = HttpUtil.doGet(request_url, paramMap);

            String results[] = StrUtil.split(resoponse, ",");
            if (results.length < 2) throw new RuntimeException("短信接口返回参数不能少于2个");
            if (results.length > 2) {
                return new SendResponse(results[0], results[5], results[1]);
            } else {
                return new SendResponse(results[0], results[1], "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new SendResponse(SEND_FAIL, "网络或接口异常", "");
        }
    }

    public static class SendResponse {

        private String responseCode;
        private String responseDesc;
        private String tradeNo;

        public SendResponse(String responseCode, String responseDesc,
                            String tradeNo) {
            super();
            this.responseCode = responseCode;
            this.responseDesc = responseDesc;
            this.tradeNo = tradeNo;
        }

        public String getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(String responseCode) {
            this.responseCode = responseCode;
        }

        public String getResponseDesc() {
            return responseDesc;
        }

        public void setResponseDesc(String responseDesc) {
            this.responseDesc = responseDesc;
        }

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }
    }

}
