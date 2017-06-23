package cn.fooltech.fool_ops.web.service;

import cn.fooltech.fool_ops.domain.message.service.MessageService;
import cn.fooltech.fool_ops.domain.message.service.PushService;
import cn.fooltech.fool_ops.domain.message.vo.SimpleMessageVo;
import cn.fooltech.fool_ops.domain.sysman.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by Derek on 2017/5/12.
 */
@Service
public class MobilePushServiceImpl implements PushService{

    private static Logger logger = LoggerFactory.getLogger(MobilePushServiceImpl.class);


    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;

//    @Value("${eureka.api-gateway.pcService.msg.defaultZone}")
//    private String msgGateway;

//    @Autowired
//    private RestTemplate restTemplate;

    /**
     * user go in websocket
     * @param userId
     * @param session
     */
    public void login(String userId, WebSocketSession session){
    }

    /**
     * user go out websocket
     * @param userId
     */
    public void logout(String userId, WebSocketSession session){
    }

    /**
     * push msg to user
     * @param userId
     * @param msg
     */
    public void push(String userId, SimpleMessageVo msg, boolean pushOther){

//        User user = userService.get(userId);
//        String userCode = user.getUserCode();
//
//        //获取未读消息条数
//        long count = messageService.countUnReadMessage();
//
//        PushData data = new PushData();
//        data.setMessage(msg);
//        data.setUnread(count);
//
//        String msgJson = JsonUtil.toJsonString(data);
//
//        messagingTemplate.convertAndSendToUser(userCode,"/queue/notify", msgJson);
//
//        if(pushOther){
//            try {
//                String url = msgGateway;
//                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
//                        .queryParam("userId", userId);
//                restTemplate.getForEntity(builder.build().encode().toUri(), String.class);
//            }catch (final HttpClientErrorException e) {
//                System.out.println(e.getStatusCode());
//                System.out.println(e.getResponseBodyAsString());
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }

    }
}
