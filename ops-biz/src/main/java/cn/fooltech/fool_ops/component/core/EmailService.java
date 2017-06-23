package cn.fooltech.fool_ops.component.core;

import cn.fooltech.fool_ops.utils.StringUtils;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 发送邮件服务类
 *
 * @author xjh
 */
@Service
public class EmailService {

    private static Map<String, String> hostMap = new HashMap<String, String>();

    static {
        // 126
        hostMap.put("smtp.126", "smtp.126.com");
        // qq
        hostMap.put("smtp.qq", "smtp.qq.com");

        // 163
        hostMap.put("smtp.163", "smtp.163.com");

        // sina
        hostMap.put("smtp.sina", "smtp.sina.com.cn");

        // tom
        hostMap.put("smtp.tom", "smtp.tom.com");

        // 263
        hostMap.put("smtp.263", "smtp.263.net");

        // yahoo
        hostMap.put("smtp.yahoo", "smtp.mail.yahoo.com");

        // hotmail
        hostMap.put("smtp.hotmail", "smtp.live.com");

        // gmail
        hostMap.put("smtp.gmail", "smtp.gmail.com");
        hostMap.put("smtp.port.gmail", "465");
    }

    @Value("${cn.fooltech.fool_ops.mail.from}")
    private String from;
    @Value("${cn.fooltech.fool_ops.mail.from.name}")
    private String fromName;
    @Value("${cn.fooltech.fool_ops.mail.charset}")
    private String charSet;
    @Value("${cn.fooltech.fool_ops.mail.username}")
    private String username;
    @Value("${cn.fooltech.fool_ops.mail.password}")
    private String password;

    private static String getFilePath(String path) {
        path = path.replace("\\", "/");
        return path.substring(0, path.lastIndexOf("/"));
    }

    private static String getFileName(String path) {
        path = path.replace("\\", "/");
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public String getHost(String email) throws Exception {
        Pattern pattern = Pattern.compile("\\w+@(\\w+)(\\.\\w+){1,2}");
        Matcher matcher = pattern.matcher(email);
        String key = "unSupportEmail";
        if (matcher.find()) {
            key = "smtp." + matcher.group(1);
        }

        if (hostMap.containsKey(key)) {
            return hostMap.get(key);
        } else {
            throw new Exception("unSupportEmail");
        }
    }

    public int getSmtpPort(String email) throws Exception {
        Pattern pattern = Pattern.compile("\\w+@(\\w+)(\\.\\w+){1,2}");
        Matcher matcher = pattern.matcher(email);
        String key = "unSupportEmail";
        if (matcher.find()) {
            key = "smtp.port." + matcher.group(1);
        }
        if (hostMap.containsKey(key)) {
            return Integer.parseInt(hostMap.get(key));
        } else {
            return 25;
        }
    }

    /**
     * 发送模板邮件
     *
     * @param toMailAddr  收信人地址
     * @param subject     email主题
     * @param templateStr 模板String
     * @param map         模板map
     */
    public boolean sendTemplateMail(String toMailAddr, String subject,
                                    String templateStr, Map<String, String> map) {
            // 模板内容转换为string
        String htmlText = null;
        try {
            htmlText = StringUtils.composeMessage(templateStr, map);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return sendCommonMail(toMailAddr, subject, htmlText);

    }

    /**
     * 发送普通邮件
     *
     * @param toMailAddr 收信人地址
     * @param subject    email主题
     * @param message    发送email信息
     */
    public boolean sendCommonMail(String toMailAddr, String subject,
                                  String message) {
        HtmlEmail hemail = new HtmlEmail();
        try {
            hemail.setHostName(getHost(from));
            hemail.setSmtpPort(getSmtpPort(from));
            hemail.setCharset(charSet);
            hemail.addTo(toMailAddr);
            hemail.setFrom(from, fromName);
            hemail.setAuthentication(username, password);
            hemail.setSubject(subject);
            hemail.setMsg(message);
            hemail.send();
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }

    }
}
