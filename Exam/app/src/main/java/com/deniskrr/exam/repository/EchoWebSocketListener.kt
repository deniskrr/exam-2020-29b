package com.deniskrr.exam.repository

import android.util.Log
import com.deniskrr.exam.model.Request
import com.google.gson.GsonBuilder
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString.Companion.decodeHex

class EchoWebSocketListener(private val showToastHandler: (String) -> Unit) : WebSocketListener() {
    val gson = GsonBuilder().create()

    override fun onOpen(webSocket: WebSocket, response: Response) {
        webSocket.send("Hello, it's SSaurel !")
        webSocket.send("What's up ?")
        webSocket.send("deadbeef".decodeHex())
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("Socket", "Receiving : $text")
        val request = gson.fromJson<Request>(text, Request::class.java)
        showToastHandler("${request.name} was added")
    }

    override fun onClosing(
        webSocket: WebSocket,
        code: Int,
        reason: String
    ) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        Log.d("Socket", "Closing : $code / $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d("Socket", "Error : " + t.message)
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}