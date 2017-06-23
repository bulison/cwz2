package cn.fooltech.fool_ops.utils;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebUtils {

    /**
     * 兼容旧版IE，写json到前台
     *
     * @param
     */
    public static void writeJsonToHtml(HttpServletResponse response, Object result) {
        JSONObject json = JSONObject.fromObject(result);
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
