package com.samehadar.program;

import com.samehadar.program.cipher.ElgamalShema;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.*;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Program {
    static Channel channel;

    static InetSocketAddress address;

    public static String destinationIP;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Source Port: ");
        int sourcePort = Integer.parseInt(scanner.nextLine());

        System.out.print("Destination IP: ");
        destinationIP = scanner.nextLine();

        System.out.print("Destination Port: ");
        int destinationPort = Integer.parseInt(scanner.nextLine());

        channel = new Channel();
        channel.bind(sourcePort);
        channel.start();

        System.out.println("Started.");

        address = new InetSocketAddress(destinationIP, destinationPort);

        while (true) {
            String message = scanner.nextLine();
            if (message.equals("$server shutdown")) {
                break;
            } else if (message.equals("$server protocol_1_3")) {
                realizeProtocol();
            }

            String messageForSending = name + " >> " + message;

            channel.sendTo(address, messageForSending);
            System.out.println(messageForSending); // remove duplicate string
        }

        scanner.close();
        channel.stop();
        System.out.println("Shut down.");
    }

    public static void realizeProtocol() throws IOException {
        System.out.println("Реализация протокола Взаимоблокировка(Алиса)");
        ServerSocket serverSocket = new ServerSocket(9909);

        channel.sendTo(address, "protocol_1_3");
        Socket clientSocket = serverSocket.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        System.out.println("Установлено тайное соединение");

        Integer p = new Integer("11337409");
        Integer g = new Integer("7");
        Integer x = (int)(Math.random() * 10000000);
        //ElgamalShema cipherAlice = new ElgamalShema(p, g, x);
        ElgamalShema cipherAlice = new ElgamalShema();
        Map<String, Integer> key = cipherAlice.generateKey(p , g, x);
        System.out.println("Создали шифратор.");

        BigInteger rA = BigInteger.probablePrime(25, new Random(303));
        System.out.print("Сгенерировали свою часть сессионного ключа: ");
        System.out.println(rA);

        //BigInteger openKeyBob = BigInteger.probablePrime(20, new Random(5));
        writer.println(key.get("p") + "," + key.get("g") + "," + key.get("y"));
        System.out.println(String.format("Отправлен открытый ключ: (%s,%s,%s)", key.get("p"), key.get("g"), key.get("y")));

        //BigInteger openKeyAlice = new BigInteger(reader.readLine());
        String[] keyBob = reader.readLine().split(",");
        Integer pBob = Integer.parseInt(keyBob[0]);
        Integer gBob = Integer.parseInt(keyBob[1]);
        Integer yBob = Integer.parseInt(keyBob[2]);
        System.out.println(String.format("Получен открытый ключ: (%s,%s,%s)", pBob, gBob, yBob));
        Integer k = (int)(Math.random() * 10000000); //change

        Map<String, Integer> ab = cipherAlice.encryption(rA.intValue(), pBob, gBob, yBob, k);
        System.out.println("Зашифровали сообщение открытым ключом Боба");

        writer.println(ab.get("a"));
        System.out.println("Отправили Бобу часть \"a\"");

        Integer partA = Integer.parseInt(reader.readLine());
        System.out.println("Получили от Боба первую часть зашифрованного сообщения: " + partA);

        writer.println(ab.get("b"));
        System.out.println("Отправили Бобу часть \"b\"");

        Integer partB = Integer.parseInt(reader.readLine());
        System.out.println("Получили от Боба вторую часть зашифрованного сообщения: " + partB);

        //TODO:: возможно что ошибка в том, что я расшифровываю своими ключами, а не Боба!
        Integer bobMessage = cipherAlice.decryption(partA, partB);
        System.out.println("Расшифрованное сообщение Боба: " + bobMessage);
//TODO:: переписать все через BigInteger
        reader.close();
        writer.close();
        clientSocket.close();
        serverSocket.close();
        System.out.println("Тайное соединение закрыто");
    }
}
