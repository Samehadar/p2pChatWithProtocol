package com.samehadar.program;

import com.samehadar.program.cipher.ElgamalShema;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.*;
import java.text.Format;
import java.util.Map;
import java.util.Random;

/**
 * Created by User on 07.12.2016.
 */
public class Channel implements Runnable {
    private DatagramSocket socket;
    private boolean running;

    public void bind(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        socket.close();
        running = false;
    }

    public void run() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        running = true;
        while (running) {
            try {
                socket.receive(packet);

                String message = new String(buffer, 0, packet.getLength());
                if (message.equals("protocol_1_3")) {
                    realizeProtocol();
                }
                System.out.println(message);
            } catch (IOException e) {
                //TODO:: handle
            }
        }
    }

    public void sendTo(InetSocketAddress address, String message) throws IOException {
        byte[] buffer = message.getBytes();

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        packet.setSocketAddress(address);

        socket.send(packet);
    }

    private void realizeProtocol() throws IOException {
        System.out.println("Реализация протокола Взаимоблокировка(Боб)");
        Socket socket = new Socket(Program.destinationIP, 9909);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Установлено тайное соединение.");

        Integer p = new Integer("11337409");
        Integer g = new Integer("7");
        Integer x = (int)(Math.random() * 10000000);
        //ElgamalShema cipherBob = new ElgamalShema(p, g, x);
        ElgamalShema cipherBob = new ElgamalShema();
        Map<String, Integer> key = cipherBob.generateKey(p, g, x);
        System.out.println("Создали шифратор.");

        BigInteger rA = BigInteger.probablePrime(25, new Random(37));
        System.out.print("Сгенерировали свою часть сессионного ключа: ");
        System.out.println(rA);

        //BigInteger openKeyAlice = new BigInteger(reader.readLine());
        String[] keyAlice = reader.readLine().split(",");
        Integer pAlice = Integer.parseInt(keyAlice[0]);
        Integer gAlice = Integer.parseInt(keyAlice[1]);
        Integer yAlice = Integer.parseInt(keyAlice[2]);
        System.out.println(String.format("Получен открытый ключ: (%s,%s,%s)", pAlice, gAlice, yAlice));
        Integer k = (int)(Math.random() * 10000000); //change

        //BigInteger openKeyBob = BigInteger.probablePrime(20, new Random(5));
        writer.println(key.get("p") + "," + key.get("g") + "," + key.get("y"));
        System.out.println(String.format("Отправлен открытый ключ: (%s,%s,%s)", key.get("p"), key.get("g"), key.get("y")));

        Integer partA = Integer.parseInt(reader.readLine());
        System.out.println("Получили от Алисы первую часть зашифрованного сообщения: " + partA);

        Map<String, Integer> ab = cipherBob.encryption(rA.intValue(), pAlice, gAlice, yAlice, k);
        System.out.println("Зашифровали сообщение открытым ключом Алисы");

        writer.println(ab.get("a"));
        System.out.println("Отправили Алисе часть \"a\"");

        Integer partB = Integer.parseInt(reader.readLine());
        System.out.println("Получили от Алисы вторую часть зашифрованного сообщения: " + partB);

        writer.println(ab.get("b"));
        System.out.println("Отправили Алисе часть \"b\"");

        Integer aliceMessage = cipherBob.decryption(partA, partB);
        System.out.println("Расшифрованное сообщение Алисы: " + aliceMessage);

        reader.close();
        writer.close();
        System.out.println("Тайное соединение закрыто");
    }
}
