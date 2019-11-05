package com.awesome.im.gateway.tcp.manager;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;

/**
 * projectName：im-gateway-tcp
 * className ：NettyChannelManager.class
 * class desc：管理netty 长连接组件
 * createTime：2019/11/3 12:38 AM
 * creator：awesome
 */
public class NettyChannelManager {


    private  static ConcurrentHashMap<String, SocketChannel> channlesMaps=new ConcurrentHashMap<String, SocketChannel>();


    private NettyChannelManager(){

    }

    /**
     * singleton
     */
    static  class Sinleton{
        public  static NettyChannelManager nettyManager=new NettyChannelManager();

    }

    /**
     * 获取单列
     * @return NettyManager instance
     */
    public  static NettyChannelManager getIntance(){
        return  Sinleton.nettyManager;

    }

    /**
     * 添加channel 对象。 socketChannel 对于 用户 channel
     * @param userId
     * @param socketChannel
     */
    public  void addChannel(String userId,SocketChannel socketChannel){
        channlesMaps.put(userId,socketChannel);
    }


    /**
     * 删除userId 对于的socketChannel
     * @param userId
     */
    public  void remove(String userId){
        channlesMaps.remove(userId);
    }

    /**
     * 判断userId 是否存在
     * @param userId
     * @return
     */
    public  boolean exites(String userId){
        return  channlesMaps.contains(userId);
    }


    /**
     * 通过userId 获取socketChannel
     * @param userId
     * @return
     */
    public  SocketChannel getUserChannelByUserId(String userId){
        return  channlesMaps.get(userId);
    }


}
