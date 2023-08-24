package me.Vark123.EpicRPGFishing.KhorinisFishing;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import io.lumine.mythic.bukkit.MythicBukkit;
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
	
	public ItemStack getRandomFish(int lucky) {
		Random rand = new Random();
		int bound = Config.get().getMultiplier() * lucky;
		int max = Integer.MIN_VALUE;
		for(int i = 0; i < lucky / 3; ++i) {
			int tmp = rand.nextInt(bound);
			if(tmp > max)
				max = tmp;
		}
		if(max < 0)
			return null;
		String mmId = Config.get().getFishChances().ceilingEntry(max).getValue();
		if(mmId == null)
			return null;
		ItemStack fish = MythicBukkit.inst().getItemManager().getItemStack(mmId);
		return fish;
	}
	
}
