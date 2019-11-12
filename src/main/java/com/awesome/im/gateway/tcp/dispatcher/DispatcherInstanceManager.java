package com.awesome.im.gateway.tcp.dispatcher;

import com.awesome.im.gateway.tcp.GatewayTcpHandler;
import com.awesome.im.gateway.tcp.utils.GateWayUtils;
import com.awesome.im.proto.ImCommunicationProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * projectName：im-gateway-tcp
 * className ：DispatcherInstanceManager
 * class desc：和 dispatcher 交互
 * createTime：2019/11/8 1:37 PM
 * creator：awesome
 * @author awesome
 */
public class DispatcherInstanceManager {

    /**
     * 分发系统实列
     */
    private static List<DispatcherInstanceAddress> dispatcherInstanceAddresses = new ArrayList<DispatcherInstanceAddress>();

    /**
     * 静态化分发系统实例地址列表
     */
    static {
        dispatcherInstanceAddresses.add(new DispatcherInstanceAddress("localhost", "127.0.0.1", 50080));
    }

    /**
     * 构造私有化
     */
    private DispatcherInstanceManager() {
        System.out.println("sout");

    }

    static class Singleton {

        static DispatcherInstanceManager instance = new DispatcherInstanceManager();


    }

    /**
     * 获取单例
     *
     * @return
     */
    public static DispatcherInstanceManager getInstance() {
        return Singleton.instance;
    }


    /**
     * 分发系统实例
     */
    private static Map<String, DispatcherInstance> dispatcherInstanceMap = new ConcurrentHashMap<String, DispatcherInstance>();

    /**
     * 随机选择一个分发系统实例
     *
     * @return
     */

    public DispatcherInstance chooseDispatcherInstance() {
        List<DispatcherInstance> dispatcherInstances = new ArrayList<DispatcherInstance>();
        dispatcherInstanceMap.values().forEach(e->{
            dispatcherInstances.add(e);
        });
        Random random = new Random();
        int index = random.nextInt(dispatcherInstanceMap.size());
        return dispatcherInstances.get(index);
    }


    public void removeDispatcherInstance(String channelId) {
        dispatcherInstanceMap.remove(channelId);
    }

    public void addDispatcherInstance(String channelId, DispatcherInstance dispatcherInstance) {
        dispatcherInstanceMap.put(channelId, dispatcherInstance);
    }





    /**
     * 初始化组件
     */
    public void init() {
        // 主动跟一批分发系统建立长连接
        for (DispatcherInstanceAddress dispatcherInstanceAddress : dispatcherInstanceAddresses) {
            try {
                connectDispatcherInstance(dispatcherInstanceAddress);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 连接一个分发系统实例
     *
     * @param dispatcherInstanceAddress
     */
    private void connectDispatcherInstance(DispatcherInstanceAddress dispatcherInstanceAddress) throws Exception {
        final EventLoopGroup threadGroup = new NioEventLoopGroup();

        Bootstrap client = new Bootstrap();

        client.group(threadGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();

                        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                        pipeline.addLast(new ProtobufVarint32FrameDecoder());
                        pipeline.addLast(new ProtobufDecoder(
                                ImCommunicationProto.CommonMessage.getDefaultInstance()

                        ));
                        pipeline.addLast(new ProtobufEncoder());
                        pipeline.addLast(new DispatcherHandler());

                    }
                });

        ChannelFuture channelFuture = client.connect(dispatcherInstanceAddress.getIp(), dispatcherInstanceAddress.getPort());

        channelFuture.addListener(new ChannelFutureListener() {
            // 给异步化的连接请求加入监听器
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {

                    DispatcherInstance dispatcherInstance = new DispatcherInstance(
                            (SocketChannel) channelFuture.channel());
//                    dispatcherInstanceMap.put(GateWayUtils.getChannelId(dispatcherInstance.getSocketChannel())
//                            ,dispatcherInstance);
                    addDispatcherInstance(dispatcherInstance.getSocketChannel().id().asLongText(),dispatcherInstance);
                    System.out.println("已经跟分发系统建立连接，分发系统地址为：" + channelFuture.channel().remoteAddress());
                } else {
                    channelFuture.channel().close();
                    threadGroup.shutdownGracefully();
                }
            }
        });

        channelFuture.sync();
    }

}
