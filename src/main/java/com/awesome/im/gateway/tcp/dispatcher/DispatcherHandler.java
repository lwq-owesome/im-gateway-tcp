package com.awesome.im.gateway.tcp.dispatcher;

import cn.hutool.json.JSONUtil;
import com.awesome.im.common.request.CommonConstant;
import com.awesome.im.common.request.CommonReponse;
import com.awesome.im.common.request.dto.AuthenticateUserDTO;
import com.awesome.im.common.request.dto.AuthenticateUserReponseDTO;
import com.awesome.im.gateway.tcp.handler.RequestHandler;
import com.awesome.im.gateway.tcp.manager.SessionManager;
import com.awesome.im.gateway.tcp.utils.GateWayUtils;
import com.awesome.im.proto.ImCommunicationProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

/**
 * projectName：im-gateway-tcp
 * className ：DispatcherHandler
 * class desc：TODO
 * createTime：2019/11/12 11:47 AM
 * creator：awesome
 * @author awesome
 */
public class DispatcherHandler extends SimpleChannelInboundHandler<ImCommunicationProto.CommonMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ImCommunicationProto.CommonMessage commonMessage) throws Exception {
        System.out.println("收到分发系统发过来的消息"+commonMessage);

        //接收到dispather客户端发过来的信息
        ImCommunicationProto.CommonMessage.DataTye dateType = commonMessage.getDateType();
        switch (dateType){
            case ReponseType:

//                System.out.println(commonMessage);
//                System.out.println(ctx.channel().id());
////                System.out.println(DispatcherInstanceManager.getInstance());
//                SessionManager.getIntance().getSession()

                ImCommunicationProto.CommonMessage.Reponse reponse = commonMessage.getReponse();

                if (CommonConstant.REQUESTTYPEAUTHREPONSE.equals(reponse.getReponseType())){
                    String reponseStrBody = reponse.getStrBody();
                    AuthenticateUserReponseDTO authenticateUserReponseDTO =
                            JSONUtil.toBean(reponseStrBody, AuthenticateUserReponseDTO.class);
                    System.out.println("dispather 认证返回"+JSONUtil.toJsonStr(authenticateUserReponseDTO));

                    String userId = authenticateUserReponseDTO.getUserId();

                    SocketChannel session = SessionManager.getIntance().getSession(userId);

                    ImCommunicationProto.CommonMessage buildReponse = CommonReponse.buildReponse(200, JSONUtil.toJsonStr(authenticateUserReponseDTO),
                            System.currentTimeMillis(),CommonConstant.REQUESTTYPEAUTHREPONSE);
                    session.writeAndFlush(buildReponse);
                    System.out.println("tcp 返回 sdk 认证信息"+JSONUtil.toJsonStr(authenticateUserReponseDTO));

                }


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
                }


                break;

                default:

                    break;

        }


    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {


    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        DispatcherInstanceManager.getInstance().removeDispatcherInstance(GateWayUtils.getChannelId((SocketChannel) ctx.channel()));

    }
}
