package com.norgerman.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel

/**
 * Created by Norgerman on 4/1/2016.
 * SimpleServer.kt
 */
class SimpleServer(port: Int) {
    val port: Int = port;

    fun run() {
        val bossGroup: EventLoopGroup = NioEventLoopGroup();
        val workerGroup: EventLoopGroup = NioEventLoopGroup();
        try {
            val bootStrap: ServerBootstrap = ServerBootstrap();

            bootStrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel::class.java)
                    .childHandler(object : ChannelInitializer<SocketChannel>() {
                        override fun initChannel(ch: SocketChannel?) {
                            if (ch != null) {
                                ch.pipeline().addLast(HttpDecoder(), ServerHandler());
                            }
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            val future: ChannelFuture = bootStrap.bind(port).sync();

            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}