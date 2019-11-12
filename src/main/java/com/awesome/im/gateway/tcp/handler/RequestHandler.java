package com.awesome.im.gateway.tcp.handler;

import cn.hutool.json.JSONUtil;
import com.awesome.im.common.request.CommonConstant;
import com.awesome.im.common.request.CommonRequest;
import com.awesome.im.common.request.dto.AuthenticateUserDTO;
import com.awesome.im.gateway.tcp.dispatcher.DispatcherInstance;
import com.awesome.im.gateway.tcp.dispatcher.DispatcherInstanceManager;
import com.awesome.im.gateway.tcp.utils.GateWayUtils;
import com.awesome.im.proto.ImCommunicationProto;
//import lombok.extern.slf4j.Slf4j;

/**
 * projectName：im-gateway-tcp
 * className ：RequestHandler
 * class desc： 处理外部的scoket请求
 * createTime：2019/11/8 11:08 PM
 * creator：awesome
 */
//@Slf4j
public class RequestHandler {

    private  RequestHandler(){

    }


    static  class Singeton {
      public static   RequestHandler requestHandler=new RequestHandler();

    }


    public  static   RequestHandler getInstance(){
        return Singeton.requestHandler;
    }


    /**
     * 向dispathcer服务发送认证请求
     * @param authenticateUserDTO
     */
    public  void authenticate( AuthenticateUserDTO authenticateUserDTO){
        /**
         * 和 dispatcher 交互的管理器
         */
        DispatcherInstanceManager dispatcherInstanceManager = DispatcherInstanceManager.getInstance();
        DispatcherInstance dispatcherInstance = dispatcherInstanceManager.chooseDispatcherInstance();
        authenticateUserDTO.setChannelId(GateWayUtils.getChannelId(dispatcherInstance.getSocketChannel()));
        String clientVerison="1.1";
        String message="";
        String body= JSONUtil.toJsonStr(authenticateUserDTO);
        ImCommunicationProto.CommonMessage commonMessage = CommonRequest
                .buildRequest(clientVerison, body, System.currentTimeMillis(), message, CommonConstant.REQUESTTYPEAUTHREQUEST);
        dispatcherInstance.getSocketChannel().writeAndFlush(commonMessage);
        System.out.println("向dispathcer服务发送认证请求");
    }


}
