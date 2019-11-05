package com.awesome.im.gateway.tcp;

import com.awesome.im.proto.AuthenticateRequestProto;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * @author awesome
 *
 * sdk 层
 */
public class GatewayTcpServer {

    public static void main(String[] args) {

        final   EventLoopGroup connectThreadGroup=new NioEventLoopGroup();

        final   EventLoopGroup ioThreadGroup=new NioEventLoopGroup();


        final ServerBootstrap server=new ServerBootstrap();


        try {
            server.group(connectThreadGroup,ioThreadGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch)  {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ProtobufVarint32FrameDecoder());
                            pipeline.addLast(new ProtobufDecoder(AuthenticateRequestProto.AuthenticateRequest.getDefaultInstance()));
                            pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                            pipeline.addLast(new ProtobufEncoder());
                            pipeline.addLast(new GatewayTcpHandler());


                        }


                    });

            ChannelFuture channelFuture = server.bind(50070).sync();



            channelFuture.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {

                    if (future.isSuccess()){
                        System.out.println("服务器开启成功");
                    }else{
                        System.err.println("服务器开启失败");
                        connectThreadGroup.shutdownGracefully();
                        ioThreadGroup.shutdownGracefully();
                    }


                }
            });
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            connectThreadGroup.shutdownGracefully();
            ioThreadGroup.shutdownGracefully();
        }


    }
}
