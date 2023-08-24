package me.Vark123.EpicRPGFishing.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import me.Vark123.EpicRPGFishing.Events.FishingRodUseEvent;

public class FishingRodClickListener implements Listener {
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Result result = e.useItemInHand();
		if(result.equals(Result.DENY)) {
			return;
		}
		if(!e.getMaterial().equals(Material.FISHING_ROD))
			return;
		if(e.getHand().equals(EquipmentSlot.OFF_HAND)) {
			e.setUseItemInHand(Result.DENY);
			e.setCancelled(true);
			return;
		}
		
		FishingRodUseEvent event = new FishingRodUseEvent(e.getPlayer(), e.getItem(), false);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled() || !event.isAllowedUsage()) {
			e.setUseItemInHand(Result.DENY);
			e.setCancelled(true);
			return;
		}		
	}

}
