package com.awesome.im.gateway.tcp;

import com.awesome.im.proto.AuthenticateRequestProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author awesome
 *
 *  socketChannel 处理器
 *
 */
public class GatewayTcpHandler extends SimpleChannelInboundHandler<AuthenticateRequestProto.AuthenticateRequest> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AuthenticateRequestProto.AuthenticateRequest authenticateRequest) throws Exception {
        System.out.println(authenticateRequest);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");

    }
}
