package me.ercadio;

import java.util.HashMap;

public class CommandProcessor {
	private HashMap<String,TypedCommand> cmdMap;
	public CommandProcessor(TypedCommand[] list){
		cmdMap = new HashMap<>();
		for(int i = 0; i < list.length; i++)
		{
			cmdMap.put(list[i].call,list[i]);
		}
	}
	public boolean trigger(String cmd, Connection invoker){
		String[] tokens = cmd.split(" ");
		if(tokens.length > 0){
			TypedCommand ex = cmdMap.get(tokens[0]);
			if(ex == null){
				return true;
			}
			ex.run(tokens,invoker);
			return false;
		}
		return true;
	}
}
