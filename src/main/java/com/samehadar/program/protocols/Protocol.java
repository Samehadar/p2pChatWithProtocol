package com.samehadar.program.protocols;

import com.samehadar.program.message.Message;

import java.util.function.Consumer;

/**
 * Created by User on 07.12.2016.
 */
public interface Protocol {
    void sendMessage(Message message);
    void onReceiveMessage(Consumer<Message> callback);
}
