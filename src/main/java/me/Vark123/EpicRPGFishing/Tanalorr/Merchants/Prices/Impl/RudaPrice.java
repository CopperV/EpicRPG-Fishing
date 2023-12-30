package me.Vark123.EpicRPGFishing.Tanalorr.Merchants.Prices.Impl;

import org.bukkit.entity.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.Vark123.EpicRPG.Core.RudaSystem;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPGFishing.Tanalorr.Merchants.Prices.IPrice;

@AllArgsConstructor
@Getter
public class RudaPrice implements IPrice {
	
	private int amount;
	
	@Override
	public String display() {
		return "§7Brylki rudy: §9§o"+amount;
	}

	@Override
	public void give(Player p) {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RudaSystem.getInstance().addRuda(rpg, amount, "sale");
	}

}
