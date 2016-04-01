@file:JvmName("HttpRequestHelper")

package com.norgerman.server

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufOutputStream
import io.netty.channel.ChannelHandlerContext
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Norgerman on 4/1/2016.
 * HttpResponse.kt
 */

val statusMap: Map<Int, String> = mapOf(Pair(200, "OK"), Pair(500, "Internal Error"));

class HttpResponse(ctx: ChannelHandlerContext, request: HttpRequest) {
    val version = request.version;
    var status: Int = 200;
        set(value) {
            field = value;

            statusMessage = statusMap[status] ?: "Unknown";
        };
    var statusMessage: String;
    val headers: MutableMap<String, String>;
    var contentLength: Int = 0
        set(value) {
            field = value;
            headers["Content-Length"] = value.toString();
        }

    private val buffer: ByteBuf;
    private val ctx = ctx;
    private var sent = false;

    init {
        val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss z");
        dateFormat.timeZone = TimeZone.getTimeZone("GMT");
        val d = Date(System.currentTimeMillis())
        status = 200;
        statusMessage = statusMap[status] ?: "Unknown";
        headers = mutableMapOf(Pair("Content-Type", "text/plain; charset=utf-8"),
                Pair("Date", dateFormat.format(d)),
                Pair("Vary", "Accept-Encoding"),
                Pair("Content-Encoding", "identity"),
                Pair("Connection", request.headers["Connection"] ?: "closed"),
                Pair("Content-Length", "0"));
        buffer = ctx.alloc().buffer(1024);
    }

    fun sendStringAndFinish(message: String) {
        if (sent)
            return;
        val bodyBytes = message.toByteArray(Charsets.UTF_8);
        val sb = StringBuilder();
        contentLength = bodyBytes.size;
        sb.append("$version $status $statusMessage\r\n");
        headers.forEach { e -> sb.append("${e.key}: ${e.value}\r\n") };
        sb.append("\r\n");
        buffer.writeBytes(sb.toString().toByteArray(Charsets.US_ASCII));
        buffer.writeBytes(bodyBytes);
        ctx.write(buffer);
        ctx.flush();
        sent = true;
    }

}