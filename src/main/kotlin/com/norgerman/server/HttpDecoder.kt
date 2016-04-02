package com.norgerman.server

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufInputStream
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder


/**
 * Created by Norgerman on 4/1/2016.
 * HttpRequestDecoder.kt
 */
class HttpDecoder : ByteToMessageDecoder() {
    override fun decode(ctx: ChannelHandlerContext, `in`: ByteBuf, out: MutableList<Any>) {
        val input = ByteBufInputStream(`in`);
        var line = input.readLine()
        if (line === null)
            return;

        val firstLine = line.split(" ");
        val request = HttpRequest(firstLine[0], firstLine[1], firstLine[2]);

        line = input.readLine();

        while (input.available() > 0 && line.isNotEmpty()) {
            request.parseAndAddHeader(line);
            line = input.readLine();
        }

        input.close();

        if (request.method != "GET") {
            request.body = ByteBufInputStream(`in`);
        }
        out.add(HttpContext(request, HttpResponse(ctx, request)));
    }
}