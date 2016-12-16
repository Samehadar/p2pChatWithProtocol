package com.samehadar.program.utils;

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

    private Random secureRandom;
    private BigInteger aliceK;
    private BigInteger bobK;

    public static Trent getInstance() {
        if (trent == null) {
            trent = new Trent();
        }
        return trent;
    }

    private Trent() {
        this.secureRandom = new SecureRandom();
        this.aliceK = new BigInteger(30, this.secureRandom);
        this.bobK = new BigInteger(30, this.secureRandom);
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(trentPort);

            Socket aliceSocket = serverSocket.accept();
            BufferedReader aliceReader = new BufferedReader(new InputStreamReader(aliceSocket.getInputStream()));
            PrintWriter aliceWriter = new PrintWriter(aliceSocket.getOutputStream(), true);
            System.out.println("Trent: accepted Alice");

            aliceWriter.println(this.aliceK);
            System.out.println("Trent: send aliceK: " + this.aliceK);

            Socket bobSocket = serverSocket.accept();
            BufferedReader bobReader = new BufferedReader(new InputStreamReader(bobSocket.getInputStream()));
            PrintWriter bobWriter = new PrintWriter(bobSocket.getOutputStream(), true);
            System.out.println("Trent: accepted Bob");

            bobWriter.println(this.bobK);
            System.out.println("Trent: send bobK: " + this.bobK);

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

    @Override
    protected void finalize() throws Throwable {
        trent = null;
        super.finalize();
    }
}
