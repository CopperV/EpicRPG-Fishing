package me.Vark123.EpicRPGFishing.Tanalorr.Merchants.Prices.Impl;

import org.bukkit.entity.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.Vark123.EpicRPG.Core.CoinsSystem;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPGFishing.Tanalorr.Merchants.Prices.IPrice;

@AllArgsConstructor
@Getter
public class CoinsPrice implements IPrice {
	
	private int amount;
	
	@Override
	public String display() {
		return "§7Smocze monety: §4§o"+amount;
	}

	@Override
	public void give(Player p) {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		CoinsSystem.getInstance().addCoins(rpg, amount, "sale");
	}

}
