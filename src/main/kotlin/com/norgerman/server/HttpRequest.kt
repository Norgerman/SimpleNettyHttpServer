package com.norgerman.server

import io.netty.buffer.ByteBufInputStream

/**
 * Created by Norgerman on 4/1/2016.
 * HttpRequest.kt
 */
class HttpRequest(method: String, path: String, version: String) {
    private var m_contentLength = 0L;

    var body: ByteBufInputStream? = null;
    val method = method;
    val path = path;
    val version = version;
    val headers: MutableMap<String, String> = mutableMapOf();
    val contentLength: Long
        get() = m_contentLength;


    fun parseAndAddHeader(headerString: String) {
        var pair = headerString.split(": ");
        headers.put(pair[0], pair[1]);
        if (pair[0] == "Content-Length") {
            m_contentLength = pair[1].toLong();
        }
    }
}