
# Java Chat Relay

A simple client-server chat application built with Java and JavaFX.

## Features
### Server
- Accepts multiple client connections  
- GUI showing connected users (IP + nickname)  
- Forwards chat requests to target users and relays responses  
- Relays messages between two users per chat session  
- *(Bonus)* plann![WhatsApp Image 2025-09-25 at 17 55 21](https://github.com/user-attachments/assets/ed05699b-65f1-44e5-82ed-6f9c96c5e361)
![WhatsApp Image 2dd025-09-25 at 17 55 21](https://github.com/user-attachments/assets/7ab83677-bedc-4c84-bb8a-5be5ab032e30)
ed support for group chats  
<img width="596" height="512" alt="Ekran Resmi 2024-11-30 15 36 39" src="https://github.com/user-attachments/assets/c29407b8-6397-4825-82c6-fae9943da52d" />

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

