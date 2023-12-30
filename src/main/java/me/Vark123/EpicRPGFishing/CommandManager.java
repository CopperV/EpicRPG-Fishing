package me.Vark123.EpicRPGFishing;

import org.bukkit.Bukkit;

import me.Vark123.EpicRPGFishing.Commands.ReloadCmd;
import me.Vark123.EpicRPGFishing.Tanalorr.Merchants.TanalorrMerchantCommand;
import me.Vark123.EpicRPGFishing.Tanalorr.Upgrades.TanalorrFishingRodUpgradeCommand;

public class CommandManager {

	private CommandManager() { }
	
	public static void setExecutors() {
		Bukkit.getPluginCommand("epicfishing").setExecutor(new ReloadCmd());
		Bukkit.getPluginCommand("epicrodupgrade").setExecutor(new TanalorrFishingRodUpgradeCommand());
		Bukkit.getPluginCommand("tanalorrmerchant").setExecutor(new TanalorrMerchantCommand());
	}
	
}
