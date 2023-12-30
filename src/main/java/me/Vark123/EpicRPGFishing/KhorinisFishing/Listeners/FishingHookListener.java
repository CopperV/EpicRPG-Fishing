package me.Vark123.EpicRPGFishing.KhorinisFishing.Listeners;

import java.util.Set;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.Vark123.EpicRPGFishing.Main;
import me.Vark123.EpicRPGFishing.KhorinisFishing.KhorinisConfig;
import me.Vark123.EpicRPGFishing.KhorinisFishing.FishingController;
import me.Vark123.EpicRPGFishing.QTESystem.QTEManager;
import me.Vark123.EpicRPGFishing.Tools.Utils;

public class FishingHookListener implements Listener {

	@EventHandler
	public void onFish(PlayerFishEvent e) {
		if(e.isCancelled())
			return;

		FishHook hook = e.getHook();
		Player p = e.getPlayer();
		String w = p.getWorld().getName();
		if(!KhorinisConfig.get().getAllowedWorlds().contains(w))
			return;
		
		Set<ProtectedRegion> regions = WorldGuard.getInstance().getPlatform()
				.getRegionContainer().createQuery()
				.getApplicableRegions(BukkitAdapter.adapt(hook.getLocation()))
				.getRegions();
		if(regions != null && !regions.isEmpty()) {
			MutableBoolean protectedRegion = new MutableBoolean();
			regions.stream()
				.map(region -> region.getId())
				.filter(KhorinisConfig.get().getDisabledRegions()::contains)
				.findAny()
				.ifPresent(region -> protectedRegion.setTrue());
			if(protectedRegion.isTrue()) {
				Utils.stopFishing(hook);
				return;
			}
		}
		
		switch(e.getState()) {
			case FISHING:
				BukkitTask task = new BukkitRunnable() {
					@Override
					public void run() {
						if(isCancelled()){
							return;
						}
						if(!hook.isValid()) {
							FishingController.get().removeHookWaterTask(p);
							return;
						}
						if(hook.isOnGround()) {
							FishingController.get().removeHookWaterTask(p);
							return;
						}
						if(hook.isInWater()) {
							FishingController.get().removeHookWaterTask(p);
							QTEManager.get().startQTE(p, hook);
							return;
						}
					}
				}.runTaskTimer(Main.getInst(), 0, 5);
				FishingController.get().addHookWaterTask(p, task);
				break;
			case BITE:
			case CAUGHT_ENTITY:
				e.setCancelled(true);
				break;
			case FAILED_ATTEMPT:
				break;
			default:
				FishingController.get().removeHookWaterTask(p);
				if(hook.getHookedEntity() != null)
					hook.setHookedEntity(null);
				break;
		}
	}
	
}
