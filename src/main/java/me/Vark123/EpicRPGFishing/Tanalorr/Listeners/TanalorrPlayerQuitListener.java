package me.Vark123.EpicRPGFishing.Tanalorr.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.Vark123.EpicRPGFishing.Tanalorr.TanalorrFishingController;

public class TanalorrPlayerQuitListener implements Listener {

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		playerCleaner(e.getPlayer());
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		playerCleaner(e.getPlayer());
	}
	
	private void playerCleaner(Player p) {
		TanalorrFishingController.get().getBaitingPlayers().remove(p);
		TanalorrFishingController.get().getFishingPlayers().remove(p);
	}
	
}
