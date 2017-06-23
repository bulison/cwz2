/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.fooltech.fool_ops.config.websocket;

import cn.fooltech.fool_ops.component.security.SecurityUser;
import cn.fooltech.fool_ops.domain.message.service.PushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * Echo messages by implementing a Spring {@link WebSocketHandler} abstraction.
 */
public class FoolWebSocketHandler extends AbstractWebSocketHandler {

	private static Logger logger = LoggerFactory.getLogger(FoolWebSocketHandler.class);


	private PushService pushService;

	public FoolWebSocketHandler(PushService pushService) {
		this.pushService = pushService;
	}

	/**
	 * 连接建立后
	 * @param session
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) {

		SecurityUser user = extractSecurityUser(session);
		if(user!=null){
			pushService.login(user.getId(), session);
		}else{
			logger.warn("can not extract userId from session:"+session.toString());
		}
	}

	/**
	 * 连接关闭
	 * @param session
	 * @param status
	 * @throws Exception
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

		SecurityUser user = extractSecurityUser(session);
		if(user!=null){
			pushService.logout(user.getId(), session);
		}else{
			logger.warn("can not extract userId from session:"+session.toString());
		}

		super.afterConnectionClosed(session, status);
	}

	/**
	 * 解析登录用户
	 * @param session
	 * @return
	 */
	private SecurityUser extractSecurityUser(WebSocketSession session){
		if(session==null || session.getPrincipal()==null)return null;

		Authentication authentication = (Authentication)session.getPrincipal();

		if(authentication.getPrincipal() instanceof SecurityUser) {
			SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
			return securityUser;
		}

		return null;
	}

}
