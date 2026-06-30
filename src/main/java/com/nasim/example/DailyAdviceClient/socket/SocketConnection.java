package com.nasim.example.DailyAdviceClient.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Optional;

public class SocketConnection {
    private static Optional<SocketChannel> socketChannel = Optional.empty();
    public  static SocketChannel connect(String ip, String port) throws IOException {
        if(socketChannel.isEmpty()){
                InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, Integer.parseInt(port));
                socketChannel = Optional.of(SocketChannel.open(inetSocketAddress));
        }
       return socketChannel.get();
    }

}
