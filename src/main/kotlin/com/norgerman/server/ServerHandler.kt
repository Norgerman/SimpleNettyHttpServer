package com.norgerman.server

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

/**
 * Created by Norgerman on 4/1/2016.
 * ServerHandler.kt
 */

class ServerHandler : ChannelInboundHandlerAdapter() {

    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
        if (msg is HttpContext) {
            println("Method: ${msg.request.method}");
            println("Path: ${msg.request.path}");
            println("Version: ${msg.request.version}");
            println();
            println("Headers");
            msg.request.headers.forEach { e -> println("${e.key}: ${e.value}") };
            if (msg.request.body != null) {
                println("Body:\r\n");
                val bytes = ByteArray(msg.request.contentLength.toInt());
                msg.request.body?.read(bytes, 0, msg.request.contentLength.toInt());
                println(String(bytes, Charsets.UTF_8));
            }
            msg.response.sendStringAndFinish("Hello world");
        }
    }

    override fun channelActive(ctx: ChannelHandlerContext?) {
        println("Channel active");
    }

    override fun channelInactive(ctx: ChannelHandlerContext?) {
        println("Channel inactive");
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        cause?.printStackTrace();
        ctx?.close();
    }

}