package me.Vark123.EpicRPGFishing.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;

@Getter
public class FishingRodUseEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	@Setter
	private boolean cancelled;
	
	private Player player;
	private ItemStack fishingRod;
	@Setter
	private boolean allowedUsage;

	public FishingRodUseEvent(Player player, ItemStack fishingRod, boolean allowedUsage) {
		super();
		this.player = player;
		this.fishingRod = fishingRod;
		this.allowedUsage = allowedUsage;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
