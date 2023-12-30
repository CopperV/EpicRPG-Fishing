package me.Vark123.EpicRPGFishing.Tanalorr.Merchants.Prices;

import org.bukkit.entity.Player;

public interface IPrice {

	public String display();
	public void give(Player p);
	public int getAmount();
	
}
