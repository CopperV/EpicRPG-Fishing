package me.Vark123.EpicRPGFishing.Tanalorr;

import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class TanalorrPlayerTempContainer {

	private Player player;
	private ItemStack fishingRod;
	private ItemStack bait;
	private FishHook hook;
	
	@Setter
	private Entity hooked;
	@Setter
	private TanalorrFishingState state;
	
	@Setter
	private double fishingStatus;
	
	public void increaseStatus(double value) {
		fishingStatus += value;
	}
	public void decreaseStatus(double value) {
		fishingStatus -= value;
	}
	
}
