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
  - $server protocol_1_3 - start protocol 1.3("deadlock").
  - #server protocol_2_6 - start protocol 2.6("Neuman-Stubblebine").
  
##Protocols:
###  - Protocol 1.3 ("Deadlock")[only russian now]:
  ![alt text][logo1]
###  - Protocol 2.6 ("Neuman-Stubblebine") [only russian now]:
  ![alt text][logo2]
[logo1]: https://github.com/Samehadar/p2pChatWithProtocol/blob/master/src/main/resources/1.3.jpg
[logo2]: https://github.com/Samehadar/p2pChatWithProtocol/blob/master/src/main/resources/2.6.jpg

