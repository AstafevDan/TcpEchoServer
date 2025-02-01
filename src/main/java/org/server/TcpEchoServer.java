package org.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Простой многопоточный TCP Echo Server.
 *
 * @author Даниил Астафьев
 * @version 1.0
 */
public class TcpEchoServer {
    /**
     * Порт, на котором работает сервер.
     */
    private final int port;

    /**
     * Пул потоков.
     */
    private final ExecutorService pool;

    /**
     * Переменная, указывающая на то, остановлен сервер или нет.
     */
    private boolean stopped;

    public TcpEchoServer(int port, int poolSize) {
        this.port = port;
        this.pool = Executors.newFixedThreadPool(poolSize); // при создании сервера пул инициализируется фиксированным количеством потоков
    }

    /**
     * С помощью этого метода можно остановить работу сервера.
     * При параметре {@code stopped = true} сервер прекратит принимать входящие соединения.
     *
     * @param stopped
     */
    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    /**
     * Метод для запуска сервера.
     * Сервер принимает входящие соединения от клиентов. Выделяется отдельный поток из пула для обработки каждого клиента
     * независимо, то есть по отдельности.
     */
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (!stopped) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client " + clientSocket.getInetAddress() + " is connected");
                pool.execute(() -> processSocket(clientSocket));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод, обрабатывающий входящие запросы от клиентов.
     * Отправляет полученные данные обратно клиенту.
     *
     * @param clientSocket сокет клиента
     */
    private void processSocket(Socket clientSocket) {
        try (clientSocket;
             BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String request;
            while ((request = reader.readLine()) != null) {
                System.out.println("Received: " + request);
                writer.println(request);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
