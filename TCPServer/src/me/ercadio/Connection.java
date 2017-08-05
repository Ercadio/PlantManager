package me.ercadio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection {
	private Thread connectionThread;
	private Thread outputThread;
	private CommandProcessor CP;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private DataInputStream clientIStream;
	private DataOutputStream clientOStream;
	public TCPServer server;
	public Connection(TCPServer s,ServerSocket ss, CommandProcessor cp){
		server = s;
		serverSocket = ss;
		CP = cp;
		connectionThread = new Thread(new ConnectionRunnable(this));
		connectionThread.start();
	}
	
	private class ConnectionRunnable implements Runnable{
		private Connection invoker;
		@Override
		public void run() {
			while(true){
				try {
					clientSocket = serverSocket.accept();
					clientIStream = new DataInputStream(clientSocket.getInputStream());
					clientOStream = new DataOutputStream(clientSocket.getOutputStream());
					System.out.println("\u001B[32m[" + clientSocket.getInetAddress().getHostName() + " connected]\u001B[0m");
					outputThread = new Thread(new OutputRunnable(invoker));
					outputThread.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
				while(clientSocket != null && outputThread.isAlive()){
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					};
				}
			}		
		}
		public ConnectionRunnable(Connection c){
			this.invoker = c;
		}
		
	}
	private class OutputRunnable implements Runnable{
		private Connection invoker;
		@Override
		public void run() {
			while(true){
				try {
					while(clientIStream.available() == 0){
						Thread.sleep(1);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
					break;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String lastReply;
				try {
					lastReply = clientIStream.readUTF();
					if(lastReply.length() > 0 && lastReply.charAt(0) == '/'){
						if(CP.trigger(lastReply,invoker)){
							sendPacket("\u001B[31mInvalid Command\u001B[0m");
						}
					}
					else{
						server.broadcast("\u001B[30;48;5;208m[" + clientSocket.getInetAddress().getHostName() + "]\u001B[0m " + lastReply);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				clientIStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public OutputRunnable(Connection c){
			this.invoker = c;
		}
	}
	public void sendPacket(String p){
		if(clientOStream != null){
			try {
				clientOStream.writeUTF(p);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
