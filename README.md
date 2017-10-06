# TCP Server Template
This is a template for a Java TCP server and client. It includes a chatroom and a command processor.

## Getting Started

To start the server, just
```
java -jar TCPServer.jar
```
For clients, same thing
```
java -jar TCPClient.jar
```
If you're on Windows, you can simply run run_windows.bat and it should open everything you need.
Otherwise on Linux, use run_linux.sh
Make sure to close your server if you're running run_linux.sh since the server is nohup'd.
You can do this by
```
top -u $(whoami) | grep java
```
and then killing the job

### Prerequisites

You will need Java 1.8 to run this template. Simply download it from [here](https://java.com/inc/BrowserRedirect1.jsp?locale=en).

## Running

Upon opening the Server and Clients console, you will be able to type some messages that will be multicasted. There is also a Command Proccessor. Each command starts with a '/' character. To quit safely, simply 
```
\quit
```
## Design
Here is a class diagram of the Server
![TCPServer.jpg](https://raw.githubusercontent.com/Ercadio/TCPTemplate/master/TCPserver.JPG)
Here is a class diagram of the Client
![TCPClient.jpg](https://raw.githubusercontent.com/Ercadio/TCPTemplate/master/TCPClient.JPG)
## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details


