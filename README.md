
# Java Chat Relay

A simple client-server chat application built with Java and JavaFX.

## Features
### Server
- Accepts multiple client connections  
- GUI showing connected users (IP + nickname)  
- Forwards chat requests to target users and relays responses  
- Relays messages between two users per chat session  
- *(Bonus)* planned support for group chats  

### Client
- Connects to the server  
- Retrieves online users list (by nickname)  
- Sends chat requests to a chosen user  
- Accepts or declines incoming chat requests  
- Sends and receives messages once connected  
- Can leave a chat anytime  
- All communication goes through the server (no direct P2P)  

## Requirements
- Java 17+  
- Maven  

## Usage
Build the project:
```bash
mvn -q -f app/pom.xml -DskipTests package

Start the server:

java -jar app/target/*.jar server

Start a client:

java -jar app/target/*.jar client

Roadmap
	•	Group chat support
	•	Chat history persistence
	•	Authentication & TLS encryption
## Screenshots

### Login Screen
![Login Screen](app/screenshots/Ekran%20Resmi%202024-11-30%2015.36.39.png)

### Chat Window
![Chat Window](app/screenshots/WhatsApp%20Image%202025-09-25%20at%2017.55.21.jpeg)

### User List
![User List](app/screenshots/WhatsApp%20Image%202dd025-09-25%20at%2017.55.21.jpeg)

