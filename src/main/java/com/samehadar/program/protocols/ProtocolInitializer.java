package com.samehadar.program.protocols;

import java.net.Socket;

/**
 * Created by DoSofRedRiver on 18.12.2016.
 */
public interface ProtocolInitializer {
    Protocol init(Socket socket);
}
