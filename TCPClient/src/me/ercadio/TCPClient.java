package me.ercadio;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class TCPClient {
	private Socket S;
	private DataInputStream din;
	private BufferedReader br;
	private DataOutputStream dout;
	private Thread InputThread;
	private Thread OutputThread;
	private boolean isConnected;
	public static void main(String argv[]) throws Exception {
		new TCPClient();
	}
	
	public TCPClient(){
		br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("\033[H\033[2J");
		System.out.flush();
		isConnected = false;
		while(!isConnected){
			try{
				System.out.println("Please type in the address: ");
				String addr = br.readLine();
				S = new Socket(addr,1234);
				din = new DataInputStream(S.getInputStream());
				dout = new DataOutputStream(S.getOutputStream());
				InputThread = new Thread(new InputRunnable());
				OutputThread = new Thread(new OutputRunnable());
				System.out.println("\u001B[32mSuccesfully connected\u001B[0m");
				InputThread.start();
				OutputThread.start();
				isConnected = true;
			} catch(IOException e){
//				e.printStackTrace();
				System.out.println("\u001B[31mThere was an error trying to connect.\u001B[0m");
			}
		}
	}
	public class InputRunnable implements Runnable{
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
					System.out.print("\r");
					System.out.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
					continue;
				}
				if(lastTyped.toLowerCase().equals("/quit")){
					break;
				}
				try {
					dout.writeUTF(lastTyped);
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
			try {
				dout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public class OutputRunnable implements Runnable{
		@Override
		public void run() {
			while(true){
				try {
					while(din.available() == 0){
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
					lastReply = din.readUTF();
					System.out.println(lastReply);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				din.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
