package com.awesome.im.gateway.tcp.utils;


import io.netty.channel.socket.SocketChannel;

/**
 * projectName：im-gateway-tcp
 * className ：GateWayUtils
 * class desc：TODO
 * createTime：2019/11/12 2:09 PM
 * creator：awesome
 * @author awesome
 */
public class GateWayUtils {

    public  static String getChannelId(SocketChannel socketChannel){
        return  socketChannel.id().asLongText();
    }
}
