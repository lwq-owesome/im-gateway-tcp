package com.awesome.im.gateway.tcp.dispatcher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * projectName：im-gateway-tcp
 * className ：DispatcherInstanceAddress
 * class desc：TODO
 * createTime：2019/11/8 1:36 PM
 * creator：awesome
 * @author awesome
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DispatcherInstanceAddress {

    private String host;
    private String ip;
    private int port;
}
