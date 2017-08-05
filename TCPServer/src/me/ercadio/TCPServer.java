package me.ercadio;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class TCPServer {
	private ServerSocket ss;
	private CommandProcessor CP;
	private Connection[] connectionList;
	private Thread inputThread;
	private BufferedReader br;
	private DataOutputStream dout;
	public static void main(String argv[]) throws Exception {
		new TCPServer();
	}
	public TCPServer(){
		System.out.print("\033[H\033[2J");
		System.out.flush();
		br = new BufferedReader(new InputStreamReader(System.in));
		
		TypedCommand[] cmds = {new SubscribeCommand(this)};
		CP = new CommandProcessor(cmds);
		try {
			ss = new ServerSocket(1234);
		} catch (IOException e) {
			e.printStackTrace();
		}
		connectionList = new Connection[20];
		for(int i = 0; i < 20; i++){
			connectionList[i] = new Connection(this,ss, CP);
		}
		inputThread = new Thread(new InputRunnable(this));
		inputThread.start();
	}
	public void broadcast(String string) {
		System.out.println(string);
		for(Connection c : connectionList){
			c.sendPacket(string);
		}
	}
	private class InputRunnable implements Runnable{
		TCPServer server;
		@Override
		public void run() {
			while(true){
				try {
					while(!br.ready()){
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
							break;
						}
					}
				} catch (IOException e2) {
					e2.printStackTrace();
					continue;
				}
				String lastTyped;
				try {
					lastTyped = br.readLine();
					System.out.print("\b\r");
					char[] ar = new char[lastTyped.length()];
					Arrays.fill(ar, ' ');
					System.out.print(new String(ar));
					System.out.print("\b\r");
					System.out.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
					continue;
				}
				if(lastTyped.toLowerCase().equals("/quit")){
					break;
				}
				server.broadcast("\u001B[30;48;5;68m[Server]\u001B[0m " + lastTyped);
			}
			try {
				dout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public InputRunnable(TCPServer s){
			this.server = s;
		}
	}
}

