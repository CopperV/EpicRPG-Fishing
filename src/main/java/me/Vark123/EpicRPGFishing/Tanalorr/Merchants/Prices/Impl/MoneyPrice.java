package me.Vark123.EpicRPGFishing.Tanalorr.Merchants.Prices.Impl;

import org.bukkit.entity.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.Vark123.EpicRPG.Core.MoneySystem;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPGFishing.Tanalorr.Merchants.Prices.IPrice;

@AllArgsConstructor
@Getter
public class MoneyPrice implements IPrice {
	
	private int amount;
	
	@Override
	public String display() {
		return "§7Kasa: §e§o"+amount+"$";
	}

	@Override
	public void give(Player p) {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		MoneySystem.getInstance().addMoney(rpg, amount, "sale");
	}

}
