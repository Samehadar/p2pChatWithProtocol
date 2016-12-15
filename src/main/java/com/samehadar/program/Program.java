package com.samehadar.program;

import com.samehadar.program.cipher.ELGamalSchema;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
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

        ELGamalSchema cipherAlice = new ELGamalSchema();
        //BigInteger p = new BigInteger("11337409");
        BigInteger p = BigInteger.probablePrime(64, cipherAlice.getSecureRandom());
        BigInteger g = new BigInteger("3");
        //BigInteger x = BigInteger.probablePrime(25, cipherAlice.getSecureRandom());
        BigInteger x = new BigInteger("12345678901234567890");
        Map<String, BigInteger> key = cipherAlice.generateKey(p , g, x);
        System.out.println("Создали шифратор и сгенерировали ключи");
        System.out.println(key);

        BigInteger rA = BigInteger.probablePrime(30, cipherAlice.getSecureRandom());
//        BigInteger rA = new BigInteger("123125125");
        System.out.print("Сгенерировали свою часть сессионного ключа: ");
        System.out.println(rA);

        //BigInteger openKeyBob = BigInteger.probablePrime(20, new Random(5));
        writer.println(key.get("p") + "," + key.get("g") + "," + key.get("y"));
        System.out.println(String.format("Отправлен открытый ключ: (%s,%s,%s)", key.get("p"), key.get("g"), key.get("y")));

        //BigInteger openKeyAlice = new BigInteger(reader.readLine());
        String[] keyBob = reader.readLine().split(",");
        BigInteger pBob = new BigInteger(keyBob[0]);
        BigInteger gBob = new BigInteger(keyBob[1]);
        BigInteger yBob = new BigInteger(keyBob[2]);
        System.out.println(String.format("Получен открытый ключ Боба: (%s,%s,%s)", pBob, gBob, yBob));
        Map<String, BigInteger> openKeysBob = new HashMap<String, BigInteger>() {{put("p", pBob); put("g", gBob); put("y", yBob);}};
        System.out.println(openKeysBob);

        Map<String, BigInteger> ab = cipherAlice.encrypt(rA.toString(), openKeysBob);
        System.out.println("Зашифровали сообщение открытым ключом Боба");
        System.out.println("result = " + ab);

        writer.println(ab.get("a"));
        System.out.println("Отправили Бобу часть \"a\"");

        BigInteger partA = new BigInteger(reader.readLine());
        System.out.println("Получили от Боба первую часть зашифрованного сообщения: " + partA);

        writer.println(ab.get("b"));
        System.out.println("Отправили Бобу часть \"b\"");

        BigInteger partB = new BigInteger(reader.readLine());
        System.out.println("Получили от Боба вторую часть зашифрованного сообщения: " + partB);

        //TODO:: возможно что ошибка в том, что я расшифровываю своими ключами, а не Боба!
        Map<String, BigInteger> abBob = new HashMap<>();
        abBob.put("a", partA);
        abBob.put("b", partB);
        String bobMessage = cipherAlice.decrypt(cipherAlice.concatenateCipherText(abBob), key);
        System.out.println("Расшифрованное сообщение Боба: " + bobMessage);

        reader.close();
        writer.close();
        clientSocket.close();
        serverSocket.close();
        System.out.println("Тайное соединение закрыто");
    }
}
