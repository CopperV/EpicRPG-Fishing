package me.Vark123.EpicRPGFishing.KhorinisFishing.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicRPGFishing.Events.FishingRodUseEvent;
import me.Vark123.EpicRPGFishing.KhorinisFishing.KhorinisConfig;

public class FishingRodUseListener implements Listener {

	@EventHandler
	public void onUse(FishingRodUseEvent e) {
		if(e.isAllowedUsage())
			return;
		if(e.isCancelled())
			return;
		
		Player p = e.getPlayer();
		ItemStack it = e.getFishingRod();
		
		String w = p.getWorld().getName();
		if(!KhorinisConfig.get().getAllowedWorlds().contains(w))
			return;
		
		NBTItem nbt = new NBTItem(it);
		if(!nbt.hasTag("type")
				|| !nbt.getString("type").equalsIgnoreCase("fish"))
			return;

		e.setAllowedUsage(true);
	}
	
}
