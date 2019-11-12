package com.awesome.im.gateway.tcp.dispatcher;

import cn.hutool.json.JSONUtil;
import com.awesome.im.common.request.CommonConstant;
import com.awesome.im.common.request.CommonRequest;
import com.awesome.im.common.request.dto.AuthenticateUserDTO;
import com.awesome.im.proto.ImCommunicationProto;
import io.netty.channel.socket.SocketChannel;
import lombok.*;

/**
 * projectName：im-gateway-tcp
 * className ：DispatcherInstance
 * class desc：分发系统实列
 * createTime：2019/11/8 1:21 PM
 * creator：awesome
 * @author awesome
 */
//@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
//@Builder
public class DispatcherInstance {


    /**
     *  于dispathcer通讯到 socketChannel
     */
    private SocketChannel socketChannel;


    /**
     * 向dispatcher发送认证请求
     * @param authenticateUserDTO 认证请求
     */
    public  void authenticate(AuthenticateUserDTO authenticateUserDTO){
        String clientVersion="1.0.0";
        Long timestamp=System.currentTimeMillis();
        String message="";
        String body= JSONUtil.toJsonStr(authenticateUserDTO);
        ImCommunicationProto.CommonMessage commonMessage = CommonRequest.buildRequest(clientVersion, body,
                timestamp, message, CommonConstant.REQUESTTYPEAUTHREQUEST);
        socketChannel.writeAndFlush(commonMessage);
        System.out.println("客户端发送认证请求到转发服务成功");

    }



}
