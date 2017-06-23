package cn.fooltech.fool_ops.web.message.service;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.domain.message.service.MessageService;
import cn.fooltech.fool_ops.domain.message.service.PushService;
import cn.fooltech.fool_ops.domain.message.vo.PushData;
import cn.fooltech.fool_ops.domain.message.vo.SimpleMessageVo;
import cn.fooltech.fool_ops.utils.JsonUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Derek on 2017/5/12.
 */
@Service
public class PcPushServiceImpl implements PushService{

    private static Logger logger = LoggerFactory.getLogger(PushService.class);

    //userId，WebSocketSession
    private Map<String, List<WebSocketSession>> sessionMap = Maps.newHashMap();

    @Autowired
    private MessageService messageService;

    //@Value("${eureka.api-gateway.mobileService.msg.defaultZone}")
    //private String msgGateway;

    //@Autowired
    //private RestTemplate restTemplate;

    /**
     * user go in websocket
     * @param userId
     * @param session
     */
    public void login(String userId, WebSocketSession session){
        List<WebSocketSession> sessions = sessionMap.get(userId);
        if(sessions!=null){
            sessions.add(session);
        }else{
            sessions = Lists.newArrayList(session);
            sessionMap.put(userId, sessions);
        }

    }

    /**
     * user go out websocket
     * @param userId
     */
    public void logout(String userId, WebSocketSession session){
        List<WebSocketSession> sessions = sessionMap.get(userId);

        for(int i=sessions.size()-1;i>=0;i--){
            WebSocketSession iter = sessions.get(i);
            if(iter.getId().equals(session.getId())){
                sessions.remove(i);
                break;
            }
        }

    }

    /**
     * push msg to user
     * @param userId
     * @param msg
     */
    public void push(String userId, SimpleMessageVo msg, boolean pushOther){

        List<WebSocketSession> sessions = sessionMap.get(userId);
        if(sessions!=null){
            for(WebSocketSession session:sessions){
                if(session.isOpen()){

                    //获取未读消息条数
                    long count = messageService.countUnReadMessage();

                    PushData data = new PushData();
                    data.setMessage(msg);
                    data.setUnread(count);

                    String msgJson = JsonUtil.toJsonString(data);
                    TextMessage textMessage = new TextMessage(msgJson);
                    try {
                        session.sendMessage(textMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    logger.warn("no socket session or session not open with userId:"+userId);
                }
            }
        }

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
