package me.Vark123.EpicRPGFishing.Tanalorr.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import me.Vark123.EpicRPGFishing.Tanalorr.TanalorrConfig;
import me.Vark123.EpicRPGFishing.Tanalorr.TanalorrFishingController;

public class TanalorrHookListener implements Listener {

	@EventHandler
	public void onFish(PlayerFishEvent e) {
		if(e.isCancelled())
			return;

		Player p = e.getPlayer();
		String w = p.getWorld().getName();
		if(!TanalorrConfig.get().getAllowedWorlds().contains(w))
			return;
		
		if(!TanalorrFishingController.get().isFishing(p))
			return;
		
		switch(e.getState()) {
			case BITE:
				TanalorrFishingController.get().signalBite(p);
				break;
			default:
				break;
		}
	}

}
