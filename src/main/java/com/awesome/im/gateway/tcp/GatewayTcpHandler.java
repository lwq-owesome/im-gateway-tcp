package com.awesome.im.gateway.tcp;

import cn.hutool.json.JSONUtil;
import com.awesome.im.common.request.CommonConstant;
import com.awesome.im.common.request.dto.AuthenticateUserDTO;
import com.awesome.im.gateway.tcp.handler.RequestHandler;
import com.awesome.im.gateway.tcp.manager.SessionManager;
import com.awesome.im.proto.ImCommunicationProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

/**
 * @author awesome
 *
 *  socketChannel 处理器
 *
 */
public class GatewayTcpHandler extends SimpleChannelInboundHandler< ImCommunicationProto.CommonMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                ImCommunicationProto.CommonMessage commonMessage) throws Exception {

        //接收到sdk客户端发过来的信息
        ImCommunicationProto.CommonMessage.DataTye dateType = commonMessage.getDateType();
        switch (dateType){
            case ReponseType:


                break;
            case RequestType:
                ImCommunicationProto.CommonMessage.Request request = commonMessage.getRequest();
                RequestHandler requestHandler = RequestHandler.getInstance();
                Integer requesType = request.getRequesType();
                //发起用户请求
                if (CommonConstant.REQUESTTYPEAUTHREQUEST.equals(requesType)){
                    String strBody = request.getStrBody();
                    AuthenticateUserDTO authenticateUserDTO = JSONUtil.toBean(strBody, AuthenticateUserDTO.class);
                    requestHandler.authenticate(authenticateUserDTO);
                    SessionManager sessionManager = SessionManager.getIntance();
                    sessionManager.addSesion(authenticateUserDTO.getUid(), (SocketChannel) ctx.channel());
                }
                break;


                default:

                    break;
        }

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
