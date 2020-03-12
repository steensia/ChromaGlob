package com.asimplenerd.simpleirc

import java.net.Socket
import java.net.SocketAddress
import java.net.URL

class IRCClient {

    var nickname = "";
    lateinit var server : URL;
    lateinit var client : Socket;

    enum class ConnState(state : Int){
        Connected(0),
        Disconnected(1),
        NoResponse(-1),
        InvalidParams(-2),
        Other(-99),
    };

    public fun IRCClient(nick : String, server : URL){
        nickname = nick;
        this.server = server;
    }

    fun connect() : ConnState{
        if(nickname.isBlank() || server.toURI().toASCIIString().isBlank())
            return ConnState.InvalidParams;
        //Begin client connection to specified server
        client = Socket(server.host, server.port);
        return when (client.isConnected){
            true ->  ConnState.Connected;
            false ->  ConnState.Disconnected;
        };

    }

    fun sendMsg(msg : String){

    }

    fun recvMsg(){

    }
}