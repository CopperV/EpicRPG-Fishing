package me.Vark123.EpicRPGFishing.Tanalorr.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicRPGFishing.Events.FishingRodUseEvent;
import me.Vark123.EpicRPGFishing.Tanalorr.TanalorrBaitMenu;
import me.Vark123.EpicRPGFishing.Tanalorr.TanalorrConfig;
import me.Vark123.EpicRPGFishing.Tanalorr.TanalorrFishingController;

public class TanalorrFishingRodUseListener implements Listener {

	@EventHandler
	public void onUse(FishingRodUseEvent e) {
		if(e.isAllowedUsage())
			return;
		if(e.isCancelled())
			return;

		Player p = e.getPlayer();
		ItemStack it = e.getFishingRod();
		
		String w = p.getWorld().getName();
		if(!TanalorrConfig.get().getAllowedWorlds().contains(w))
			return;

		NBTItem nbt = new NBTItem(it);
		if(!nbt.hasTag("type")
				|| !nbt.getString("type").equalsIgnoreCase("fish-tanalorr"))
			return;

		if(TanalorrFishingController.get().isFishing(p)) {
			e.setAllowedUsage(false);
			TanalorrFishingController.get().signalClick(p);
			return;
		}
		
		if(!TanalorrFishingController.get().isBaiting(p)) {
			TanalorrBaitMenu.get().openMenu(p, it);
			e.setAllowedUsage(false);
			return;
		}

		e.setAllowedUsage(true);
	}

}
