package me.Vark123.EpicRPGFishing.Tanalorr.Merchants;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TanalorrMerchantCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("tanalorrmerchant"))
			return false;
		if(!sender.hasPermission("epicfishing.admin"))
			return false;
		if(args.length < 2) {
			sender.sendMessage("§c§lPoprawne uzycie komendy");
			sender.sendMessage("§a§o/tanalorrmerchant <player> <merchant>");
			return false;
		}
		
		Player p = Bukkit.getPlayerExact(args[0]);
		if(p == null) {
			sender.sendMessage("§7"+args[0]+" §c§ojest offline!");
			return false;
		}
		
		Optional<TanalorrMerchant> oMerchant = TanalorrMerchantManager.get().getMerchantById(args[1]);
		if(oMerchant.isEmpty()) {
			sender.sendMessage("§c§oKupiec §7"+args[1]+" §c§onie istnieje!");
			return false;
		}
		
		TanalorrMerchant merchant = oMerchant.get();
		TanalorrMerchantMenu.get().openMenu(p, merchant);
		return true;
	}

}
