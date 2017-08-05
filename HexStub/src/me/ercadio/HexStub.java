package me.ercadio;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;

public class HexStub {
	private Socket server;
	public HexStub(){
		while(true){
			try {
				server = new Socket("localhost",1235);
				DataOutputStream ds = new DataOutputStream(server.getOutputStream());
				while(true){
					byte[]  resBuf = new byte[2];
					new Random().nextBytes(resBuf);
					String  resStr = Hex.encodeHexString(resBuf);
					ds.writeUTF("0x" + resStr);
					Thread.sleep(5000);
				}
			} catch (IOException e) {
				e.printStackTrace();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				continue;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
	}
	public static void main(String argv[]) throws Exception {
		new HexStub();
	}
}
