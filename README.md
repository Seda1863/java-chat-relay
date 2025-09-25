# Java Chat Relay (Server-Client via Server)

A desktop chat app where **all messages go through the server** (no direct client-to-client).

## Tech
- Java 17, JavaFX, TCP Sockets, Maven

## Server
- Lists online users (IP + nickname)
- Forwards chat requests & responses
- Relays messages between 2 participants (group chat: WIP)

## Client
- Connect to server
- Fetch online users (by nickname)
- Send/accept/decline chat requests
- Send/receive messages
- Leave chat

## Run
### Build
mvn -q -f app/pom.xml -DskipTests package

### Start server
java -jar app/target/*.jar server

### Start client
java -jar app/target/*.jar client

## Roadmap
- Group chats
- Chat history persistence
- Auth & TLS
