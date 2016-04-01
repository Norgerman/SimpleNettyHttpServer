@file:JvmName("Startup")

import com.norgerman.server.SimpleServer

/**
 * Created by Norgerman on 4/1/2016.
 * Main.kt
 */

fun main(args: Array<String>) {
    SimpleServer(12306).run();
}