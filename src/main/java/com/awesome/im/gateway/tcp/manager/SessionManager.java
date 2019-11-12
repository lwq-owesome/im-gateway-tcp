package com.awesome.im.gateway.tcp.manager;

import io.netty.channel.socket.SocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * projectName：im-gateway-tcp
 * className ：SessionManager
 * class desc：TODO
 * createTime：2019/11/12 2:30 PM
 * creator：awesome
 */
public class SessionManager {
    private static Map<String, SocketChannel> sessions=new ConcurrentHashMap<String, SocketChannel>();

    private static Map<String, String> channelId2UserId=new ConcurrentHashMap<String, String>();



    private SessionManager(){

    }

    static class Singeton{
          static   SessionManager sessionManager=new SessionManager();
     }



     public  static SessionManager getIntance(){
        return  Singeton.sessionManager;
     }


    public  void addSesion(String uid,SocketChannel socketChannel) {
        sessions.put(uid,socketChannel);
        System.out.println("uid" + uid + "session 添加成功");
    }

    public  String getChannelIdByUserId(String channelId){
        return  channelId2UserId.get(channelId);
    }

    public  Boolean isConnect(String userId){
        return  sessions.containsKey(userId);
    }


    public SocketChannel getSession(String userId){
        return  sessions.get(userId);
    }

    public  void removeSession(String userId){
        sessions.remove(userId);
    }

}
