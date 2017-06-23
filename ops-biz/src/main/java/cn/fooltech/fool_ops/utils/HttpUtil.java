package cn.fooltech.fool_ops.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    /**
     * 默认编码
     */
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final int MAX_CONNECTTION = 10;
    /**
     * 连接超时时间
     */
    private static final int DEFAULT_CONNECTION_TIMEOUT_MS = 5 * 1000;
    /**
     * 读超时时间
     */
    private static final int DEFAULT_READ_TIMEOUT_MS = 10 * 1000;
    private static final HttpClient HTTP_CLIENT;
    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    static {
        final MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        HTTP_CLIENT = new HttpClient();

        connectionManager.getParams().setMaxTotalConnections(MAX_CONNECTTION);
        connectionManager.getParams().setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT_MS);
        connectionManager.getParams().setSoTimeout(DEFAULT_READ_TIMEOUT_MS);
        HTTP_CLIENT.setHttpConnectionManager(connectionManager);
    }

    /**
     * 发送POST请求
     *
     * @param uParamsMap    Map形式的参数
     * @param postUrl       POST请求的URL
     * @param expireSeconds 请求超时时间，单位为秒
     * @param charSet       字符集
     *                      add by xjh @date 2015/07/03
     */
    public static String postHttpRequest(Map<String, String> uParamsMap, String postUrl, int expireSeconds, String charSet) {
        logger.debug("以commons-httpclient的方式，发送POST请求.");
        NameValuePair[] nameValuPairArray = convertParamsMap2NameValuePair(uParamsMap);

        PostMethod postMethod = new PostMethod(postUrl);
        postMethod.setRequestBody(nameValuPairArray);

        String result = httpRequest(postMethod, expireSeconds, postUrl, charSet);
        logger.debug("POST请求发送完毕. ");
        return result;
    }

    /**
     * 转换Map的参数成NameValuePair
     *
     * @param uParamsMap
     * @return add by xjh @date 2015/07/03
     */
    public static NameValuePair[] convertParamsMap2NameValuePair(Map<String, String> uParamsMap) {
        if (uParamsMap == null)
            uParamsMap = new HashMap<String, String>();
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : uParamsMap.entrySet()) {
            nameValuePairs.add(new NameValuePair(entry.getKey(), entry.getValue()));
        }
        NameValuePair[] nameValuPairArray = nameValuePairs.toArray(new NameValuePair[nameValuePairs.size()]);
        return nameValuPairArray;
    }

    /**
     * 发出Http请求
     *
     * @param httpMethod
     * @param expireSeconds
     * @param requestUri
     * @param charSet
     * @return add by xjh @date 2015/07/03
     */
    protected static String httpRequest(HttpMethodBase httpMethod, int expireSeconds, String requestUri, String charSet) {
        if (expireSeconds > 0) {
            HTTP_CLIENT.getHttpConnectionManager().getParams().setConnectionTimeout(expireSeconds * 1000);
        }
        String respBody = null;
        try {
            int responseCode = HTTP_CLIENT.executeMethod(httpMethod);

            if (HttpServletResponse.SC_OK == responseCode) {
                logger.debug("HTTP请求发送完毕. 响应结果为：{}", respBody);
                respBody = new String(httpMethod.getResponseBodyAsString().getBytes(DEFAULT_CHARSET));
            } else if (HttpServletResponse.SC_INTERNAL_SERVER_ERROR == responseCode) {
                String errMsg = "调用【" + httpMethod.getURI() + "】远程服务时出现500内部服务错误";
                logger.warn(errMsg);
            } else if (HttpServletResponse.SC_NOT_FOUND == responseCode) {
                String errMsg = "调用【" + httpMethod.getURI() + "】远程服务时出现找不到对应的服务. ";
                logger.warn(errMsg);
            } else if (HttpServletResponse.SC_FORBIDDEN == responseCode) {
                String errMsg = "调用【" + httpMethod.getURI() + "】远程服务时出现请求受限（未经允许访问，如受限IP）. ";
                logger.warn(errMsg);
            } else {
                String errMsg = "调用【" + httpMethod.getURI() + "】远程服务时出现未处理的响应状态. Exception code: " + responseCode;
                logger.warn(errMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpMethod.releaseConnection();
        }
        return respBody;
    }

    /**
     * 获取链接
     */
    public static URLConnection getConnection(String url) throws Exception {
        if (StringUtils.isBlank(url)) return null;
        // 创建url
        URL realurl = new URL(url);
        // 打开连接
        URLConnection connection = realurl.openConnection();
        // 设置通用的请求属性
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        return connection;
    }

    /**
     * 执行GET方法
     */
    public static String doGet(String url, Map<String, String> params) throws Exception {
        String result = "".intern();// 访问返回结果
        BufferedReader read = null;// 读取访问结果
        try {
            if (StringUtils.isBlank(url))
                return result;
            if (params != null) {
                url = url.indexOf("?".intern()) != -1 ? url : url
                        + "?".intern();
                for (String key : params.keySet()) {
                    url += key + "=".intern() + params.get(key) + "&".intern();
                }
            }

            //if(false){
            // 创建url
            //	URL realurl = new URL(url);
            // 打开连接
            //	URLConnection connection = realurl.openConnection();
            // 设置通用的请求属性
            //	connection.setRequestProperty("accept", "*/*");
            //		connection.setRequestProperty("connection", "Keep-Alive");
            //	connection.setRequestProperty("user-agent",
            //			"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //}
            URLConnection connection = getConnection(url);
            // 建立连接
            connection.connect();
            // 获取所有响应头字段
            // Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段，获取到cookies等
            // for (String key : map.keySet()) {
            // System.out.println(key + "--->" + map.get(key));
            // }
            // 定义 BufferedReader输入流来读取URL的响应
            read = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "UTF-8"));
            String line;// 循环读取
            while ((line = read.readLine()) != null) {
                result += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (read != null) {// 关闭流
                try {
                    read.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}
