package cn.fooltech.fool_ops.web.chat;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.message.service.MessageService;
import cn.fooltech.fool_ops.domain.message.service.PushService;
import cn.fooltech.fool_ops.domain.message.vo.MessageVo;
import cn.fooltech.fool_ops.domain.message.vo.SimpleMessageVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Derek on 2017/2/28.
 */
@RestController
@RequestMapping(value = "/api/push")
public class PushController {

    @Autowired
    private PushService pushService;

    @Autowired
    private MessageService msgService;


//    @ResponseBody
//    @GetMapping("/pushTest")
//    public String pushTest(String msg) {
//
//        String userId = SecurityUtil.getCurrentUserId();
//
//        SimpleMessageVo svo = new SimpleMessageVo();
//        svo.setContent(msg);
//        svo.setTitle(msg);
//        svo.setTriggerType("0");
//        pushService.push(userId, svo, false);
//        return "success";
//
//    }
}
