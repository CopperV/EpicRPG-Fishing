package me.Vark123.EpicRPGFishing.KhorinisFishing.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;

@Getter
public class KoloniaFishEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	
	@Setter
	private boolean cancelled;
	
	private Player player;
	@Setter
	private ItemStack fish;

	public KoloniaFishEvent(Player player, ItemStack fish) {
		super();
		this.player = player;
		this.fish = fish;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
