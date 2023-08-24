package me.Vark123.EpicRPGFishing.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.Vark123.EpicRPGFishing.FileManager;

public class ReloadCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("epicfishing"))
			return false;
		if(!sender.hasPermission("epicfishing.admin"))
			return false;
		if(args.length == 0) {
			sender.sendMessage("§c§lPoprawne uzycie komendy");
			sender.sendMessage("§a§o/epicfishing reload");
			return false;
		}
		switch(args[0].toLowerCase()) {
			case "reload":
				FileManager.init();
				sender.sendMessage("§a§lPrzeladowales plugin");
				break;
			default:
				sender.sendMessage("§c§lPoprawne uzycie komendy");
				sender.sendMessage("§a§o/epicfishing reload");
				return false;
		}
		return true;
	}

}
