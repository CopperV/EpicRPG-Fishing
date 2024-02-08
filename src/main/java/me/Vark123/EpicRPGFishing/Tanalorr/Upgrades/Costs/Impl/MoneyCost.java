package me.Vark123.EpicRPGFishing.Tanalorr.Upgrades.Costs.Impl;

import org.bukkit.entity.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPGFishing.Tanalorr.Upgrades.Costs.ICost;

@AllArgsConstructor
@Getter
public class MoneyCost implements ICost {

	private int amount;

	@Override
	public String display() {
		return "§7Cena: §e§o"+amount+"$";
	}
	
	@Override
	public boolean check(Player p) {
		return !(Main.eco.getBalance(p) < amount);
	}

	@Override
	public void spend(Player p) {
		Main.eco.withdrawPlayer(p, amount);
	}

}
