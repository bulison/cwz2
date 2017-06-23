package cn.fooltech.fool_ops.domain.message.service;

import cn.fooltech.fool_ops.domain.message.vo.PushData;
import cn.fooltech.fool_ops.domain.message.vo.SimpleMessageVo;
import cn.fooltech.fool_ops.utils.JsonUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Derek on 2017/3/9.
 */
public interface PushService {


    /**
     * user go in websocket
     * @param userId
     * @param session
     */
    public void login(String userId, WebSocketSession session);

    /**
     * user go out websocket
     * @param userId
     */
    public void logout(String userId, WebSocketSession session);

    /**
     * push msg to user
     * @param userId
     * @param msg
     */
    public void push(String userId, SimpleMessageVo msg, boolean pushOther);
}
