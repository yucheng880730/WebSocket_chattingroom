package com.example.socket;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;


@Component
@ServerEndpoint(value = "/WebSocketServer/{usernick}")
public class WebSocketController {

    @OnOpen
    // 打開一個新連接用
    public void onOpen(@PathParam(value = "usernick") String userNick, Session session) {
        String message = "有新成員[" + userNick + "]加入聊天室!";
        System.out.println(message);

        // 加入使用者
        WebSocketUtil.addSession(userNick, session);
        WebSocketUtil.sendMessageForAll(message);
    }

    @OnClose
    // 關閉連接時調用
    public void onClose(@PathParam(value = "usernick") String userNick, Session session) {
        String message = "成員[" + userNick + "]退出聊天室!";
        System.out.println(message);

        // 移除使用者
        WebSocketUtil.remoteSession(userNick);
        WebSocketUtil.sendMessageForAll(message);
    }

    @OnMessage
    // 當伺服器接收到客戶端發送的消息時所調用的方法
    public void OnMessage(@PathParam(value = "usernick") String userNick, String message) {
        String info = "成員[" + userNick + "]：" + message;
        System.out.println(info);
        WebSocketUtil.sendMessageForAll(message);
    }

    @OnError
    // 錯誤時觸發
    public void onError(Session session, Throwable throwable) {
        System.out.println("錯誤:" + throwable);
        try {
            session.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        throwable.printStackTrace();
    }

}
