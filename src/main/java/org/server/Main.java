package org.server;

/**
 * В этом классе в методе main создается экземпляр сервера с указанием порта
 * и количества потоков, которое будет создано в пуле.
 */
public class Main {
    public static void main(String[] args) {
        TcpEchoServer server = new TcpEchoServer(7777, 15);
        server.run();
    }
}
