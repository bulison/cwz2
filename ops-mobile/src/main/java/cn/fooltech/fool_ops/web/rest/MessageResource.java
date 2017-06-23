package cn.fooltech.fool_ops.web.rest;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.message.service.MessageService;
import cn.fooltech.fool_ops.domain.message.vo.MessageVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Derek on 2017/5/8.
 */
@RestController
@RequestMapping(value ="/api/message")
public class MessageResource {

    private static Logger logger = LoggerFactory.getLogger(MessageResource.class);

    @Autowired
    private MessageService messageService;

//    @Autowired
//    private PushService pushService;

    /**
     * 消息列表页
     */
    @ApiOperation("获取消息列表分页")
    @GetMapping(value = "/query")
    public PageJson query(PageParamater pageParamater, MessageVo vo){
        Page<MessageVo> page = messageService.querySimple(vo,pageParamater);

        String lastEndTime = "";
        if(page.getContent().size()>0){
            MessageVo message = page.getContent().get(page.getContent().size()-1);
            lastEndTime = DateUtilTools.time2String(message.getSendTime());
        }
        PageJson pageJson = new PageJson(page);
        pageJson.setOther(lastEndTime);
        return pageJson;
    }

    /**
     * 查看信息
     */
    @ApiOperation("查看信息")
    @PutMapping(value="/read")
    public RequestResult read(@RequestParam String messageId){
        return messageService.read(messageId);
    }

    /**
     * 拉取当前登录用户第一条未通知的消息和未读消息的条数
     */
    @ApiOperation("拉取当前登录用户第一条未通知的消息和未读消息的条数")
    @GetMapping(value = "/checkMessage")
    public UnReadMessage checkMessage(){
        //拉取当前登录用户第一条未通知的消息
        MessageVo messageVo = messageService.existUnNotifyMessage();

        //获取未读消息条数
        long count = messageService.countUnReadMessage();
        JSONObject json = new JSONObject();
        json.put("message", messageVo);
        json.put("unread", count);

        UnReadMessage unReadMessage = new UnReadMessage();
        unReadMessage.setMessage(messageVo);
        unReadMessage.setUnread(count);
        return unReadMessage;
    }

//    /**
//     * 其他服务刷新推送
//     * @param userId
//     */
//    @GetMapping(value = "/refreshPush")
//    public void refreshPush(@RequestParam String userId){
//
//        logger.info("mobil push:"+userId);
//
//        //拉取当前登录用户第一条未通知的消息
//        MessageVo message = messageService.existUnNotifyMessage();
//
//        if(message!=null){
//            SimpleMessageVo svo = new SimpleMessageVo();
//            svo.setContent(message.getContent());
//            svo.setTitle(message.getTitle());
//            svo.setTriggerType(message.getTriggerType());
//            pushService.push(userId, svo, false);
//        }else{
//            pushService.push(userId, null, false);
//        }
//    }
//
//    @GetMapping(value = "/pushTest")
//    public String pushTest(String userId, String msg){
//
//        SimpleMessageVo svo = new SimpleMessageVo();
//        svo.setContent(msg);
//        svo.setTitle(msg);
//        svo.setTriggerType("0");
//
//        pushService.push(userId, svo, true);
//        return "success";
//    }

    @Getter
    @Setter
    @NoArgsConstructor
    class UnReadMessage{
        MessageVo message;
        long unread;
    }
}
