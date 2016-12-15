# p2pChatWithProtocol
CryptoChat

1. Build maven project with command: mvn package - in project directory
2. Start java with command: java -jar p2p_chat-1.0-SNAPSHOT.jar - in target directory
3. Write params:
  - Name - your nickname in chat
  - Source Port - port on your machine for sending and recieving messages
  - Destination IP - ip of your friend
  - Destination Port - port on machine of friend
4. After this, you start chat.

##Server command(may be changed): 
Start with '$server':
  - $server schutdown - suthdown your chat client
  - $server protocol_1_3 - start protocol 1.3("deadlock"). After writing this command all your and friends massage encrypt by ELGamalSchema
  
##Protocols:
  - ###Protocol 1.3 ("deadlock") [only russian now: http://studopedia.org/2-89925.html]
  (1) Алиса посылает Бобу свой открытый ключ.
  (2) Боб посылает Алисе свой открытый ключ.
  (3) Алиса зашифровывает свое сообщение открытым ключом Боба. Половину зашифрованного сообщения она отправляет Бобу.
  (4) Боб зашифровывает свое сообщение открытым ключом Алисы. Половину зашифрованного сообщения он отправляет Алисе.
  (5) Алиса отправляет Бобу вторую половину зашифрованного сообщения.
  (6) Боб складывает две части сообщения Алисы и расшифровывает его с помощью своего закрытого ключа. Боб отправляет Алисе вторую половину    своего зашифрованного сообщения.
  (7) Алиса складывает две части сообщения Боба и расшифровывает его с помощью своего закрытого ключа.
    Идея в том, что половина зашифрованного сообщения бесполезна без второй половины, она не может быть дешифрирована. Боб не сможет   прочитать ни одной части сообщения Алисы до этапа (6), а Алиса не сможет прочитать ни одной части сообщения Боба до этапа (7). Существует множество способов разбить сообщение на части:
    Если используется блочный алгоритм шифрования, половина каждого блока (например, каждый второй бит) может быть передана в каждой половине сообщения.
    Дешифрирование сообщения может зависеть от вектора инициализации (см. раздел 9.3), который может быть передан во второй части сообщения.
    Первая половина сообщения может быть однонаправленной хэш -функцией шифрованного сообщения (см. раздел 2.4), а во вторая половина - собственно шифрованным сообщением.
