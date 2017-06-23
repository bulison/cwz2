//package cn.fooltech.fool_ops.component.qiniu.controller;
//
//
//import cn.fooltech.fool_ops.component.core.RequestResult;
//import cn.fooltech.fool_ops.component.qiniu.service.QiniuService;
//import com.qiniu.common.QiniuException;
//import com.qiniu.util.StringMap;
//import net.sf.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Map;
//
///**
// * 七牛云存储相关api
// */
//@Controller
//public class QiniuController {
//    /** 七牛云存储api操作服务类 */
//    @Autowired
//    private QiniuService qiniuService;
//    /** 七牛图片处理模块 */
//    class Qiniu {
//        /** 获取token */
//        public static final String UPTOKEN = "qiniu/uptoken";
//        /** 抓取网络资源上传到七牛 */
//        public static final String FETCH = "qiniu/fetch";
//        /** 文字转语音 */
//        public static final String TEXT_TO_VOICE = "qiniu/textToVoice";
//        /** 将base64上传至七牛 */
//        public static final String BASE64 = "qiniu/base64";
//        /** 将base64上传至七牛 */
//        public static final String PUTFILE = "qiniu/putfile";
//
//    }
//
//    /**
//     * 获取七牛上传默认策略的token
//    * @return   上传token
//     */
//    @RequestMapping(value = Qiniu.UPTOKEN)
//    @ResponseBody
//    public Map<String, String> qiniuUptoken() {
//        return qiniuService.qiniuUptoken();
//    }
//
//
//    /**
//     * 抓取网络资源上传到七牛
//    * @param fromUrl       资源文件地址
//    * @param key           七牛目标地址
//    * @return              成功返回码0,失败返回其他错误码
//     */
//    @RequestMapping(value = Qiniu.FETCH, method = RequestMethod.POST)
//    @ResponseBody
//    public RequestResult fetch(String fromUrl, String key) {
//        return qiniuService.fetch(fromUrl, key);
//    }
//
//    @RequestMapping(value = Qiniu.PUTFILE, method = RequestMethod.POST)
//    @ResponseBody
//    public StringMap putFile(File file, String key) throws QiniuException {
//        return qiniuService.putFile(file, key);
//    }
//
//
////    public StringMap putFile(File file, String key) throws QiniuException {
//    /**
//     * 将base64上传至七牛图片库
//    * @param qiniu 七牛上传参数
//    * @return 七牛地址
//     * @throws IOException
//     */
//    @RequestMapping(value = Qiniu.BASE64, method = RequestMethod.POST)
//    @ResponseBody
//    public RequestResult base64(@RequestBody JSONObject qiniu) throws IOException {
//        return qiniuService.put64(qiniu.get("stringBase64").toString(), qiniu.get("key").toString());
//    }
//}
