package com.samehadar.program;

import com.samehadar.program.cipher.VigenereWithoutMod;
import com.samehadar.program.cipher.Cipher;
import com.samehadar.program.cipher.ELGamalSchema;
import com.samehadar.program.utils.DateTimeFormat;
import com.samehadar.program.utils.Trent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 07.12.2016.
 */
public class Channel implements Runnable {
    private DatagramSocket socket;
    private boolean running;

    public static String sessionKey = null;
    public static VigenereWithoutMod cesar;
    static {
        cesar = new VigenereWithoutMod();
    }

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
                message = cesar.decrypt(message, sessionKey);

                if (message.equals("protocol_1_3")) {
                    realizeProtocol_1_3();
                } else if (message.equals("protocol_2_6")) {
                    realizeProtocol_2_6();
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

    //TODO::remove more sout after production
    private void realizeProtocol_2_6() throws IOException {
        System.out.println("Реализация протокола Neuman-Stubblebine(Боб)");
        Socket trentSocket = new Socket(Program.destinationIP, 9909);
        BufferedReader trentReader = new BufferedReader(new InputStreamReader(trentSocket.getInputStream()));
        PrintWriter trentWriter = new PrintWriter(trentSocket.getOutputStream(), true);
        System.out.println("Установлено тайное соединение with Trent.");

        BigInteger kB = new BigInteger(trentReader.readLine());
        System.out.println("Получен общий with Trent секретный ключ: " + kB);

        Socket aliceSocket = new Socket(Program.destinationIP, 9910);
        BufferedReader aliceReader = new BufferedReader(new InputStreamReader(aliceSocket.getInputStream()));
        PrintWriter aliceWriter = new PrintWriter(aliceSocket.getOutputStream(), true);
        System.out.println("Установлено тайное соединение с Алисой.");

        //receive Alice
        String receiveAlice = aliceReader.readLine();
        System.out.println("Получили сообщение от Алисы: " + receiveAlice);

        BigInteger rB = BigInteger.probablePrime(25, Trent.getSecureRandom());
        Cipher<String, String> cipher = new VigenereWithoutMod();
        String cipherNow = cipher.encrypt(DateTimeFormat.getNowTimeStamp(), kB.toString());
        List<String> cipherReceive = new ArrayList<>();
        for (String part : Trent.parseMessage(receiveAlice)) {
            cipherReceive.add(cipher.encrypt(part, kB.toString()));
        }
        String toTrent = Trent.createMessage(
                Program.nickname, rB.toString(), cipherReceive.get(0), cipherReceive.get(1), cipherNow
        );
        trentWriter.println(toTrent);
        System.out.println("Отправили to Trent сообщение(nickname,rB,E(aliceNickname, aliceMessage, DateTimeNow)): " + toTrent);

        //closing streams
        trentSocket.close();
        trentReader.close();
        trentWriter.close();
        aliceSocket.close();
        aliceReader.close();
        aliceWriter.close();
    }

    private void realizeProtocol_1_3() throws IOException {
        System.out.println("Реализация протокола Взаимоблокировка(Боб)");
        Socket socket = new Socket(Program.destinationIP, 9909);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Установлено тайное соединение.");

        ELGamalSchema cipherBob = new ELGamalSchema();
//        BigInteger p = new BigInteger("11337409");
        BigInteger p = BigInteger.probablePrime(64, cipherBob.getSecureRandom());
        BigInteger g = new BigInteger("3");
        //BigInteger x = BigInteger.probablePrime(25, cipherBob.getSecureRandom());
        BigInteger x = new BigInteger("12345678901234567890");
        System.out.println("x = " + x);
        Map<String, BigInteger> key = cipherBob.generateKey(p, g, x);
        System.out.println("Создали шифратор и сгенерировали ключи.");
        System.out.println(key);

        //BigInteger rA = BigInteger.probablePrime(25, cipherBob.getSecureRandom());
        BigInteger rB = new BigInteger("123125125");
        System.out.print("Сгенерировали свою часть сессионного ключа: ");
        System.out.println("rB = " + rB);

        String[] keyAlice = reader.readLine().split(",");
        BigInteger pAlice = new BigInteger(keyAlice[0]);
        BigInteger gAlice = new BigInteger(keyAlice[1]);
        BigInteger yAlice = new BigInteger(keyAlice[2]);
        System.out.println(String.format("Получен открытый ключ Алисы: (%s,%s,%s)", pAlice, gAlice, yAlice));
        Map<String, BigInteger> openKeysAlice = new HashMap<String, BigInteger>() {{put("p", pAlice); put("g", gAlice); put("y", yAlice);}};
        System.out.println(openKeysAlice);
        System.out.println();

        writer.println(key.get("p") + "," + key.get("g") + "," + key.get("y"));
        System.out.println(String.format("Отправлен открытый ключ: (%s,%s,%s)", key.get("p"), key.get("g"), key.get("y")));

        BigInteger partA = new BigInteger(reader.readLine());
        System.out.println("Получили от Алисы первую часть зашифрованного сообщения: " + partA);

        Map<String, BigInteger> ab = cipherBob.encrypt(rB.toString(), openKeysAlice);
        System.out.println("Зашифровали сообщение открытым ключом Алисы");
        System.out.println("result = " + ab);

        writer.println(ab.get("a"));
        System.out.println("Отправили Алисе часть \"a\"");

        BigInteger partB = new BigInteger(reader.readLine());
        System.out.println("Получили от Алисы вторую часть зашифрованного сообщения: " + partB);

        writer.println(ab.get("b"));
        System.out.println("Отправили Алисе часть \"b\"");

        Map<String, BigInteger> abAlice = new HashMap<>();
        abAlice.put("a", partA);
        abAlice.put("b", partB);
        String aliceMessage = cipherBob.decrypt(cipherBob.concatenateCipherText(abAlice), key);
        System.out.println("Расшифрованное сообщение Алисы: " + aliceMessage);

        BigInteger rA = new BigInteger(aliceMessage);
        sessionKey = rA.xor(rB).toString();
        System.out.println("Ключ сессии: " + sessionKey);

        reader.close();
        writer.close();
        System.out.println("Тайное соединение закрыто");
    }
}
