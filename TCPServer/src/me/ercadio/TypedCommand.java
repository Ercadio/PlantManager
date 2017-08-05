package me.ercadio;

public class TypedCommand{
	public String call;
	public void run(String[] argv, Connection invoker) {
		invoker.sendPacket("Running " + call);
	}
	protected TCPServer server;
	public TypedCommand(TCPServer s){
		this.call = "/ping";
		this.server = s;
	}
}
