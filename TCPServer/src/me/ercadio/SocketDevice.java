package me.ercadio;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;


public class SocketDevice {
	private Thread connectionThread;
	private Thread outputThread;
	private Socket stubSocket;
	private ServerSocket serverSocket;
	private DataInputStream stubIStream;
	public LinkedList<String> data;
	public SocketDevice(int port){
		try {
			serverSocket = new ServerSocket(port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		data = new LinkedList<>();
		connectionThread = new Thread(new ConnectionRunnable());
		connectionThread.start();
	}
	
	private class ConnectionRunnable implements Runnable{
		@Override
		public void run() {
			while(true){
				try {
					stubSocket = serverSocket.accept();
					stubIStream = new DataInputStream(stubSocket.getInputStream());
					outputThread = new Thread(new OutputRunnable());
					outputThread.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
				while(stubSocket != null && outputThread.isAlive()){
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}		
		}
		
	}
	private class OutputRunnable implements Runnable{
		@Override
		public void run() {
			while(true){
				try {
					while(stubIStream.available() == 0){
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
					lastReply = stubIStream.readUTF();
					data.add(lastReply);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				stubIStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
