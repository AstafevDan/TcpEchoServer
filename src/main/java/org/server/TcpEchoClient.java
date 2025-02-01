package org.server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Простой TCP Echo client.
 * В этом классе в методе main создается клиент, который подключается к работающему TCP серверу.
 * Клиент вводит сообщения в консоль и отправляет их серверу для обработки. В качестве ответа сервер возвращает то же самое сообщение.
 * Клиент может завершить работу с сервером написав в консоли слово {@code exit}.
 */
public class TcpEchoClient {
    /**
     * Хост, на котором работает сервер.
     */
    private static final String SERVER_ADDRESS = "localhost";

    /**
     * Порт, на котором работает сервер.
     */
    private static final int PORT = 7777;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             Scanner sc = new Scanner(System.in);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Connected to server " + SERVER_ADDRESS + ":" + PORT);
            System.out.println("Enter the message (or 'exit' to exit the server):");

            String request;
            while (sc.hasNextLine()) {
                request = sc.nextLine();
                writer.println(request);
                String response = reader.readLine();
                System.out.println("Response from server: " + response);

                if (request.equals("exit")) {
                    break;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
