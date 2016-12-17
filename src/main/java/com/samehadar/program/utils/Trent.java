package com.samehadar.program.utils;

import com.samehadar.program.cipher.VigenereWithoutMod;
import com.samehadar.program.cipher.Cipher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.*;

/**
 * //TODO:: есть две идеи: первая - трент находится на ПК Алисы, и при необходимости реализации протокола
 * //TODO:: она его "будит". вторая - трент отдельное приложение, которое нужно запустить до протокола
 * //TODO:: остановился на первой
 */
public class Trent implements Runnable {

    private static Trent trent;
    private Integer trentPort;

    private static Random secureRandom;
    static {
        secureRandom = new SecureRandom();
    }
    private BigInteger kA;
    private BigInteger kB;
    private BigInteger sessionKey;

    public static Trent getInstance() {
        if (trent == null) {
            trent = new Trent();
        }
        return trent;
    }

    private Trent() {
        this.kA = new BigInteger(30, secureRandom);
        this.kB = new BigInteger(30, secureRandom);
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            //TODO::need to check timestamp validity (etc timeout)
            ServerSocket serverSocket = new ServerSocket(trentPort);

            Socket aliceSocket = serverSocket.accept();
            BufferedReader aliceReader = new BufferedReader(new InputStreamReader(aliceSocket.getInputStream()));
            PrintWriter aliceWriter = new PrintWriter(aliceSocket.getOutputStream(), true);
            System.out.println("Trent: accepted Alice");

            aliceWriter.println(this.kA);
            System.out.println("Trent: send kA: " + this.kA);

            Socket bobSocket = serverSocket.accept();
            BufferedReader bobReader = new BufferedReader(new InputStreamReader(bobSocket.getInputStream()));
            PrintWriter bobWriter = new PrintWriter(bobSocket.getOutputStream(), true);
            System.out.println("Trent: accepted Bob");

            bobWriter.println(this.kB);
            System.out.println("Trent: send kB: " + this.kB);

            String receiveBob = bobReader.readLine();
            System.out.println("Trent: receive message from Bob: " + receiveBob);
            this.sessionKey = new BigInteger(30, secureRandom);
            System.out.println("Trent: generate sessionKey: " + sessionKey);
            List<String> receiveBobParsed = parseMessage(receiveBob);
            List<String> mess1 = new ArrayList<String>(){{
                add(receiveBobParsed.get(0));//Bob nickname
                //TODO:: отправляю защифрованное, а нужно нет(у трента есть не зашифрованное!)
                add(receiveBobParsed.get(3));//rA
                add(sessionKey.toString());  //sessionKey
                add(receiveBobParsed.get(4));//timestamp
            }};
            Cipher<String , String> cesar = new VigenereWithoutMod();
            List<String> mess1Cipher = CipherUtils.encryptionForEach(cesar, mess1, kA.toString());
            aliceWriter.println(Trent.createMessage(mess1Cipher));
            System.out.println("Trent: send to Alice mess1: " + mess1Cipher);
            List<String> mess2 = new ArrayList<String>(){{
                add(receiveBobParsed.get(2));//Alice nickname
                add(sessionKey.toString());  //sessionKey
                add(receiveBobParsed.get(4));//timestamp
            }};
            List<String> mess2Cipher = CipherUtils.encryptionForEach(cesar, mess2, kB.toString());
            aliceWriter.println(Trent.createMessage(mess2Cipher));
            System.out.println("Trent: send to Alice mess2: " + mess2Cipher);
            aliceWriter.println(receiveBobParsed.get(1)); //rB
            System.out.println("Trent: send to Alice mess3: " + receiveBobParsed.get(1));


            //closing streams
            serverSocket.close();
            aliceSocket.close();
            aliceReader.close();
            aliceWriter.close();
            bobSocket.close();
            bobReader.close();
            bobWriter.close();
        } catch (IOException e) {
            //TODO::
        }
    }

    public void setTrentPort(Integer port) {
        this.trentPort = port;
    }

    public static String createMessage(List<String> strings, String ... args) {
        return createMessage(strings) + createMessage(args);
    }

    public static String createMessage(String ... args) {
        return createMessage(Arrays.asList(args));
    }
//TODO;; may be change to Map?
    public static String createMessage(List<String> args) {
        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            builder.append(arg).append("|");
        }
        return builder.toString();
    }

    public static List<String> parseMessage(String message) {
        return Arrays.asList(message.split("\\|"));
    }

    public static Random getSecureRandom() {
        return secureRandom;
    }

    @Override
    protected void finalize() throws Throwable {
        trent = null;
        super.finalize();
    }
}
