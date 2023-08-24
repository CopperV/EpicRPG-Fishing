package me.Vark123.EpicRPGFishing;

import org.bukkit.Bukkit;

import me.Vark123.EpicRPGFishing.Commands.ReloadCmd;

public class CommandManager {

	private CommandManager() { }
	
	public static void setExecutors() {
		Bukkit.getPluginCommand("epicfishing").setExecutor(new ReloadCmd());
	}
	
}
