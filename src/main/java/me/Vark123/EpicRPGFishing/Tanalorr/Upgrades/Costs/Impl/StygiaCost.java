package me.Vark123.EpicRPGFishing.Tanalorr.Upgrades.Costs.Impl;

import org.bukkit.entity.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPGFishing.Tanalorr.Upgrades.Costs.ICost;

@AllArgsConstructor
@Getter
public class StygiaCost implements ICost {

	private int amount;

	@Override
	public String display() {
		return "§7Stygia: §3§o"+amount;
	}
	
	@Override
	public boolean check(Player p) {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		return !(rpg.getVault().getStygia() < amount);
	}

	@Override
	public void spend(Player p) {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		rpg.getVault().removeStygia(amount);
	}

}
