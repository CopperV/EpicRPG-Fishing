package me.Vark123.EpicRPGFishing.KhorinisFishing;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;

@Getter
public final class FishingController {

	private static final FishingController inst = new FishingController();
	
	private final Map<Player, BukkitTask> hookWaterTasks;
	
	private FishingController() {
		hookWaterTasks = new ConcurrentHashMap<>();
	}
	
	public static final FishingController get() {
		return inst;
	}
	
	public void addHookWaterTask(Player p, BukkitTask task) {
		if(hookWaterTasks.containsKey(p))
			hookWaterTasks.get(p).cancel();
		hookWaterTasks.put(p, task);
	}
	
	public void removeHookWaterTask(Player p) {
		if(!hookWaterTasks.containsKey(p))
			return;
		hookWaterTasks.get(p).cancel();
		hookWaterTasks.remove(p);
	}
	
}
